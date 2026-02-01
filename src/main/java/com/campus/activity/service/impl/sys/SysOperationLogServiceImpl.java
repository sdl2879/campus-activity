package com.campus.activity.service.impl.sys;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.sys.SysOperationLog;
import com.campus.activity.entity.sys.dto.LogQueryDTO;
import com.campus.activity.mapper.sys.SysOperationLogMapper;
import com.campus.activity.service.sys.SysOperationLogService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 操作日志服务实现类
 */
@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog> implements SysOperationLogService {

    @Resource
    private SysOperationLogMapper operationLogMapper;

    @Override
    public IPage<SysOperationLog> getOperationLogPage(Page<SysOperationLog> page, LogQueryDTO query) {
        return operationLogMapper.selectOperationLogPage(page, query);
    }

    @Override
    public void exportOperationLog(LogQueryDTO query, HttpServletResponse response) {
        try {
            // 1. 查询导出数据
            List<SysOperationLog> logList = operationLogMapper.selectOperationLogExport(query);

            // 2. 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("操作日志_" + System.currentTimeMillis(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 3. 写入Excel并返回
            EasyExcel.write(response.getOutputStream(), SysOperationLog.class)
                    .sheet("操作日志")
                    .doWrite(logList);
        } catch (Exception e) {
            throw new RuntimeException("导出操作日志失败：" + e.getMessage());
        }
    }

    @Override
    public boolean saveOperationLog(SysOperationLog log) {
        return save(log);
    }
}