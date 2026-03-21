package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data // 自动生成 getter、setter、toString、equals、hashCode 等方法
@TableName("student_course") // MyBatis-Plus 注解，映射到数据库的 student_course 表
public class StudentCourse {
    @TableId(type = IdType.AUTO) // 标记主键字段，type = IdType.AUTO：表示主键是自增长的（AUTO_INCREMENT）
    private Integer id;

    @TableField("student_id")
    private Integer studentId;  // 映射 student_id

    @TableField("course_id")
    private Integer courseId;   // 映射 course_id

    @TableField("select_time")
    private LocalDateTime selectTime; // 映射 select_time

    private Integer status; // 状态：1正常 2退课
}
