package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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

    @TableField("file_path")// 映射 file_path
    private String filePath; // 文件路径

    @TableField("file_type") // 映射 file_type
    private String fileType; // 文件类型

    @TableField("upload_time") // 映射 upload_time
    private LocalDateTime uploadTime; // 上传时间
}
