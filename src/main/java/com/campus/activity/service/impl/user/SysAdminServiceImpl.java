package com.campus.activity.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.sys.dto.AdminAddDTO;
import com.campus.activity.entity.sys.dto.AdminQueryDTO;
import com.campus.activity.entity.sys.dto.AdminUpdateDTO;
import com.campus.activity.entity.user.SysAdmin;
import com.campus.activity.entity.user.SysAdminOperationLog;
import com.campus.activity.mapper.user.SysAdminMapper;
import com.campus.activity.mapper.user.SysAdminOperationLogMapper;
import com.campus.activity.service.user.SysAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统管理员服务实现类 —— 完全修复版（无编译错误、字段匹配、日志正确）
 */
@Service
public class SysAdminServiceImpl
        extends ServiceImpl<SysAdminMapper, SysAdmin>
        implements SysAdminService {

    @Autowired
    private SysAdminOperationLogMapper logMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ========== Controller 直接调用的核心方法 ==========

    @Override
    public IPage<SysAdmin> pageQuery(Page<SysAdmin> page, String username, String name, Integer status) {
        QueryWrapper<SysAdmin> qw = new QueryWrapper<>();
        if (StringUtils.hasText(username)) {
            qw.like("username", username);
        }
        if (StringUtils.hasText(name)) {
            qw.like("real_name", name); // ✅ 数据库字段是 real_name
        }
        if (status != null) {
            qw.eq("status", status);
        }
        qw.orderByDesc("create_time");
        return this.page(page, qw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addAdmin(SysAdmin sysAdmin) {
        if (sysAdmin == null || sysAdmin.getUsername() == null || sysAdmin.getPassword() == null) {
            throw new RuntimeException("账号或密码不能为空");
        }
        if (this.getBaseMapper().selectByUsername(sysAdmin.getUsername()) != null) {
            throw new RuntimeException("登录账号已存在，请更换账号");
        }

        sysAdmin.setPassword(passwordEncoder.encode(sysAdmin.getPassword()));
        sysAdmin.setStatus(1);
        sysAdmin.setCreateTime(LocalDateTime.now());
        sysAdmin.setUpdateTime(LocalDateTime.now());

        boolean success = this.save(sysAdmin);
        if (success) {
            recordLog(1L, "超级管理员", "新增", "系统管理员管理", sysAdmin.toString(), "127.0.0.1");
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(SysAdmin sysAdmin) {
        SysAdmin oldAdmin = this.getById(sysAdmin.getId());
        if (oldAdmin == null) {
            throw new RuntimeException("管理员不存在，无法编辑");
        }
        sysAdmin.setUsername(oldAdmin.getUsername());
        sysAdmin.setPassword(oldAdmin.getPassword());
        sysAdmin.setUpdateTime(LocalDateTime.now());

        boolean success = this.updateById(sysAdmin);
        if (success) {
            recordLog(1L, "超级管理员", "编辑", "系统管理员管理", sysAdmin.toString(), "127.0.0.1");
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeStatus(Long id, Integer status) {
        if (status == null || (status != 0 && status != 1)) {
            throw new RuntimeException("状态参数错误，仅支持 0（禁用）或 1（启用）");
        }
        SysAdmin admin = this.getById(id);
        if (admin == null) {
            throw new RuntimeException("管理员不存在");
        }
        if (admin.getStatus().equals(status)) {
            throw new RuntimeException("管理员当前已为" + (status == 1 ? "启用" : "禁用") + "状态");
        }

        admin.setStatus(status);
        admin.setUpdateTime(LocalDateTime.now());
        boolean success = this.updateById(admin);

        if (success) {
            String operation = status == 1 ? "启用" : "禁用";
            recordLog(1L, "超级管理员", operation, "系统管理员管理", "{id:" + id + ",status:" + status + "}", "127.0.0.1");
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(Long id, String newPassword) {
        if (id == null || newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("管理员ID和新密码不能为空");
        }
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("新密码长度不能少于6位");
        }

        SysAdmin admin = this.getById(id);
        if (admin == null) {
            throw new RuntimeException("管理员不存在，无法重置密码");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        admin.setPassword(encodedPassword);
        admin.setUpdateTime(LocalDateTime.now());

        boolean success = this.updateById(admin);
        if (success) {
            recordLog(0L, "System", "重置密码", "系统管理员管理", "管理员ID: " + id, "127.0.0.1");
        }
        return success;
    }

    @Override
    public SysAdmin getById(Long id) {
        return super.getById(id);
    }

    // ========== 架构要求的扩展方法 ==========

    @Override
    public IPage<SysAdmin> getAdminPage(AdminQueryDTO query) {
        if (query == null) query = new AdminQueryDTO();
        Page<SysAdmin> page = new Page<>(query.getPageNum(), query.getPageSize());

        QueryWrapper<SysAdmin> qw = new QueryWrapper<>();
        if (StringUtils.hasText(query.getUsername())) {
            qw.like("username", query.getUsername());
        }
        if (StringUtils.hasText(query.getName())) {
            qw.like("real_name", query.getName()); // ✅
        }
        if (query.getStatus() != null) {
            qw.eq("status", query.getStatus());
        }
        qw.orderByDesc("create_time");

        return this.page(page, qw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addAdmin(AdminAddDTO addDTO, Long operatorId, String operatorName, String ip) {
        if (addDTO == null || operatorId == null || operatorName == null || ip == null) {
            throw new RuntimeException("入参不能为空");
        }
        if (this.getBaseMapper().selectByUsername(addDTO.getUsername()) != null) {
            throw new RuntimeException("登录账号已存在");
        }

        String encryptPwd = passwordEncoder.encode(addDTO.getPassword());
        SysAdmin admin = new SysAdmin();
        admin.setUsername(addDTO.getUsername());
        admin.setPassword(encryptPwd);
        admin.setRealName(addDTO.getName()); // ✅ setRealName
        admin.setDept(addDTO.getDept());
        admin.setPhone(addDTO.getPhone());
        admin.setAvatar(addDTO.getAvatar());
        admin.setRoleId(addDTO.getRoleId());
        admin.setStatus(1);
        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());

        boolean success = this.save(admin);
        if (success) {
            recordLog(operatorId, operatorName, "新增", "系统管理员管理", addDTO.toString(), ip);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAdmin(AdminUpdateDTO updateDTO, Long operatorId, String operatorName, String ip) {
        if (updateDTO == null || updateDTO.getId() == null) {
            throw new RuntimeException("管理员ID不能为空");
        }
        SysAdmin admin = this.getById(updateDTO.getId());
        if (admin == null) {
            throw new RuntimeException("管理员不存在");
        }

        admin.setRealName(updateDTO.getName()); // ✅ setRealName
        admin.setDept(updateDTO.getDept());
        admin.setPhone(updateDTO.getPhone());
        admin.setAvatar(updateDTO.getAvatar());
        admin.setRoleId(updateDTO.getRoleId());
        admin.setStatus(updateDTO.getStatus());
        admin.setUpdateTime(LocalDateTime.now());

        boolean success = this.updateById(admin);
        if (success) {
            recordLog(operatorId, operatorName, "编辑", "系统管理员管理", updateDTO.toString(), ip);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAdminStatus(Long id, Integer status, Long operatorId, String operatorName, String ip) {
        if (id == null || status == null || (status != 0 && status != 1)) {
            throw new RuntimeException("参数错误：ID不能为空，状态仅支持0/1");
        }
        SysAdmin admin = this.getById(id);
        if (admin == null) {
            throw new RuntimeException("管理员不存在");
        }

        admin.setStatus(status);
        admin.setUpdateTime(LocalDateTime.now());
        boolean success = this.updateById(admin);

        if (success) {
            String operation = status == 1 ? "启用" : "禁用";
            recordLog(operatorId, operatorName, operation, "系统管理员管理", "{id:" + id + ",status:" + status + "}", ip);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean resetAdminPassword(Long id, String newPassword, Long operatorId, String operatorName, String ip) {
        if (id == null || newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("参数错误：ID不能为空，密码长度≥6位");
        }
        SysAdmin admin = this.getById(id);
        if (admin == null) {
            throw new RuntimeException("管理员不存在");
        }

        String encryptPwd = passwordEncoder.encode(newPassword);
        admin.setPassword(encryptPwd);
        admin.setUpdateTime(LocalDateTime.now());
        boolean success = this.updateById(admin);

        if (success) {
            recordLog(operatorId, operatorName, "重置密码", "系统管理员管理", "{id:" + id + ",newPassword:******}", ip);
        }
        return success;
    }

    // ✅ 关键修复：正确实现接口方法 exportAdminList
    @Override
    public List<SysAdmin> exportAdminList(AdminQueryDTO query) {
        QueryWrapper<SysAdmin> qw = new QueryWrapper<>();
        if (query != null) {
            if (StringUtils.hasText(query.getUsername())) {
                qw.like("username", query.getUsername());
            }
            if (StringUtils.hasText(query.getName())) {
                qw.like("real_name", query.getName()); // ✅
            }
            if (query.getStatus() != null) {
                qw.eq("status", query.getStatus());
            }
        }
        qw.orderByDesc("create_time");
        return this.list(qw);
    }

    // ========== 内部工具方法 ==========
    private void recordLog(Long operatorId, String operatorName, String operation, String module, String detail, String ip) {
        SysAdminOperationLog log = new SysAdminOperationLog();
        log.setAdminId(operatorId);
        log.setAdminName(operatorName);
        log.setOperation(operation);
        log.setModule(module);
        log.setIp(ip);
        log.setDetail(detail);
        log.setOperationTime(LocalDateTime.now());
        logMapper.insert(log);
    }
}