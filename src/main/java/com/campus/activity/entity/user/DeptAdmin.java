package com.campus.activity.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("dept_admin") // 与数据库表名一致
public class DeptAdmin {
    @TableId(type = IdType.AUTO)
    private Long id;                // 主键ID
    private String username;        // 管理员账号/工号
    private String password;        // 加密密码（BCrypt）
    private String name;            // 管理员姓名
    private Long deptId;            // 所属院系ID

    // ✅ 修复：使用驼峰命名 deptName（不是 dept_name）
    private String deptName;        // 所属院系名称（冗余）

    private String manageMajors;    // 管理专业范围（专业ID逗号分隔）
    private String phone;           // 联系方式
    private Long roleId;            // 角色ID（默认2）
    private Integer status;         // 状态：1-启用，0-禁用
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private LocalDateTime lastLoginTime; // 最后登录时间
}