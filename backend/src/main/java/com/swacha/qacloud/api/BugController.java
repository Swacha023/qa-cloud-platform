package com.swacha.qacloud.api;

import com.swacha.qacloud.api.dto.BugDtos; import com.swacha.qacloud.service.BugService;
import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api")
public class BugController{
 private final BugService service; public BugController(BugService service){this.service=service;}
 @GetMapping("/projects/{projectId}/bugs") public List<BugDtos.BugResponse> list(@PathVariable Long projectId){return service.list(projectId);}
 @PostMapping("/projects/{projectId}/bugs") public BugDtos.BugResponse create(@PathVariable Long projectId,@RequestBody BugDtos.CreateBugRequest r){return service.create(projectId,r);}
 @PatchMapping("/bugs/{id}") public BugDtos.BugResponse update(@PathVariable Long id,@RequestBody BugDtos.UpdateBugRequest r){return service.update(id,r);}
}
