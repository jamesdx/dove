package com.helix.dove.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Locale;

/**
 * Internationalization Configuration
 */
@Configuration
public class LocaleConfig implements WebFilter {

    private static final String LANGUAGE_HEADER = "Accept-Language";
    private static final String DEFAULT_LANGUAGE = "en";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String language = request.getHeaders().getFirst(LANGUAGE_HEADER);

        // If language is not provided, use default language
        if (language == null) {
            language = DEFAULT_LANGUAGE;
        }

        // Parse language to locale
        Locale locale = Locale.forLanguageTag(language);

        // Add locale to the request attributes
        exchange.getAttributes().put(LANGUAGE_HEADER, locale);

        // Add language to the request headers
        ServerHttpRequest mutatedRequest = request.mutate()
                .header(LANGUAGE_HEADER, language)
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }
} 