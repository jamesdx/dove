package com.helix.dove.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helix.dove.common.api.ResultCode;
import com.helix.dove.common.exception.GlobalException;
import com.helix.dove.user.entity.User;
import com.helix.dove.user.mapper.UserMapper;
import com.helix.dove.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * User Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getByUsername(String username, String tenantId) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getTenantId, tenantId));
    }

    @Override
    public User getByEmail(String email, String tenantId) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .eq(User::getTenantId, tenantId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User createUser(User user) {
        // Check username uniqueness
        if (getByUsername(user.getUsername(), user.getTenantId()) != null) {
            throw new GlobalException(ResultCode.DUPLICATE_USERNAME);
        }

        // Check email uniqueness
        if (getByEmail(user.getEmail(), user.getTenantId()) != null) {
            throw new GlobalException(ResultCode.DUPLICATE_EMAIL);
        }

        // Set default values
        user.setStatus(1); // Normal status
        user.setFailedAttempts(0);
        user.setLocale(user.getLocale() != null ? user.getLocale() : "en_US");
        user.setTimezone(user.getTimezone() != null ? user.getTimezone() : "UTC");
        user.setDateFormat(user.getDateFormat() != null ? user.getDateFormat() : "yyyy-MM-dd");
        user.setTimeFormat(user.getTimeFormat() != null ? user.getTimeFormat() : "HH:mm:ss");
        user.setCurrency(user.getCurrency() != null ? user.getCurrency() : "USD");

        // Save user
        save(user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(User user) {
        // Get existing user
        User existingUser = getById(user.getId());
        if (existingUser == null) {
            throw new GlobalException(ResultCode.USER_NOT_FOUND);
        }

        // Check username uniqueness if changed
        if (!existingUser.getUsername().equals(user.getUsername()) &&
                getByUsername(user.getUsername(), user.getTenantId()) != null) {
            throw new GlobalException(ResultCode.DUPLICATE_USERNAME);
        }

        // Check email uniqueness if changed
        if (!existingUser.getEmail().equals(user.getEmail()) &&
                getByEmail(user.getEmail(), user.getTenantId()) != null) {
            throw new GlobalException(ResultCode.DUPLICATE_EMAIL);
        }

        // Update user
        updateById(user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateStatus(Long userId, Integer status, String tenantId) {
        User user = getById(userId);
        if (user == null || !user.getTenantId().equals(tenantId)) {
            throw new GlobalException(ResultCode.USER_NOT_FOUND);
        }

        user.setStatus(status);
        updateById(user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateLocaleSettings(Long userId, String locale, String timezone, String dateFormat,
            String timeFormat, String currency, String region, String tenantId) {
        User user = getById(userId);
        if (user == null || !user.getTenantId().equals(tenantId)) {
            throw new GlobalException(ResultCode.USER_NOT_FOUND);
        }

        user.setLocale(locale);
        user.setTimezone(timezone);
        user.setDateFormat(dateFormat);
        user.setTimeFormat(timeFormat);
        user.setCurrency(currency);
        user.setRegion(region);
        updateById(user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLastLoginInfo(Long userId, String loginIp, String tenantId) {
        User user = getById(userId);
        if (user == null || !user.getTenantId().equals(tenantId)) {
            throw new GlobalException(ResultCode.USER_NOT_FOUND);
        }

        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(loginIp);
        user.setFailedAttempts(0);
        user.setLockedUntil(null);
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User recordFailedLoginAttempt(Long userId, String tenantId) {
        User user = getById(userId);
        if (user == null || !user.getTenantId().equals(tenantId)) {
            throw new GlobalException(ResultCode.USER_NOT_FOUND);
        }

        user.setFailedAttempts(user.getFailedAttempts() + 1);
        updateById(user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetFailedLoginAttempts(Long userId, String tenantId) {
        User user = getById(userId);
        if (user == null || !user.getTenantId().equals(tenantId)) {
            throw new GlobalException(ResultCode.USER_NOT_FOUND);
        }

        user.setFailedAttempts(0);
        user.setLockedUntil(null);
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User lockUserAccount(Long userId, Integer lockDuration, String tenantId) {
        User user = getById(userId);
        if (user == null || !user.getTenantId().equals(tenantId)) {
            throw new GlobalException(ResultCode.USER_NOT_FOUND);
        }

        user.setStatus(2); // Locked status
        user.setLockedUntil(LocalDateTime.now().plusMinutes(lockDuration));
        updateById(user);
        return user;
    }

    @Override
    public boolean isUserAccountLocked(Long userId, String tenantId) {
        User user = getById(userId);
        if (user == null || !user.getTenantId().equals(tenantId)) {
            throw new GlobalException(ResultCode.USER_NOT_FOUND);
        }

        return user.getStatus() == 2 && user.getLockedUntil() != null && 
                user.getLockedUntil().isAfter(LocalDateTime.now());
    }
} 