package com.example.service;

import com.example.entity.StudentCourse;

import java.util.List;

public interface StudentCourseService {
    List<StudentCourse> findByStudentId(Integer studentId);
    List<StudentCourse> findByCourseId(Integer courseId);
    void selectCourse(Integer studentId, Integer courseId);
    void cancelCourse(Integer studentId, Integer courseId);
}
