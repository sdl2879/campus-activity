package com.campus.activity.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LoginResponse {
    private String  token;
    private String  username;
    private String  realName;
    private String  roleId;      // ① 新增
    private Integer deptId;
}