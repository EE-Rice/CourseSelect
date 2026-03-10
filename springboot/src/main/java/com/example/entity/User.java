package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("student") // 映射到数据库的student_course表
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username; // 学号
    private String password;
    private String name;
    private String phone;
    private String email;
    private String sex;
    private String birth;
    private String avater; // 头像路径
    private String role; // 'student'或'admin'
}
