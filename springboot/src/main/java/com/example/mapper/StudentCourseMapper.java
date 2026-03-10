package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.StudentCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentCourseMapper extends BaseMapper<StudentCourse> {

    // 检查学生是否已选该课程
    @Select("SELECT COUNT(*) FROM student_course WHERE student_id = #{student_id} AND course_id = #{courseId} AND status = 1")
    int countByStudentIdAndCourseId(@Param("studentId") Integer student_id, @Param("courseId") Integer course_id);
}
