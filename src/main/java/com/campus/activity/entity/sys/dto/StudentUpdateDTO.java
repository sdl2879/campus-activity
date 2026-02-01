package com.campus.activity.entity.sys.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 编辑学生信息请求 DTO
 * 用于更新学生资料（不含密码）
 */
@Data
public class StudentUpdateDTO {

    @NotNull(message = "学生ID不能为空")
    private Long id;                // 主键ID（必填）

    @NotBlank(message = "学生姓名不能为空")
    @Size(min = 2, max = 50, message = "姓名长度必须在2到50个字符之间")
    private String name;            // 姓名

    private Integer gender;         // 性别

    @NotNull(message = "所属院系ID不能为空")
    private Long deptId;            // 院系ID

    private String deptName;        // 院系名称（冗余更新）

    @NotNull(message = "所属专业ID不能为空")
    private Long majorId;           // 专业ID

    private String majorName;       // 专业名称（冗余更新）

    private String grade;           // 年级（如 "2023级"）

    private String className;       // 班级

    private String phone;           // 联系方式

    private String email;           // 邮箱

    private String avatar;          // 头像URL

    private String interestTags;    // 兴趣标签

    private Integer status;         // 账号状态（1-启用，0-禁用）
}