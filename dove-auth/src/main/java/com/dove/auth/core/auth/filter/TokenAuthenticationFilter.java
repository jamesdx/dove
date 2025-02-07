package com.dove.auth.core.auth.filter;

import com.dove.auth.core.auth.token.TokenService;
import com.dove.auth.core.security.SecurityEventPublisher;
import com.dove.auth.core.security.event.TokenExpiredEvent;
import com.dove.common.core.model.LoginUser;
import com.dove.common.core.utils.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Token认证过滤器
 */
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final SecurityEventPublisher eventPublisher;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain chain) throws ServletException, IOException {
        LoginUser loginUser = null;
        try {
            // 获取token
            String token = tokenService.getToken(request);
            if (StringUtils.isNotEmpty(token)) {
                // 获取登录用户信息
                loginUser = tokenService.getLoginUser(token);
                if (loginUser != null) {
                    // 验证token是否过期
                    if (!tokenService.verifyToken(loginUser)) {
                        eventPublisher.publishEvent(new TokenExpiredEvent(loginUser));
                    } else {
                        // 刷新token
                        tokenService.refreshToken(loginUser);
                        
                        // 设置认证信息
                        UsernamePasswordAuthenticationToken authenticationToken = 
                            new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Token认证异常", e);
        }
        
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 不需要验证token的请求路径
        return StringUtils.matches(request.getRequestURI(), 
            "/auth/login",
            "/auth/logout",
            "/auth/captcha",
            "/auth/sms/code",
            "/auth/email/code",
            "/auth/oauth2/**",
            "/actuator/**",
            "/error",
            "/swagger-ui/**",
            "/v3/api-docs/**"
        );
    }
} 