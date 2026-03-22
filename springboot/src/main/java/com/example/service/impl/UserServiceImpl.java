package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service // 标记为服务层组件
public class UserServiceImpl implements UserService {

    // 1. Spring框架自动创建的 UserMapper 实现对象
    // 2. @Autowired 自动注入到 userMapper 变量
    // 3. 创建查询条件包装器

    @Autowired // 自动注入Mapper
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        // 密码加密后比较
        // String encryptPassword = DigestUtils.md5DigestAsHex(password.getBytes());

        /**
         * QueryWrapper 是MyBatis-Plus 提供的查询条件构造器
         * 用来构建 SQL 的 WHERE 条件
         * 不是数据库表，只是一个条件容器
         */
        QueryWrapper<User> wrapper = new QueryWrapper<>(); // 创建查询条件包装器
        wrapper.eq("student_no", username).eq("password", password);
        // 等价于 SQL: WHERE username = ? AND password = ?

        User user = userMapper.selectOne(wrapper); // Spring 注入的 Mapper 对象，执行查询，返回一个 User 对象
        if(user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        return user;
    }

    @Override
    public void register(User user) {
        // 检查用户名是否已经存在，用户名是学号！
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("student_no", user.getUsername());
        if (userMapper.selectCount(wrapper) > 0) { // selectCount()是MyBatis-Plus 提供的方法
            throw new RuntimeException("用户名已存在");
        }

        // 密码加密
        // user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        userMapper.insert(user); // MyBatis-Plus 提供的方法
    }

    @Override
    public User findById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public void updateProfile(User user) {
        userMapper.updateById(user);
    }

    // 添加这个方法
    @Override
    public List<User> findAll() {
        return userMapper.selectList(null);
    }
}
