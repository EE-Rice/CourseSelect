package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.FileResource;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileResourceMapper extends BaseMapper<FileResource> {
    // 继承BaseMapper即可获得基本的CRUD操作
}
