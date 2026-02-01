package com.campus.activity.Vo;

import lombok.Data;

/**
 * 管理员操作日志查询VO（适配架构文档「日志管理」多条件筛选要求）
 */
@Data
public class LogQueryVO {
    private Long adminId;         // 按操作人ID筛选（架构要求：按用户筛选）
    private String module;        // 按操作模块筛选（架构要求：按模块筛选，如"系统管理员管理"）
    private String operation;     // 按操作类型筛选（架构要求：按操作类型筛选，如"新增/编辑"）
    private String startTime;     // 操作开始时间（格式：yyyy-MM-dd HH:mm:ss，架构要求：按时间筛选）
    private String endTime;       // 操作结束时间（格式：yyyy-MM-dd HH:mm:ss，架构要求：按时间筛选）
    private Integer pageNum = 1;  // 页码（默认1，适配分页查询）
    private Integer pageSize = 10;// 每页条数（默认10，适配分页查询）
}