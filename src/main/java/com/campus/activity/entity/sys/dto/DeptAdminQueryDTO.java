package com.campus.activity.entity.sys.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeptAdminQueryDTO {
    private String username;        // 账号模糊查询
    private String name;            // 姓名模糊查询
    private Long deptId;            // 院系ID筛选
    private Integer status;         // 状态筛选（1-启用，0-禁用）
    private LocalDateTime startTime; // 创建时间起始
    private LocalDateTime endTime;   // 创建时间结束
    private Integer pageNum = 1;    // 页码
    private Integer pageSize = 10;  // 每页条数
}