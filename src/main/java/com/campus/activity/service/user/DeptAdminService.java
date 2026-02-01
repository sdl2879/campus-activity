package com.campus.activity.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campus.activity.entity.sys.dto.DeptAdminAddDTO;
import com.campus.activity.entity.sys.dto.DeptAdminQueryDTO;
import com.campus.activity.entity.sys.dto.DeptAdminUpdateDTO;
import com.campus.activity.entity.user.DeptAdmin;
import com.campus.activity.entity.user.SysAdminOperationLog;

import java.util.List;

/**
 * 院系管理员服务接口（适配架构文档「院系管理员管理」三级功能）
 */
public interface DeptAdminService {
    // 分页查询院系管理员列表
    IPage<DeptAdmin> getDeptAdminPage(DeptAdminQueryDTO query);

    // 按ID查询详情
    DeptAdmin getDeptAdminById(Long id);

    // 新增院系管理员
    Boolean addDeptAdmin(DeptAdminAddDTO addDTO, Long operatorId, String operatorName, String ip);

    // 编辑院系管理员信息
    Boolean updateDeptAdmin(DeptAdminUpdateDTO updateDTO, Long operatorId, String operatorName, String ip);

    // 禁用/启用院系管理员
    Boolean updateStatus(Long id, Integer status, Long operatorId, String operatorName, String ip);

    // 重置密码（默认123456）
    Boolean resetPassword(Long id, Long operatorId, String operatorName, String ip);

    // 导出院系管理员列表
    List<DeptAdmin> exportDeptAdminList(DeptAdminQueryDTO query);

    // 按院系ID查询启用的院系管理员
    List<DeptAdmin> listByDeptId(Long deptId);
}