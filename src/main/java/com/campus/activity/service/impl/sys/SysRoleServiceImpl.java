package com.campus.activity.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.sys.SysRole;
import com.campus.activity.mapper.sys.SysRoleMapper;
import com.campus.activity.service.sys.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;

/**
 * 角色服务实现类
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    // ❌ 旧方法（可保留，但 Controller 不再使用）
    @Override
    public IPage<SysRole> getRolePage(Page<SysRole> page) {
        return sysRoleMapper.selectPage(page, null);
    }

    // ✅ 新方法：支持 roleName 模糊查询 + status 精确匹配
    @Override
    public IPage<SysRole> getRolePage(Page<SysRole> page, String roleName, Integer status) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();

        // 角色名称模糊查询
        if (StringUtils.hasText(roleName)) {
            wrapper.like(SysRole::getRoleName, roleName.trim());
        }

        // 状态精确匹配（0: 禁用, 1: 启用）
        if (status != null) {
            wrapper.eq(SysRole::getStatus, status);
        }

        // 按创建时间倒序（可选）
        wrapper.orderByDesc(SysRole::getCreateTime);

        return this.page(page, wrapper);
    }

    @Override
    public boolean addRole(SysRole role) {
        // 校验角色编码是否重复
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleCode, role.getRoleCode());
        if (this.count(wrapper) > 0) {
            throw new RuntimeException("角色编码已存在");
        }
        // 校验角色名称不能为空
        if (!StringUtils.hasText(role.getRoleName())) {
            throw new RuntimeException("角色名称不能为空");
        }
        return this.save(role);
    }

    @Override
    public boolean changeStatus(Long id, Integer status) {
        SysRole role = this.getById(id);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        role.setStatus(status);
        return this.updateById(role);
    }
}