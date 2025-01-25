package com.helix.dove.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.helix.dove.auth.entity.User;
import com.helix.dove.auth.mapper.UserMapper;
import com.helix.dove.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public User findByEmail(String email) {
        return userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
        );
    }

    @Override
    public void updateLastLoginTime(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);
    }

    @Override
    public void updateUserSettings(User user) {
        userMapper.updateById(user);
    }
} 