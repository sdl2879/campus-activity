package com.campus.activity.utils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redis 缓存工具类
 * 功能：封装 Redis 常用操作（增删改查），简化业务层缓存调用
 * 核心：基于 Spring Data Redis 实现，统一序列化方式（String + JSON）
 */
@Component
public class RedisCacheUtil {

    // 注入 RedisTemplate（已在 RedisConfig 中配置自定义序列化）
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 缓存数据（带过期时间）
     * @param key 缓存键（建议格式：业务模块:唯一标识，如 user:1、role:permissions:2）
     * @param value 缓存值（任意对象，自动序列化为 JSON）
     * @param timeout 过期时间（数值）
     * @param timeUnit 时间单位（如 TimeUnit.HOURS、TimeUnit.MINUTES）
     */
    public void setCacheObject(String key, Object value, Long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 缓存数据（永久有效，需手动删除）
     * @param key 缓存键
     * @param value 缓存值
     */
    public void setCacheObject(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取缓存数据
     * @param key 缓存键
     * @param clazz 返回值类型（自动反序列化为指定类型）
     * @return 缓存值（null 表示缓存不存在）
     */
    public <T> T getCacheObject(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? (T) value : null;
    }

    /**
     * 删除缓存
     * @param key 缓存键
     * @return true=删除成功，false=缓存不存在
     */
    public boolean deleteCacheObject(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 判断缓存是否存在
     * @param key 缓存键
     * @return true=存在，false=不存在
     */
    public boolean existsCacheObject(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 更新缓存过期时间
     * @param key 缓存键
     * @param timeout 新过期时间
     * @param timeUnit 时间单位
     */
    public void expireCacheObject(String key, Long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeout, timeUnit);
    }
}