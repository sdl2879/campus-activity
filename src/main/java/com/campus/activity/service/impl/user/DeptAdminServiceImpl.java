package com.campus.activity.service.impl.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.alibaba.fastjson.JSON;
import com.campus.activity.entity.sys.dto.DeptAdminAddDTO;
import com.campus.activity.entity.sys.dto.DeptAdminQueryDTO;
import com.campus.activity.entity.sys.dto.DeptAdminUpdateDTO;
import com.campus.activity.entity.user.DeptAdmin;
import com.campus.activity.entity.user.SysAdminOperationLog;
import com.campus.activity.mapper.user.DeptAdminMapper;
import com.campus.activity.mapper.user.SysAdminOperationLogMapper;
import com.campus.activity.service.user.DeptAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeptAdminServiceImpl implements DeptAdminService {

    @Autowired
    private DeptAdminMapper deptAdminMapper;

    @Autowired
    private SysAdminOperationLogMapper logMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public IPage<DeptAdmin> getDeptAdminPage(DeptAdminQueryDTO query) {
        Page<DeptAdmin> page = new Page<>(query.getPageNum(), query.getPageSize());
        return deptAdminMapper.selectDeptAdminPage(page, query);
    }

    @Override
    public DeptAdmin getDeptAdminById(Long id) {
        if (id == null) {
            throw new RuntimeException("院系管理员ID不能为空");
        }
        return deptAdminMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addDeptAdmin(DeptAdminAddDTO addDTO, Long operatorId, String operatorName, String ip) {
        // 1. 账号唯一性校验
        DeptAdmin exist = deptAdminMapper.selectByUsername(addDTO.getUsername());
        if (exist != null) {
            throw new RuntimeException("登录账号已存在");
        }

        // 2. 密码加密
        String encryptPwd = passwordEncoder.encode(addDTO.getPassword());

        // 3. 构建实体
        DeptAdmin deptAdmin = new DeptAdmin();
        deptAdmin.setUsername(addDTO.getUsername());
        deptAdmin.setPassword(encryptPwd);
        deptAdmin.setName(addDTO.getName());
        deptAdmin.setDeptId(addDTO.getDeptId());
        deptAdmin.setDeptName(addDTO.getDeptName());
        deptAdmin.setManageMajors(addDTO.getManageMajors() == null ? "" : addDTO.getManageMajors());
        deptAdmin.setPhone(addDTO.getPhone());
        deptAdmin.setRoleId(2L); // 固定为院系管理员角色
        deptAdmin.setStatus(1); // 默认启用
        deptAdmin.setCreateTime(LocalDateTime.now());
        deptAdmin.setUpdateTime(LocalDateTime.now());

        // 4. 保存数据
        deptAdminMapper.insert(deptAdmin);

        // 5. 记录操作日志
        recordLog(operatorId, operatorName, "新增", "院系管理员管理", JSON.toJSONString(addDTO), ip);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDeptAdmin(DeptAdminUpdateDTO updateDTO, Long operatorId, String operatorName, String ip) {
        // 1. 校验是否存在
        DeptAdmin deptAdmin = deptAdminMapper.selectById(updateDTO.getId());
        if (deptAdmin == null) {
            throw new RuntimeException("院系管理员不存在");
        }

        // 2. 更新信息
        deptAdmin.setName(updateDTO.getName());
        deptAdmin.setDeptId(updateDTO.getDeptId());
        deptAdmin.setDeptName(updateDTO.getDeptName());
        deptAdmin.setManageMajors(updateDTO.getManageMajors() == null ? "" : updateDTO.getManageMajors());
        deptAdmin.setPhone(updateDTO.getPhone());
        deptAdmin.setStatus(updateDTO.getStatus());
        deptAdmin.setUpdateTime(LocalDateTime.now());

        // 3. 保存更新
        deptAdminMapper.updateById(deptAdmin);

        // 4. 记录日志
        recordLog(operatorId, operatorName, "编辑", "院系管理员管理", JSON.toJSONString(updateDTO), ip);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStatus(Long id, Integer status, Long operatorId, String operatorName, String ip) {
        // 1. 入参校验
        if (id == null || status == null || (status != 0 && status != 1)) {
            throw new RuntimeException("参数错误：ID不能为空，状态仅支持0（禁用）或1（启用）");
        }

        // 2. 校验存在性
        DeptAdmin deptAdmin = deptAdminMapper.selectById(id);
        if (deptAdmin == null) {
            throw new RuntimeException("院系管理员不存在");
        }

        // 3. 避免重复操作
        if (deptAdmin.getStatus().equals(status)) {
            throw new RuntimeException("当前状态已为" + (status == 1 ? "启用" : "禁用") + "，无需操作");
        }

        // 4. 更新状态
        deptAdmin.setStatus(status);
        deptAdmin.setUpdateTime(LocalDateTime.now());
        deptAdminMapper.updateById(deptAdmin);

        // 5. 记录日志 —— ✅ 使用 Map 替代非法匿名对象
        Map<String, Object> logData = new HashMap<>();
        logData.put("id", id);
        logData.put("status", status);
        recordLog(operatorId, operatorName, status == 1 ? "启用" : "禁用", "院系管理员管理",
                JSON.toJSONString(logData), ip);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean resetPassword(Long id, Long operatorId, String operatorName, String ip) {
        // 1. 校验存在性
        DeptAdmin deptAdmin = deptAdminMapper.selectById(id);
        if (deptAdmin == null) {
            throw new RuntimeException("院系管理员不存在");
        }

        // 2. 重置默认密码并加密
        String defaultPwd = "123456";
        String encryptPwd = passwordEncoder.encode(defaultPwd);
        deptAdmin.setPassword(encryptPwd);
        deptAdmin.setUpdateTime(LocalDateTime.now());
        deptAdminMapper.updateById(deptAdmin);

        // 3. 记录日志（密码脱敏）—— ✅ 使用 Map
        Map<String, Object> logData = new HashMap<>();
        logData.put("id", id);
        logData.put("password", "******");
        recordLog(operatorId, operatorName, "重置密码", "院系管理员管理",
                JSON.toJSONString(logData), ip);

        return true;
    }

    @Override
    public List<DeptAdmin> exportDeptAdminList(DeptAdminQueryDTO query) {
        if (query == null) {
            query = new DeptAdminQueryDTO();
        }
        Page<DeptAdmin> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<DeptAdmin> deptAdminPage = deptAdminMapper.selectDeptAdminPage(page, query);
        return deptAdminPage.getRecords();
    }

    @Override
    public List<DeptAdmin> listByDeptId(Long deptId) {
        if (deptId == null) {
            throw new RuntimeException("院系ID不能为空");
        }
        return deptAdminMapper.selectByDeptId(deptId);
    }

    // 内部工具方法：记录操作日志
    private void recordLog(Long operatorId, String operatorName, String operation,
                           String module, String detail, String ip) {
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