package com.campus.activity.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 系统配置实体（对应sys_config表）
 */
@Data
@TableName("sys_config")
public class SysConfig {
    @TableId(type = IdType.AUTO)
    private Long id;                // 配置ID
    private String configKey;       // 配置键（唯一）
    private String configValue;     // 配置值
    private Integer configType;     // 配置类型：1-基础信息，2-安全配置，3-推荐算法
    private BigDecimal weight;      // 推荐算法权重（仅类型3有效）
    private String remark;          // 备注
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}