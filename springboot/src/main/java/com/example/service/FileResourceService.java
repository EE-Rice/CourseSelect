package com.example.service;

import com.example.entity.FileResource;

import java.util.List;

public interface FileResourceService {
    List<FileResource> findAll();
    FileResource findById(Integer id);
    void save(FileResource fileResource);
    void update(FileResource fileResource);
    void delete(Integer id);
}
