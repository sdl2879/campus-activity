package com.campus.activity.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学生实体类
 * 对应数据库表：student
 * 注意：
 *   - grade 字段为字符串格式，如 "2023级"
 *   - totalScore 记录第二课堂累计分值
 */
@Data
@TableName("student")
public class Student {

    @TableId(type = IdType.AUTO)
    private Long id;                // 主键ID

    private String studentNo;       // 学号（唯一标识）

    private String username;        // 登录账号（通常等于学号）

    private String password;        // 加密后的密码（BCrypt）

    private String name;            // 学生姓名

    private Integer gender;         // 性别：0-未知，1-男，2-女

    private Long deptId;            // 所属院系ID（关联字典或组织架构）

    private String deptName;        // 所属院系名称（冗余字段，避免频繁联表）

    private Long majorId;           // 所属专业ID

    private String majorName;       // 所属专业名称（冗余字段）

    private String grade;           // 年级，格式为 "YYYY级"，例如 "2023级"

    private String className;       // 班级名称，如 "计算机科学与技术1班"

    private String phone;           // 联系电话

    private String email;           // 邮箱地址

    private String avatar;          // 头像URL（可为空）

    private String interestTags;    // 兴趣标签，逗号分隔，如 "编程,篮球,摄影"

    private BigDecimal totalScore;  // 第二课堂总分值（精确到小数点后两位）

    private Integer status;         // 账号状态：1-启用（正常），0-禁用（冻结）

    private LocalDateTime createTime; // 账号创建时间

    private LocalDateTime updateTime; // 最后更新时间（MyBatis-Plus 可自动填充）

    private LocalDateTime lastLoginTime; // 最后登录时间
}