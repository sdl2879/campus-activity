package com.campus.activity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.sys.SysConfig;
import com.campus.activity.Mapper.SysConfigMapper;
import com.campus.activity.service.SysConfigService;
import org.springframework.stereotype.Service;

@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {
    // 系统配置通常只有一条数据，直接查询第一条
    @Override
    public SysConfig getSystemConfig() {
        return this.lambdaQuery().one();
    }
}