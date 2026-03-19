package com.example.controller;

import com.example.common.Result;
import com.example.entity.StudentCourse;
import com.example.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 选课控制器
 * 处理学生选课、退课等核心业务
 * 基础路径: /api/student-course
 */
@RestController
@RequestMapping("/api/student-course")
/**
 * 学生选课
 * @param studentId 学生ID
 * @param courseId 课程ID
 * @return 成功响应(无返回数据) 或 错误信息
 *
 * 请求方式: POST
 * 请求示例: POST /api/student-course/select?studentId=1&courseId=2
 */
public class StudentCourseController {

    @Autowired
    private StudentCourseService studentCourseService;

    @PostMapping("/select")
    public Result<Void> selectCourse(
            @RequestParam Integer studentId, // 从请求参数获取学生ID
            @RequestParam Integer courseId) {
        try {
            // 调用Service层执行选课逻辑
            studentCourseService.selectCourse(studentId, courseId);
            // 选课成功，返回成功响应（无数据）
            return Result.success(null);
        } catch (RuntimeException e) {
            // 选课失败（业务异常），返回错误信息
            // 例如："课程已满"、"已选过该课程"、"时间冲突"等
            return Result.error(e.getMessage());
        }
    }

/**
 * 学生退课
 * @param studentId 学生ID
 * @param courseId 课程ID
 * @return 成功响应(无返回数据) 或 错误信息
 *
 * 请求方式: POST
 * 请求示例: POST /api/student-course/cancel?studentId=1&courseId=2
 */
 @PostMapping("/cancel")
    public Result<Void> cancelCourse(@RequestParam Integer studentId, @RequestParam Integer courseId) {
        try {
            // 调用Service层执行退课逻辑
            studentCourseService.cancelCourse(studentId, courseId);
            return Result.success(null);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

/**
 * 获取学生的选课列表
 * @param studentId 学生ID (从URL路径获取)
 * @return 选课列表数据
 *
 * 请求方式: GET
 * 请求示例: GET /api/student-course/student/1
 */
 @GetMapping("/student/{studentId}")
    public Result<Object> getStudentCourse(@PathVariable Integer studentId) {
        // 这里需要返回学生选的课程详情，简化处理
        return Result.success("学生 " + studentId + " 的选课列表");
    }

}
