package com.campus.activity.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.activity.entity.sys.SysRole;

/**
 * 角色服务接口
 */
public interface SysRoleService extends IService<SysRole> {

    // ❌ 保留旧方法（可选，用于兼容）
    IPage<SysRole> getRolePage(Page<SysRole> page);

    // ✅ 新增：支持条件查询的分页方法（关键！）
    IPage<SysRole> getRolePage(Page<SysRole> page, String roleName, Integer status);

    // 新增角色（含参数校验）
    boolean addRole(SysRole role);

    // 修改角色状态（启用/禁用）
    boolean changeStatus(Long id, Integer status);
}