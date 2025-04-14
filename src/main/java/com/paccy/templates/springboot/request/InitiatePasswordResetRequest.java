package com.paccy.templates.springboot.request;


import lombok.Data;

@Data
public class InitiatePasswordResetRequest {
    private  String email;
}
