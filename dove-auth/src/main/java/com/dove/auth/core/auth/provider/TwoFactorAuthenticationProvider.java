package com.dove.auth.core.auth.provider;

import com.dove.auth.core.auth.service.GoogleAuthenticator;
import com.dove.auth.core.auth.service.SmsCodeService;
import com.dove.auth.core.auth.service.EmailCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * 双因素认证提供者
 */
@Component
@RequiredArgsConstructor
public class TwoFactorAuthenticationProvider implements AuthenticationProvider {

    private final GoogleAuthenticator googleAuthenticator;
    private final SmsCodeService smsCodeService;
    private final EmailCodeService emailCodeService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证信息
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        String twoFactorCode = getTwoFactorCode(authentication);
        String twoFactorType = getTwoFactorType(authentication);

        // 验证双因素认证码
        boolean verified = switch (twoFactorType) {
            case "GOOGLE" -> googleAuthenticator.verify(username, twoFactorCode);
            case "SMS" -> smsCodeService.verify(username, twoFactorCode);
            case "EMAIL" -> emailCodeService.verify(username, twoFactorCode);
            default -> throw new BadCredentialsException("Unsupported two-factor type: " + twoFactorType);
        };

        if (!verified) {
            throw new BadCredentialsException("Invalid two-factor code");
        }

        // 创建认证令牌
        return new UsernamePasswordAuthenticationToken(
                username,
                password,
                authentication.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 获取双因素认证码
     */
    private String getTwoFactorCode(Authentication authentication) {
        Object details = authentication.getDetails();
        if (details instanceof TwoFactorAuthenticationDetails) {
            return ((TwoFactorAuthenticationDetails) details).getTwoFactorCode();
        }
        throw new BadCredentialsException("Two-factor code is required");
    }

    /**
     * 获取双因素认证类型
     */
    private String getTwoFactorType(Authentication authentication) {
        Object details = authentication.getDetails();
        if (details instanceof TwoFactorAuthenticationDetails) {
            return ((TwoFactorAuthenticationDetails) details).getTwoFactorType();
        }
        throw new BadCredentialsException("Two-factor type is required");
    }

    /**
     * 双因素认证详情
     */
    public static class TwoFactorAuthenticationDetails {
        private final String twoFactorCode;
        private final String twoFactorType;

        public TwoFactorAuthenticationDetails(String twoFactorCode, String twoFactorType) {
            this.twoFactorCode = twoFactorCode;
            this.twoFactorType = twoFactorType;
        }

        public String getTwoFactorCode() {
            return twoFactorCode;
        }

        public String getTwoFactorType() {
            return twoFactorType;
        }
    }
} 