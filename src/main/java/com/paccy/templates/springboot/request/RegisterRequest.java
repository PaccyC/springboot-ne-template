package com.paccy.templates.springboot.request;

import com.paccy.templates.springboot.enums.EGender;
import com.paccy.templates.springboot.enums.ERole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(

        @NotBlank
        String fullName,

        @NotBlank
        String email,

        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[A-Za-z\\d\\W_]{8,}$",
           message = "Password must have at least 8 characters, one symbol, one number, and one uppercase letter.")
        String password,

        @NotBlank
        @Pattern(regexp = "[0-9]{10,12}", message = "Your phone number must be one among these formats: 2507***, or 07*** ")
        String phoneNumber,

        ERole role,
        EGender gender
) {

}
