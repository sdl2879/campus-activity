package com.campus.activity.entity.sys;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统基础配置实体
 */
@Data
@TableName("sys_config")
public class SysConfig {
    @TableId(type = IdType.AUTO)
    private Long id;                 // 配置ID
    private String systemName;       // 系统名称
    private String systemVersion;    // 系统版本
    private String systemDomain;     // 系统域名
    private String backupCycle;      // 备份周期（day/week/month）
    private String enableLog;        // 是否开启日志（1是/0否）
    private String systemDesc;       // 系统描述
}