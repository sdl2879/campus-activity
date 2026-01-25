package com.campus.activity.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.sys.SysRole;
import com.campus.activity.service.SysRoleService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 角色管理接口
 */
@RestController
@RequestMapping("/api/system/role")
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;

    // 分页查询角色列表
    @GetMapping("/list")
    public IPage<SysRole> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String roleName
    ) {
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        return sysRoleService.lambdaQuery()
                .like(roleName != null, SysRole::getRoleName, roleName)
                .page(page);
    }

    // 新增角色
    @PostMapping
    public Boolean add(@RequestBody SysRole role) {
        role.setCreateTime(LocalDateTime.now());
        return sysRoleService.save(role);
    }

    // 修改角色
    @PutMapping
    public Boolean update(@RequestBody SysRole role) {
        return sysRoleService.updateById(role);
    }

    // 切换角色状态
    @PutMapping("/status/{id}/{status}")
    public Boolean changeStatus(@PathVariable Long id, @PathVariable String status) {
        SysRole role = new SysRole();
        role.setId(id);
        role.setStatus(status);
        return sysRoleService.updateById(role);
    }
}