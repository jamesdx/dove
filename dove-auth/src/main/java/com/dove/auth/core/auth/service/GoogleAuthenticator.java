package com.dove.auth.core.auth.service;

import com.dove.common.core.utils.Base32;
import com.dove.common.core.utils.QrCodeUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Google认证器服务
 */
@Service
@RequiredArgsConstructor
public class GoogleAuthenticator {

    /**
     * 时间步长(30秒)
     */
    private static final int TIME_STEP = 30;

    /**
     * 验证码长度
     */
    private static final int CODE_DIGITS = 6;

    /**
     * 生成密钥
     */
    public String generateSecret() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    /**
     * 生成二维码URL
     */
    public String generateQrCodeUrl(String issuer, String username, String secret) {
        String format = "otpauth://totp/%s:%s?secret=%s&issuer=%s";
        return String.format(format, issuer, username, secret, issuer);
    }

    /**
     * 生成二维码图片
     */
    public byte[] generateQrCodeImage(String content) {
        return QrCodeUtils.generateQrCode(content);
    }

    /**
     * 验证验证码
     */
    public boolean verify(String secret, String code) {
        if (code == null || code.length() != CODE_DIGITS) {
            return false;
        }

        Base32 base32 = new Base32();
        byte[] decodedKey = base32.decode(secret);

        long currentTime = System.currentTimeMillis() / 1000;
        long timeStep = currentTime / TIME_STEP;

        // 验证前后30秒的验证码
        for (int i = -1; i <= 1; i++) {
            String generatedCode = generateCode(decodedKey, timeStep + i);
            if (generatedCode.equals(code)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 生成验证码
     */
    private String generateCode(byte[] key, long timeStep) {
        try {
            byte[] data = new byte[8];
            long value = timeStep;
            for (int i = 8; i-- > 0; value >>>= 8) {
                data[i] = (byte) value;
            }

            SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signKey);
            byte[] hash = mac.doFinal(data);

            int offset = hash[hash.length - 1] & 0xF;
            long truncatedHash = 0;
            for (int i = 0; i < 4; ++i) {
                truncatedHash <<= 8;
                truncatedHash |= (hash[offset + i] & 0xFF);
            }

            truncatedHash &= 0x7FFFFFFF;
            truncatedHash %= Math.pow(10, CODE_DIGITS);

            return String.format("%0" + CODE_DIGITS + "d", truncatedHash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error generating code", e);
        }
    }
} 