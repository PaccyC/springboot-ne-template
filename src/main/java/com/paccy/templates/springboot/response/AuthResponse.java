package com.paccy.templates.springboot.response;


import com.paccy.templates.springboot.entities.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {

    private String token;
    private User user;

    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
