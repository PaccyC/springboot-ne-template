package com.paccy.templates.springboot.services.impl;

import com.paccy.templates.springboot.entities.User;
import com.paccy.templates.springboot.exceptions.BadRequestException;
import com.paccy.templates.springboot.helpers.ApiResponse;
import com.paccy.templates.springboot.mappers.UserMapper;
import com.paccy.templates.springboot.repository.UserRepository;
import com.paccy.templates.springboot.request.RegisterRequest;
import com.paccy.templates.springboot.services.IUserService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;



    @Override
    public User register(RegisterRequest registerRequest) {
        Optional<User> existingUser= userRepository.findByEmail(registerRequest.email());
        if (existingUser.isPresent()){
            throw new BadRequestException( String.format("User with email % already exists", registerRequest.email()));
        }
        return  userRepository.save(userMapper.toUser(registerRequest));

    }

    @Override
    public User getMe() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof  String)){
            UserDetails userDetails= (UserDetails) authentication.getPrincipal();

            Optional<User> user= getUserByEmail(userDetails.getUsername());
            return user.orElse(null);
        }
        else {
            log.warn("No authenticated user found");
        }
        return null;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
      return userRepository.findByEmail(email);
}
}
