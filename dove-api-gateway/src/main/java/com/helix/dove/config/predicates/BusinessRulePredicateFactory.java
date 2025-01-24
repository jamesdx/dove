package com.helix.dove.config.predicates;

import com.helix.dove.service.BusinessRuleEngine;
import lombok.Data;
import lombok.Setter;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Route predicate factory that filters requests based on business rules.
 */
@Component
public class BusinessRulePredicateFactory extends AbstractRoutePredicateFactory<BusinessRulePredicateFactory.Config> {

    @Setter
    private BusinessRuleEngine ruleEngine;

    public BusinessRulePredicateFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("ruleType", "ruleParams", "combinationOperator");
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            if (ruleEngine == null) {
                return false;
            }

            switch (config.getRuleType()) {
                case "PERCENTAGE_SPLIT":
                    return ruleEngine.evaluatePercentageSplit(exchange, config.getRuleParams());
                case "USER_GROUP":
                    return ruleEngine.evaluateUserGroup(exchange, config.getRuleParams());
                case "FEATURE_FLAG":
                    return ruleEngine.evaluateFeatureFlag(exchange, config.getRuleParams());
                case "CUSTOM_RULE":
                    return ruleEngine.evaluateCustomRule(exchange, config.getRuleParams());
                case "COMBINED":
                    return ruleEngine.evaluateCombinedRules(exchange, config.getRuleParams(), 
                        config.getCombinationOperator());
                default:
                    return ruleEngine.evaluateRule(exchange, config.getRuleParams());
            }
        };
    }

    /**
     * Configuration class for BusinessRulePredicateFactory.
     */
    @Data
    public static class Config {
        /**
         * The type of business rule to apply.
         */
        private String ruleType;

        /**
         * Parameters for the business rule.
         */
        private Map<String, Object> ruleParams;

        /**
         * Operator for combining multiple rules (AND/OR).
         */
        private String combinationOperator;
    }
}