package com.paccy.templates.springboot.services;

import com.paccy.templates.springboot.entities.User;
import com.paccy.templates.springboot.helpers.ApiResponse;
import com.paccy.templates.springboot.request.RegisterRequest;

import java.util.Optional;

public interface IUserService {

  User register(RegisterRequest registerRequest);

  User getMe();
  Optional<User> getUserByEmail(String email);


}
