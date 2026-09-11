import {
  SQSClient,
  ReceiveMessageCommand,
  DeleteMessageCommand,
} from "@aws-sdk/client-sqs";

import { spawn } from "node:child_process";

const region = process.env.AWS_REGION || "ca-central-1";
const queueUrl = process.env.SQS_QUEUE_URL;

// This is the QACloud HTTPS address used for worker callbacks.
// Example: https://xxxxxxxx.cloudfront.net
const apiBaseUrl = process.env.API_BASE_URL;

// Shared secret between the worker and Spring Boot.
const workerToken = process.env.WORKER_TOKEN;

if (!queueUrl) {
  console.error("SQS_QUEUE_URL is not configured.");
  process.exit(1);
}

if (!apiBaseUrl) {
  console.error("API_BASE_URL is not configured.");
  process.exit(1);
}

if (!workerToken) {
  console.error("WORKER_TOKEN is not configured.");
  process.exit(1);
}

const sqs = new SQSClient({ region });

function normalizeTitle(title) {
  return title.trim().toLowerCase();
}

async function apiRequest(path, options = {}) {
  const response = await fetch(`${apiBaseUrl}${path}`, {
    ...options,
    headers: {
      "X-Worker-Token": workerToken,
      "Content-Type": "application/json",
      ...(options.headers || {}),
    },
  });

  if (!response.ok) {
    const body = await response.text();

    throw new Error(
      `QACloud API request failed: ${response.status} ${response.statusText} ${body}`
    );
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

async function getProjectCases(projectId) {
  return apiRequest(`/api/internal/projects/${projectId}/cases`);
}

function runPlaywright() {
  return new Promise((resolve, reject) => {
    console.log("Starting Playwright tests...");

    const command =
      process.platform === "win32"
        ? "npx.cmd"
        : "npx";

    const child = spawn(
      command,
      ["playwright", "test", "--reporter=json"],
      {
        env: process.env,
        shell: process.platform === "win32",
      }
    );

    let stdout = "";
    let stderr = "";

    child.stdout.on("data", (data) => {
      stdout += data.toString();
    });

    child.stderr.on("data", (data) => {
      stderr += data.toString();
    });

    child.on("error", (error) => {
      reject(error);
    });

    child.on("close", (exitCode) => {
      if (stderr.trim()) {
        console.log(stderr);
      }

      try {
        const report = JSON.parse(stdout);

        resolve({
          exitCode: exitCode ?? 1,
          report,
        });
      } catch (error) {
        console.error("Could not parse Playwright JSON report.");
        console.error(stdout);

        reject(error);
      }
    });
  });
}

function collectPlaywrightResults(suites) {
  const collected = [];

  function visitSuite(suite) {
    for (const spec of suite.specs || []) {
      const test = spec.tests?.[0];

      if (!test) {
        continue;
      }

      const attempts = test.results || [];
      const lastResult = attempts[attempts.length - 1];

      if (!lastResult) {
        continue;
      }

      let status;

      switch (lastResult.status) {
        case "passed":
          status = "PASSED";
          break;

        case "skipped":
          status = "SKIPPED";
          break;

        default:
          status = "FAILED";
          break;
      }

      const errorMessage =
        lastResult.error?.message ||
        lastResult.errors?.[0]?.message ||
        null;

      collected.push({
        title: spec.title,
        status,
        durationMs: lastResult.duration || 0,
        errorMessage,
      });
    }

    for (const childSuite of suite.suites || []) {
      visitSuite(childSuite);
    }
  }

  for (const suite of suites || []) {
    visitSuite(suite);
  }

  return collected;
}

async function reportResults(runId, projectId, playwrightReport) {
  console.log("Loading QACloud test cases...");

  const projectCases = await getProjectCases(projectId);

  const casesByTitle = new Map(
    projectCases.map((testCase) => [
      normalizeTitle(testCase.title),
      testCase,
    ])
  );

  const results = collectPlaywrightResults(
    playwrightReport.suites
  );

  if (results.length === 0) {
    throw new Error("Playwright returned no test results.");
  }

  console.log(`Playwright returned ${results.length} result(s).`);

  for (const result of results) {
    const testCase = casesByTitle.get(
      normalizeTitle(result.title)
    );

    if (!testCase) {
      throw new Error(
        `No QACloud test case matches Playwright test: "${result.title}"`
      );
    }

    console.log(
      `${result.status}: ${result.title} -> case ${testCase.id}`
    );

    await apiRequest(
      `/api/internal/runs/${runId}/results`,
      {
        method: "POST",
        body: JSON.stringify({
          caseId: testCase.id,
          status: result.status,
          durationMs: result.durationMs,
          errorMessage: result.errorMessage,
        }),
      }
    );
  }

  await apiRequest(
    `/api/internal/runs/${runId}/complete`,
    {
      method: "POST",
    }
  );

  console.log(`Run ${runId} reported back to QACloud.`);
}

console.log("QACloud test worker starting...");
console.log(`Region: ${region}`);
console.log("Waiting for SQS messages...");

while (true) {
  try {
    const response = await sqs.send(
      new ReceiveMessageCommand({
        QueueUrl: queueUrl,
        MaxNumberOfMessages: 1,
        WaitTimeSeconds: 20,
        VisibilityTimeout: 300,
      })
    );

    const messages = response.Messages ?? [];

    if (messages.length === 0) {
      continue;
    }

    for (const message of messages) {
      console.log("\nReceived SQS message:");

      let job;

      try {
        job = JSON.parse(message.Body);
      } catch {
        console.error(
          "Invalid JSON message:",
          message.Body
        );

        continue;
      }

      console.log(`Run ID: ${job.runId}`);
      console.log(`Project ID: ${job.projectId}`);

      const { exitCode, report } =
        await runPlaywright();

      if (exitCode === 0) {
        console.log(
          `Run ${job.runId}: Playwright execution PASSED.`
        );
      } else {
        console.log(
          `Run ${job.runId}: Playwright execution contained failures.`
        );
      }

      await reportResults(
        job.runId,
        job.projectId,
        report
      );

      await sqs.send(
        new DeleteMessageCommand({
          QueueUrl: queueUrl,
          ReceiptHandle: message.ReceiptHandle,
        })
      );

      console.log("SQS message deleted.");
      console.log("Waiting for next test run...");
    }
  } catch (error) {
    console.error("Worker error:", error);

    // The SQS message is deliberately NOT deleted when
    // processing/reporting fails, so it can be retried.
    await new Promise((resolve) => {
      setTimeout(resolve, 5000);
    });
  }
}