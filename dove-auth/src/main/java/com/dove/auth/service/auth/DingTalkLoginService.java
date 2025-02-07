package com.dove.auth.service.auth;

import com.dove.auth.core.event.SecurityEventPublisher;
import com.dove.auth.core.event.LoginSuccessEvent;
import com.dove.auth.core.event.LoginFailureEvent;
import com.dove.common.core.model.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 钉钉登录服务
 */
@Service
@RequiredArgsConstructor
public class DingTalkLoginService {

    private final RestTemplate restTemplate;
    private final SecurityEventPublisher eventPublisher;

    /**
     * 钉钉API地址
     */
    private static final String DINGTALK_API_URL = "https://oapi.dingtalk.com";

    /**
     * 应用Key
     */
    private static final String APP_KEY = "your_app_key";

    /**
     * 应用Secret
     */
    private static final String APP_SECRET = "your_app_secret";

    /**
     * 通过code登录
     */
    public Authentication loginByCode(String code) {
        try {
            // 获取access_token
            String accessToken = getAccessToken();

            // 获取用户信息
            String userInfo = getUserInfo(accessToken, code);

            // TODO: 解析用户信息，创建或更新用户

            // 创建登录用户信息
            LoginUser loginUser = createLoginUser(userInfo);

            // 创建认证信息
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    loginUser, null, AuthorityUtils.createAuthorityList("ROLE_USER"));

            // 发布登录成功事件
            eventPublisher.publish(new LoginSuccessEvent(loginUser));

            return authentication;
        } catch (Exception e) {
            // 发布登录失败事件
            eventPublisher.publish(new LoginFailureEvent("DingTalk", e.getMessage()));
            throw e;
        }
    }

    /**
     * 获取access_token
     */
    private String getAccessToken() {
        String url = String.format("%s/gettoken?appkey=%s&appsecret=%s",
                DINGTALK_API_URL, APP_KEY, APP_SECRET);
        // TODO: 调用钉钉API获取access_token
        return "access_token";
    }

    /**
     * 获取用户信息
     */
    private String getUserInfo(String accessToken, String code) {
        String url = String.format("%s/user/getuserinfo?access_token=%s&code=%s",
                DINGTALK_API_URL, accessToken, code);
        // TODO: 调用钉钉API获取用户信息
        return "user_info";
    }

    /**
     * 创建登录用户信息
     */
    private LoginUser createLoginUser(String userInfo) {
        // TODO: 根据用户信息创建LoginUser对象
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(1L);
        loginUser.setUsername("dingtalk_user");
        return loginUser;
    }
} 