package com.swacha.qacloud.service;

import com.swacha.qacloud.config.AppProperties;
import com.swacha.qacloud.domain.TestCaseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader; import java.io.InputStreamReader; import java.nio.file.Path; import java.util.List;

@Service
public class LocalTestRunnerService {
 private final TestManagementService tests; private final AppProperties properties;
 public LocalTestRunnerService(TestManagementService tests, AppProperties properties){this.tests=tests;this.properties=properties;}
 @Async
 public void execute(Long runId, Long projectId){
   try{
     List<TestCaseEntity> all=tests.projectCases(projectId);
     if(all.isEmpty()){Thread.sleep(300); tests.complete(runId); return;}
     // Local portfolio mode: deterministic execution so the product works without a remote worker.
     // Replace this adapter with the SQS worker in the AWS profile.
     for(TestCaseEntity c:all){
       long duration=50L+(c.getId()%7)*31L;
       boolean pass=(c.getId()%5)!=0;
       tests.addResult(runId,c.getId(),pass?com.swacha.qacloud.domain.TestStatus.PASSED:com.swacha.qacloud.domain.TestStatus.FAILED,duration,pass?null:"Assertion failed in demo test runner");
     }
     tests.complete(runId);
    } catch (Exception e) {
      e.printStackTrace();
    }
 }
}
