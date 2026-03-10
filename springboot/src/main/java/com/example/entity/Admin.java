package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("admin") // 映射到数据库的 admin 表
public class Admin {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username; // 用户名
    private String password; // 密码
    private String name; // 姓名
    private String role; // 角色:ADMIN
}
