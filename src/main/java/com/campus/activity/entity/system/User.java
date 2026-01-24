package com.campus.activity.entity.system;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 系统用户实体（与数据库user表对应）
 */
@Data
@TableName("sys_user")
public class User implements UserDetails {
    @TableId(type = IdType.AUTO)
    private Long id;                // 用户ID
    private String username;        // 用户名（登录账号）
    private String password;        // 加密后的密码
    private String realName;        // 真实姓名
    private String roleId;          // 角色ID（关联sys_role表）
    private Integer deptId;         // 院系ID（关联sys_dept表）
    private Integer status;         // 状态（0-禁用，1-正常）
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private Integer isDeleted;      // 逻辑删除（0-未删，1-已删）

    // 角色权限（非数据库字段，用于Spring Security）
    private transient List<GrantedAuthority> authorities;

    // UserDetails接口实现方法
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 账号未过期（实际项目可扩展）
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 账号未锁定（实际项目可扩展）
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 凭证未过期（实际项目可扩展）
    }

    @Override
    public boolean isEnabled() {
        return status == 1; // 账号是否启用（对应status字段）
    }
}