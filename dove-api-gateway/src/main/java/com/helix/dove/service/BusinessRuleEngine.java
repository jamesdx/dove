package com.helix.dove.service;

import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

/**
 * Interface for evaluating business rules in the gateway routing.
 */
public interface BusinessRuleEngine {
    /**
     * Evaluate a percentage-based split rule.
     *
     * @param exchange the current server exchange
     * @param ruleParams the rule parameters
     * @return true if the rule passes, false otherwise
     */
    boolean evaluatePercentageSplit(ServerWebExchange exchange, Map<String, Object> ruleParams);

    /**
     * Evaluate a user group based rule.
     *
     * @param exchange the current server exchange
     * @param ruleParams the rule parameters
     * @return true if the rule passes, false otherwise
     */
    boolean evaluateUserGroup(ServerWebExchange exchange, Map<String, Object> ruleParams);

    /**
     * Evaluate a feature flag based rule.
     *
     * @param exchange the current server exchange
     * @param ruleParams the rule parameters
     * @return true if the rule passes, false otherwise
     */
    boolean evaluateFeatureFlag(ServerWebExchange exchange, Map<String, Object> ruleParams);

    /**
     * Evaluate a custom rule.
     *
     * @param exchange the current server exchange
     * @param ruleParams the rule parameters
     * @return true if the rule passes, false otherwise
     */
    boolean evaluateCustomRule(ServerWebExchange exchange, Map<String, Object> ruleParams);

    /**
     * Evaluate combined rules.
     *
     * @param exchange the current server exchange
     * @param ruleParams the rule parameters
     * @param operator the operator to combine rules (AND/OR)
     * @return true if the combined rules pass, false otherwise
     */
    boolean evaluateCombinedRules(ServerWebExchange exchange, Map<String, Object> ruleParams, String operator);

    /**
     * Evaluate a generic rule.
     *
     * @param exchange the current server exchange
     * @param ruleParams the rule parameters
     * @return true if the rule passes, false otherwise
     */
    boolean evaluateRule(ServerWebExchange exchange, Map<String, Object> ruleParams);
} 