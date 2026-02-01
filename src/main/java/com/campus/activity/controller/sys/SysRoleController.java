package com.campus.activity.controller.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.sys.SysRole;
import com.campus.activity.service.sys.SysRoleService;
import com.campus.activity.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 角色管理控制器（适配前端 POST /page）
 */
@RestController
@RequestMapping("/api/sys/role") // ✅ 已加 /api 前缀
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 分页查询角色（✅ 改为 POST，接收 JSON body）
     */
    @PostMapping("/page")
    @PreAuthorize("hasAnyAuthority('sys:role:view', 'sys:role:list')")
    public Result<IPage<SysRole>> getRolePage(@RequestBody Map<String, Object> query) {
        // 从 JSON body 中安全取值
        Integer pageNum = query != null && query.get("pageNum") instanceof Number
                ? ((Number) query.get("pageNum")).intValue() : 1;
        Integer pageSize = query != null && query.get("pageSize") instanceof Number
                ? ((Number) query.get("pageSize")).intValue() : 10;
        String roleName = query != null && query.get("roleName") instanceof String
                ? (String) query.get("roleName") : null;
        Integer status = query != null && query.get("status") instanceof Number
                ? ((Number) query.get("status")).intValue() : null;

        Page<SysRole> page = new Page<>(pageNum, pageSize);
        IPage<SysRole> rolePage = sysRoleService.getRolePage(page, roleName, status); // ← 需要重载 service 方法
        return Result.success(rolePage);
    }

    /**
     * 新增角色
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('sys:role:add')")
    public Result<String> addRole(@RequestBody SysRole role) {
        boolean success = sysRoleService.addRole(role);
        return success ? Result.success("新增成功") : Result.error("新增失败");
    }

    /**
     * 修改角色状态（启用/禁用）
     */
    @PostMapping("/changeStatus")
    @PreAuthorize("hasAuthority('sys:role:edit')")
    public Result<String> changeStatus(@RequestBody Map<String, Object> payload) {
        Long id = payload != null && payload.get("id") instanceof Number
                ? ((Number) payload.get("id")).longValue() : null;
        Integer status = payload != null && payload.get("status") instanceof Number
                ? ((Number) payload.get("status")).intValue() : null;

        if (id == null || status == null) {
            return Result.error("参数缺失");
        }

        boolean success = sysRoleService.changeStatus(id, status);
        String msg = status == 1 ? "启用成功" : "禁用成功";
        return success ? Result.success(msg) : Result.error("状态修改失败");
    }
}