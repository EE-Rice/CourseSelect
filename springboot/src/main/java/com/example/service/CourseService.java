package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Course;

import java.awt.print.Pageable;
import java.util.List;

public interface CourseService {
    List<Course> findAll();
    Page<Course> findPage(int pageNum, int pageSize);
    Course findById(Integer id);
    void save(Course course);
    void update(Course course);
    void delete(Integer id);
}
