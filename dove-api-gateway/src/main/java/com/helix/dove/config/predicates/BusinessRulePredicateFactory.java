package com.helix.dove.config.predicates;

import lombok.Data;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class BusinessRulePredicateFactory extends AbstractRoutePredicateFactory<BusinessRulePredicateFactory.Config> {

    public BusinessRulePredicateFactory() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            // 获取请求头中的业务参数
            Map<String, String> headers = exchange.getRequest().getHeaders()
                    .toSingleValueMap();
            
            // 检查业务规则
            return matchBusinessRules(headers, config);
        };
    }

    private boolean matchBusinessRules(Map<String, String> headers, Config config) {
        // 实现业务规则匹配逻辑
        String userType = headers.getOrDefault("X-User-Type", "");
        String businessLine = headers.getOrDefault("X-Business-Line", "");

        return config.getUserTypes().contains(userType) &&
               config.getBusinessLines().contains(businessLine);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("userTypes", "businessLines");
    }

    @Data
    public static class Config {
        private List<String> userTypes = Arrays.asList("*");
        private List<String> businessLines = Arrays.asList("*");
    }
}