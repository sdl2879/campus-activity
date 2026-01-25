package com.campus.activity.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.activity.entity.system.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色Mapper（操作sys_role表）
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据角色ID查询角色
     */
    Role selectById(@Param("id") Long id);
}