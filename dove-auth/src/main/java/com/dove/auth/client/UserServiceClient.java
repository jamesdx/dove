package com.dove.auth.client;

import com.dove.common.core.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务客户端
 */
@FeignClient(name = "dove-user", path = "/user")
public interface UserServiceClient {

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/getByUsername")
    LoginUser getUserByUsername(@RequestParam("username") String username);

    /**
     * 根据邮箱获取用户信息
     */
    @GetMapping("/getByEmail")
    LoginUser getUserByEmail(@RequestParam("email") String email);

    /**
     * 根据手机号获取用户信息
     */
    @GetMapping("/getByPhone")
    LoginUser getUserByPhone(@RequestParam("phone") String phone);

    /**
     * 更新用户登录信息
     */
    @GetMapping("/updateLoginInfo")
    void updateLoginInfo(@RequestParam("userId") Long userId, 
                        @RequestParam("loginIp") String loginIp,
                        @RequestParam("loginTime") Long loginTime);
} 