package com.swacha.qacloud.api;

import com.swacha.qacloud.api.dto.TestDtos; import com.swacha.qacloud.domain.*; import com.swacha.qacloud.service.*;
import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api")
public class TestController{
 private final TestManagementService tests; private final LocalTestRunnerService runner;
 public TestController(TestManagementService tests, LocalTestRunnerService runner){this.tests=tests;this.runner=runner;}
 @GetMapping("/projects/{projectId}/suites") public List<TestSuite> suites(@PathVariable Long projectId){return tests.suites(projectId);}
 @PostMapping("/projects/{projectId}/suites") public TestSuite createSuite(@PathVariable Long projectId,@RequestBody TestDtos.CreateSuiteRequest r){return tests.createSuite(projectId,r);}
 @GetMapping("/suites/{suiteId}/cases") public List<TestCaseEntity> cases(@PathVariable Long suiteId){return tests.cases(suiteId);}
 @PostMapping("/suites/{suiteId}/cases") public TestCaseEntity createCase(@PathVariable Long suiteId,@RequestBody TestDtos.CreateCaseRequest r){return tests.createCase(suiteId,r);}
 @GetMapping("/projects/{projectId}/runs") public List<TestDtos.RunResponse> runs(@PathVariable Long projectId){return tests.recentRuns(projectId).stream().map(tests::summary).toList();}
 @PostMapping("/projects/{projectId}/runs") public TestDtos.RunResponse startRun(@PathVariable Long projectId,@RequestParam(required=false) String commitHash){var run=tests.startRun(projectId,commitHash);runner.execute(run.id(),projectId);return run;}
 @GetMapping("/runs/{runId}/results") public List<TestResult> results(@PathVariable Long runId){return tests.results(runId);}
}
