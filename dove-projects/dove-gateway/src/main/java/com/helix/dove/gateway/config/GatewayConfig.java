package com.helix.dove.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

/**
 * Gateway Configuration
 */
@Configuration
public class GatewayConfig {

    @PostConstruct
    public void init() {
        // Custom response when blocked by Sentinel
        GatewayCallbackManager.setBlockHandler((exchange, t) -> {
            String message = "{\"code\":429,\"message\":\"" + t.getClass().getSimpleName() + "\",\"data\":null}";
            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(message)
                    .then();
        });
    }
} 