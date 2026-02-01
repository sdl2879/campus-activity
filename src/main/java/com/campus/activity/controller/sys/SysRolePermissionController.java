package com.campus.activity.controller.sys;

import com.campus.activity.entity.sys.dto.RolePermissionAssignDTO;
import com.campus.activity.service.sys.SysRolePermissionService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色-权限关联控制器（权限分配）
 */
@RestController
@RequestMapping("/sys/role/permission")
public class SysRolePermissionController {

    @Resource
    private SysRolePermissionService sysRolePermissionService;

    /**
     * 给角色分配权限
     */
    @PostMapping("/assign")
    public Map<String, Object> assignPermissions(@Valid @RequestBody RolePermissionAssignDTO assignDTO) {
        Map<String, Object> result = new HashMap<>();
        try {
            sysRolePermissionService.assignPermissions(assignDTO);
            result.put("code", 200);
            result.put("msg", "权限分配成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "权限分配失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询角色已分配的权限ID
     */
    @GetMapping("/getAssigned/{roleId}")
    public Map<String, Object> getAssignedPermIds(@PathVariable Long roleId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Long> permIds = sysRolePermissionService.getAssignedPermIds(roleId);
            result.put("code", 200);
            result.put("data", permIds);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询已分配权限失败：" + e.getMessage());
        }
        return result;
    }
}