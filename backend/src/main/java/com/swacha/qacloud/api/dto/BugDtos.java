package com.swacha.qacloud.api.dto;
public final class BugDtos {
 private BugDtos(){}
 public record CreateBugRequest(String title, String description, String severity){}
 public record UpdateBugRequest(String status, Long assignedToId){}
 public record BugResponse(Long id, String title, String description, String severity, String status, String assignee, String createdAt){}
}
