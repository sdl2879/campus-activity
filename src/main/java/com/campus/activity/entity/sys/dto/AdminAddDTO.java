package com.campus.activity.entity.sys.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class AdminAddDTO {
    @NotBlank(message = "登录账号不能为空")
    @Size(min = 4, max = 50, message = "账号长度为4-50字符")
    private String username;        // 登录账号

    @NotBlank(message = "真实姓名不能为空")
    @Size(min = 2, max = 50, message = "姓名长度为2-50字符")
    private String name;            // 真实姓名

    private String dept;            // 所属部门
    private String phone;           // 联系方式
    private String avatar;          // 头像URL
    private Long roleId = 1L;       // 角色ID（默认1）

    @NotBlank(message = "初始密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度为6-20字符")
    private String password;        // 初始密码
}