import {
  SQSClient,
  ReceiveMessageCommand,
  DeleteMessageCommand,
} from "@aws-sdk/client-sqs";

const region = process.env.AWS_REGION || "ca-central-1";
const queueUrl = process.env.SQS_QUEUE_URL;

if (!queueUrl) {
  console.error("SQS_QUEUE_URL is not configured.");
  process.exit(1);
}

const sqs = new SQSClient({ region });

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
        VisibilityTimeout: 60,
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

      // Temporary behavior for this checkpoint:
      // We are only proving that this worker can consume SQS messages.
      console.log("Message received successfully.");

      await sqs.send(
        new DeleteMessageCommand({
          QueueUrl: queueUrl,
          ReceiptHandle: message.ReceiptHandle,
        })
      );

      console.log("Test message deleted from SQS.");
    }
  } catch (error) {
    console.error("Worker error:", error);
    await new Promise((resolve) => setTimeout(resolve, 5000));
  }
}