package com.campus.activity.entity.sys.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 日志查询DTO（支持多条件筛选）
 */
@Data
public class LogQueryDTO {
    // 通用参数
    private String userName;        // 用户名（模糊查询）
    private LocalDateTime startTime;// 开始时间
    private LocalDateTime endTime;  // 结束时间
    private Integer pageNum = 1;    // 页码
    private Integer pageSize = 10;  // 每页条数

    // 操作日志专属参数
    private String module;          // 操作模块
    private String operation;       // 操作类型
    private Integer status;         // 操作状态（仅操作日志）

    // 登录日志专属参数
    private String loginResult;     // 登录结果（仅登录日志）
    private String loginType;       // 登录类型（仅登录日志）
}