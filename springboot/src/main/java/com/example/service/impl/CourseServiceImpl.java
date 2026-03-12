package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Course;
import com.example.mapper.CourseMapper;
import com.example.mapper.UserMapper;
import com.example.service.CourseService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public java.util.List<Course> findAll() {
        return courseMapper.selectList(null);
    }

    @Override
    /**
     * 返回类型：MyBatis-Plus 提供的分页对象
     * 泛型：<Course> 表示分页结果包含 Course 对象
     */
    public Page<Course> findPage(int pageNum, int pageSize) {
        return courseMapper.selectPage(new Page<>(pageNum, pageSize), null);
        /**
         * 方法：MyBatis-Plus 提供的分页查询方法
         * 参数1：分页对象（告诉要查第几页，每页几条）
         * 参数2：查询条件（null 表示查所有记录）
         *
         *如果调用：findPage(2, 10)
         * 意思是：查询第2页，每页10条记录
         * 结果：返回第11-20条课程数据
         */
    }

    @Override
    public Course findById(Integer id) {
        return courseMapper.selectById(id);
    }

    @Override
    public void save(Course course) {
        // 检查课程编号是否重复
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("course_no", course.getCourseNo());
        if (courseMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("课程编号已存在");
        }
        courseMapper.insert(course);
    }

    @Override
    public void update(Course course) {
        courseMapper.updateById(course);
    }

    @Override
    public void delete(Integer id) {
        courseMapper.deleteById(id);
    }
}
