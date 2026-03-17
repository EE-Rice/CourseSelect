package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Course;
import com.example.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public Result<Page<Course>> getCourse(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize){
        Page<Course> page = courseService.findPage(pageNum, pageSize);
        return Result.success(page);
    }

    @PutMapping
    public Result<Void> addCourse(@RequestBody Course course){
        courseService.save(course);
        return Result.success(null);
    }

    @PutMapping
    public Result<Void> updateCourse(@RequestBody Course course){
        courseService.update(course);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteCourse(@RequestBody Integer id){
        courseService.delete(id);
        return Result.success(null);
    }
}
