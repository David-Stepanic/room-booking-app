package com.david.RoomReservation.service.email;

import com.david.RoomReservation.model.auth.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(VerificationToken token) {

        String link = "http://localhost:8080/api/auth/verify?token="
                + token.getToken();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(token.getUser().getEmail());
        message.setSubject("Verify your email");
        message.setText("Click to verify: " + link);

        mailSender.send(message);
    }

    public void sendResetPasswordEmail(String email, String token) {

        String link = "http://localhost:8080/api/auth/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset Password");
        message.setText("Click to reset password: " + link);

        mailSender.send(message);
    }
}
