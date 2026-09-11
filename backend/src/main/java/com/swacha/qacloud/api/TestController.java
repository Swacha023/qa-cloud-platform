package com.swacha.qacloud.api;

import com.swacha.qacloud.api.dto.TestDtos;
import com.swacha.qacloud.domain.TestCaseEntity;
import com.swacha.qacloud.domain.TestResult;
import com.swacha.qacloud.domain.TestSuite;
import com.swacha.qacloud.service.SqsTestRunService;
import com.swacha.qacloud.service.TestManagementService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {

    private final TestManagementService tests;
    private final SqsTestRunService runner;

    public TestController(
            TestManagementService tests,
            SqsTestRunService runner) {
        this.tests = tests;
        this.runner = runner;
    }

    @GetMapping("/projects/{projectId}/suites")
    public List<TestSuite> suites(@PathVariable Long projectId) {
        return tests.suites(projectId);
    }

    @PostMapping("/projects/{projectId}/suites")
    public TestSuite createSuite(
            @PathVariable Long projectId,
            @RequestBody TestDtos.CreateSuiteRequest request) {

        return tests.createSuite(projectId, request);
    }

    @PatchMapping("/suites/{suiteId}")
    public TestSuite updateSuite(
        @PathVariable Long suiteId,
        @RequestBody TestDtos.UpdateSuiteRequest request) {

        return tests.updateSuite(suiteId, request);
    }

    @GetMapping("/suites/{suiteId}/cases")
    public List<TestCaseEntity> cases(@PathVariable Long suiteId) {
        return tests.cases(suiteId);
    }

    @PostMapping("/suites/{suiteId}/cases")
    public TestCaseEntity createCase(
            @PathVariable Long suiteId,
            @RequestBody TestDtos.CreateCaseRequest request) {

        return tests.createCase(suiteId, request);
    }

    @GetMapping("/projects/{projectId}/runs")
    public List<TestDtos.RunResponse> runs(@PathVariable Long projectId) {
        return tests.recentRuns(projectId)
                .stream()
                .map(tests::summary)
                .toList();
    }

    @PostMapping("/projects/{projectId}/runs")
    public TestDtos.RunResponse startRun(
            @PathVariable Long projectId,
            @RequestParam(required = false) String commitHash) {

        var run = tests.startRun(projectId, commitHash);

        runner.queue(run.id(), projectId);

        return run;
    }

    @GetMapping("/runs/{runId}/results")
    public List<TestResult> results(@PathVariable Long runId) {
        return tests.results(runId);
    }
}
