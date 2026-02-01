package com.campus.activity.service.impl.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.sys.SysPermission;
import com.campus.activity.mapper.sys.SysPermissionMapper;
import com.campus.activity.service.sys.SysPermissionService;
import org.springframework.stereotype.Service;

/**
 * 权限服务实现类
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {
}