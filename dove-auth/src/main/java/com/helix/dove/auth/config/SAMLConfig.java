package com.helix.dove.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SAMLConfig {

    @Bean
    @ConfigurationProperties(prefix = "saml")
    public SAMLProperties samlProperties() {
        return new SAMLProperties();
    }

    @Bean
    public RelyingPartyRegistrationRepository relyingPartyRegistrationRepository(SAMLProperties properties) {
        List<RelyingPartyRegistration> registrations = new ArrayList<>();

        if (properties.getOkta() != null) {
            RelyingPartyRegistration registration = RelyingPartyRegistrations
                .fromMetadataLocation(properties.getOkta().getMetadataUrl())
                .registrationId("okta")
                .entityId(properties.getOkta().getEntityId())
                .assertionConsumerServiceLocation(properties.getOkta().getAcsUrl())
                .build();
            registrations.add(registration);
        }

        return new InMemoryRelyingPartyRegistrationRepository(registrations);
    }
}

class SAMLProperties {
    private SAMLProviderProperties okta;

    public SAMLProviderProperties getOkta() {
        return okta;
    }

    public void setOkta(SAMLProviderProperties okta) {
        this.okta = okta;
    }
}

class SAMLProviderProperties {
    private String metadataUrl;
    private String entityId;
    private String acsUrl;

    public String getMetadataUrl() {
        return metadataUrl;
    }

    public void setMetadataUrl(String metadataUrl) {
        this.metadataUrl = metadataUrl;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getAcsUrl() {
        return acsUrl;
    }

    public void setAcsUrl(String acsUrl) {
        this.acsUrl = acsUrl;
    }
} 