package com.campus.activity.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.activity.entity.sys.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper // 或在启动类加@MapperScan("com.campus.activity.mapper")
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {
}