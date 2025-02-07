package com.dove.auth.controller;

import com.dove.auth.core.auth.LoginTypeDetector;
import com.dove.auth.core.auth.token.TokenService;
import com.dove.common.core.model.LoginUser;
import com.dove.common.core.model.Result;
import com.dove.common.core.enums.LoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final LoginTypeDetector loginTypeDetector;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody @Valid LoginRequest request) {
        // 检测登录类型
        LoginType loginType = loginTypeDetector.detectLoginType(request.getIdentifier());
        
        // 构建认证token
        UsernamePasswordAuthenticationToken authenticationToken = 
            new UsernamePasswordAuthenticationToken(request.getIdentifier(), request.getPassword());
        
        // 执行认证
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        
        // 生成token
        String token = tokenService.createToken(loginUser);
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("loginType", loginType);
        result.put("user", loginUser);
        
        return Result.success(result);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        tokenService.removeToken();
        return Result.success();
    }

    /**
     * 刷新token
     */
    @PostMapping("/refresh")
    public Result<String> refresh() {
        String token = tokenService.refreshToken();
        return Result.success(token);
    }
}

/**
 * 登录请求参数
 */
@Data
class LoginRequest {
    
    /**
     * 登录标识符（用户名/邮箱/手机号）
     */
    @NotBlank(message = "登录标识符不能为空")
    private String identifier;
    
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
    
    /**
     * 验证码
     */
    private String captcha;
    
    /**
     * 记住我
     */
    private Boolean rememberMe;
} 