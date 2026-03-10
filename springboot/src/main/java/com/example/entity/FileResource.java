package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("doc") // 映射到数据库的 doc 表
public class FileResource {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title; // 文件标题
    private String time; // 发文时间
    private String authority; // 发文作者
    private String number; // 发文字号
    private String pdf; // 文件路径
    private LocalDateTime createTime; // 创建时间
}
