package com.dove.auth.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 登录日志实体
 */
@Data
@Entity
@Table(name = "sys_login_log")
@EntityListeners(AuditingEntityListener.class)
public class LoginLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("主键ID")
    private Long id;

    @Column(name = "user_id")
    @Comment("用户ID")
    private Long userId;

    @Column(name = "login_type", length = 20)
    @Comment("登录类型")
    private String loginType;

    @Column(name = "login_ip", length = 50)
    @Comment("登录IP")
    private String loginIp;

    @Column(name = "login_location", length = 100)
    @Comment("登录地点")
    private String loginLocation;

    @Column(name = "browser", length = 50)
    @Comment("浏览器")
    private String browser;

    @Column(name = "os", length = 50)
    @Comment("操作系统")
    private String os;

    @Column(name = "device_id", length = 100)
    @Comment("设备ID")
    private String deviceId;

    @Column(name = "status")
    @Comment("状态(0-失败,1-成功)")
    private Integer status;

    @Column(name = "msg", length = 255)
    @Comment("提示消息")
    private String msg;

    @CreatedDate
    @Column(name = "login_time", nullable = false)
    @Comment("登录时间")
    private LocalDateTime loginTime;

    @Version
    @Column(name = "version")
    @Comment("版本号")
    private Integer version;
} 