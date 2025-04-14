package com.paccy.templates.springboot.services;

import com.paccy.templates.springboot.helpers.ApiResponse;
import com.paccy.templates.springboot.request.*;
import com.paccy.templates.springboot.response.AuthResponse;

public interface IAuthService {

    AuthResponse login(LoginRequest loginRequest);
    void  initiateAccountVerification(InitiateAccountVerificationRequest verificationRequest);
    void verifyAccount(String verificationCode);
    void initiatePasswordReset(InitiatePasswordResetRequest passwordResetRequest);
    void resetPassword(String email,String verificationCode,String newPassword);

}
