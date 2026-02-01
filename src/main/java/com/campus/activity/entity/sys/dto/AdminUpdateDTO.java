package com.campus.activity.entity.sys.dto;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class AdminUpdateDTO {
    @NotNull(message = "管理员ID不能为空")
    private Long id;                // 管理员ID

    @NotBlank(message = "真实姓名不能为空")
    @Size(min = 2, max = 50, message = "姓名长度为2-50字符")
    private String name;            // 真实姓名

    private String dept;            // 所属部门
    private String phone;           // 联系方式
    private String avatar;          // 头像URL
    private Integer status;         // 状态（1-启用，0-禁用）
    private Long roleId;            // 角色ID
}