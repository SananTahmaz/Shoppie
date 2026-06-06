package com.shoppie.services.implementations;

import com.shoppie.services.EmailService;
import com.shoppie.services.EmailTemplateService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final EmailTemplateService templateService;

    @Async
    @Override
    public void sendOtp(String to, String otp) {
        String html = templateService.processTemplate("otp-verification", Map.of("otp", otp));
        sendHtml(to, html);
    }

    private void sendHtml(String to, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Verify Your Account");
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException exception) {
            throw new RuntimeException("Failed to send email", exception);
        }
    }
}
