package com.paccy.templates.springboot.security.service;

import com.paccy.templates.springboot.entities.User;
import com.paccy.templates.springboot.exceptions.UserNotFoundException;
import com.paccy.templates.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user= userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found with email: " + email)
        );

        List<SimpleGrantedAuthority> authorities= user.getRole().getAuthorities();


        return  new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );



    }
}
