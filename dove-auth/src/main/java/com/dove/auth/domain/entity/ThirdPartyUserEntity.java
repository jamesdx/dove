package com.dove.auth.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 第三方用户关联实体
 */
@Data
@Entity
@Table(name = "sys_user_third")
@EntityListeners(AuditingEntityListener.class)
public class ThirdPartyUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("主键ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Comment("用户ID")
    private Long userId;

    @Column(name = "third_type", length = 20, nullable = false)
    @Comment("第三方类型")
    private String thirdType;

    @Column(name = "third_id", length = 100, nullable = false)
    @Comment("第三方用户ID")
    private String thirdId;

    @Column(name = "third_data", columnDefinition = "text")
    @Comment("第三方用户数据")
    private String thirdData;

    @CreatedDate
    @Column(name = "bind_time", nullable = false)
    @Comment("绑定时间")
    private LocalDateTime bindTime;

    @Version
    @Column(name = "version")
    @Comment("版本号")
    private Integer version;
} 