package com.campus.activity.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campus.activity.entity.sys.dto.ActivityManagerAddDTO;
import com.campus.activity.entity.sys.dto.ActivityManagerQueryDTO;
import com.campus.activity.entity.sys.dto.ActivityManagerUpdateDTO;
import com.campus.activity.entity.sys.dto.QualificationAuditDTO;
import com.campus.activity.entity.user.ActivityManager;

import java.util.List;

/**
 * 活动负责人服务接口（适配架构文档「活动负责人管理」三级功能）
 */
public interface ActivityManagerService {
    // 分页查询活动负责人列表
    IPage<ActivityManager> getActivityManagerPage(ActivityManagerQueryDTO query);

    // 按ID查询详情
    ActivityManager getActivityManagerById(Long id);

    // 新增活动负责人
    Boolean addActivityManager(ActivityManagerAddDTO addDTO, Long operatorId, String operatorName, String ip);

    // 编辑活动负责人信息
    Boolean updateActivityManager(ActivityManagerUpdateDTO updateDTO, Long operatorId, String operatorName, String ip);

    // 禁用/启用活动负责人账号
    Boolean updateStatus(Long id, Integer status, Long operatorId, String operatorName, String ip);

    // 资质审核（通过/驳回）
    Boolean auditQualification(QualificationAuditDTO auditDTO, Long operatorId, String operatorName, String ip);

    // 重置密码（默认123456）
    Boolean resetPassword(Long id, Long operatorId, String operatorName, String ip);

    // 导出活动负责人列表
    List<ActivityManager> exportActivityManagerList(ActivityManagerQueryDTO query);

    // 按院系ID查询活动负责人（支持筛选资质状态）
    List<ActivityManager> listByDeptId(Long deptId, Integer qualificationStatus);

    // 更新负责活动数（活动关联/解绑时调用）
    Boolean updateManageActivityCount(Long id, Integer count);

    // ✅ 新增：删除活动负责人（逻辑删除 or 物理删除）
    Boolean deleteActivityManager(Long id);
}