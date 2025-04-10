package com.paccy.templates.springboot.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

        @Email
        @NotBlank
        String email;

        @NotBlank
        String password;
}
