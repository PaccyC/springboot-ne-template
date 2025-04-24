package com.paccy.templates.springboot.services.impl;

import com.paccy.templates.springboot.entities.User;
import com.paccy.templates.springboot.enums.EUserStatus;
import com.paccy.templates.springboot.exceptions.BadRequestException;
import com.paccy.templates.springboot.exceptions.ResourceNotFoundException;
import com.paccy.templates.springboot.exceptions.UnAuthorizedException;
import com.paccy.templates.springboot.helpers.ApiResponse;
import com.paccy.templates.springboot.repository.UserRepository;
import com.paccy.templates.springboot.request.InitiateAccountVerificationRequest;
import com.paccy.templates.springboot.request.InitiatePasswordResetRequest;
import com.paccy.templates.springboot.request.LoginRequest;
import com.paccy.templates.springboot.request.VerifyAccountRequest;
import com.paccy.templates.springboot.response.AuthResponse;
import com.paccy.templates.springboot.security.service.JwtService;
import com.paccy.templates.springboot.security.service.UserDetailsServiceImpl;
import com.paccy.templates.springboot.services.IAuthService;
import com.paccy.templates.springboot.standalone.MailService;
import com.paccy.templates.springboot.utils.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements IAuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final MailService mailService;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new UnAuthorizedException("Invalid Email or Password")
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

    @Override
    public void initiateAccountVerification(InitiateAccountVerificationRequest verificationRequest) {
        User user= userRepository.findByEmail(verificationRequest.getEmail()).orElseThrow(
               () -> new UnAuthorizedException("User doesn't exist")
       );
        if (user.getUserStatus() == EUserStatus.VERIFIED){
            throw new BadRequestException("User is already verified");

        }
       String verificationCode= Utility.generateAuthCode();
        LocalDateTime verificationCodeExpiresAt= LocalDateTime.now().plusHours(6);
        user.setActivationCode(verificationCode);
        user.setActivationCodeExpiresAt(verificationCodeExpiresAt);
       this.mailService.sendActivateAccountEmail(user.getEmail(),user.getFullName(),verificationCode);
        userRepository.save(user);

    }

    @Override
    public void verifyAccount(String verificationCode) {
//     Check if the user exists
        Optional<User> _user= this.userRepository.findByActivationCode(verificationCode);
        if (_user.isEmpty()){
            throw new ResourceNotFoundException("User",verificationCode,verificationCode);
        }
     User  user=_user.get();
        System.out.println("User"+user);

//        Check if the activationCode is not expired
        if (user.getActivationCodeExpiresAt().isBefore(LocalDateTime.now())){
           throw new BadRequestException("Verification has code expired,try generating another");
        }

        if (!Utility.isCodeValid(user.getActivationCode(),verificationCode)){
            throw new BadRequestException("Verification code is invalid");
        }
        user.setUserStatus(EUserStatus.VERIFIED);
        user.setActivationCode(null);
        user.setActivationCodeExpiresAt(null);
        this.mailService.sendAccountVerifiedSuccessfullyEmail(user.getEmail(),user.getFullName());
        this.userRepository.save(user);

    }

    @Override
    public void initiatePasswordReset(InitiatePasswordResetRequest passwordResetRequest) {
        User user = this.userRepository.findByEmail(passwordResetRequest.getEmail()).orElseThrow(
         () -> new UnAuthorizedException("User doesn't exist")
        );
        user.setActivationCode(Utility.randomUUID(3, 0, 'N'));
        user.setUserStatus(EUserStatus.RESET);
        this.userRepository.save(user);
        mailService.sendResetPasswordMail(user.getEmail(), user.getFullName(), user.getActivationCode());
    }


    @Override
    public void resetPassword(String email,String verificationCode,String newPassword) {
      User user= this.userRepository.findByEmail(email).orElseThrow(
              () -> new UnAuthorizedException("User doesn't exist")
      );
      if (Utility.isCodeValid(user.getActivationCode(),verificationCode)){
          user.setPassword(passwordEncoder.encode(newPassword));
          user.setActivationCode(Utility.randomUUID(6, 0, 'N'));
          user.setUserStatus(EUserStatus.VERIFIED);
          this.userRepository.save(user);
          this.mailService.sendPasswordResetSuccessEmail(user.getEmail(),user.getFullName());
      }
      else {
          throw new BadRequestException("Verification code is invalid");
      }
    }
}
