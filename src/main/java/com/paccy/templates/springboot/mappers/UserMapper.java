package com.paccy.templates.springboot.mappers;


import com.paccy.templates.springboot.entities.User;
import com.paccy.templates.springboot.enums.EUserStatus;
import com.paccy.templates.springboot.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private  final PasswordEncoder passwordEncoder;

    public User  toUser(RegisterRequest  registerRequest){
        return  User
                .builder()
                .fullName(registerRequest.fullName())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .phoneNumber(registerRequest.phoneNumber())
                .gender(registerRequest.gender())
                .role(registerRequest.role())
                .userStatus(EUserStatus.PENDING)

                .build();
    }

    private Optional<User> fromUser(){
        return  null;
    }
}
