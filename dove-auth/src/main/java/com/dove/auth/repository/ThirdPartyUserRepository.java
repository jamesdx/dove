package com.dove.auth.repository;

import com.dove.auth.domain.entity.ThirdPartyUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * 第三方用户Repository
 */
public interface ThirdPartyUserRepository extends JpaRepository<ThirdPartyUserEntity, Long>, JpaSpecificationExecutor<ThirdPartyUserEntity> {

    /**
     * 根据第三方类型和第三方用户ID查询
     */
    Optional<ThirdPartyUserEntity> findByThirdTypeAndThirdId(String thirdType, String thirdId);

    /**
     * 根据用户ID和第三方类型查询
     */
    Optional<ThirdPartyUserEntity> findByUserIdAndThirdType(Long userId, String thirdType);

    /**
     * 根据用户ID查询列表
     */
    List<ThirdPartyUserEntity> findByUserId(Long userId);

    /**
     * 根据第三方类型和第三方用户ID删除
     */
    void deleteByThirdTypeAndThirdId(String thirdType, String thirdId);

    /**
     * 根据用户ID和第三方类型删除
     */
    void deleteByUserIdAndThirdType(Long userId, String thirdType);
} 