package com.campus.activity.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统操作日志实体（兼容 oper 缩写方法名）
 */
@Data
@TableName("sys_operation_log")
public class SysOperLog {
    @TableId(type = IdType.AUTO)
    private Long id;                // 日志ID
    private Long userId;            // 操作用户ID
    private String userName;        // 操作用户名（兼容 operator）
    private String roleType;        // 用户角色类型
    private String module;          // 操作模块
    private String moduleCode;      // 模块编码
    private String operation;       // 操作类型（兼容 operType）
    private String operationParams; // 操作参数（JSON）
    private String operationResult; // 操作结果
    private String operationIp;     // 操作IP
    private LocalDateTime operationTime; // 操作时间（兼容 operTime）
    private String operationDetail; // 操作详情
    private Integer status;         // 操作状态（兼容 operStatus）
    private Integer costTime;       // 操作耗时（毫秒）

    // ========== 新增兼容方法（解决方法引用无效） ==========
    // 兼容 getOperator() 方法（映射到 userName）
    public String getOperator() {
        return this.userName;
    }

    // 兼容 setOperator() 方法
    public void setOperator(String operator) {
        this.userName = operator;
    }

    // 兼容 getOperType() 方法（映射到 operation）
    public String getOperType() {
        return this.operation;
    }

    // 兼容 setOperType() 方法
    public void setOperType(String operType) {
        this.operation = operType;
    }

    // 兼容 getOperStatus() 方法（映射到 status）
    public Integer getOperStatus() {
        return this.status;
    }

    // 兼容 setOperStatus() 方法
    public void setOperStatus(Integer operStatus) {
        this.status = operStatus;
    }

    // 兼容 getOperTime() 方法（映射到 operationTime）
    public LocalDateTime getOperTime() {
        return this.operationTime;
    }

    // 兼容 setOperTime() 方法
    public void setOperTime(LocalDateTime operTime) {
        this.operationTime = operTime;
    }
}