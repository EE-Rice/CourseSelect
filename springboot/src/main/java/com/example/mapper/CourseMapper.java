package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    // 手动更新已选人数（配合数据库触发器使用）
    @Update("UPDATE course SET selected_count = selected_count + 1 WHERE id = #{courseId}")
    void incrementSelectedCount(Integer courseId);

    @Update("UPDATE course SET selected_count = selected_count - 1 WHERE id = #{courseId}")
    void decrementSelectedCount(Integer courseId);
}
