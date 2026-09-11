package com.swacha.qacloud.service;

import com.swacha.qacloud.config.AppProperties;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SqsTestRunService {

    private final SqsClient sqsClient;
    private final AppProperties properties;

    public SqsTestRunService(SqsClient sqsClient, AppProperties properties) {
        this.sqsClient = sqsClient;
        this.properties = properties;
    }

    public void queue(Long runId, Long projectId) {
        String queueUrl = properties.getSqsQueueUrl();

        if (queueUrl == null || queueUrl.isBlank()) {
            throw new IllegalStateException("SQS queue URL is not configured");
        }

        String body = """
                {
                  "runId": %d,
                  "projectId": %d
                }
                """.formatted(runId, projectId);

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(body)
                .build();

        sqsClient.sendMessage(request);
    }
}