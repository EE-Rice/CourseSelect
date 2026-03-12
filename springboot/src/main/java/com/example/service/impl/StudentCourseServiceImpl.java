package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Course;
import com.example.entity.StudentCourse;
import com.example.mapper.CourseMapper;
import com.example.mapper.StudentCourseMapper;
import com.example.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    @Autowired
    private StudentCourseMapper studentCourseMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<StudentCourse> findByStudentId(Integer studentId) {
        QueryWrapper<StudentCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId).eq("status", 1); // 只查询正常选课
        return studentCourseMapper.selectList(wrapper);
    }

    @Override
    public List<StudentCourse> findByCourseId(Integer courseId) {
        QueryWrapper<StudentCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId).eq("status", 1);
        return studentCourseMapper.selectList(wrapper);
    }

    @Override
    @Transactional // 事务注解，保证数据一致性
    public void selectCourse(Integer studentId, Integer courseId) {
        // 检查是否已选
        QueryWrapper<StudentCourse> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("student_id", studentId)
                .eq("course_id", courseId)
                .eq("status", 1);
        if (studentCourseMapper.selectCount(checkWrapper) > 0) {
            throw new RuntimeException("您已选过该课程");
        }

        // 检查课程容量
        Course course = courseMapper.selectById(courseId);
        if (course.getSelectedCount() >= course.getCapacity()) {
            throw new RuntimeException("课程已满，无法选择");
        }

        // 插入选课记录
        StudentCourse enrollment = new StudentCourse();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setSelectTime(LocalDateTime.now());
        enrollment.setStatus(1);

        studentCourseMapper.insert(enrollment);
        // 课程人数更新由数据库触发器自动完成
    }

    @Override
    @Transactional
    public void cancelCourse(Integer studentId, Integer courseId) {
        QueryWrapper<StudentCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId)
                .eq("course_id", courseId)
                .eq("status", 1);

        StudentCourse enrollment = studentCourseMapper.selectOne(wrapper);
        if (enrollment != null) {
            enrollment.setStatus(2); // 设置为退课状态
            studentCourseMapper.updateById(enrollment);
        }
    }
}
