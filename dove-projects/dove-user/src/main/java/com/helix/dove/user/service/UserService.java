package com.helix.dove.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helix.dove.user.entity.User;

/**
 * User Service Interface
 */
public interface UserService extends IService<User> {

    /**
     * Get user by username
     *
     * @param username username
     * @param tenantId tenant ID
     * @return user
     */
    User getByUsername(String username, String tenantId);

    /**
     * Get user by email
     *
     * @param email email
     * @param tenantId tenant ID
     * @return user
     */
    User getByEmail(String email, String tenantId);

    /**
     * Create user
     *
     * @param user user
     * @return user
     */
    User createUser(User user);

    /**
     * Update user
     *
     * @param user user
     * @return user
     */
    User updateUser(User user);

    /**
     * Update user status
     *
     * @param userId user ID
     * @param status status
     * @param tenantId tenant ID
     * @return updated user
     */
    User updateStatus(Long userId, Integer status, String tenantId);

    /**
     * Update user locale settings
     *
     * @param userId user ID
     * @param locale locale
     * @param timezone timezone
     * @param dateFormat date format
     * @param timeFormat time format
     * @param currency currency
     * @param region region
     * @param tenantId tenant ID
     * @return updated user
     */
    User updateLocaleSettings(Long userId, String locale, String timezone, String dateFormat,
            String timeFormat, String currency, String region, String tenantId);

    /**
     * Update last login info
     *
     * @param userId user ID
     * @param loginIp login IP
     * @param tenantId tenant ID
     */
    void updateLastLoginInfo(Long userId, String loginIp, String tenantId);

    /**
     * Record failed login attempt
     *
     * @param userId user ID
     * @param tenantId tenant ID
     * @return updated user
     */
    User recordFailedLoginAttempt(Long userId, String tenantId);

    /**
     * Reset failed login attempts
     *
     * @param userId user ID
     * @param tenantId tenant ID
     */
    void resetFailedLoginAttempts(Long userId, String tenantId);

    /**
     * Lock user account
     *
     * @param userId user ID
     * @param lockDuration lock duration in minutes
     * @param tenantId tenant ID
     * @return updated user
     */
    User lockUserAccount(Long userId, Integer lockDuration, String tenantId);

    /**
     * Check if user account is locked
     *
     * @param userId user ID
     * @param tenantId tenant ID
     * @return true if locked
     */
    boolean isUserAccountLocked(Long userId, String tenantId);
} 