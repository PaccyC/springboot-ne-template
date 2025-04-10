package com.paccy.templates.springboot.controller;

import com.paccy.templates.springboot.helpers.ApiResponse;
import com.paccy.templates.springboot.request.LoginRequest;
import com.paccy.templates.springboot.response.AuthResponse;
import com.paccy.templates.springboot.services.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>>  login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return new ApiResponse<>("User Logged in successfully", HttpStatus.OK, authResponse).toResponseEntity();
    }


}
