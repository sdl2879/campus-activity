package com.campus.activity.service.impl.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.sys.SysRolePermission;
import com.campus.activity.entity.sys.dto.RolePermissionAssignDTO;
import com.campus.activity.mapper.sys.SysRolePermissionMapper;
import com.campus.activity.service.sys.SysRolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色-权限关联服务实现类
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements SysRolePermissionService {

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    /**
     * 分配权限：事务保证原子性（清空+新增）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(RolePermissionAssignDTO assignDTO) {
        // 1. 清空该角色原有所有权限
        sysRolePermissionMapper.deleteByRoleId(assignDTO.getRoleId());

        // 2. 如果有新权限，批量新增关联
        if (assignDTO.getPermIds() != null && !assignDTO.getPermIds().isEmpty()) {
            List<SysRolePermission> rolePermList = new ArrayList<>();
            for (Long permId : assignDTO.getPermIds()) {
                SysRolePermission rolePerm = new SysRolePermission();
                rolePerm.setRoleId(assignDTO.getRoleId());
                rolePerm.setPermId(permId);
                rolePermList.add(rolePerm);
            }
            // 批量插入（比循环插入效率高）
            saveBatch(rolePermList);
        }
    }

    @Override
    public List<Long> getAssignedPermIds(Long roleId) {
        // 查询角色已分配的权限ID
        return sysRolePermissionMapper.selectPermIdsByRoleId(roleId);
    }
}