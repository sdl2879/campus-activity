package com.campus.activity.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 角色操作日志实体
 * 对应数据库表：sys_role_log
 */
@Data
@TableName("sys_role_log")
public class SysRoleLog {
    /**
     * 日志ID（主键，自增）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联的角色ID
     * 数据库字段：role_id
     */
    private Long roleId;

    /**
     * 操作人（如：admin/用户ID）
     * 数据库字段：operate_by
     */
    private String operateBy;

    /**
     * 操作内容（如：新增角色/分配权限）
     * 数据库字段：operate_content
     */
    private String operateContent;

    /**
     * 操作时间
     * 数据库字段：operate_time
     */
    private LocalDateTime operateTime;
}