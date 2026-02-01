package com.campus.activity.entity.sys.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class ActivityManagerUpdateDTO {
    @NotNull(message = "活动负责人ID不能为空")
    private Long id;                // 主键ID

    @NotBlank(message = "真实姓名不能为空")
    @Size(min = 2, max = 50, message = "姓名长度为2-50字符")
    private String name;            // 真实姓名

    @NotNull(message = "所属院系ID不能为空")
    private Long deptId;            // 所属院系ID

    private String deptName;        // 所属院系名称（冗余存储）

    private String major;           // 所属专业

    private String phone;           // 联系方式

    private Integer status;         // 账号状态：1-启用，0-禁用
}