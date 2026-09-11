package com.swacha.qacloud.api;

import com.swacha.qacloud.api.dto.AuthDtos;
import com.swacha.qacloud.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final AuthService service; public AuthController(AuthService service){this.service=service;}
 @PostMapping("/login") public AuthDtos.LoginResponse login(@RequestBody @Valid AuthDtos.LoginRequest request){return service.login(request);}
}
