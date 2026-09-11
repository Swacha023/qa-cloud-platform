package com.swacha.qacloud.service;

import com.swacha.qacloud.api.dto.AuthDtos;
import com.swacha.qacloud.security.JwtService;
import com.swacha.qacloud.user.UserAccount;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
 private final AuthenticationManager authenticationManager;
 private final JwtService jwtService;
 public AuthService(AuthenticationManager authenticationManager, JwtService jwtService){this.authenticationManager=authenticationManager;this.jwtService=jwtService;}
 public AuthDtos.LoginResponse login(AuthDtos.LoginRequest request){
   Authentication auth=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),request.password()));
   UserAccount user=(UserAccount)auth.getPrincipal();
   return new AuthDtos.LoginResponse(jwtService.generateToken(user), user.getEmail(), user.getDisplayName());
 }
}
