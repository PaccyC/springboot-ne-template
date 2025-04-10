package com.paccy.templates.springboot.services.impl;

import com.paccy.templates.springboot.entities.User;
import com.paccy.templates.springboot.exceptions.BadRequestException;
import com.paccy.templates.springboot.exceptions.UnAuthorizedException;
import com.paccy.templates.springboot.helpers.ApiResponse;
import com.paccy.templates.springboot.repository.UserRepository;
import com.paccy.templates.springboot.request.LoginRequest;
import com.paccy.templates.springboot.response.AuthResponse;
import com.paccy.templates.springboot.security.service.JwtService;
import com.paccy.templates.springboot.security.service.UserDetailsServiceImpl;
import com.paccy.templates.springboot.services.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new UnAuthorizedException("Email not found")
        );

        if (!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            throw new  UnAuthorizedException("Invalid email or password");
        }


        UserDetails userDetails= userDetailsService.loadUserByUsername(loginRequest.getEmail());
        Authentication authentication= new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String,Object> claims= new HashMap<>();
        claims.put("email",userDetails.getUsername());
        String token= jwtService.generateToken(claims,userDetails);
        return new AuthResponse(token,user);

    }
}
