package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data // 自动生成 getter、setter、toString、equals、hashCode 等方法
@TableName("student_course") // MyBatis-Plus 注解，映射到数据库的 student_course 表
public class StudentCourse {
    @TableId(type = IdType.AUTO) // 标记主键字段，type = IdType.AUTO：表示主键是自增长的（AUTO_INCREMENT）
    private Integer id;

    private Integer studentId; // 学生ID
    private Integer courseId; // 课程ID
    private LocalDateTime selectTime; // 选课时间
    private Integer status; // 状态：1正常 2退课
}
