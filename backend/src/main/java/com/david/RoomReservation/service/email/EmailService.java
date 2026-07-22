package com.david.RoomReservation.service.email;

import com.david.RoomReservation.model.auth.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Async
    public void sendVerificationEmail(VerificationToken token) {

        String link = "http://localhost:5173/verify-email?token="
                + token.getToken();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(token.getUser().getEmail());
        message.setSubject("Verify your email");
        message.setText("Click to verify: " + link);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send verification email to {}", token.getUser().getEmail(), e);
        }
    }

    @Async
    public void sendResetPasswordEmail(String email, String token) {

        String link = "http://localhost:5173/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset Password");
        message.setText("Click to reset password: " + link);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send verification email to {}", email, e);
        }
    }
}
