package com.campus.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.user.SysAdmin;
import com.campus.activity.Mapper.user.SysAdminMapper;
import com.campus.activity.service.SysAdminService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public IPage<SysAdmin> pageQuery(Page<SysAdmin> page, String username, String realName, Integer status) {
        LambdaQueryWrapper<SysAdmin> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)) {
            wrapper.like(SysAdmin::getUsername, username);
        }
        if (StringUtils.hasText(realName)) {
            wrapper.like(SysAdmin::getRealName, realName);
        }
        if (status != null) {
            wrapper.eq(SysAdmin::getStatus, status);
        }
        wrapper.orderByDesc(SysAdmin::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean addAdmin(SysAdmin sysAdmin) {
        // 密码加密
        sysAdmin.setPassword(passwordEncoder.encode(sysAdmin.getPassword()));
        return save(sysAdmin);
    }

    @Override
    public boolean changeStatus(Long id, Integer status) {
        SysAdmin sysAdmin = new SysAdmin();
        sysAdmin.setId(id);
        sysAdmin.setStatus(status);
        return updateById(sysAdmin);
    }

    @Override
    public boolean resetPassword(Long id) {
        SysAdmin sysAdmin = new SysAdmin();
        sysAdmin.setId(id);
        // 默认密码123456加密
        sysAdmin.setPassword(passwordEncoder.encode("123456"));
        return updateById(sysAdmin);
    }
}