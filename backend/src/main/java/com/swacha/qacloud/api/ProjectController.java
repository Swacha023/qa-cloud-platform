package com.swacha.qacloud.api;

import com.swacha.qacloud.api.dto.ProjectDtos; import com.swacha.qacloud.service.ProjectService; import com.swacha.qacloud.user.UserAccount;
import jakarta.validation.Valid; import org.springframework.security.core.annotation.AuthenticationPrincipal; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/projects")
public class ProjectController{
 private final ProjectService service; public ProjectController(ProjectService service){this.service=service;}
 @GetMapping public List<ProjectDtos.ProjectResponse> list(){return service.list();}
 @PostMapping public ProjectDtos.ProjectResponse create(@RequestBody @Valid ProjectDtos.CreateProjectRequest r,@AuthenticationPrincipal UserAccount user){return service.create(r,user);}
}
