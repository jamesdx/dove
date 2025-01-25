package com.helix.dove.auth.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RateLimitConfig {

    @PostConstruct
    public void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();
        
        // 登录接口限流规则
        FlowRule loginRule = new FlowRule();
        loginRule.setResource("auth_login");
        loginRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        loginRule.setCount(1000); // 每秒1000次请求
        loginRule.setLimitApp("default");
        rules.add(loginRule);

        // 密码重置接口限流规则
        FlowRule resetPasswordRule = new FlowRule();
        resetPasswordRule.setResource("reset_password");
        resetPasswordRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        resetPasswordRule.setCount(100); // 每秒100次请求
        resetPasswordRule.setLimitApp("default");
        rules.add(resetPasswordRule);

        // MFA验证接口限流规则
        FlowRule mfaRule = new FlowRule();
        mfaRule.setResource("mfa_verify");
        mfaRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        mfaRule.setCount(500); // 每秒500次请求
        mfaRule.setLimitApp("default");
        rules.add(mfaRule);

        // 社交登录接口限流规则
        FlowRule socialLoginRule = new FlowRule();
        socialLoginRule.setResource("social_login");
        socialLoginRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        socialLoginRule.setCount(800); // 每秒800次请求
        socialLoginRule.setLimitApp("default");
        rules.add(socialLoginRule);

        FlowRuleManager.loadRules(rules);
    }
} 