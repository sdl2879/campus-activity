package com.campus.activity.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_admin")
public class SysAdmin {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;
    private String password;

    // ✅ 修正：数据库字段是 `name`，不是 `real_name`
    @TableField("name")  // ← 显式映射到数据库的 `name` 列
    private String realName; // Java 层仍可叫 realName（语义清晰）

    private Long roleId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String avatar;
    private String dept;
    private String phone;
    private LocalDateTime lastLoginTime;
}