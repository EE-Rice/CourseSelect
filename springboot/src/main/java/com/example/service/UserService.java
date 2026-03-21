package com.example.service;

import com.example.entity.User;

import java.util.List;

public interface UserService {
    User login(String username, String password);
    void register(User user);
    User findById(Integer id);
    void updateProfile(User user);

    // 添加这个方法
    List<User> findAll();
}
