package com.example.controller;

import com.example.common.Result;
import com.example.entity.FileResource;
import com.example.mapper.FileResourceMapper;
import com.example.service.FileResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/file")
public class FileResourceController {

    @Autowired
    private FileResourceService fileResourceService;

    @GetMapping // 获取所有文件
    public Result<List<FileResource>> getAllFiles() {
        List<FileResource> files = fileResourceService.findAll();
        return Result.success(files);
    }

    @GetMapping("/{id}") // 获取单个文件
    public Result<FileResource> getFile(@PathVariable Integer id) {
        FileResource file = fileResourceService.findById(id);
        return Result.success(file);
    }

    @PostMapping // 上传文件（只有管理员能调用）
    public  Result<Void> updateFile(@RequestParam("file") MultipartFile file,
                                    @RequestParam ("title") String title) {
        // 这里需要添加权限验证（在实际项目中）
        // 检查当前用户是否为管理员

        FileResource fileResource = new FileResource();
        fileResource.setTitle(title);
        fileResource.setFilePath("/uploads/" + file.getOriginalFilename());
        fileResource.setFileType(file.getContentType());

        fileResourceService.save(fileResource);
        return Result.success(null);
    }

    @PutMapping // 更新文件信息
    public  Result<Void> updateFile(@RequestBody FileResource fileResource) {
        fileResourceService.update(fileResource);
        return Result.success(null);
    }

    @DeleteMapping("/{id}") // 删除文件（只有管理员能调用）
    public Result<Void> deleteFile(@PathVariable Integer id) {
        fileResourceService.delete(id);
        return Result.success(null);
    }
}
