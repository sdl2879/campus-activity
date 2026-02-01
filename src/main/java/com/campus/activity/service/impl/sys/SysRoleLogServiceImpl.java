package com.campus.activity.service.impl.sys;

// 正确导入MyBatis-Plus的ServiceImpl
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.sys.SysRoleLog;
import com.campus.activity.mapper.sys.SysRoleLogMapper;
import com.campus.activity.service.sys.SysRoleLogService;
import org.springframework.stereotype.Service;

/**
 * 角色日志服务实现
 */
@Service
public class SysRoleLogServiceImpl extends ServiceImpl<SysRoleLogMapper, SysRoleLog> implements SysRoleLogService {
    // MyBatis-Plus已自动实现基础CRUD，无需额外代码
}