package com.campus.activity.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.activity.entity.sys.SysRoleLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色日志Mapper接口
 * 对应表：sys_role_log
 */
@Mapper
public interface SysRoleLogMapper extends BaseMapper<SysRoleLog> {
}