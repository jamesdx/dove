package com.helix.dove.auth.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CircuitBreakerConfig {

    @PostConstruct
    public void initDegradeRules() {
        List<DegradeRule> rules = new ArrayList<>();
        
        // 登录接口熔断规则
        DegradeRule loginRule = new DegradeRule();
        loginRule.setResource("auth_login");
        loginRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
        loginRule.setCount(0.5); // 异常比例阈值
        loginRule.setTimeWindow(10); // 熔断时长，单位为秒
        loginRule.setMinRequestAmount(100); // 熔断触发的最小请求数
        loginRule.setStatIntervalMs(1000); // 统计时长，单位为毫秒
        rules.add(loginRule);

        // 密码重置接口熔断规则
        DegradeRule resetPasswordRule = new DegradeRule();
        resetPasswordRule.setResource("reset_password");
        resetPasswordRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
        resetPasswordRule.setCount(0.3);
        resetPasswordRule.setTimeWindow(10);
        resetPasswordRule.setMinRequestAmount(50);
        resetPasswordRule.setStatIntervalMs(1000);
        rules.add(resetPasswordRule);

        // MFA验证接口熔断规则
        DegradeRule mfaRule = new DegradeRule();
        mfaRule.setResource("mfa_verify");
        mfaRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
        mfaRule.setCount(0.4);
        mfaRule.setTimeWindow(10);
        mfaRule.setMinRequestAmount(80);
        mfaRule.setStatIntervalMs(1000);
        rules.add(mfaRule);

        // 社交登录接口熔断规则
        DegradeRule socialLoginRule = new DegradeRule();
        socialLoginRule.setResource("social_login");
        socialLoginRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
        socialLoginRule.setCount(0.4);
        socialLoginRule.setTimeWindow(10);
        socialLoginRule.setMinRequestAmount(100);
        socialLoginRule.setStatIntervalMs(1000);
        rules.add(socialLoginRule);

        DegradeRuleManager.loadRules(rules);
    }
} 