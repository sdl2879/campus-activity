package com.campus.activity.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.activity.entity.sys.SysRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色Mapper（MyBatis-Plus基础CRUD）
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    // 无需额外方法，MyBatis-Plus已封装基础CRUD
}