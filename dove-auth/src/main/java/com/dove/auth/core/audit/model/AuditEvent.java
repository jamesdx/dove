package com.dove.auth.core.audit.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 审计事件
 */
@Data
@Accessors(chain = true)
public class AuditEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件ID
     */
    private String id;

    /**
     * 事件类型
     */
    private String type;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 操作时间
     */
    private LocalDateTime timestamp;

    /**
     * 操作结果
     */
    private String result;

    /**
     * 事件详情
     */
    private String details;

    /**
     * 设备信息
     */
    private String userAgent;

    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 异常信息
     */
    private String exception;
} 