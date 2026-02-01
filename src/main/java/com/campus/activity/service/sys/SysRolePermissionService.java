package com.campus.activity.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.activity.entity.sys.SysRolePermission;
import com.campus.activity.entity.sys.dto.RolePermissionAssignDTO;

import java.util.List;

/**
 * 角色-权限关联服务接口
 */
public interface SysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 给角色分配权限（先清空原有权限，再新增）
     * @param assignDTO 权限分配参数
     */
    void assignPermissions(RolePermissionAssignDTO assignDTO);

    /**
     * 查询角色已分配的权限ID列表
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> getAssignedPermIds(Long roleId);
}