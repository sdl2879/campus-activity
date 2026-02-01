package com.campus.activity.service.impl.sys;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.sys.SysLoginLog;
import com.campus.activity.entity.sys.dto.LogQueryDTO;
import com.campus.activity.mapper.sys.SysLoginLogMapper; // 重点：正确导入 Mapper
import com.campus.activity.service.sys.SysLoginLogService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 登录日志服务实现类
 */
@Service // 关键：添加 @Service 注解，让 Spring 扫描到
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    @Resource // 关键：正确注入 Mapper（也可用 @Autowired）
    private SysLoginLogMapper loginLogMapper;

    @Override
    public IPage<SysLoginLog> getLoginLogPage(Page<SysLoginLog> page, LogQueryDTO query) {
        return loginLogMapper.selectLoginLogPage(page, query);
    }

    @Override
    public void exportLoginLog(LogQueryDTO query, HttpServletResponse response) {
        try {
            // 1. 查询导出数据
            List<SysLoginLog> logList = loginLogMapper.selectLoginLogExport(query);

            // 2. 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("登录日志_" + System.currentTimeMillis(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 3. 写入Excel并返回
            EasyExcel.write(response.getOutputStream(), SysLoginLog.class)
                    .sheet("登录日志")
                    .doWrite(logList);
        } catch (Exception e) {
            throw new RuntimeException("导出登录日志失败：" + e.getMessage());
        }
    }

    @Override
    public boolean saveLoginLog(SysLoginLog log) {
        return save(log);
    }
}