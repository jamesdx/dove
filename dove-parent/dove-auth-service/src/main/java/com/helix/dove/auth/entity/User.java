package com.helix.dove.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    
    private String email;
    
    private String passwordHash;
    
    private Integer status;
    
    private LocalDateTime lastLoginTime;
    
    private String locale;
    
    private String timezone;
    
    private String dateFormat;
    
    private String timeFormat;
    
    private String currency;
    
    private String region;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
} 