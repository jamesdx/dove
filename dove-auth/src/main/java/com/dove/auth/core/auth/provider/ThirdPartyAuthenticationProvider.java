package com.dove.auth.core.auth.provider;

import com.dove.auth.service.auth.DingTalkLoginService;
import com.dove.auth.service.auth.WeChatLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * 第三方认证提供者
 */
@Component
@RequiredArgsConstructor
public class ThirdPartyAuthenticationProvider implements AuthenticationProvider {

    private final WeChatLoginService weChatLoginService;
    private final DingTalkLoginService dingTalkLoginService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证信息
        String code = authentication.getCredentials().toString();
        String thirdPartyType = getThirdPartyType(authentication);

        // 根据第三方类型进行认证
        Authentication result = switch (thirdPartyType) {
            case "WECHAT" -> weChatLoginService.loginByCode(code);
            case "DINGTALK" -> dingTalkLoginService.loginByCode(code);
            default -> throw new BadCredentialsException("Unsupported third-party type: " + thirdPartyType);
        };

        // 返回认证结果
        return new UsernamePasswordAuthenticationToken(
                result.getPrincipal(),
                result.getCredentials(),
                result.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 获取第三方认证类型
     */
    private String getThirdPartyType(Authentication authentication) {
        Object details = authentication.getDetails();
        if (details instanceof ThirdPartyAuthenticationDetails) {
            return ((ThirdPartyAuthenticationDetails) details).getThirdPartyType();
        }
        throw new BadCredentialsException("Third-party type is required");
    }

    /**
     * 第三方认证详情
     */
    public static class ThirdPartyAuthenticationDetails {
        private final String thirdPartyType;

        public ThirdPartyAuthenticationDetails(String thirdPartyType) {
            this.thirdPartyType = thirdPartyType;
        }

        public String getThirdPartyType() {
            return thirdPartyType;
        }
    }
} 