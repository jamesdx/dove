package com.helix.dove.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helix.dove.auth.dto.LoginDTO;
import com.helix.dove.auth.dto.LoginResultDTO;
import com.helix.dove.auth.dto.RegisterUserDTO;
import com.helix.dove.auth.dto.ResetPasswordDTO;
import com.helix.dove.auth.entity.User;
import com.helix.dove.auth.mapper.UserMapper;
import com.helix.dove.auth.service.UserService;
import com.helix.dove.auth.util.JwtTokenUtil;
import com.helix.dove.common.api.ResultCode;
import com.helix.dove.common.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * User Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtTokenUtil jwtTokenUtil;

    private static final String EMAIL_CODE_PREFIX = "email:code:";
    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final String CAPTCHA_CODE_PREFIX = "captcha:code:";
    private static final String LOGIN_FAILED_COUNT_PREFIX = "login:failed:count:";

    private static final int MAX_LOGIN_FAILED_COUNT = 3;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(RegisterUserDTO registerUserDTO) {
        // Verify password match
        if (!Objects.equals(registerUserDTO.getPassword(), registerUserDTO.getConfirmPassword())) {
            throw new GlobalException(ResultCode.VALIDATE_FAILED, "Passwords do not match");
        }

        // Verify username not exists
        if (checkUsernameExists(registerUserDTO.getUsername())) {
            throw new GlobalException(ResultCode.VALIDATE_FAILED, "Username already exists");
        }

        // Verify email not exists
        if (checkEmailExists(registerUserDTO.getEmail())) {
            throw new GlobalException(ResultCode.VALIDATE_FAILED, "Email already exists");
        }

        // Verify mobile not exists
        if (registerUserDTO.getMobile() != null && checkMobileExists(registerUserDTO.getMobile())) {
            throw new GlobalException(ResultCode.VALIDATE_FAILED, "Mobile already exists");
        }

        // Verify captcha
        if (!verifyCaptcha(registerUserDTO.getCaptchaKey(), registerUserDTO.getCaptchaCode())) {
            throw new GlobalException(ResultCode.VALIDATE_FAILED, "Invalid captcha code");
        }

        // Verify verification code
        if ("email".equals(registerUserDTO.getVerificationCodeType())) {
            if (!verifyEmailCode(registerUserDTO.getEmail(), registerUserDTO.getVerificationCode())) {
                throw new GlobalException(ResultCode.VALIDATE_FAILED, "Invalid email verification code");
            }
        } else if ("sms".equals(registerUserDTO.getVerificationCodeType())) {
            if (!verifySmsCode(registerUserDTO.getMobile(), registerUserDTO.getVerificationCode())) {
                throw new GlobalException(ResultCode.VALIDATE_FAILED, "Invalid SMS verification code");
            }
        }

        // Create user
        User user = new User();
        user.setUsername(registerUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        user.setEmail(registerUserDTO.getEmail());
        user.setMobile(registerUserDTO.getMobile());
        user.setNickname(registerUserDTO.getNickname());
        user.setStatus(1);
        user.setTenantId(registerUserDTO.getTenantCode());

        // Save user
        save(user);

        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResultDTO login(LoginDTO loginDTO) {
        // Check login failed count
        String loginFailedKey = LOGIN_FAILED_COUNT_PREFIX + loginDTO.getAccount();
        Integer failedCount = (Integer) redisTemplate.opsForValue().get(loginFailedKey);
        if (failedCount != null && failedCount >= MAX_LOGIN_FAILED_COUNT) {
            // Verify captcha if login failed too many times
            if (loginDTO.getCaptchaCode() == null || !verifyCaptcha(loginDTO.getCaptchaKey(), loginDTO.getCaptchaCode())) {
                throw new GlobalException(ResultCode.VALIDATE_FAILED, "Invalid captcha code");
            }
        }

        // Get user by account type
        User user;
        switch (loginDTO.getAccountType()) {
            case "username":
                user = getUserByUsername(loginDTO.getAccount());
                break;
            case "email":
                user = getUserByEmail(loginDTO.getAccount());
                break;
            case "mobile":
                user = getUserByMobile(loginDTO.getAccount());
                break;
            default:
                throw new GlobalException(ResultCode.VALIDATE_FAILED, "Invalid account type");
        }

        // Check user exists
        if (user == null) {
            increaseLoginFailedCount(loginFailedKey);
            throw new GlobalException(ResultCode.ACCOUNT_NOT_EXIST);
        }

        // Check tenant
        if (loginDTO.getTenantCode() != null && !Objects.equals(loginDTO.getTenantCode(), user.getTenantId())) {
            increaseLoginFailedCount(loginFailedKey);
            throw new GlobalException(ResultCode.VALIDATE_FAILED, "Invalid tenant code");
        }

        // Check password
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            increaseLoginFailedCount(loginFailedKey);
            throw new GlobalException(ResultCode.USERNAME_OR_PASSWORD_INCORRECT);
        }

        // Check user status
        if (user.getStatus() == 0) {
            throw new GlobalException(ResultCode.ACCOUNT_DISABLED);
        }

        // Clear login failed count
        redisTemplate.delete(loginFailedKey);

        // Update user login info
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(loginDTO.getLoginIp());
        updateById(user);

        // Generate token
        String token = jwtTokenUtil.generateToken(user.getUsername(), user.getTenantId(), 
                loginDTO.getRememberMe() != null && loginDTO.getRememberMe());

        // Build login result
        return LoginResultDTO.builder()
                .accessToken(token)
                .tokenType(jwtTokenUtil.formatToken("").trim())
                .refreshToken(null) // TODO: Implement refresh token
                .expiresIn(loginDTO.getRememberMe() != null && loginDTO.getRememberMe() ? 
                        7 * 24 * 60 * 60L : 24 * 60 * 60L)
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .tenantCode(user.getTenantId())
                .roles(new String[]{}) // TODO: Get user roles
                .permissions(new String[]{}) // TODO: Get user permissions
                .build();
    }

    private void increaseLoginFailedCount(String key) {
        Integer failedCount = (Integer) redisTemplate.opsForValue().get(key);
        if (failedCount == null) {
            failedCount = 1;
        } else {
            failedCount++;
        }
        redisTemplate.opsForValue().set(key, failedCount, 1, TimeUnit.HOURS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        // Verify passwords match
        if (!Objects.equals(resetPasswordDTO.getNewPassword(), resetPasswordDTO.getConfirmNewPassword())) {
            throw new GlobalException(ResultCode.VALIDATE_FAILED, "Passwords do not match");
        }

        // Verify captcha
        if (!verifyCaptcha(resetPasswordDTO.getCaptchaKey(), resetPasswordDTO.getCaptchaCode())) {
            throw new GlobalException(ResultCode.VALIDATE_FAILED, "Invalid captcha code");
        }

        // Get user by account type
        User user;
        if ("email".equals(resetPasswordDTO.getAccountType())) {
            user = getUserByEmail(resetPasswordDTO.getAccount());
            if (user == null) {
                throw new GlobalException(ResultCode.VALIDATE_FAILED, "Email not found");
            }
            // Verify email code
            if (!verifyEmailCode(resetPasswordDTO.getAccount(), resetPasswordDTO.getVerificationCode())) {
                throw new GlobalException(ResultCode.VALIDATE_FAILED, "Invalid email verification code");
            }
        } else if ("mobile".equals(resetPasswordDTO.getAccountType())) {
            user = getUserByMobile(resetPasswordDTO.getAccount());
            if (user == null) {
                throw new GlobalException(ResultCode.VALIDATE_FAILED, "Mobile not found");
            }
            // Verify SMS code
            if (!verifySmsCode(resetPasswordDTO.getAccount(), resetPasswordDTO.getVerificationCode())) {
                throw new GlobalException(ResultCode.VALIDATE_FAILED, "Invalid SMS verification code");
            }
        } else {
            throw new GlobalException(ResultCode.VALIDATE_FAILED, "Invalid account type");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        updateById(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return baseMapper.exists(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
    }

    @Override
    public boolean checkEmailExists(String email) {
        return baseMapper.exists(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email));
    }

    @Override
    public boolean checkMobileExists(String mobile) {
        return baseMapper.exists(new LambdaQueryWrapper<User>()
                .eq(User::getMobile, mobile));
    }

    @Override
    public User getUserByEmail(String email) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email));
    }

    @Override
    public User getUserByMobile(String mobile) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getMobile, mobile));
    }

    @Override
    public boolean verifyEmailCode(String email, String code) {
        String key = EMAIL_CODE_PREFIX + email;
        return verifyCode(key, code);
    }

    @Override
    public boolean verifySmsCode(String mobile, String code) {
        String key = SMS_CODE_PREFIX + mobile;
        return verifyCode(key, code);
    }

    @Override
    public boolean verifyCaptcha(String key, String code) {
        String fullKey = CAPTCHA_CODE_PREFIX + key;
        return verifyCode(fullKey, code);
    }

    private boolean verifyCode(String key, String code) {
        Object cacheCode = redisTemplate.opsForValue().get(key);
        if (cacheCode == null) {
            return false;
        }
        boolean result = Objects.equals(cacheCode.toString(), code);
        if (result) {
            redisTemplate.delete(key);
        }
        return result;
    }
} 