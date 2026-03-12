package com.example.controller;

import com.example.common.Result;
import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @RestController = @Controller + @ResponseBody，不用在每个方法上加 @ResponseBody
 * 以后访问所有用户相关接口都是：http://localhost:8080/api/user/xxx
 */
@RestController // 告诉 Spring：这是一个控制器，所有方法返回的数据直接写进 HTTP 响应体
@RequestMapping("/api/user") // 基础路径：这个控制器的所有接口都以 /api/user 开头
public class UserController {

    @Autowired // 让 Spring 自动把 UserService 的对象注入进来
    private UserService userService; // 不用new

    /**
     *登陆接口
     * HTTP方法POST对应数据库INSERT
     */
    @PostMapping("/login") //接口路径： POST 请求，地址：/api/user/login
    public Result<Map<String, Object>> login(@RequestBody User user) { // @RequestBody 把前端传来的 JSON 转成 User 对象
        try {
            // 1. 调用service层验证用户名密码
            User loginUser = userService.login(user.getUsername(), user.getPassword());

            // 2. 准备返回的数据
            Map<String, Object> data = new HashMap<>();
            data.put("user", loginUser); // 用户信息
            data.put("token", "user_token_" + loginUser.getId()); // 登陆凭证

            // 3. 用写的Result类返回成功
            return Result.success(data);
        } catch (Exception e) {
            // 4. 错误处理
            return Result.error(e.getMessage());
        }
    }

    /**
     * 注册接口
     * POST对应INSERT
     */
    @PostMapping("/register") // POST 请求，地址：/api/user/register
    public Result<Void> register(@RequestBody User user) { // 方法返回null
        try {
            userService.register(user); // 调用service层注册
            return Result.success(null); // 注册成功，返回null数据
        } catch (RuntimeException e) {
            return Result.error(e.getMessage()); // 注册失败
        }
    }

    /**
     * 查询用户接口
     * @param id
     * @return
     * GET对应SELECT
     */
    @GetMapping("/{id}") // GET 请求，地址：/api/user/1
    public Result<User> getById(@PathVariable("id") Integer id) { // @PathVariable 从 URL 路径中获取参数
        User user = userService.findById(id);
        return Result.success(user); // 直接返回用户信息
    }

    /**
     * 更新用户接口
     * PUT对应UPDATE
     * @param user
     * @return
     */
    @PutMapping // PUT 请求，地址：/api/user
    public Result<Void> update(@RequestBody User user) {
        // PUT常用于更新完整资源
        userService.updateProfile(user);
        return Result.success(null);
    }
}
