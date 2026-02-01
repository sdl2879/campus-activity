package com.campus.activity.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.activity.entity.sys.dto.AdminAddDTO;
import com.campus.activity.entity.sys.dto.AdminQueryDTO;
import com.campus.activity.entity.sys.dto.AdminUpdateDTO;
import com.campus.activity.entity.user.SysAdmin;

import java.util.List;

/**
 * 系统管理员服务接口 —— 已修复 resetPassword 参数问题
 */
public interface SysAdminService extends IService<SysAdmin> {

    // ========== Controller 直接调用的方法 ==========
    IPage<SysAdmin> pageQuery(Page<SysAdmin> page, String username, String name, Integer status);
    boolean addAdmin(SysAdmin sysAdmin);
    boolean updateById(SysAdmin sysAdmin);
    boolean changeStatus(Long id, Integer status);

    // 🔥 修复点：resetPassword 接收两个参数（id + newPassword）
    boolean resetPassword(Long id, String newPassword); // ← 关键修改！

    SysAdmin getById(Long id);

    // ========== 架构扩展方法 ==========
    IPage<SysAdmin> getAdminPage(AdminQueryDTO query);
    Boolean addAdmin(AdminAddDTO addDTO, Long operatorId, String operatorName, String ip);
    Boolean updateAdmin(AdminUpdateDTO updateDTO, Long operatorId, String operatorName, String ip);
    Boolean updateAdminStatus(Long id, Integer status, Long operatorId, String operatorName, String ip);

    // 注意：下面这个方法保留用于内部调用（带操作人信息）
    Boolean resetAdminPassword(Long id, String newPassword, Long operatorId, String operatorName, String ip);

    List<SysAdmin> exportAdminList(AdminQueryDTO query);
}