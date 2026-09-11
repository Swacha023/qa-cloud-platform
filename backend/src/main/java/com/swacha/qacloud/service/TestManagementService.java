package com.swacha.qacloud.service;

import com.swacha.qacloud.api.dto.TestDtos;
import com.swacha.qacloud.domain.*;
import com.swacha.qacloud.repo.*;
import com.swacha.qacloud.user.UserAccount;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class TestManagementService {
 private final ProjectService projects; private final TestSuiteRepository suites; private final TestCaseRepository cases;
 private final TestRunRepository runs; private final TestResultRepository results;
 public TestManagementService(ProjectService projects, TestSuiteRepository suites, TestCaseRepository cases, TestRunRepository runs, TestResultRepository results){this.projects=projects;this.suites=suites;this.cases=cases;this.runs=runs;this.results=results;}
 public List<TestSuite> suites(Long projectId){projects.get(projectId); return suites.findByProjectId(projectId);}
 public TestSuite createSuite(Long projectId, TestDtos.CreateSuiteRequest r){Project p=projects.get(projectId); TestSuite s=new TestSuite(); s.setProject(p); s.setName(r.name()); s.setDescription(r.description()); return suites.save(s);}
 public TestSuite updateSuite(
        Long suiteId,
        TestDtos.UpdateSuiteRequest request) {

    TestSuite suite = suites.findById(suiteId)
            .orElseThrow(() -> new IllegalArgumentException("Suite not found"));

    if (request.name() != null && !request.name().isBlank()) {
        suite.setName(request.name());
    }

    if (request.description() != null) {
        suite.setDescription(request.description());
    }

    return suites.save(suite);
}
 public List<TestCaseEntity> cases(Long suiteId){return cases.findBySuiteId(suiteId);}
 public List<TestCaseEntity> projectCases(Long projectId){projects.get(projectId); return cases.findByProjectId(projectId);}
 public TestCaseEntity createCase(Long suiteId, TestDtos.CreateCaseRequest r){TestSuite s=suites.findById(suiteId).orElseThrow(()->new IllegalArgumentException("Suite not found")); TestCaseEntity c=new TestCaseEntity(); c.setSuite(s); c.setTitle(r.title()); c.setDescription(r.description()); c.setPreconditions(r.preconditions()); c.setSteps(r.steps()); c.setExpectedResult(r.expectedResult()); c.setPriority(Priority.valueOf(r.priority()==null?"MEDIUM":r.priority())); c.setCreatedAt(Instant.now()); return cases.save(c);}
 public List<TestRun> recentRuns(Long projectId){return runs.findTop10ByProjectIdOrderByStartedAtDesc(projectId);}
 public TestDtos.RunResponse startRun(Long projectId, String commitHash){Project p=projects.get(projectId); TestRun run=new TestRun(); run.setProject(p); run.setStartedAt(Instant.now()); run.setCommitHash(commitHash); run.setStatus(TestStatus.QUEUED); run=runs.save(run); return summary(run);}
 public TestRun findRun(Long id){return runs.findById(id).orElseThrow(()->new IllegalArgumentException("Run not found"));}
 public List<TestResult> results(Long runId){return results.findByRunId(runId);}
 public TestDtos.RunResponse summary(TestRun run){long total=0,passed=0,failed=0,skipped=0; for(TestResult r:results.findByRunId(run.getId())){total++; switch(r.getStatus()){case PASSED->passed++;case FAILED->failed++;case SKIPPED->skipped++;default->{} }} return new TestDtos.RunResponse(run.getId(),run.getStatus().name(),total,passed,failed,skipped);}
public void addResult(Long runId, Long caseId, TestStatus status, Long duration, String error) {
    TestRun run = findRun(runId);
    TestCaseEntity testCase = cases.findById(caseId)
            .orElseThrow(() -> new IllegalArgumentException("Test case not found"));

    TestResult result = new TestResult();
    result.setRun(run);
    result.setTestCase(testCase);
    result.setStatus(status);
    result.setDurationMs(duration);
    result.setErrorMessage(error);

    results.save(result);

    if (run.getStatus() != TestStatus.FAILED) {
        run.setStatus(status == TestStatus.FAILED
                ? TestStatus.FAILED
                : TestStatus.RUNNING);
    }

    runs.save(run);
}
 public void complete(Long runId){TestRun run=findRun(runId); run.setCompletedAt(Instant.now()); boolean failed=results.findByRunId(runId).stream().anyMatch(r->r.getStatus()==TestStatus.FAILED); run.setStatus(failed?TestStatus.FAILED:TestStatus.PASSED); runs.save(run);}
}
