package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.FileResource;
import com.example.mapper.FileResourceMapper;
import com.example.service.FileResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileResourceServiceImpl implements FileResourceService {

    @Autowired
    private FileResourceMapper fileResourceMapper;

    @Override
    public List<FileResource> findAll() {
        return fileResourceMapper.selectList(null);
    }

    @Override
    public FileResource findById(Integer id) {
        return fileResourceMapper.selectById(id);
    }

    @Override
    public void save(FileResource fileResource) {
        fileResourceMapper.insert(fileResource);
    }

    @Override
    public void update(FileResource fileResource) {
        fileResourceMapper.updateById(fileResource);
    }

    @Override
    public void delete(Integer id) {
        fileResourceMapper.deleteById(id);
    }

}
