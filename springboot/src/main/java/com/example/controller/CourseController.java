package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Course;
import com.example.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 课程控制器
 * 处理所有和课程相关的HTTP请求
 * 基础路径: /api/course
 */
@RestController // 标记这是一个REST风格的控制器
// 相当于 @Controller + @ResponseBody
// 所有方法的返回值直接写入HTTP响应体
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 分页查询课程列表
     * @param pageNum 当前页码，默认第1页
     * @param pageSize 每页大小，默认10条
     * @return 统一响应对象，包含分页数据
     *
     * 请求方式: GET
     * 请求示例: GET /api/course?pageNum=1&pageSize=10
     */
    @GetMapping
    public Result<Page<Course>> getCourse(
            // 从请求参数中获取值，如：/api/course?pageNum=2
            // defaultValue: 如果前端没传，默认用1
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize){
        // 调用Service层分页查询方法
        Page<Course> page = courseService.findPage(pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 添加课程
     * @param course 课程对象(由前端JSON自动转换)
     * @return 成功响应(无返回数据)
     *
     * 请求方式: POST
     * 请求示例: POST /api/course
     * 请求体: {"name":"Java编程", "credit":3, "teacher":"张老师"}
     */
    @PostMapping
    // 从HTTP请求体中获取JSON数据，自动转换为Course对象
    // 前端发送的JSON必须和Course属性对应
    public Result<Void> addCourse(@RequestBody Course course){
        courseService.save(course); // 调用Service层保存课程
        return Result.success(null);
    }

    /**
     * 更新课程信息
     * @param course 课程对象(必须包含id)
     * @return 成功响应(无返回数据)
     *
     * 请求方式: PUT
     * 请求示例: PUT /api/course
     * 请求体: {"id":1, "name":"Java高级编程", "credit":4}
     */
    @PutMapping
    public Result<Void> updateCourse(@RequestBody Course course){
        courseService.update(course); // 调用Service层更新课程
        return Result.success(null);
    }

    /**
     * 删除课程
     * @param id 课程ID(从URL路径获取)
     * @return 成功响应(无返回数据)
     *
     * 请求方式: DELETE
     * 请求示例: DELETE /api/course/1
     */
    @DeleteMapping("/{id}")
    // 从URL路径中获取值，如：/api/course/5
    // 方法参数名id和{id}对应
    public Result<Void> deleteCourse(@PathVariable Integer id){ // @PathVariable 用于从URL路径获取参数
        courseService.delete(id); // 调用Service层删除课程
        return Result.success(null);
    }
}
