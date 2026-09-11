import {
  SQSClient,
  ReceiveMessageCommand,
  DeleteMessageCommand,
} from "@aws-sdk/client-sqs";

import { spawn } from "node:child_process";

const region = process.env.AWS_REGION || "ca-central-1";
const queueUrl = process.env.SQS_QUEUE_URL;

if (!queueUrl) {
  console.error("SQS_QUEUE_URL is not configured.");
  process.exit(1);
}

const sqs = new SQSClient({ region });

function runPlaywright() {
  return new Promise((resolve) => {
    console.log("Starting Playwright tests...");

    const npmCommand =
      process.platform === "win32" ? "npm.cmd" : "npm";

    const child = spawn(
      npmCommand,
      ["test"],
      {
        stdio: "inherit",
        env: process.env,
        shell: process.platform === "win32",
      }
    );

    child.on("close", (exitCode) => {
      resolve(exitCode ?? 1);
    });

    child.on("error", (error) => {
      console.error("Could not start Playwright:", error);
      resolve(1);
    });
  });
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
        console.error("Invalid JSON message:", message.Body);
        continue;
      }

      console.log(`Run ID: ${job.runId}`);
      console.log(`Project ID: ${job.projectId}`);

      const exitCode = await runPlaywright();

      if (exitCode === 0) {
        console.log(`Run ${job.runId}: Playwright tests PASSED.`);
      } else {
        console.log(`Run ${job.runId}: Playwright tests FAILED.`);
      }

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

    await new Promise((resolve) => {
      setTimeout(resolve, 5000);
    });
  }
}