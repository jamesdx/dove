package com.dove.auth.core.auth.provider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 双因素认证Token
 */
public class TwoFactorAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String code;

    public TwoFactorAuthenticationToken(Object principal, Object credentials, String code) {
        super(principal, credentials);
        this.code = code;
    }

    public TwoFactorAuthenticationToken(Object principal, Object credentials, String code,
                                      Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
} 