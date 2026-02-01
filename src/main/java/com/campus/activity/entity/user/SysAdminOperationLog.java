package com.campus.activity.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_admin_operation_log") // 与数据库表名一致
public class SysAdminOperationLog {
    @TableId(type = IdType.AUTO)
    private Long id;                // 日志ID（主键）

    private Long adminId;           // 操作管理员ID（关联sys_admin表id）

    private String adminName;       // 操作管理员姓名（冗余存储，便于日志查看）

    private String operation;       // 操作类型（架构要求：新增/编辑/禁用/重置密码等）

    private String module;          // 操作模块（架构要求：系统管理员管理/系统配置等）

    private LocalDateTime operationTime; // 操作时间（架构要求：记录操作时序）

    private String ip;              // 操作IP（架构要求：登录/操作日志需包含IP信息）

    private String detail;          // 操作详情（JSON格式，架构要求：记录操作具体内容）
}