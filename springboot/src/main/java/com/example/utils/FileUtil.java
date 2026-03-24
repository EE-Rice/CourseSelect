package com.example.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * ============================================
 * 文件工具类
 *
 * 作用：处理所有文件相关的操作
 * 1. 保存文件到磁盘（头像、普通文件）
 * 2. 生成唯一文件名（防止覆盖）
 * 3. 创建存储目录
 * 4. 删除文件
 * 5. 文件合法性校验（大小、类型）
 *
 * 使用方式：
 * - 自动注入Spring配置（@Component + @Value）
 * - 提供静态方法供Controller直接调用
 * ============================================
 */
@Component // Spring注解：把这个类交给Spring管理，成为Bean
public class FileUtil {

    // ============================================
    // 实例变量：用于接收Spring注入的配置值
    // ============================================

    @Value("${upload.path}")           // 从application.yml读取upload.path
    private String uploadPath;          // 实例变量，接收注入值

    @Value("${upload.avatar-path}")     // 读取头像路径配置
    private String avatarPath;

    @Value("${upload.file-path}")       // 读取文件路径配置
    private String filePath;

    @Value("${upload.allowed-types}")   // 读取允许的文件类型
    private String allowedTypes;

    @Value("${upload.max-size}")        // 读取最大文件大小
    private long maxSize;

    // ============================================
    // 静态变量：供静态方法使用
    //
    // 为什么需要两套？
    // - 实例变量：Spring注入用（非静态）
    // - 静态变量：静态方法用（方便直接调用 FileUtil.xxx()）
    // ============================================
    private static String STATIC_UPLOAD_PATH;      // 静态：根路径
    private static String STATIC_AVATAR_PATH;      // 静态：头像路径
    private static String STATIC_FILE_PATH;          // 静态：文件路径
    private static List<String> STATIC_ALLOWED_TYPES; // 静态：允许的类型列表
    private static long STATIC_MAX_SIZE;             // 静态：最大大小

    /**
     * ============================================
     * @PostConstruct 初始化方法
     *
     * 执行时机：Spring创建对象 → 注入配置值 → 执行此方法
     * 作用：
     * 1. 把实例变量值复制给静态变量（让静态方法能用）
     * 2. 创建必要的存储目录（如果不存在）
     * ============================================
     */
    @PostConstruct
    public void init() {
        // 复制值到静态变量，静态方法才能访问配置
        STATIC_UPLOAD_PATH = uploadPath;
        STATIC_AVATAR_PATH = avatarPath;
        STATIC_FILE_PATH = filePath;

        // 把逗号分隔的字符串转成List，方便后续校验
        // "image/jpeg,image/png" → ["image/jpeg", "image/png"]
        STATIC_ALLOWED_TYPES = Arrays.asList(allowedTypes.split(","));

        STATIC_MAX_SIZE = maxSize;

        // 启动时自动创建目录，避免运行时报错
        createDirectory(STATIC_UPLOAD_PATH);    // 创建根目录
        createDirectory(STATIC_AVATAR_PATH);    // 创建头像目录
        createDirectory(STATIC_FILE_PATH);      // 创建文件目录
    }

    /**
     * ============================================
     * 保存用户头像
     *
     * 调用场景：用户在个人中心上传头像
     * 存储策略：按用户ID隔离，每个用户有自己的子目录
     *
     * @param file   前端上传的文件对象（Spring自动封装）
     * @param userId 用户ID，用于创建专属目录
     * @return       访问路径（如 /uploads/avatar/1/xxx.jpg）
     * @throws IOException 文件读写异常
     * ============================================
     */
    public static String saveAvatar(MultipartFile file, Integer userId) throws IOException {
        // 步骤1：检查文件是否合法（大小、类型）
        validateFile(file);

        // 步骤2：获取原始文件名，用于生成新文件名
        String originalName = file.getOriginalFilename();

        // 步骤3：生成唯一文件名，防止不同用户上传同名文件导致覆盖
        // UUID.randomUUID() 生成类似：a1b2c3d4-e5f6-7890-abcd-ef1234567890
        // replace("-", "") 去掉横线，更简洁
        // 最终文件名：a1b2c3d4e5f67890abcdef1234567890_avatar.jpg
        String newName = UUID.randomUUID().toString().replace("-", "") + "_" + originalName;

        // 步骤4：构建用户专属目录路径
        // File.separator 根据操作系统自动选择 / 或 \
        // Windows: uploads\avatar\1
        // Linux/Mac: uploads/avatar/1
        String userDir = STATIC_AVATAR_PATH + File.separator + userId;

        // 步骤5：确保目录存在（不存在则创建）
        createDirectory(userDir);

        // 步骤6：构建完整的目标路径
        // userDir = ./uploads/avatar/1
        // newName = xxx_avatar.jpg
        // targetPath = ./uploads/avatar/1/xxx_avatar.jpg
        Path targetPath = Paths.get(userDir, newName);

        // 步骤7：将上传的文件流复制到目标路径（真正保存到磁盘）
        // file.getInputStream() 获取上传文件的输入流
        // Files.copy() 是Java NIO的高效文件复制方法
        Files.copy(file.getInputStream(), targetPath);

        // 步骤8：返回访问路径（前端用这个路径显示图片）
        // 注意：这里是URL路径，用正斜杠 /，不是文件路径
        // 前端访问：http://localhost:8080/uploads/avatar/1/xxx_avatar.jpg
        return "/uploads/avatar/" + userId + "/" + newName;
    }

    /**
     * ============================================
     * 保存普通文件（课程资料、文档等）
     *
     * 调用场景：管理员上传课程资料
     * 存储策略：按日期分目录，方便管理和清理
     *
     * @param file 前端上传的文件对象
     * @return     访问路径（如 /uploads/files/2024-03/xxx.pdf）
     * @throws IOException 文件读写异常
     * ============================================
     */
    public static String saveFile(MultipartFile file) throws IOException {
        // 步骤1：校验文件合法性
        validateFile(file);

        // 步骤2：获取原始文件名
        String originalName = file.getOriginalFilename();

        // 步骤3：生成唯一文件名
        String newName = UUID.randomUUID().toString().replace("-", "") + "_" + originalName;

        // 步骤4：按当前日期创建子目录（如 2024-03）
        // LocalDate.now() 获取当前日期：2024-03-24
        // DateTimeFormatter 格式化：2024-03
        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // 完整目录：./uploads/files/2024-03
        String targetDir = STATIC_FILE_PATH + File.separator + dateDir;

        // 步骤5：创建目录
        createDirectory(targetDir);

        // 步骤6：保存文件
        Path targetPath = Paths.get(targetDir, newName);
        Files.copy(file.getInputStream(), targetPath);

        // 步骤7：返回访问路径
        return "/uploads/files/" + dateDir + "/" + newName;
    }

    /**
     * ============================================
     * 删除文件（同时删除磁盘文件）
     *
     * 调用场景：用户删除头像、管理员删除课程资料
     *
     * @param filePath 文件的访问路径（数据库中存储的值）
     *                 如：/uploads/avatar/1/xxx.jpg
     * ============================================
     */
    public static void deleteFile(String filePath) {
        // 参数校验：空路径直接返回
        if (filePath == null || filePath.isEmpty()) {
            return;
        }

        // 将访问路径转换为实际磁盘路径
        // filePath: /uploads/avatar/1/xxx.jpg
        // STATIC_UPLOAD_PATH: ./uploads
        // File.separator: / 或 \
        // realPath: ./uploads/avatar/1/xxx.jpg
        String realPath = filePath.replace("/uploads/", STATIC_UPLOAD_PATH + File.separator);

        // 创建File对象，检查是否存在
        File file = new File(realPath);
        if (file.exists()) {
            // 存在则删除
            file.delete();
        }
        // 不存在则静默处理（可能已经被删了）
    }

    /**
     * ============================================
     * 获取文件的绝对路径（用于下载）
     *
     * @param relativePath 相对路径（数据库存储的值）
     *                     如：/uploads/files/2024-03/xxx.pdf
     * @return 绝对路径（磁盘完整路径）
     *         如：D:/project/uploads/files/2024-03/xxx.pdf
     * ============================================
     */
    public static String getAbsolutePath(String relativePath) {
        return relativePath.replace("/uploads/", STATIC_UPLOAD_PATH + File.separator);
    }

    /**
     * ============================================
     * 获取文件扩展名（小写）
     *
     * 示例：
     * - avatar.JPG → jpg
     * - document.PDF → pdf
     * - file → ""（无扩展名）
     *
     * @param filename 原始文件名
     * @return 扩展名（不含点号）
     * ============================================
     */
    private static String getExtension(String filename) {
        // 空值检查
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        // 截取最后一个点号后的内容，转小写
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * ============================================
     * 验证文件合法性（私有方法，内部使用）
     *
     * 检查项：
     * 1. 文件是否为空（用户没选文件就提交）
     * 2. 文件大小是否超过限制
     * 3. 文件类型是否在白名单中
     *
     * 不通过则抛出 RuntimeException，由Controller捕获返回错误信息
     *
     * @param file 待校验的文件
     * ============================================
     */
    private static void validateFile(MultipartFile file) {
        // 检查1：文件是否为空
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        // 检查2：文件大小
        // file.getSize() 返回字节数
        // STATIC_MAX_SIZE = 10MB = 10485760字节
        if (file.getSize() > STATIC_MAX_SIZE) {
            // 转换为MB显示，更友好
            long maxMB = STATIC_MAX_SIZE / 1024 / 1024;
            throw new RuntimeException("文件大小超过限制（最大" + maxMB + "MB）");
        }

        // 检查3：文件类型
        // file.getContentType() 获取MIME类型，如 image/jpeg
        String contentType = file.getContentType();
        if (!STATIC_ALLOWED_TYPES.contains(contentType)) {
            throw new RuntimeException("不支持的文件类型: " + contentType);
        }
    }

    /**
     * ============================================
     * 创建目录（如果不存在）
     *
     * mkdirs() 方法特点：
     * - 如果目录已存在，什么都不做
     * - 如果不存在，创建目录（包括所有不存在的父目录）
     * - 如：./uploads/avatar/1，如果avatar也不存在，会一起创建
     *
     * @param path 目录路径
     * ============================================
     */
    private static void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();  // 注意是mkdirs（带s），不是mkdir
        }
    }

}
