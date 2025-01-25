-- 创建数据库
CREATE DATABASE IF NOT EXISTS dove_auth DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dove_auth;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL,
  `email` varchar(128) NOT NULL,
  `password_hash` varchar(128) NOT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  `last_login_time` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `locale` varchar(10) NOT NULL DEFAULT 'en_US',
  `timezone` varchar(40) NOT NULL DEFAULT 'UTC',
  `date_format` varchar(20) NOT NULL DEFAULT 'yyyy-MM-dd',
  `time_format` varchar(20) NOT NULL DEFAULT 'HH:mm:ss',
  `currency` varchar(3) NOT NULL DEFAULT 'USD',
  `region` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`),
  UNIQUE KEY `idx_email` (`email`),
  KEY `idx_locale` (`locale`),
  KEY `idx_region` (`region`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户Token表
CREATE TABLE IF NOT EXISTS `user_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `refresh_token` varchar(256) NOT NULL,
  `expires_at` datetime NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入测试用户
INSERT INTO `user` (`username`, `email`, `password_hash`, `status`)
VALUES ('admin', 'admin@example.com', '$2a$10$uWqxGHg6GiHKdaUC0r5pveq92ZH.jG9TQtQT9.owvJJ3ypNUWwhLG', 1);
-- 密码是: admin123 