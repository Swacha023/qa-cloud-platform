package com.swacha.qacloud.api.dto;
public final class TestDtos {
 private TestDtos(){}
 public record CreateSuiteRequest(String name, String description){}
 public record CreateCaseRequest(String title, String description, String preconditions, String steps, String expectedResult, String priority){}
 public record ResultResponse(Long id, Long testCaseId, String title, String status, Long durationMs, String errorMessage){}
 public record RunResponse(Long id, String status, long total, long passed, long failed, long skipped){}
}
