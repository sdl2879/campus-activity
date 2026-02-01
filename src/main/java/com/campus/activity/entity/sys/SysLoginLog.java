package com.campus.activity.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统登录日志实体（对应sys_login_log表）
 */
@Data
@TableName("sys_login_log")
public class SysLoginLog {
    @TableId(type = IdType.AUTO)
    private Long id;                // 日志ID
    private Long userId;            // 用户ID
    private String userName;        // 登录用户名
    private String loginIp;         // 登录IP
    private LocalDateTime loginTime; // 登录时间
    private String loginResult;     // 登录结果
    private String failReason;      // 失败原因
    private String loginType;       // 登录类型
    private String device;          // 登录设备
}