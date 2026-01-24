package com.campus.activity.entity.system;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role")
public class Role {
    private Long   id;
    private String roleName;
    private String roleCode;   // ① 新增
}