package com.example.common;

import org.springframework.beans.factory.annotation.Value;  // 读取配置
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * ============================================
 * Web MVC 配置类
 *
 * 作用：自定义Spring MVC的行为
 * 当前功能：将URL路径映射到本地文件系统（让上传的文件可直接访问）
 *
 * 为什么需要这个？
 * - Spring Boot默认只暴露classpath下的静态资源（如templates、static目录）
 * - 我们上传的文件存在项目根目录的uploads文件夹，Spring不认识
 * - 需要手动配置映射规则
 * ============================================
 */
@Configuration // 静态资源映射，标记为配置类，Spring启动时会加载
public class WebMvcConfig implements WebMvcConfigurer {
    // 实现WebMvcConfigurer接口，可以自定义各种MVC配置

    /**
     * @Value 从application.yml读取配置值
     * 这里读取文件存储的根路径
     */
    @Value("${upload.path}")
    private String uploadPath;

    /**
     * ============================================
     * 配置静态资源处理器
     *
     * 核心作用：
     * 当浏览器访问 http://localhost:8080/uploads/xxx.jpg 时
     * Spring知道去哪个磁盘目录找文件
     *
     * 参数说明：
     * - ResourceHandlerRegistry：资源处理器注册器
     * ============================================
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // ========================================
        // 将相对路径转换为绝对路径
        // ========================================
        // 相对路径：./uploads
        // 绝对路径：D:/Users/xxx/IdeaProjects/course-system/uploads（Windows）
        //          /home/user/course-system/uploads（Linux）
        File uploadDir = new File(uploadPath);
        String absolutePath = uploadDir.getAbsolutePath();

        // 确保路径以分隔符结尾，并且加上file:协议前缀
        // file:D:/Users/.../uploads/
        String resourceLocation = "file:" + absolutePath + File.separator;

        // 注册资源映射规则
        registry.
                // addResourceHandler：定义URL匹配规则

                // /uploads/** 匹配所有以 /uploads/ 开头的请求
                // ** 表示任意子路径，如 /uploads/avatar/1/xxx.jpg
                addResourceHandler("/uploads/**")
                // addResourceLocations：映射到的磁盘位置
                // 当匹配到 /uploads/xxx 时，去 file:xxx/uploads/ 目录找
                .addResourceLocations(resourceLocation);

        // 示例映射关系：
        // URL: http://localhost:8080/uploads/avatar/1/xxx.jpg
        // 磁盘: D:/.../uploads/avatar/1/xxx.jpg
    }
}
