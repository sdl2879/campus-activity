package com.campus.activity.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.sys.dto.DeptAdminQueryDTO;
import com.campus.activity.entity.user.DeptAdmin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeptAdminMapper extends BaseMapper<DeptAdmin> {
    // 分页多条件查询院系管理员列表
    IPage<DeptAdmin> selectDeptAdminPage(Page<DeptAdmin> page, @Param("query") DeptAdminQueryDTO query);

    // 按账号查询（账号唯一性校验）
    DeptAdmin selectByUsername(@Param("username") String username);

    // 按院系ID查询院系管理员列表
    List<DeptAdmin> selectByDeptId(@Param("deptId") Long deptId);
}