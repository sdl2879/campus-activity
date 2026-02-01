package com.campus.activity.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("activity_manager") // 与数据库表名一致
public class ActivityManager {
    @TableId(type = IdType.AUTO)
    private Long id;                // 主键ID
    private String username;        // 登录账号/工号
    private String password;        // 加密密码（BCrypt）
    private String name;            // 负责人姓名
    private Long deptId;            // 所属院系ID
    private String deptName;        // 所属院系名称（冗余）
    private String major;           // 所属专业
    private String phone;           // 联系方式
    private Integer qualificationStatus; // 资质状态（0-待审核，1-已通过，2-已驳回）
    private String rejectReason;    // 驳回原因
    private Long roleId;            // 角色ID（默认3）
    private Integer status;         // 账号状态：1-启用，0-禁用
    private Integer manageActivityCount; // 负责活动数
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private LocalDateTime lastLoginTime; // 最后登录时间
    @TableLogic
    private Integer deleted; // 0-未删除, 1-已删除
}