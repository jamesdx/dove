package com.dove.auth.core.session.filter;

import com.dove.auth.core.session.SessionRegistry;
import com.dove.auth.core.session.model.SessionInformation;
import com.dove.common.core.model.LoginUser;
import com.dove.common.core.model.Result;
import com.dove.common.core.utils.JsonUtils;
import com.dove.common.core.utils.ServletUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 并发会话过滤器
 */
@Component
@RequiredArgsConstructor
public class ConcurrentSessionFilter extends OncePerRequestFilter {

    private final SessionRegistry sessionRegistry;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain chain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            String sessionId = request.getHeader("X-Session-Id");

            if (sessionId != null) {
                SessionInformation session = sessionRegistry.getSessionInformation(sessionId);
                if (session == null) {
                    handleExpiredSession(response, "会话不存在");
                    return;
                }

                if (session.isExpired()) {
                    handleExpiredSession(response, "会话已过期");
                    return;
                }

                // 更新最后访问时间
                sessionRegistry.updateLastAccessTime(sessionId);
            }
        }

        chain.doFilter(request, response);
    }

    private void handleExpiredSession(HttpServletResponse response, String message) throws IOException {
        SecurityContextHolder.clearContext();
        ServletUtils.renderString(response, JsonUtils.toJson(Result.error(message)));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().contains("/auth/login") ||
               request.getRequestURI().contains("/auth/logout") ||
               request.getRequestURI().contains("/auth/captcha");
    }
} 