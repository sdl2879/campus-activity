package com.campus.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.activity.entity.system.Role;
import com.campus.activity.entity.system.User;
import com.campus.activity.Mapper.SystemMapper.RoleMapper;
import com.campus.activity.Mapper.SystemMapper.UserMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring Security用户详情服务（加载用户信息和权限）
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 查询用户（修复：定义user变量接收查询结果）
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
                        .eq(User::getIsDeleted, 0)   // 如果有逻辑删除
        );

        // 校验用户是否存在
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        // 2. 查询用户角色权限（增加空指针防护）
        Role role = roleMapper.selectById(Long.parseLong(user.getRoleId()));
        if (role == null) {
            throw new UsernameNotFoundException("用户角色不存在");
        }

        // 封装权限列表
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 注意：Spring Security角色需要以ROLE_开头，这里做兼容处理
        String roleCode = role.getRoleCode();
        if (!roleCode.startsWith("ROLE_")) {
            roleCode = "ROLE_" + roleCode;
        }
        authorities.add(new SimpleGrantedAuthority(roleCode));

        // 3. 构建UserDetails返回（修复：如果User实体未实现UserDetails，需用Security的User类封装）
        // 方式1：如果你的User实体实现了UserDetails接口，直接返回（推荐）
        // user.setAuthorities(authorities);
        // return user;

        // 方式2：如果User实体未实现UserDetails，使用Spring Security内置的User类封装
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getStatus() == 1, // 假设1=启用，0=禁用（根据你的字段逻辑调整）
                true,  // 账户未过期
                true,  // 凭证未过期
                true,  // 账户未锁定
                authorities
        );
    }
}