package com.campus.activity.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统权限实体（对应sys_permission表）
 */
@Data
@TableName("sys_permission")
public class SysPermission {
    @TableId(type = IdType.AUTO)
    private Long id;            // 权限ID
    private String permName;    // 权限名称
    private String permCode;    // 权限编码
    private Integer permType;   // 权限类型：1-菜单，2-功能，3-数据
    private Integer status;     // 状态：0-禁用，1-启用
}