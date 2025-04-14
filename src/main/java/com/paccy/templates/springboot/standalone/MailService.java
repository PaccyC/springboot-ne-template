package com.paccy.templates.springboot.standalone;


import com.paccy.templates.springboot.exceptions.AppException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;


    public void sendActivateAccountEmail(String to, String fullName, String verificationCode){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("fullName", fullName);
            context.setVariable("verificationCode", verificationCode);
//            context.setVariable("resetUrl", resetPasswordUrl);
//            context.setVariable("supportEmail", supportEmail);
            context.setVariable("currentYear", LocalDate.now().getYear());

            String htmlContent= templateEngine.process("activateAccountEmail",context);

            helper.setTo(to);
            helper.setSubject("Account Activation Request");
            helper.setText(htmlContent,true);

            mailSender.send(message);
        }
        catch (MessagingException e){
            throw new AppException(e.getMessage());
        }
    }

    public void sendAccountVerifiedSuccessfullyEmail(String to, String fullName){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("fullName", fullName);
//            context.setVariable("resetUrl", resetPasswordUrl);
//            context.setVariable("supportEmail", supportEmail);
            context.setVariable("currentYear", LocalDate.now().getYear());

            String htmlContent= templateEngine.process("account-verified-successfully",context);

            helper.setTo(to);
            helper.setSubject("Account Verified Successfully");
            helper.setText(htmlContent,true);

            mailSender.send(message);
        }
        catch (MessagingException e){
            throw new AppException(e.getMessage());
        }
    }
    public void sendResetPasswordMail(String to, String fullName, String resetCode) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("fullName", fullName);
            context.setVariable("resetCode", resetCode.toCharArray());
            context.setVariable("currentYear", LocalDate.now().getYear());

            String htmlContent = templateEngine.process("initiate-password-reset", context);

            helper.setTo(to);
            helper.setSubject("Password Reset Request");
            helper.setText(htmlContent, true);

            this.mailSender.send(message);
        } catch (MessagingException e) {
            throw new AppException("Error sending email", e);
        }
    }

    public void sendPasswordResetSuccessEmail(String to, String fullName){
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("fullName", fullName);
            context.setVariable("currentYear", LocalDate.now().getYear());

            String htmlContent = templateEngine.process("password-reset-successful", context);

            helper.setTo(to);
            helper.setSubject("Password Reset Successfully");
            helper.setText(htmlContent, true);

            this.mailSender.send(message);
        } catch (MessagingException e) {
            throw new AppException("Error sending message", e);
        }
    }


}
