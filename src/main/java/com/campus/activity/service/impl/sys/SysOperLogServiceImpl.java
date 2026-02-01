package com.campus.activity.service.impl.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.sys.SysOperLog;
import com.campus.activity.mapper.sys.SysOperLogMapper; // 需确保你有这个Mapper类
import com.campus.activity.service.sys.SysOperLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统操作日志服务实现类
 * 核心：添加@Service注解，让Spring扫描并注册为Bean
 */
@Service // 关键注解：必须添加，否则Spring找不到该Bean
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog>
        implements SysOperLogService {

    /**
     * 清空所有日志
     * 添加@Transactional保证事务一致性（可选，但建议加）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean clearAllLog() {
        try {
            // 调用Mapper的删除方法清空所有日志
            // baseMapper是ServiceImpl提供的内置Mapper对象
            int deleteCount = baseMapper.delete(null);
            // 返回是否删除成功（删除数量≥0即视为成功）
            return deleteCount >= 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}