package com.dove.auth.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 安全审计实体
 */
@Data
@Entity
@Table(name = "sys_security_audit")
@EntityListeners(AuditingEntityListener.class)
public class SecurityAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("主键ID")
    private Long id;

    @Column(name = "user_id")
    @Comment("用户ID")
    private Long userId;

    @Column(name = "event_type", length = 50, nullable = false)
    @Comment("事件类型")
    private String eventType;

    @CreatedDate
    @Column(name = "event_time", nullable = false)
    @Comment("事件时间")
    private LocalDateTime eventTime;

    @Column(name = "ip", length = 50)
    @Comment("IP地址")
    private String ip;

    @Column(name = "details", columnDefinition = "text")
    @Comment("详细信息")
    private String details;

    @Column(name = "risk_level")
    @Comment("风险等级")
    private Integer riskLevel;

    @Version
    @Column(name = "version")
    @Comment("版本号")
    private Integer version;
} 