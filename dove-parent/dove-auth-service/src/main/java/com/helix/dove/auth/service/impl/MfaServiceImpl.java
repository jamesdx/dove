package com.helix.dove.auth.service.impl;

import com.helix.dove.auth.entity.User;
import com.helix.dove.auth.service.MfaService;
import com.helix.dove.auth.service.UserService;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Slf4j
@Service
@RequiredArgsConstructor
public class MfaServiceImpl implements MfaService {

    private final UserService userService;
    private final SecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final TimeProvider timeProvider = new SystemTimeProvider();
    private final CodeGenerator codeGenerator = new DefaultCodeGenerator();
    private final CodeVerifier codeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

    @Override
    public MfaSetupResult generateMfaSecret(Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        String secret = secretGenerator.generate();
        QrData qrData = new QrData.Builder()
            .label(user.getEmail())
            .secret(secret)
            .issuer("Dove Auth")
            .algorithm(HashingAlgorithm.SHA1)
            .digits(6)
            .period(30)
            .build();

        QrGenerator qrGenerator = new ZxingPngQrGenerator();
        byte[] imageData;
        try {
            imageData = qrGenerator.generate(qrData);
        } catch (QrGenerationException e) {
            throw new RuntimeException("Error generating QR code", e);
        }

        String qrCodeUrl = getDataUriForImage(imageData, qrGenerator.getImageMimeType());
        
        // 临时保存secret，等待验证
        user.setTempMfaSecret(secret);
        userService.updateUser(user);

        return new MfaSetupResult(secret, qrCodeUrl);
    }

    @Override
    public void enableMfa(Long userId, String code) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        String secret = user.getTempMfaSecret();
        if (secret == null) {
            throw new IllegalArgumentException("No MFA setup in progress");
        }

        if (!verifyCode(secret, code)) {
            throw new IllegalArgumentException("Invalid MFA code");
        }

        user.setMfaSecret(secret);
        user.setTempMfaSecret(null);
        user.setMfaEnabled(true);
        userService.updateUser(user);
    }

    @Override
    public void disableMfa(Long userId, String code) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (!user.isMfaEnabled()) {
            throw new IllegalArgumentException("MFA is not enabled");
        }

        if (!verifyCode(user.getMfaSecret(), code)) {
            throw new IllegalArgumentException("Invalid MFA code");
        }

        user.setMfaSecret(null);
        user.setMfaEnabled(false);
        userService.updateUser(user);
    }

    @Override
    public boolean verifyCode(Long userId, String code) {
        User user = userService.findById(userId);
        if (user == null || !user.isMfaEnabled()) {
            return false;
        }
        return verifyCode(user.getMfaSecret(), code);
    }

    private boolean verifyCode(String secret, String code) {
        return codeVerifier.isValidCode(secret, code);
    }
} 