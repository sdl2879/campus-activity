package com.campus.activity.entity.sys.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class DeptAdminAddDTO {

    @NotBlank(message = "登录账号不能为空")
    @Size(min = 4, max = 50, message = "账号长度为4-50字符")
    private String username;        // 登录账号

    @NotBlank(message = "真实姓名不能为空")
    @Size(min = 2, max = 50, message = "姓名长度为2-50字符")
    private String name;            // 真实姓名

    @NotNull(message = "所属院系ID不能为空")
    private Long deptId;            // 所属院系ID

    // ✅ 修复：使用驼峰命名 deptName（不是 dept_Name）
    private String deptName;        // 所属院系名称（前端传入，冗余存储）

    private String manageMajors;    // 管理专业范围（专业ID逗号分隔）

    private String phone;           // 联系方式

    @NotBlank(message = "初始密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度为6-20字符")
    private String password;        // 初始密码
}