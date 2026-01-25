package com.campus.activity.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_admin")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long id;             // 主键ID
    private String username;     // 用户名
    private String password;     // 密码（加密存储）
    private String name;         // 姓名
    private String phone;        // 手机号
    private String email;        // 邮箱
    private String avatar;       // 头像地址
    private Integer status;      // 状态 1-启用 0-禁用
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}