package com.dove.auth.core.auth.provider;

import com.dove.auth.core.auth.LoginTypeDetector;
import com.dove.auth.core.security.SecurityEventPublisher;
import com.dove.auth.core.security.event.LoginFailureEvent;
import com.dove.auth.core.security.event.LoginSuccessEvent;
import com.dove.common.core.model.LoginUser;
import com.dove.common.core.enums.LoginType;
import com.dove.user.api.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 默认认证提供者
 */
@Component
@RequiredArgsConstructor
public class DefaultAuthenticationProvider implements AuthenticationProvider {

    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final LoginTypeDetector loginTypeDetector;
    private final SecurityEventPublisher eventPublisher;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String identifier = authentication.getName();
        String password = authentication.getCredentials().toString();

        try {
            // 检测登录类型
            LoginType loginType = loginTypeDetector.detectLoginType(identifier);

            // 获取用户信息
            LoginUser loginUser = switch (loginType) {
                case USERNAME -> userServiceClient.getUserByUsername(identifier);
                case EMAIL -> userServiceClient.getUserByEmail(identifier);
                case PHONE -> userServiceClient.getUserByPhone(identifier);
            };

            if (loginUser == null) {
                throw new BadCredentialsException("用户不存在");
            }

            // 验证密码
            if (!passwordEncoder.matches(password, loginUser.getPassword())) {
                eventPublisher.publishEvent(new LoginFailureEvent(identifier, "密码错误"));
                throw new BadCredentialsException("密码错误");
            }

            // 构建认证信息
            UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());

            // 发布登录成功事件
            eventPublisher.publishEvent(new LoginSuccessEvent(loginUser));

            return authenticationToken;

        } catch (AuthenticationException e) {
            eventPublisher.publishEvent(new LoginFailureEvent(identifier, e.getMessage()));
            throw e;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
} 