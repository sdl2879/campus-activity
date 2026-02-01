package com.campus.activity.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;             // 主键ID
    private String username;     // 用户名
    private String password;     // 密码
    private String realName;     // 真实姓名（对应 real_name）
    private Long roleId;         // 角色ID（对应 role_id）
    private Long deptId;         // 部门ID（对应 dept_id）
    private Integer status;      // 状态（1-启用，0-禁用）
    private Integer isDeleted;   // 是否删除（对应 is_deleted）
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}