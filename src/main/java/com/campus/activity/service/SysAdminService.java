package com.campus.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.activity.entity.user.SysAdmin;

public interface SysAdminService extends IService<SysAdmin> {
    /**
     * 分页查询系统管理员
     */
    IPage<SysAdmin> pageQuery(Page<SysAdmin> page, String username, String realName, Integer status);

    /**
     * 新增管理员（密码加密）
     */
    boolean addAdmin(SysAdmin sysAdmin);

    /**
     * 修改管理员状态
     */
    boolean changeStatus(Long id, Integer status);

    /**
     * 重置密码（默认123456）
     */
    boolean resetPassword(Long id);
}