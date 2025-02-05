package com.helix.dove.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OAuth2Config {

    @Bean
    @ConfigurationProperties(prefix = "oauth2")
    public OAuth2Properties oauth2Properties() {
        return new OAuth2Properties();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(OAuth2Properties properties) {
        List<ClientRegistration> registrations = new ArrayList<>();
        
        // 配置 Google OAuth2 客户端
        if (properties.getGoogle() != null) {
            registrations.add(ClientRegistration.withRegistrationId("google")
                .clientId(properties.getGoogle().getClientId())
                .clientSecret(properties.getGoogle().getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build());
        }

        return new InMemoryClientRegistrationRepository(registrations);
    }
}

class OAuth2Properties {
    private OAuth2ClientProperties google;

    public OAuth2ClientProperties getGoogle() {
        return google;
    }

    public void setGoogle(OAuth2ClientProperties google) {
        this.google = google;
    }
}

class OAuth2ClientProperties {
    private String clientId;
    private String clientSecret;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
} 