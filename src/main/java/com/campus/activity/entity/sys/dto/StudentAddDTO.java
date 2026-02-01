package com.campus.activity.entity.sys.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class StudentAddDTO {
    @NotBlank(message = "学号不能为空")
    @Size(min = 6, max = 50, message = "学号长度为6-50字符")
    private String studentNo;       // 学号

    @NotBlank(message = "学生姓名不能为空")
    @Size(min = 2, max = 50, message = "姓名长度为2-50字符")
    private String name;            // 学生姓名

    private Integer gender = 0;     // 性别：0-未知，1-男，2-女

    @NotNull(message = "所属院系ID不能为空")
    private Long deptId;            // 所属院系ID

    private String deptName;        // 所属院系名称（前端传入）

    @NotNull(message = "所属专业ID不能为空")
    private Long majorId;           // 所属专业ID

    private String majorName;       // 所属专业名称（前端传入）

    private String grade;           // 年级

    private String className;       // 班级

    private String phone;           // 联系方式

    private String email;           // 邮箱

    private String avatar;          // 头像URL

    private String interestTags;    // 兴趣标签（逗号分隔）

    @NotBlank(message = "初始密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度为6-20字符")
    private String password;        // 初始密码
}