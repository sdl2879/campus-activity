package com.campus.activity.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("student")
public class Student {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "学号不能为空")
    private String studentNo;

    @NotBlank(message = "学生姓名不能为空")
    private String studentName;

    @NotNull(message = "性别不能为空")
    private Integer gender;

    @NotNull(message = "院系ID不能为空")
    private Long deptId;

    private String className;

    @NotBlank(message = "学科分类不能为空")
    private String subjectType;

    @NotBlank(message = "年级不能为空")
    private String grade;

    private Integer isGraduate = 0;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入正确的手机号")
    private String phone;

    private Integer activityCount = 0;

    private Integer volunteerHours = 0;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}