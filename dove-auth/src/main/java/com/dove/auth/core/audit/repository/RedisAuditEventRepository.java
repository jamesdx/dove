package com.dove.auth.core.audit.repository;

import com.dove.auth.core.audit.model.AuditEvent;
import com.dove.common.core.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis审计事件仓库实现
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisAuditEventRepository implements AuditEventRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String AUDIT_KEY_PREFIX = "dove:auth:audit:";
    private static final String AUDIT_INDEX_PREFIX = "dove:auth:audit_index:";
    private static final long DEFAULT_RETENTION_DAYS = 30;

    @Override
    public void save(AuditEvent event) {
        try {
            String eventKey = getEventKey(event.getId());
            String userIndexKey = getUserIndexKey(event.getUserId());
            String typeIndexKey = getTypeIndexKey(event.getType());
            String timeIndexKey = getTimeIndexKey(event.getTimestamp());

            // 保存事件数据
            redisTemplate.opsForValue().set(eventKey, JsonUtils.toJson(event), DEFAULT_RETENTION_DAYS, TimeUnit.DAYS);

            // 更新索引
            redisTemplate.opsForZSet().add(userIndexKey, event.getId(), event.getTimestamp().toEpochSecond(java.time.ZoneOffset.UTC));
            redisTemplate.opsForZSet().add(typeIndexKey, event.getId(), event.getTimestamp().toEpochSecond(java.time.ZoneOffset.UTC));
            redisTemplate.opsForZSet().add(timeIndexKey, event.getId(), event.getTimestamp().toEpochSecond(java.time.ZoneOffset.UTC));

            // 设置索引过期时间
            redisTemplate.expire(userIndexKey, DEFAULT_RETENTION_DAYS, TimeUnit.DAYS);
            redisTemplate.expire(typeIndexKey, DEFAULT_RETENTION_DAYS, TimeUnit.DAYS);
            redisTemplate.expire(timeIndexKey, DEFAULT_RETENTION_DAYS, TimeUnit.DAYS);

            log.debug("审计事件保存成功: {}", event.getId());
        } catch (Exception e) {
            log.error("审计事件保存失败: {}", event.getId(), e);
        }
    }

    @Override
    public AuditEvent findById(String id) {
        try {
            String eventKey = getEventKey(id);
            String json = (String) redisTemplate.opsForValue().get(eventKey);
            return json != null ? JsonUtils.fromJson(json, AuditEvent.class) : null;
        } catch (Exception e) {
            log.error("查询审计事件失败: {}", id, e);
            return null;
        }
    }

    @Override
    public List<AuditEvent> findByUserId(Long userId, int limit) {
        try {
            String userIndexKey = getUserIndexKey(userId);
            Set<Object> eventIds = redisTemplate.opsForZSet().reverseRange(userIndexKey, 0, limit - 1);
            return getEventsByIds(eventIds);
        } catch (Exception e) {
            log.error("查询用户审计事件失败: {}", userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<AuditEvent> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        try {
            String timeIndexKey = getTimeIndexKey(start);
            double min = start.toEpochSecond(java.time.ZoneOffset.UTC);
            double max = end.toEpochSecond(java.time.ZoneOffset.UTC);
            Set<Object> eventIds = redisTemplate.opsForZSet().rangeByScore(timeIndexKey, min, max);
            return getEventsByIds(eventIds);
        } catch (Exception e) {
            log.error("查询时间范围审计事件失败: {} - {}", start, end, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<AuditEvent> findByType(String type, int limit) {
        try {
            String typeIndexKey = getTypeIndexKey(type);
            Set<Object> eventIds = redisTemplate.opsForZSet().reverseRange(typeIndexKey, 0, limit - 1);
            return getEventsByIds(eventIds);
        } catch (Exception e) {
            log.error("查询类型审计事件失败: {}", type, e);
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteBeforeTime(LocalDateTime time) {
        try {
            String timeIndexKey = getTimeIndexKey(time);
            double max = time.toEpochSecond(java.time.ZoneOffset.UTC);
            Set<Object> eventIds = redisTemplate.opsForZSet().rangeByScore(timeIndexKey, 0, max);
            if (eventIds != null && !eventIds.isEmpty()) {
                // 删除事件数据
                for (Object eventId : eventIds) {
                    String eventKey = getEventKey((String) eventId);
                    redisTemplate.delete(eventKey);
                }
                // 删除索引
                redisTemplate.opsForZSet().removeRangeByScore(timeIndexKey, 0, max);
            }
            log.debug("删除历史审计事件成功: {}", time);
        } catch (Exception e) {
            log.error("删除历史审计事件失败: {}", time, e);
        }
    }

    @Override
    public void cleanupExpiredEvents() {
        try {
            LocalDateTime expiryTime = LocalDateTime.now().minus(DEFAULT_RETENTION_DAYS, ChronoUnit.DAYS);
            deleteBeforeTime(expiryTime);
            log.debug("清理过期审计事件成功");
        } catch (Exception e) {
            log.error("清理过期审计事件失败", e);
        }
    }

    private List<AuditEvent> getEventsByIds(Set<Object> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return new ArrayList<>();
        }
        return eventIds.stream()
                .map(id -> findById((String) id))
                .filter(event -> event != null)
                .collect(Collectors.toList());
    }

    private String getEventKey(String eventId) {
        return AUDIT_KEY_PREFIX + "event:" + eventId;
    }

    private String getUserIndexKey(Long userId) {
        return AUDIT_INDEX_PREFIX + "user:" + userId;
    }

    private String getTypeIndexKey(String type) {
        return AUDIT_INDEX_PREFIX + "type:" + type;
    }

    private String getTimeIndexKey(LocalDateTime time) {
        return AUDIT_INDEX_PREFIX + "time:" + time.toLocalDate();
    }
} 