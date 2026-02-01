package com.campus.activity.service.impl.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.user.SysAdminOperationLog;
import com.campus.activity.mapper.user.SysAdminOperationLogMapper;
import com.campus.activity.service.user.SysAdminOperationLogService;
import com.campus.activity.utils.ExcelExportUtil;
import com.campus.activity.Vo.LogQueryVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员操作日志服务实现（完全贴合架构文档要求）
 */
@Service
public class SysAdminOperationLogServiceImpl extends ServiceImpl<SysAdminOperationLogMapper, SysAdminOperationLog>
        implements SysAdminOperationLogService {

    /**
     * 保存操作日志（异步调用，不影响主流程）
     */
    @Override
    public boolean save(SysAdminOperationLog log) {
        // 补充默认操作时间（避免前端漏传）
        if (log.getOperationTime() == null) {
            log.setOperationTime(LocalDateTime.now());
        }
        return baseMapper.insert(log) > 0;
    }

    /**
     * 分页多条件查询（架构核心筛选功能）
     */
    @Override
    public IPage<SysAdminOperationLog> pageQuery(Page<SysAdminOperationLog> page, LogQueryVO queryVO) {
        // 转换时间格式（String → LocalDateTime）
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (queryVO.getStartTime() != null && !queryVO.getStartTime().isEmpty()) {
            startTime = LocalDateTime.parse(queryVO.getStartTime(), formatter);
        }
        if (queryVO.getEndTime() != null && !queryVO.getEndTime().isEmpty()) {
            endTime = LocalDateTime.parse(queryVO.getEndTime(), formatter);
        }

        // 调用Mapper多条件分页查询
        return baseMapper.selectLogPage(
                page,
                queryVO.getAdminId(),
                queryVO.getModule(),
                queryVO.getOperation(),
                startTime,
                endTime
        );
    }

    /**
     * 导出Excel（架构要求：支持日志导出与操作行为分析）
     */
    @Override
    public void exportLog(LogQueryVO queryVO, HttpServletResponse response) {
        // 1. 查询符合条件的所有日志（不分页）
        Page<SysAdminOperationLog> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<SysAdminOperationLog> logPage = this.pageQuery(page, queryVO);
        List<SysAdminOperationLog> logList = logPage.getRecords();

        if (CollectionUtils.isEmpty(logList)) {
            throw new RuntimeException("无符合条件的日志数据");
        }

        // 2. 数据格式化（便于Excel展示）
        List<LogExportDTO> exportList = logList.stream().map(log -> {
            LogExportDTO dto = new LogExportDTO();
            dto.setLogId(log.getId());
            dto.setAdminId(log.getAdminId());
            dto.setAdminName(log.getAdminName());
            dto.setOperation(log.getOperation());
            dto.setModule(log.getModule());
            dto.setOperationTime(log.getOperationTime().format(formatter));
            dto.setIp(log.getIp());
            dto.setDetail(log.getDetail());
            return dto;
        }).collect(Collectors.toList());

        // 3. 导出Excel（使用项目工具类，无则需自行实现）
        String[] headers = {"日志ID", "操作人ID", "操作人姓名", "操作类型", "操作模块", "操作时间", "操作IP", "操作详情"};
        String[] fields = {"logId", "adminId", "adminName", "operation", "module", "operationTime", "ip", "detail"};
        ExcelExportUtil.exportExcel(response, "管理员操作日志_" + System.currentTimeMillis() + ".xlsx", headers, fields, exportList);
    }

    /**
     * 批量删除日志（按时间范围清理）
     */
    @Override
    public boolean batchDeleteByTimeRange(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endTime, formatter);

        int deleteCount = baseMapper.deleteByTimeRange(start, end);
        return deleteCount > 0;
    }

    /**
     * 查询单个管理员的操作轨迹（架构要求：追溯关键操作）
     */
    @Override
    public List<SysAdminOperationLog> listByAdminId(Long adminId) {
        return baseMapper.selectByAdminId(adminId);
    }

    // ========== 内部DTO：Excel导出数据格式化 ==========
    @lombok.Data
    private static class LogExportDTO {
        private Long logId;          // 日志ID
        private Long adminId;        // 操作人ID
        private String adminName;    // 操作人姓名
        private String operation;    // 操作类型
        private String module;       // 操作模块
        private String operationTime;// 操作时间
        private String ip;           // 操作IP
        private String detail;       // 操作详情
    }

    // 时间格式化器（全局复用）
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}