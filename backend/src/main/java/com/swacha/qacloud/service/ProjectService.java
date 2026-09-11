package com.swacha.qacloud.service;

import com.swacha.qacloud.api.dto.ProjectDtos;
import com.swacha.qacloud.domain.Project;
import com.swacha.qacloud.repo.ProjectRepository;
import com.swacha.qacloud.user.UserAccount;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class ProjectService {
 private final ProjectRepository repo;
 public ProjectService(ProjectRepository repo){this.repo=repo;}
 public List<ProjectDtos.ProjectResponse> list(){return repo.findAll().stream().map(this::toResponse).toList();}
 public ProjectDtos.ProjectResponse create(ProjectDtos.CreateProjectRequest r, UserAccount user){
   Project p=new Project(); p.setName(r.name()); p.setDescription(r.description()); p.setCreatedAt(Instant.now()); p.setCreatedBy(user); return toResponse(repo.save(p));
 }
 public Project get(Long id){return repo.findById(id).orElseThrow(()->new IllegalArgumentException("Project not found"));}
 private ProjectDtos.ProjectResponse toResponse(Project p){return new ProjectDtos.ProjectResponse(p.getId(),p.getName(),p.getDescription());}
}
