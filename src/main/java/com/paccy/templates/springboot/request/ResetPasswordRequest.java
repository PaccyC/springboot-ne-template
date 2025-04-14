package com.paccy.templates.springboot.request;

import lombok.Data;
import lombok.Getter;

@Data
public class ResetPasswordRequest {
    private String email;
    private String newPassword;
    private String verificationCode;

}
