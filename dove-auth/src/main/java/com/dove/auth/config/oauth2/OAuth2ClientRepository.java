package com.dove.auth.config.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2客户端配置仓库
 */
@Component
@RequiredArgsConstructor
public class OAuth2ClientRepository implements ClientRegistrationRepository {

    private final OAuth2Properties oauth2Properties;
    private final Map<String, ClientRegistration> registrations = new HashMap<>();

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        // 从缓存中获取客户端注册信息
        if (registrations.containsKey(registrationId)) {
            return registrations.get(registrationId);
        }

        // 从配置中查找客户端
        OAuth2Properties.Client client = oauth2Properties.getClients().stream()
                .filter(c -> c.getClientId().equals(registrationId))
                .findFirst()
                .orElse(null);

        if (client == null) {
            return null;
        }

        // 创建客户端注册信息
        ClientRegistration registration = ClientRegistration.withRegistrationId(registrationId)
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(client.getRedirectUris().get(0))
                .scope(client.getScopes().toArray(new String[0]))
                .build();

        // 缓存客户端注册信息
        registrations.put(registrationId, registration);

        return registration;
    }

    /**
     * 刷新客户端配置
     */
    public void refresh() {
        registrations.clear();
    }
} 