package com.helix.dove.config.predicates;

import lombok.Data;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class GeoLocationPredicateFactory extends AbstractRoutePredicateFactory<GeoLocationPredicateFactory.Config> {

    public GeoLocationPredicateFactory() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            String clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            // 这里可以集成实际的 IP 地理位置服务
            return isIpInRegion(clientIp, config.getAllowedRegions());
        };
    }

    private boolean isIpInRegion(String ip, List<String> allowedRegions) {
        // 实现 IP 地理位置检查逻辑
        return true; // 默认允许所有
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("allowedRegions");
    }

    @Data
    public static class Config {
        private List<String> allowedRegions = Arrays.asList("*");
    }
}