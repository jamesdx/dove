package com.helix.dove.auth.service.impl;

import com.helix.dove.auth.entity.User;
import com.helix.dove.auth.service.PasswordService;
import com.helix.dove.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final PasswordEncoder passwordEncoder;

    private static final String RESET_KEY_PREFIX = "password:reset:";
    private static final long RESET_TOKEN_EXPIRE = 30; // 30分钟过期

    @Override
    public void sendResetPasswordEmail(String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            log.warn("User not found with email: {}", email);
            return;
        }

        String token = UUID.randomUUID().toString();
        String key = RESET_KEY_PREFIX + token;
        redisTemplate.opsForValue().set(key, user.getId().toString(), RESET_TOKEN_EXPIRE, TimeUnit.MINUTES);

        try {
            sendEmail(user.getEmail(), token);
        } catch (MessagingException e) {
            log.error("Failed to send reset password email", e);
            throw new RuntimeException("Failed to send reset password email");
        }
    }

    @Override
    public boolean validateResetToken(String token) {
        String key = RESET_KEY_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        String key = RESET_KEY_PREFIX + token;
        String userId = redisTemplate.opsForValue().get(key);
        if (userId == null) {
            throw new IllegalArgumentException("Invalid or expired reset token");
        }

        User user = userService.findById(Long.parseLong(userId));
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userService.updateUser(user);
        redisTemplate.delete(key);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid old password");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userService.updateUser(user);
    }

    private void sendEmail(String to, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        Context context = new Context();
        context.setVariable("resetLink", "https://your-domain.com/reset-password?token=" + token);
        String content = templateEngine.process("reset-password", context);

        helper.setTo(to);
        helper.setSubject("Reset Your Password");
        helper.setText(content, true);
        
        mailSender.send(message);
    }
} 