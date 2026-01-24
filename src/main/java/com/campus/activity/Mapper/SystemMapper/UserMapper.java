package com.campus.activity.Mapper.SystemMapper;
// 改为小写 mapper，和 XML 一致
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.activity.entity.system.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表数据库访问（MyBatis-Plus）
 * 自带 CRUD，但自定义方法需要显式声明
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 新增：声明自定义的 selectByUsername 方法
    User selectByUsername(@Param("username") String username);
}