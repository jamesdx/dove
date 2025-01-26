package com.helix.dove.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helix.dove.user.dto.UserRegistrationDTO;
import com.helix.dove.user.entity.User;
import com.helix.dove.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserRegistrationDTO registrationDTO;
    private User testUser;
    private final String TEST_TENANT_ID = "test_tenant";

    @BeforeEach
    void setUp() {
        registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUsername("testuser");
        registrationDTO.setEmail("test@example.com");
        registrationDTO.setPassword("Test@123");
        registrationDTO.setAccountType(1);
        registrationDTO.setLocale("en_US");
        registrationDTO.setTimezone("UTC");
        registrationDTO.setRegion("US");
        registrationDTO.setTenantId(TEST_TENANT_ID);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername(registrationDTO.getUsername());
        testUser.setEmail(registrationDTO.getEmail());
        testUser.setStatus(1);
        testUser.setAccountType(registrationDTO.getAccountType());
        testUser.setLocale(registrationDTO.getLocale());
        testUser.setTimezone(registrationDTO.getTimezone());
        testUser.setRegion(registrationDTO.getRegion());
        testUser.setTenantId(TEST_TENANT_ID);
    }

    @Test
    void register_Success() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.data.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.data.status").value(testUser.getStatus()));
    }

    @Test
    void register_InvalidInput() throws Exception {
        registrationDTO.setUsername("");
        registrationDTO.setEmail("invalid-email");

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUser_Success() throws Exception {
        when(userService.getById(1L)).thenReturn(testUser);

        mockMvc.perform(get("/api/v1/users/1")
                .header("X-Tenant-ID", TEST_TENANT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.data.email").value(testUser.getEmail()));
    }

    @Test
    void updateLocaleSettings_Success() throws Exception {
        when(userService.updateLocaleSettings(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(testUser);

        mockMvc.perform(put("/api/v1/users/1/locale-settings")
                .header("X-Tenant-ID", TEST_TENANT_ID)
                .param("locale", "zh_CN")
                .param("timezone", "Asia/Shanghai")
                .param("dateFormat", "yyyy-MM-dd")
                .param("timeFormat", "HH:mm:ss")
                .param("currency", "CNY")
                .param("region", "CN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void updateStatus_Success() throws Exception {
        testUser.setStatus(2);
        when(userService.updateStatus(any(), any(), any())).thenReturn(testUser);

        mockMvc.perform(put("/api/v1/users/1/status")
                .header("X-Tenant-ID", TEST_TENANT_ID)
                .param("status", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.status").value(2));
    }
} 