package com.swacha.qacloud.service;

import com.swacha.qacloud.domain.*;
import com.swacha.qacloud.repo.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DashboardService {
 private final ProjectRepository projects; private final TestCaseRepository cases; private final TestRunRepository runs; private final BugRepository bugs;
 public DashboardService(ProjectRepository projects, TestCaseRepository cases, TestRunRepository runs, BugRepository bugs){this.projects=projects;this.cases=cases;this.runs=runs;this.bugs=bugs;}
 public Map<String,Object> summary(){long totalCases=cases.count(); long failedRuns=runs.findAll().stream().filter(r->r.getStatus()==TestStatus.FAILED).count(); long openBugs=bugs.findAll().stream().filter(b->b.getStatus()==BugStatus.OPEN||b.getStatus()==BugStatus.IN_PROGRESS).count(); return Map.of("projects",projects.count(),"testCases",totalCases,"failedRuns",failedRuns,"openBugs",openBugs);}
}
