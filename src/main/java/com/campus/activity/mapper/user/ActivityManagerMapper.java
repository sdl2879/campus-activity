package com.campus.activity.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.sys.dto.ActivityManagerQueryDTO;
import com.campus.activity.entity.user.ActivityManager;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityManagerMapper extends BaseMapper<ActivityManager> {
    // 分页多条件查询活动负责人列表
    IPage<ActivityManager> selectActivityManagerPage(Page<ActivityManager> page, @Param("query") ActivityManagerQueryDTO query);

    // 按账号查询（账号唯一性校验）
    ActivityManager selectByUsername(@Param("username") String username);

    // 按院系ID查询活动负责人列表（支持筛选资质状态）
    List<ActivityManager> selectByDeptId(@Param("deptId") Long deptId, @Param("qualificationStatus") Integer qualificationStatus);

    // 资质审核（更新资质状态和驳回原因）
    int updateQualificationStatus(@Param("id") Long id,
                                  @Param("qualificationStatus") Integer qualificationStatus,
                                  @Param("rejectReason") String rejectReason);

    // 更新负责活动数（活动关联/解绑时调用）
    int updateManageActivityCount(@Param("id") Long id, @Param("count") Integer count);
}