package com.swacha.qacloud.service;

import com.swacha.qacloud.domain.*; import com.swacha.qacloud.repo.*;
import org.junit.jupiter.api.Test; import org.junit.jupiter.api.extension.ExtendWith; import org.mockito.InjectMocks; import org.mockito.Mock; import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List; import static org.junit.jupiter.api.Assertions.*; import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {
 @Mock ProjectRepository projects; @Mock TestCaseRepository cases; @Mock TestRunRepository runs; @Mock BugRepository bugs;
 @InjectMocks DashboardService service;
 @Test void summaryCountsOpenBugsAndFailedRuns(){
  TestRun run=new TestRun();run.setStatus(TestStatus.FAILED); Bug b=new Bug();b.setStatus(BugStatus.OPEN);
  when(projects.count()).thenReturn(2L);when(cases.count()).thenReturn(10L);when(runs.findAll()).thenReturn(List.of(run));when(bugs.findAll()).thenReturn(List.of(b));
  var result=service.summary();
  assertEquals(2L,result.get("projects"));assertEquals(10L,result.get("testCases"));assertEquals(1L,result.get("failedRuns"));assertEquals(1L,result.get("openBugs"));
 }
}
