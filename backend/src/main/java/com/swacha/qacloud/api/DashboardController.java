package com.swacha.qacloud.api;
import com.swacha.qacloud.service.DashboardService; import org.springframework.web.bind.annotation.*; import java.util.Map;
@RestController @RequestMapping("/api/dashboard") public class DashboardController{private final DashboardService service; public DashboardController(DashboardService service){this.service=service;} @GetMapping public Map<String,Object> summary(){return service.summary();}}
