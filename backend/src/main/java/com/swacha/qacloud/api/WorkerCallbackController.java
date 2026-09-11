package com.swacha.qacloud.api;

import com.swacha.qacloud.config.AppProperties;
import com.swacha.qacloud.domain.TestStatus;
import com.swacha.qacloud.service.TestManagementService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/api/internal")
public class WorkerCallbackController {

    private final TestManagementService tests;
    private final AppProperties properties;

    public WorkerCallbackController(
            TestManagementService tests,
            AppProperties properties) {
        this.tests = tests;
        this.properties = properties;
    }

    @PostMapping("/runs/{runId}/results")
    public ResponseEntity<Void> addResult(
            @PathVariable Long runId,
            @RequestHeader(value = "X-Worker-Token", required = false) String workerToken,
            @RequestBody ResultRequest request) {

        if (!validWorkerToken(workerToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TestStatus status;

        try {
            status = TestStatus.valueOf(request.status().toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        tests.addResult(
                runId,
                request.caseId(),
                status,
                request.durationMs(),
                request.errorMessage()
        );

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/runs/{runId}/complete")
    public ResponseEntity<Void> completeRun(
            @PathVariable Long runId,
            @RequestHeader(value = "X-Worker-Token", required = false) String workerToken) {

        if (!validWorkerToken(workerToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        tests.complete(runId);

        return ResponseEntity.noContent().build();
    }

    private boolean validWorkerToken(String providedToken) {
        String expectedToken = properties.getWorkerToken();

        if (providedToken == null ||
                expectedToken == null ||
                expectedToken.isBlank()) {
            return false;
        }

        return MessageDigest.isEqual(
                providedToken.getBytes(StandardCharsets.UTF_8),
                expectedToken.getBytes(StandardCharsets.UTF_8)
        );
    }

    public record ResultRequest(
            Long caseId,
            String status,
            Long durationMs,
            String errorMessage
    ) {
    }
}