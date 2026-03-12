package com.example.service;

import com.example.entity.Admin;

import java.util.List;

public interface AdminService {
    Admin login(String username, String password);
    Admin findById(Integer id);
}
