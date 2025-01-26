package com.helix.dove.user.service;

import com.helix.dove.common.exception.GlobalException;
import com.helix.dove.user.entity.User;
import com.helix.dove.user.mapper.UserMapper;
import com.helix.dove.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private final String TEST_TENANT_ID = "test_tenant";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("hashedpassword");
        testUser.setStatus(1);
        testUser.setAccountType(1);
        testUser.setTenantId(TEST_TENANT_ID);
    }

    @Test
    void createUser_Success() {
        when(userMapper.insert(any(User.class))).thenReturn(1);
        when(userService.getByUsername(anyString(), anyString())).thenReturn(null);
        when(userService.getByEmail(anyString(), anyString())).thenReturn(null);

        User result = userService.createUser(testUser);

        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(1, result.getStatus());
        assertEquals(0, result.getFailedAttempts());
        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    void createUser_DuplicateUsername() {
        when(userService.getByUsername(anyString(), anyString())).thenReturn(testUser);

        assertThrows(GlobalException.class, () -> userService.createUser(testUser));
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void createUser_DuplicateEmail() {
        when(userService.getByUsername(anyString(), anyString())).thenReturn(null);
        when(userService.getByEmail(anyString(), anyString())).thenReturn(testUser);

        assertThrows(GlobalException.class, () -> userService.createUser(testUser));
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void updateLocaleSettings_Success() {
        when(userMapper.selectById(anyLong())).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        User result = userService.updateLocaleSettings(1L, "en_US", "UTC",
                "yyyy-MM-dd", "HH:mm:ss", "USD", "US", TEST_TENANT_ID);

        assertNotNull(result);
        assertEquals("en_US", result.getLocale());
        assertEquals("UTC", result.getTimezone());
        assertEquals("yyyy-MM-dd", result.getDateFormat());
        assertEquals("HH:mm:ss", result.getTimeFormat());
        assertEquals("USD", result.getCurrency());
        assertEquals("US", result.getRegion());
        verify(userMapper, times(1)).updateById(any(User.class));
    }

    @Test
    void updateLocaleSettings_UserNotFound() {
        when(userMapper.selectById(anyLong())).thenReturn(null);

        assertThrows(GlobalException.class, () ->
                userService.updateLocaleSettings(1L, "en_US", "UTC",
                        "yyyy-MM-dd", "HH:mm:ss", "USD", "US", TEST_TENANT_ID));
        verify(userMapper, never()).updateById(any(User.class));
    }

    @Test
    void lockUserAccount_Success() {
        when(userMapper.selectById(anyLong())).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        User result = userService.lockUserAccount(1L, 30, TEST_TENANT_ID);

        assertNotNull(result);
        assertEquals(2, result.getStatus());
        assertNotNull(result.getLockedUntil());
        verify(userMapper, times(1)).updateById(any(User.class));
    }

    @Test
    void isUserAccountLocked_True() {
        testUser.setStatus(2);
        testUser.setLockedUntil(java.time.LocalDateTime.now().plusMinutes(30));
        when(userMapper.selectById(anyLong())).thenReturn(testUser);

        boolean result = userService.isUserAccountLocked(1L, TEST_TENANT_ID);

        assertTrue(result);
    }

    @Test
    void isUserAccountLocked_False() {
        testUser.setStatus(1);
        testUser.setLockedUntil(null);
        when(userMapper.selectById(anyLong())).thenReturn(testUser);

        boolean result = userService.isUserAccountLocked(1L, TEST_TENANT_ID);

        assertFalse(result);
    }
} 