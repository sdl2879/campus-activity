package com.campus.activity.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.activity.entity.Admin;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService extends IService<Admin> {
    boolean changePassword(Long adminId, String oldPwd, String newPwd);
    String uploadAvatar(MultipartFile file) throws Exception;
}

