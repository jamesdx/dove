package com.dove.auth.controller.login;

import com.dove.common.web.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登出控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LogoutController {

    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        // TODO: 实现登出逻辑
        return Result.success();
    }
} 