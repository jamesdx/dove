package com.dove.auth.core.audit.aspect;

import com.dove.auth.core.audit.annotation.Audit;
import com.dove.auth.core.audit.service.AuditService;
import com.dove.auth.core.utils.SecurityUtils;
import com.dove.common.core.model.LoginUser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 审计切面
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    @Around("@annotation(com.dove.auth.core.audit.annotation.Audit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return point.proceed();
        }

        // 获取注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Audit audit = signature.getMethod().getAnnotation(Audit.class);

        try {
            // 执行方法
            Object result = point.proceed();

            // 记录审计日志
            auditService.log(
                audit.type(),
                loginUser.getUserId(),
                loginUser.getUsername(),
                "成功",
                audit.description()
            );

            return result;
        } catch (Exception e) {
            // 记录异常审计日志
            auditService.logError(
                audit.type(),
                loginUser.getUserId(),
                loginUser.getUsername(),
                audit.description(),
                e
            );
            throw e;
        }
    }
} 