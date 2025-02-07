package com.dove.auth.controller.login;

import com.dove.common.web.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 第三方用户控制器
 */
@RestController
@RequestMapping("/auth/third-party")
@RequiredArgsConstructor
public class ThirdPartyUserController {

    /**
     * 第三方登录回调
     */
    @GetMapping("/callback/{type}")
    public Result<Void> callback(@PathVariable String type, @RequestParam String code) {
        // TODO: 实现第三方登录回调逻辑
        return Result.success();
    }

    /**
     * 获取第三方登录二维码
     */
    @GetMapping("/qrcode/{type}")
    public Result<String> getQRCode(@PathVariable String type) {
        // TODO: 实现获取二维码逻辑
        return Result.success();
    }

    /**
     * 绑定第三方账号
     */
    @PostMapping("/bind/{type}")
    public Result<Void> bind(@PathVariable String type, @RequestParam String code) {
        // TODO: 实现绑定第三方账号逻辑
        return Result.success();
    }

    /**
     * 解绑第三方账号
     */
    @PostMapping("/unbind/{type}")
    public Result<Void> unbind(@PathVariable String type) {
        // TODO: 实现解绑第三方账号逻辑
        return Result.success();
    }
} 