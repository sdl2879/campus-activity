package com.campus.activity.mapper.sys; // 包名是 sys 而非 system

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.activity.entity.sys.User; // 引用正确的实体类
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}