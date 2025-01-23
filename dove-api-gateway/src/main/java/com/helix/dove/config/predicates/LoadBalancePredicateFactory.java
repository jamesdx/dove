package com.helix.dove.config.predicates;

import lombok.Data;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@Component
public class LoadBalancePredicateFactory extends AbstractRoutePredicateFactory<LoadBalancePredicateFactory.Config> {

    private final AtomicInteger currentLoad = new AtomicInteger(0);

    public LoadBalancePredicateFactory() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            int load = currentLoad.get();
            
            // 检查当前负载是否在阈值内
            if (load < config.getMaxLoad()) {
                currentLoad.incrementAndGet();
                exchange.getResponse().beforeCommit(() -> {
                    currentLoad.decrementAndGet();
                    return true;
                });
                return true;
            }
            
            return false;
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("maxLoad");
    }

    @Data
    public static class Config {
        private int maxLoad = 1000;
    }
}