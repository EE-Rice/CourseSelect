package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Admin;
import com.example.mapper.AdminMapper;
import com.example.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(String username, String password) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).eq("password", password);  // 确保字段名正确

        Admin admin = adminMapper.selectOne(wrapper);
        if (admin == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        return admin;
    }

    @Override
    public Admin findById(Integer id) {
        return adminMapper.selectById(id);
    }
}
