package com.example.controller;

import com.example.common.Result;
import com.example.entity.Admin;
import com.example.mapper.AdminMapper;
import com.example.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result <Map<String,Object>> login(@RequestBody Admin admin){
        try {
            Admin loginAdmin = adminService.login(admin.getUsername(), admin.getPassword());

            Map<String,Object> data = new HashMap<>();
            data.put("user", loginAdmin);
            data.put("token","admin_token_" + loginAdmin.getId());
            data.put("role","admin");

            return Result.success(data);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<Admin> getAdmin(@PathVariable Integer id){
        Admin admin = adminService.findById(id);
        return Result.success(admin);
    }
}
