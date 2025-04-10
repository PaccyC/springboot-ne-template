package com.paccy.templates.springboot.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.paccy.templates.springboot.exceptions.UnAuthorizedException;
import com.paccy.templates.springboot.security.filter.JwtAuthFilter;
import com.paccy.templates.springboot.security.service.UserDetailsServiceImpl;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/api/v1/user/me")
                                .authenticated()
                                .anyRequest()
                                .permitAll()

                )
                .userDetailsService(userDetailsService)
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return  http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ServletOutputStream outputStream= response.getOutputStream();
            new ObjectMapper().writeValue(outputStream, new UnAuthorizedException("You are not authorized to access this resource"));
            outputStream.flush();
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationErrorHandler(){
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ServletOutputStream outputStream= response.getOutputStream();
            new ObjectMapper().writeValue(outputStream, new UnAuthorizedException("Invalid or missing auth token"));
            outputStream.flush();
        };

    }
}
