package com.campus.activity.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统操作日志实体（对应sys_operation_log表）
 */
@Data
@TableName("sys_operation_log")
public class SysOperationLog {
    @TableId(type = IdType.AUTO)
    private Long id;                // 日志ID
    private Long userId;            // 操作用户ID
    private String userName;        // 操作用户名
    private String roleType;        // 用户角色类型
    private String module;          // 操作模块
    private String moduleCode;      // 模块编码
    private String operation;       // 操作类型
    private String operationParams; // 操作参数（JSON）
    private String operationResult; // 操作结果
    private String operationIp;     // 操作IP
    private LocalDateTime operationTime; // 操作时间
    private String operationDetail; // 操作详情
    private Integer status;         // 操作状态：0-失败，1-成功
    private Integer costTime;       // 操作耗时（毫秒）
}