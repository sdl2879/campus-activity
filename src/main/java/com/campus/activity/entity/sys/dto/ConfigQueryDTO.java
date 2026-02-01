package com.campus.activity.entity.sys.dto;

import lombok.Data;

/**
 * 配置查询DTO（用于按类型筛选配置）
 */
@Data
public class ConfigQueryDTO {
    private Integer configType;     // 配置类型：1-基础，2-安全，3-算法
    private String configKey;       // 配置键（模糊查询）
}