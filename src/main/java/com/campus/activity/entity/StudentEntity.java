package com.campus.activity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

// entity/campus/StudentEntity.java
@Data
@TableName("student")
public class StudentEntity implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String username;
    private String password;
    private String role;

    // 文理科字段
    @TableField("subject_type")
    private SubjectTypeEnum subjectType = SubjectTypeEnum.UNSPECIFIED;

    private Integer totalScore;
    private Integer isGraduate;

    public enum SubjectTypeEnum {
        UNSPECIFIED, ARTS, SCIENCE
    }
}