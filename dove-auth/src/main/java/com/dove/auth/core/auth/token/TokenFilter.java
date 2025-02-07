package com.dove.auth.core.auth.token;

import com.dove.common.core.model.LoginUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Token过滤器
 */
@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final TokenProperties tokenProperties;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 获取请求token
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            // 获取登录用户信息
            LoginUser loginUser = tokenService.getLoginUser(token);
            if (loginUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 验证token有效期
                if (tokenService.verifyToken(loginUser)) {
                    // 设置用户身份信息
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            loginUser, null, loginUser.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * 获取请求token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(tokenProperties.getHeader());
        if (StringUtils.isNotEmpty(token) && token.startsWith(tokenProperties.getPrefix())) {
            token = token.substring(tokenProperties.getPrefix().length());
        }
        return token;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 不需要验证token的请求路径
        return request.getServletPath().startsWith("/auth/login") ||
                request.getServletPath().startsWith("/auth/logout") ||
                request.getServletPath().startsWith("/auth/captcha") ||
                request.getServletPath().startsWith("/auth/sms/code") ||
                request.getServletPath().startsWith("/auth/email/code") ||
                request.getServletPath().startsWith("/auth/oauth2") ||
                request.getServletPath().startsWith("/actuator");
    }
} 