package com.swacha.qacloud.api.dto;
public final class ProjectDtos {
 private ProjectDtos(){}
 public record CreateProjectRequest(String name, String description){}
 public record ProjectResponse(Long id, String name, String description){}
}
