package com.campus.activity.entity.user;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long userId;          // 用户ID
    private String username;      // 账号
    private String password;      // 密码
    private String realName;      // 真实姓名
    private String roleType;      // 角色类型
    private String deptName;      // 所属院系
    private String phone;         // 联系电话
    private String email;         // 邮箱
    private String status;        // 账号状态
    private String tags;          // 兴趣标签（JSON字符串）
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; // 更新时间
    @TableLogic
    private Integer deleted;      // 逻辑删除

    // 非数据库字段 - 标签列表
    @TableField(exist = false)
    private List<String> tagList;
}