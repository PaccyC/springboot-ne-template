package com.paccy.templates.springboot.controller;

import com.paccy.templates.springboot.helpers.ApiResponse;
import com.paccy.templates.springboot.request.InitiateAccountVerificationRequest;
import com.paccy.templates.springboot.request.InitiatePasswordResetRequest;
import com.paccy.templates.springboot.request.LoginRequest;
import com.paccy.templates.springboot.request.ResetPasswordRequest;
import com.paccy.templates.springboot.response.AuthResponse;
import com.paccy.templates.springboot.services.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/initiate-account-verification")
    public ResponseEntity<ApiResponse<Object>> sendAccountVerificationRequest(
           @RequestBody @Valid InitiateAccountVerificationRequest accountVerificationRequest
    ){
        authService.initiateAccountVerification(accountVerificationRequest);
      return new ApiResponse<>("Account Activation Email sent successfully,expiring in 6 hours",HttpStatus.OK,null).toResponseEntity();
    }

    @PutMapping("/verify-account/{verificationCode}")
    public ResponseEntity<ApiResponse<Object>> verifyAccount(
            @PathVariable String verificationCode
    ){
        authService.verifyAccount(verificationCode);
        return new ApiResponse<>("Account Verification completed successfully",HttpStatus.OK,null).toResponseEntity();
    }


    @PutMapping("/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(
            @RequestBody @Valid ResetPasswordRequest request
    ){
        authService.resetPassword(request.getEmail(),request.getVerificationCode(),request.getNewPassword());
        return new ApiResponse<>("Password Reset successfully",HttpStatus.OK,null).toResponseEntity();
    }


}
