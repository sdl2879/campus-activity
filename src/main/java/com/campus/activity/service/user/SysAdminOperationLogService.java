package com.campus.activity.service.user;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.user.SysAdminOperationLog;
import com.campus.activity.Vo.LogQueryVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 管理员操作日志服务接口（适配架构文档「日志管理」模块）
 */
public interface SysAdminOperationLogService {

    /**
     * 保存操作日志（核心：记录全角色操作轨迹）
     * @param log 日志实体
     * @return 保存结果
     */
    boolean save(SysAdminOperationLog log);

    /**
     * 分页多条件查询操作日志（架构要求：支持按用户、模块、时间、操作类型筛选）
     * @param page 分页参数
     * @param queryVO 筛选条件（adminId/module/operation/startTime/endTime）
     * @return 分页日志列表
     */
    IPage<SysAdminOperationLog> pageQuery(Page<SysAdminOperationLog> page, LogQueryVO queryVO);

    /**
     * 导出日志为Excel（架构要求：支持日志导出功能）
     * @param queryVO 筛选条件
     * @param response 响应对象（下载Excel）
     */
    void exportLog(LogQueryVO queryVO, HttpServletResponse response);

    /**
     * 批量删除日志（按时间范围）
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 删除结果
     */
    boolean batchDeleteByTimeRange(String startTime, String endTime);

    /**
     * 查询单个管理员的操作轨迹（架构要求：追溯模块配置、权限调整等关键操作）
     * @param adminId 管理员ID
     * @return 操作日志列表
     */
    List<SysAdminOperationLog> listByAdminId(Long adminId);
}