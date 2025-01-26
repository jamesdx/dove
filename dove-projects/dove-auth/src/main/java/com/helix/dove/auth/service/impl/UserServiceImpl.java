package com.helix.dove.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helix.dove.auth.dto.RegisterUserDTO;
import com.helix.dove.auth.entity.User;
import com.helix.dove.auth.mapper.UserMapper;
import com.helix.dove.auth.service.UserService;
import com.helix.dove.common.api.ResultCode;
import com.helix.dove.common.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * User Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String EMAIL_CODE_PREFIX = "email:code:";
    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final String CAPTCHA_CODE_PREFIX = "captcha:code:";

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