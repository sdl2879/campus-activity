package com.campus.activity.service.impl.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.alibaba.fastjson.JSON;
import com.campus.activity.entity.sys.dto.ActivityManagerAddDTO;
import com.campus.activity.entity.sys.dto.ActivityManagerQueryDTO;
import com.campus.activity.entity.sys.dto.ActivityManagerUpdateDTO;
import com.campus.activity.entity.sys.dto.QualificationAuditDTO;
import com.campus.activity.entity.user.ActivityManager;
import com.campus.activity.entity.user.SysAdminOperationLog;
import com.campus.activity.mapper.user.ActivityManagerMapper;
import com.campus.activity.mapper.user.SysAdminOperationLogMapper;
import com.campus.activity.service.user.ActivityManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityManagerServiceImpl implements ActivityManagerService {

    @Autowired
    private ActivityManagerMapper activityManagerMapper;

    @Autowired
    private SysAdminOperationLogMapper logMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public IPage<ActivityManager> getActivityManagerPage(ActivityManagerQueryDTO query) {
        Page<ActivityManager> page = new Page<>(query.getPageNum(), query.getPageSize());
        return activityManagerMapper.selectActivityManagerPage(page, query);
    }

    @Override
    public ActivityManager getActivityManagerById(Long id) {
        if (id == null) {
            throw new RuntimeException("活动负责人ID不能为空");
        }
        return activityManagerMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addActivityManager(ActivityManagerAddDTO addDTO, Long operatorId, String operatorName, String ip) {
        ActivityManager exist = activityManagerMapper.selectByUsername(addDTO.getUsername());
        if (exist != null) {
            throw new RuntimeException("登录账号已存在");
        }

        String encryptPwd = passwordEncoder.encode(addDTO.getPassword());

        ActivityManager manager = new ActivityManager();
        manager.setUsername(addDTO.getUsername());
        manager.setPassword(encryptPwd);
        manager.setName(addDTO.getName());
        manager.setDeptId(addDTO.getDeptId());
        manager.setDeptName(addDTO.getDeptName());
        manager.setMajor(addDTO.getMajor() == null ? "" : addDTO.getMajor());
        manager.setPhone(addDTO.getPhone());
        manager.setQualificationStatus(0);
        manager.setRoleId(3L);
        manager.setStatus(1);
        manager.setManageActivityCount(0);
        manager.setCreateTime(LocalDateTime.now());
        manager.setUpdateTime(LocalDateTime.now());

        activityManagerMapper.insert(manager);

        recordLog(operatorId, operatorName, "新增", "活动负责人管理", JSON.toJSONString(addDTO), ip);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateActivityManager(ActivityManagerUpdateDTO updateDTO, Long operatorId, String operatorName, String ip) {
        ActivityManager manager = activityManagerMapper.selectById(updateDTO.getId());
        if (manager == null) {
            throw new RuntimeException("活动负责人不存在");
        }

        manager.setName(updateDTO.getName());
        manager.setDeptId(updateDTO.getDeptId());
        manager.setDeptName(updateDTO.getDeptName());
        manager.setMajor(updateDTO.getMajor() == null ? "" : updateDTO.getMajor());
        manager.setPhone(updateDTO.getPhone());
        manager.setStatus(updateDTO.getStatus());
        manager.setUpdateTime(LocalDateTime.now());

        activityManagerMapper.updateById(manager);

        recordLog(operatorId, operatorName, "编辑", "活动负责人管理", JSON.toJSONString(updateDTO), ip);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStatus(Long id, Integer status, Long operatorId, String operatorName, String ip) {
        if (id == null || status == null || (status != 0 && status != 1)) {
            throw new RuntimeException("参数错误：ID不能为空，状态仅支持0（禁用）或1（启用）");
        }

        ActivityManager manager = activityManagerMapper.selectById(id);
        if (manager == null) {
            throw new RuntimeException("活动负责人不存在");
        }

        if (manager.getStatus().equals(status)) {
            throw new RuntimeException("当前状态已为" + (status == 1 ? "启用" : "禁用") + "，无需操作");
        }

        manager.setStatus(status);
        manager.setUpdateTime(LocalDateTime.now());
        activityManagerMapper.updateById(manager);

        String operation = status == 1 ? "启用" : "禁用";
        Map<String, Object> logData = new HashMap<>();
        logData.put("id", id);
        logData.put("status", status);
        recordLog(operatorId, operatorName, operation, "活动负责人管理", JSON.toJSONString(logData), ip);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean auditQualification(QualificationAuditDTO auditDTO, Long operatorId, String operatorName, String ip) {
        if (auditDTO.getId() == null || auditDTO.getQualificationStatus() == null) {
            throw new RuntimeException("ID和审核结果不能为空");
        }
        if (auditDTO.getQualificationStatus() == 2 && (auditDTO.getRejectReason() == null || auditDTO.getRejectReason().trim().isEmpty())) {
            throw new RuntimeException("驳回时必须填写驳回原因");
        }

        ActivityManager manager = activityManagerMapper.selectById(auditDTO.getId());
        if (manager == null) {
            throw new RuntimeException("活动负责人不存在");
        }

        if (manager.getQualificationStatus() != 0) {
            throw new RuntimeException("该负责人资质状态已审核，无需重复操作");
        }

        activityManagerMapper.updateQualificationStatus(
                auditDTO.getId(),
                auditDTO.getQualificationStatus(),
                auditDTO.getRejectReason() == null ? "" : auditDTO.getRejectReason().trim()
        );

        String operation = auditDTO.getQualificationStatus() == 1 ? "资质审核通过" : "资质审核驳回";
        recordLog(operatorId, operatorName, operation, "活动负责人管理", JSON.toJSONString(auditDTO), ip);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean resetPassword(Long id, Long operatorId, String operatorName, String ip) {
        ActivityManager manager = activityManagerMapper.selectById(id);
        if (manager == null) {
            throw new RuntimeException("活动负责人不存在");
        }

        String defaultPwd = "123456";
        String encryptPwd = passwordEncoder.encode(defaultPwd);
        manager.setPassword(encryptPwd);
        manager.setUpdateTime(LocalDateTime.now());
        activityManagerMapper.updateById(manager);

        Map<String, Object> logData = new HashMap<>();
        logData.put("id", id);
        logData.put("password", "******");
        recordLog(operatorId, operatorName, "重置密码", "活动负责人管理", JSON.toJSONString(logData), ip);

        return true;
    }

    @Override
    public List<ActivityManager> exportActivityManagerList(ActivityManagerQueryDTO query) {
        if (query == null) {
            query = new ActivityManagerQueryDTO();
        }
        Page<ActivityManager> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<ActivityManager> managerPage = activityManagerMapper.selectActivityManagerPage(page, query);
        return managerPage.getRecords();
    }

    @Override
    public List<ActivityManager> listByDeptId(Long deptId, Integer qualificationStatus) {
        if (deptId == null) {
            throw new RuntimeException("院系ID不能为空");
        }
        return activityManagerMapper.selectByDeptId(deptId, qualificationStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateManageActivityCount(Long id, Integer count) {
        if (id == null || count == null || count < 0) {
            throw new RuntimeException("ID和活动数不能为空，且活动数不能为负数");
        }
        ActivityManager manager = activityManagerMapper.selectById(id);
        if (manager == null) {
            throw new RuntimeException("活动负责人不存在");
        }
        activityManagerMapper.updateManageActivityCount(id, count);
        return true;
    }

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
    @Override
    public Boolean deleteActivityManager(Long id) {
              if (id == null || id <= 0) return false;
              // ✅ 自动转为逻辑删除 UPDATE ... SET deleted=1
              int rows = activityManagerMapper.deleteById(id);
               return rows > 0;
            }
}