package com.campus.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.activity.entity.system.User;
import com.campus.activity.Mapper.UserMapper;
import com.campus.activity.service.SystemService;
import com.campus.activity.utils.JwtUtil;
import com.campus.activity. dto.LoginRequest;
import com.campus.activity.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 系统业务实现类
 */
@Service
public class SystemServiceImpl implements SystemService {

    // 1. 注入 JwtUtil 实例（核心修复点）
    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserMapper userMapper;

    // 如需密码加密，注入 PasswordEncoder（根据你的业务选择）
    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    /**
     * 登录接口实现
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 1. 校验入参
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            throw new RuntimeException("用户名或密码不能为空");
        }

        // 2. 查询用户（排除逻辑删除）
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, loginRequest.getUsername())
                        .eq(User::getIsDeleted, 0)
        );

        // 3. 校验用户是否存在 + 密码是否正确
        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }
        // 如果你用了密码加密，需用 passwordEncoder.matches 校验
        // if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        //     throw new RuntimeException("密码错误");
        // }
        // 未加密的密码校验（仅测试用，正式环境必须加密）
        if (!loginRequest.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 4. 生成 JWT Token（核心：通过实例调用，而非静态调用）
        String token = jwtUtil.createToken(user.getId()); // 修复：替换 JwtUtil.createToken(...)

        // 5. 组装返回结果
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setRoleId(user.getRoleId());

        return response;
    }
}