package com.swacha.qacloud.service;

import com.swacha.qacloud.api.dto.BugDtos;
import com.swacha.qacloud.domain.*;
import com.swacha.qacloud.repo.BugRepository;
import com.swacha.qacloud.user.UserAccount;
import com.swacha.qacloud.user.UserRepository;
import org.springframework.stereotype.Service;
import java.time.Instant; import java.util.List;

@Service
public class BugService {
 private final BugRepository bugs; private final ProjectService projects; private final UserRepository users;
 public BugService(BugRepository bugs, ProjectService projects, UserRepository users){this.bugs=bugs;this.projects=projects;this.users=users;}
 public List<BugDtos.BugResponse> list(Long projectId){return bugs.findByProjectIdOrderByCreatedAtDesc(projectId).stream().map(this::toResponse).toList();}
 public BugDtos.BugResponse create(Long projectId, BugDtos.CreateBugRequest r){Bug b=new Bug(); b.setProject(projects.get(projectId)); b.setTitle(r.title()); b.setDescription(r.description()); b.setSeverity(Severity.valueOf(r.severity()==null?"MEDIUM":r.severity())); b.setStatus(BugStatus.OPEN); b.setCreatedAt(Instant.now()); return toResponse(bugs.save(b));}
 public BugDtos.BugResponse update(Long id, BugDtos.UpdateBugRequest r){Bug b=bugs.findById(id).orElseThrow(()->new IllegalArgumentException("Bug not found")); if(r.status()!=null){b.setStatus(BugStatus.valueOf(r.status())); if(b.getStatus()==BugStatus.RESOLVED||b.getStatus()==BugStatus.CLOSED)b.setResolvedAt(Instant.now());} if(r.assignedToId()!=null){b.setAssignedTo(users.findById(r.assignedToId()).orElseThrow());} return toResponse(bugs.save(b));}
 private BugDtos.BugResponse toResponse(Bug b){return new BugDtos.BugResponse(b.getId(),b.getTitle(),b.getDescription(),b.getSeverity().name(),b.getStatus().name(),b.getAssignedTo()==null?null:b.getAssignedTo().getDisplayName(),b.getCreatedAt().toString());}
}
