package com.helix.dove.config.predicates;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Component
public class LoadBalancePredicateFactory extends AbstractRoutePredicateFactory<LoadBalancePredicateFactory.Config> {

    public LoadBalancePredicateFactory() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            // 在这里实现负载均衡逻辑
            return true;
        };
    }

    public static class Config {
        private String group;
        private int weight;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }
}