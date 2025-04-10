package com.paccy.templates.springboot.controller;


import com.paccy.templates.springboot.entities.User;
import com.paccy.templates.springboot.helpers.ApiResponse;
import com.paccy.templates.springboot.request.RegisterRequest;
import com.paccy.templates.springboot.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(
            @RequestBody RegisterRequest registerRequest
            ){
        User response= userService.register(registerRequest);
        return new  ApiResponse<>("User Registered Successfully", HttpStatus.OK,response).toResponseEntity();
    }

    @GetMapping("/me")
    public  ResponseEntity<ApiResponse<User>> getMe(){
        User response= userService.getMe();
        return new  ApiResponse<>("User Profile retrieved successfully", HttpStatus.OK,response).toResponseEntity();
    }

}
