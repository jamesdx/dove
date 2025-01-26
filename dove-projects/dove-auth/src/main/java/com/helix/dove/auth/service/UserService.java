package com.helix.dove.auth.service;

import com.helix.dove.auth.dto.LoginDTO;
import com.helix.dove.auth.dto.LoginResultDTO;
import com.helix.dove.auth.dto.RegisterUserDTO;
import com.helix.dove.auth.dto.ResetPasswordDTO;
import com.helix.dove.auth.entity.User;

/**
 * User Service Interface
 */
public interface UserService {

    /**
     * Register user
     *
     * @param registerUserDTO register user dto
     * @return user info
     */
    User register(RegisterUserDTO registerUserDTO);

    /**
     * Login
     *
     * @param loginDTO login dto
     * @return login result
     */
    LoginResultDTO login(LoginDTO loginDTO);

    /**
     * Reset password
     *
     * @param resetPasswordDTO reset password dto
     */
    void resetPassword(ResetPasswordDTO resetPasswordDTO);

    /**
     * Get user by username
     *
     * @param username username
     * @return user info
     */
    User getUserByUsername(String username);

    /**
     * Check if username exists
     *
     * @param username username
     * @return true if exists
     */
    boolean checkUsernameExists(String username);

    /**
     * Check if email exists
     *
     * @param email email
     * @return true if exists
     */
    boolean checkEmailExists(String email);

    /**
     * Check if mobile exists
     *
     * @param mobile mobile
     * @return true if exists
     */
    boolean checkMobileExists(String mobile);

    /**
     * Get user by email
     *
     * @param email email
     * @return user info
     */
    User getUserByEmail(String email);

    /**
     * Get user by mobile
     *
     * @param mobile mobile
     * @return user info
     */
    User getUserByMobile(String mobile);

    /**
     * Verify email verification code
     *
     * @param email email
     * @param code verification code
     * @return true if valid
     */
    boolean verifyEmailCode(String email, String code);

    /**
     * Verify SMS verification code
     *
     * @param mobile mobile
     * @param code verification code
     * @return true if valid
     */
    boolean verifySmsCode(String mobile, String code);

    /**
     * Verify captcha code
     *
     * @param key captcha key
     * @param code captcha code
     * @return true if valid
     */
    boolean verifyCaptcha(String key, String code);
} 