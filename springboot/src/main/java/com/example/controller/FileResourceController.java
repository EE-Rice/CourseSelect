package com.example.controller;

import com.example.common.Result;
import com.example.entity.FileResource;
import com.example.mapper.FileResourceMapper;
import com.example.service.FileResourceService;
import com.example.utils.FileUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ============================================
 * 文件资源控制器
 *
 * 作用：处理所有文件相关的HTTP请求
 * 包括：上传、下载、查询、删除文件
 *
 * 基础路径：/api/file
 * 完整URL示例：http://localhost:8080/api/file
 * ============================================
 */
@RestController
@RequestMapping("/api/file") // 标记为REST控制器，返回JSON数据
public class FileResourceController {

    @Autowired
    private FileResourceService fileResourceService;

    @GetMapping // 获取所有文件
    public Result<List<FileResource>> getAllFiles() {
        // 调用Service层查询所有文件
        List<FileResource> files = fileResourceService.findAll();

        // 使用Result.success包装成功结果
        return Result.success(files);
    }

    @GetMapping("/{id}") // 获取单个文件
    public Result<FileResource> getFile(@PathVariable Integer id) {
        FileResource file = fileResourceService.findById(id);
        return Result.success(file);
    }


    /**
     * ============================================
     * 上传文件（核心接口）
     *
     * 请求方式：POST
     * 请求URL：/api/file
     * Content-Type: multipart/form-data（表单文件上传）
     *
     * 请求参数：
     * - file: 二进制文件数据（必填）
     * - title: 文件标题（可选，默认使用原文件名）
     *
     * 完整流程：
     * 1. 接收前端传来的文件
     * 2. 用FileUtil保存到磁盘
     * 3. 将文件信息写入数据库
     * 4. 返回包含ID和路径的结果
     *
     * 前端调用示例（JavaScript）：
     * const formData = new FormData();
     * formData.append('file', fileInput.files[0]);
     * formData.append('title', '我的文档');
     * axios.post('/api/file', formData);
     * ============================================
     */
    @PostMapping // 上传文件（只有管理员能调用）
    public  Result<FileResource> updateFile(
            // @RequestParam 从请求参数中获取值
            @RequestParam("file") MultipartFile file,

            // required = false 表示可选参数，不提供也不会报错
            @RequestParam (value = "title",
            required = false) String title) {

        try {
            // 步骤1：保存文件到磁盘
            String filePath = FileUtil.saveFile(file); // 调用FileUtil工具类，返回访问路径

            // 步骤2:处理文件标题
            // 如果前端没传title，使用原文件名作为标题
            if (title == null || title.isEmpty()) {
                title = file.getOriginalFilename();
            }

            // 步骤3：构造数据库实体对象
            FileResource fileResource = new FileResource();
            fileResource.setTitle(title); // 显示标题
            fileResource.setFilePath(filePath); // 访问路径（存相对路径）
            fileResource.setFileType(file.getContentType()); // MIME类型
            fileResource.setUploadTime(LocalDateTime.now()); // 当前时间

            // 步骤4：写入数据库
            // insert操作后，MyBatis-Plus会自动将生成的主键ID设置回对象
            // 所以fileResource.getId() 可以获取到新插入的ID
            fileResourceService.save(fileResource);

            // 步骤5：返回成功结果
            // 包含完整的文件信息（包括ID和路径）
            return Result.success(fileResource);

        } catch (IOException e) {
            // IO异常：磁盘满了、权限不足、路径不存在等
            return Result.error("文件上传失败：" + e.getMessage());
        } catch (RuntimeException e) {
            // 业务异常：文件太大、类型不允许等（FileUtil中抛出）
            return Result.error(e.getMessage());
        }
    }


    /**
     * ============================================
     * 更新文件信息（只改标题，不换文件）
     *
     * 请求方式：PUT
     * 请求URL：/api/file/{id}
     * 请求体：{"title": "新标题"}
     *
     * 使用场景：管理员修改文件名称，但保留原文件
     * ============================================
     */
    @PutMapping("/{id}")
    public  Result<Void> updateFile(
            @PathVariable Integer id, // URL中的文件ID
            @RequestBody FileResource fileResource // 请求体JSON转对象
    ) {

        // 设置ID，确保更新的是指定记录，而不是新增
        // 如果不设置ID，MyBatis-Plus会认为是insert操作
        fileResource.setId(id);

        // 调用updateById更新（继承自BaseMapper的方法）
        fileResourceService.update(fileResource);

        // 返回成功，无数据（Void表示空）
        return Result.success(null);
    }


    /**
     * ============================================
     * 删除文件（同时删除磁盘文件和数据库记录）
     *
     * 请求方式：DELETE
     * 请求URL：/api/file/{id}
     * 示例：DELETE /api/file/1
     *
     * 重要：不仅删数据库记录，还要删磁盘文件，否则变成垃圾文件
     * ============================================
     */
    @DeleteMapping("/{id}") // DELETE请求用于删除资源
    public Result<Void> deleteFile(@PathVariable Integer id) {
        // 步骤1：查询文件信息（需要知道磁盘路径）
        FileResource fileResource = fileResourceService.findById(id);

        // 检查文件是否存在
        if (fileResource == null) {
            return Result.error("文件不存在");
        }

        // 步骤2:删除磁盘文件
        // 调用FileUtil，根据存储的路径删除实际文件
        FileUtil.deleteFile(fileResource.getFilePath());

        // 步骤3：删除数据库记录
        fileResourceService.delete(id);
        return Result.success(null);
    }


    /**
     * ============================================
     * 下载文件（核心接口）
     *
     * 请求方式：GET
     * 请求URL：/api/file/download/{id}
     * 示例：/api/file/download/1
     *
     * 响应：文件二进制流（浏览器自动触发下载）
     *
     * 注意：返回类型是void，因为数据直接写入响应流，不返回JSON
     * ============================================
     */
    @GetMapping("/download/{id}")
    public void downloadFile(
            @PathVariable Integer id,
            HttpServletResponse response) { // Spring注入的响应对象

        // 步骤1:查询文件信息
        FileResource fileResource = fileResourceService.findById(id);

        if (fileResource == null) {
            throw new RuntimeException("文件不存在");
        }

        // 步骤2:获取磁盘绝对路径

        // 数据库存的是相对路径：/uploads/files/2024-03/xxx.pdf
        // 需要转换成：D:/project/uploads/files/2024-03/xxx.pdf
        String absolutePath = FileUtil.getAbsolutePath(fileResource.getFilePath());
        File file = new File(absolutePath);

        // 检查磁盘文件是否存在（可能被手动删除了）
        if (!file.exists()) {
            throw new RuntimeException("文件已经丢失");
        }

        // 步骤3:设置HTTP响应头（告诉浏览器如何处理）

        // Content-Type：告诉浏览器这是什么类型的文件
        // 如 application/pdf，浏览器就知道用PDF阅读器打开
        response.setContentType(fileResource.getFileType());

        // Content-Disposition：控制浏览器行为
        // attachment = 作为附件下载（弹出保存对话框）
        // filename = 建议保存的文件名（使用encodeFilename处理中文）
        response.setHeader("Content-Disposition", "attachment; filename=" + encodeFilename(fileResource.getTitle()));

        // 步骤4:文件路复制到响应流
        // try-with-resources 语法：自动关闭流，防止内存泄漏
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {

            // 缓冲区：一次读1KB，平衡内存和速度
            byte[] buffer = new byte[1024];
            int len;

            // 循环读取，直到文件末尾（read返回-1）
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len); // 写入响应
            }

            // 刷新缓冲区，确保所有数据都发送给浏览器
            os.flush();

        } catch (IOException e) {
            throw new RuntimeException("文件下载失败：" + e.getMessage());
        }
    }

    /**
     * ============================================
     * 辅助方法：编码文件名（处理中文）--> private
     *
     * 问题：HTTP头中直接写中文会乱码
     * 解决：用URLEncoder编码成 %E4%B8%AD%E6%96%87 这种格式
     *
     * 示例："中文.pdf" → "%E4%B8%AD%E6%96%87.pdf"
     *
     * @param filename 原始文件名
     * @return 编码后的文件名
     * ============================================
     */
    private String encodeFilename(String filename) {
        try {
            return URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20"); // StandardCharsets.UTF_8.toString() 转成 "UTF-8"
        } catch (Exception e) {
            // 编码失败，返回原文件名（一般不会发生）
            return filename;
        }
    }
}
