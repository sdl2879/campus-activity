package com.campus.activity.entity.sys;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 角色实体
 */
@Data
@TableName("sys_role")
public class SysRole {
    @TableId(type = IdType.AUTO)
    private Long id;                 // 角色ID
    private String roleName;         // 角色名称
    private String roleCode;         // 角色编码
    private String roleDesc;         // 角色描述
    private String status;           // 状态（1启用/0禁用）
    private LocalDateTime createTime;// 创建时间
}