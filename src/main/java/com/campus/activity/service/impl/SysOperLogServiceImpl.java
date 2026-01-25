package com.campus.activity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.sys.SysOperLog;
import com.campus.activity.Mapper.SysOperLogMapper;
import com.campus.activity.service.SysOperLogService;
import org.springframework.stereotype.Service;

@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {
    @Override
    public Boolean clearAllLog() {
        return this.baseMapper.delete(null) > 0;
    }
}