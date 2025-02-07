package com.dove.auth.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 操作日志实体
 */
@Data
@Entity
@Table(name = "sys_operation_log")
@EntityListeners(AuditingEntityListener.class)
public class OperationLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("主键ID")
    private Long id;

    @Column(name = "user_id")
    @Comment("用户ID")
    private Long userId;

    @Column(name = "module", length = 50)
    @Comment("操作模块")
    private String module;

    @Column(name = "operation", length = 50)
    @Comment("操作类型")
    private String operation;

    @Column(name = "method", length = 100)
    @Comment("方法名")
    private String method;

    @Column(name = "params", columnDefinition = "text")
    @Comment("请求参数")
    private String params;

    @Column(name = "time")
    @Comment("执行时长(毫秒)")
    private Long time;

    @Column(name = "ip", length = 50)
    @Comment("操作IP")
    private String ip;

    @Column(name = "location", length = 255)
    @Comment("操作地点")
    private String location;

    @Column(name = "status")
    @Comment("操作状态(0-失败,1-成功)")
    private Integer status;

    @Column(name = "error_msg", columnDefinition = "text")
    @Comment("错误消息")
    private String errorMsg;

    @CreatedDate
    @Column(name = "create_time", nullable = false)
    @Comment("创建时间")
    private LocalDateTime createTime;

    @Version
    @Column(name = "version")
    @Comment("版本号")
    private Integer version;
} 