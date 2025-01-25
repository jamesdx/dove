package com.helix.dove.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helix.dove.auth.domain.dto.LoginRequest;
import com.helix.dove.auth.domain.dto.LoginResponse;
import com.helix.dove.auth.domain.dto.RegisterRequest;
import com.helix.dove.auth.domain.dto.RegisterResponse;
import com.helix.dove.auth.domain.entity.User;
import com.helix.dove.auth.mapper.UserMapper;
import com.helix.dove.auth.service.UserService;
import com.helix.dove.auth.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest request) {
        User user = getByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // 更新最后登录时间
        updateLastLoginTime(user.getId());

        // 生成token
        String token = jwtUtil.generateToken(user);

        return LoginResponse.of(token, user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse register(RegisterRequest request) {
        // 验证密码确认
        Assert.isTrue(Objects.equals(request.getPassword(), request.getConfirmPassword()),
                "Password confirmation does not match");

        // 验证用户名和邮箱是否可用
        Assert.isTrue(isUsernameAvailable(request.getUsername()),
                "Username is already taken");
        Assert.isTrue(isEmailAvailable(request.getEmail()),
                "Email is already registered");

        // 创建用户
        User user = new User()
                .setUsername(request.getUsername())
                .setEmail(request.getEmail())
                .setPassword(passwordEncoder.encode(request.getPassword()))
                .setLocale(request.getLocale())
                .setTimezone(request.getTimezone())
                .setTenantId(request.getTenantId())
                .setStatus(1);

        // 保存用户
        save(user);

        return RegisterResponse.of("Registration successful", user);
    }

    @Override
    public User getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getDeleted, 0));
    }

    @Override
    public User getByEmail(String email) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .eq(User::getDeleted, 0));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLastLoginTime(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        updateById(user);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return !exists(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getDeleted, 0));
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return !exists(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .eq(User::getDeleted, 0));
    }
} 