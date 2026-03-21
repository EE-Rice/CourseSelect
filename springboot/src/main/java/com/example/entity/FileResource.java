package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("file_resource") // 映射到数据库的file_resource表
public class FileResource {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title; // 文件标题
    private String filePath; // 文件路径
    private String fileType; // 文件类型
    private LocalDateTime uploadTime; // 上传时间
}
