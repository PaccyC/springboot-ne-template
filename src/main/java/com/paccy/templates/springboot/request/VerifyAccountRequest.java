package com.paccy.templates.springboot.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class VerifyAccountRequest {
    String verificationCode;
}
