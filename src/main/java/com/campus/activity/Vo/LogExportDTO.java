package com.campus.activity.Vo;

import lombok.Data;

/**
 * 日志导出DTO（适配架构文档「日志导出」功能，格式化Excel展示数据）
 */
@Data
public class LogExportDTO {
    private Long logId;          // 日志ID
    private Long adminId;        // 操作人ID
    private String adminName;    // 操作人姓名
    private String operation;    // 操作类型
    private String module;       // 操作模块
    private String operationTime;// 操作时间（格式化后字符串）
    private String ip;           // 操作IP
    private String detail;       // 操作详情
}