package com.campus.activity.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.activity.entity.user.SysAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统管理员 Mapper 接口
 *
 * 注意：所有分页和条件查询均由 Service 层通过 QueryWrapper 实现，
 *       因此本接口不再包含 selectAdminPage 方法。
 */
@Mapper
public interface SysAdminMapper extends BaseMapper<SysAdmin> {

    /**
     * 根据用户名查询系统管理员（用于登录或账号唯一性校验）
     *
     * @param username 登录账号
     * @return SysAdmin 实体（含密码）
     */
    SysAdmin selectByUsername(@Param("username") String username);
}