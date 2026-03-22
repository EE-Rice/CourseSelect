package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper // MyBatis Mapper注解，告诉SpringBoot这是一个数据访问接口，Spring自动创建实现类，Java得以访问数据库
public interface UserMapper extends BaseMapper<User> { // BaseMapper：MyBatis-Plus 提供的通用 Mapper

    // 根据用户名查询用户
    @Select("SELECT * FROM student WHERE student_no = #{username}")
    User findByUsername(@Param("username") String username);

    // 也可以继承 BaseMapper，获得基本的 CRUD 操作
}
