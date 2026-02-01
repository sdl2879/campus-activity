package com.campus.activity.entity.sys.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色权限分配DTO（接收前端传递的角色ID和权限ID列表）
 */
@Data
public class RolePermissionAssignDTO {
    @NotNull(message = "角色ID不能为空")
    private Long roleId;                // 角色ID
    private List<Long> permIds;         // 要分配的权限ID列表（空则清空该角色所有权限）
    private String remark;              // 分配备注（可选）
}