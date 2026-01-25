package com.campus.activity.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.Admin;
import com.campus.activity.Mapper.AdminMapper;
import com.campus.activity.service.AdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    // 头像存储路径（添加默认值，兜底）
    @Value("${upload.avatar.path:D:/campus/upload/avatar/}")
    private String avatarPath;

    // 头像访问URL（匹配server.context-path: /api）
    @Value("${upload.avatar.url:http://localhost:8080/api/avatar/}")
    private String avatarUrl;

    // 密码加密器
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public boolean changePassword(Long adminId, String oldPwd, String newPwd) {
        Admin admin = this.getById(adminId);
        if (admin == null || !passwordEncoder.matches(oldPwd, admin.getPassword())) {
            return false;
        }
        admin.setPassword(passwordEncoder.encode(newPwd));
        return this.updateById(admin);
    }

    @Override
    public String uploadAvatar(MultipartFile file) throws Exception {
        // 1. 校验文件
        if (file.isEmpty()) {
            throw new Exception("上传文件不能为空");
        }
        // 2. 自动创建存储目录
        File dir = new File(avatarPath);
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) {
                throw new Exception("创建头像存储目录失败，请检查路径权限");
            }
        }
        // 3. 生成唯一文件名（避免重复）
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new Exception("文件格式错误，请上传图片（jpg/png等）");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + suffix;
        // 4. 保存文件到本地
        File dest = new File(avatarPath + fileName);
        file.transferTo(dest);
        // 5. 返回可访问的头像URL（包含/api前缀）
        return avatarUrl + fileName;
    }
}