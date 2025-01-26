CREATE TABLE IF NOT EXISTS sys_user_session (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    token VARCHAR(255) NOT NULL COMMENT '访问令牌',
    refresh_token VARCHAR(255) NOT NULL COMMENT '刷新令牌',
    token_expiration_time DATETIME NOT NULL COMMENT '访问令牌过期时间',
    refresh_token_expiration_time DATETIME NOT NULL COMMENT '刷新令牌过期时间',
    device_info VARCHAR(255) COMMENT '设备信息',
    login_ip VARCHAR(50) COMMENT '登录IP',
    last_access_time DATETIME NOT NULL COMMENT '最后访问时间',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-无效，1-有效',
    tenant_id VARCHAR(32) NOT NULL COMMENT '租户ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_tenant (user_id, tenant_id),
    INDEX idx_token (token),
    INDEX idx_refresh_token (refresh_token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户会话表'; 