package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Admin;
import com.example.mapper.AdminMapper;
import com.example.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(String username, String password) {
        /**
         * getBytes() 将字符串转为字节数组
         *DigestUtils.md5DigestAsHex([字节数组])，将字节数组进行MD5加密，返回十六进制字符串
         *
         */
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());

        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).eq("password", encryptedPassword);

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
