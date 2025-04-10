package com.paccy.templates.springboot.services;

import com.paccy.templates.springboot.helpers.ApiResponse;
import com.paccy.templates.springboot.request.LoginRequest;
import com.paccy.templates.springboot.request.RegisterRequest;
import com.paccy.templates.springboot.response.AuthResponse;

public interface IAuthService {

    public AuthResponse login(LoginRequest loginRequest);
}
