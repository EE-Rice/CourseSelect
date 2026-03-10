package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("course") // 映射到数据库的course表
public class Course {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String courseNo; // 课程编号
    private String name; // 课程名称
    private BigDecimal credit; // 学分
    private Integer capacity; // 最大容量
    private Integer selectedCount; // 已选人数
    private String teacher; // 任课教师
    private String semester; // 学期
}
