package com.example.common;

import lombok.Data;

import java.io.Serializable;
/**
 * 控制器层 (Controller)，处理前端HTTP请求的关键层。
 * 创建统一返回结果类/统一响应对象
 * 负责把数据包装成统一格式返回给前端
 */
@Data // Lombok 注解，自动生成 getter/setter/toString 等
public class Result<T> { // <T> 表示这是一个泛型类
    private int code;
    private String msg;
    /**
     * 泛型的作用：让这个类可以处理不同类型的数据
     * T 是类型占位符，可以是任何类型
     * 返回用户信息时，T 就是 User 类型；返回课程列表时，T 就是 List<Course> 类型
     */
    private T data;

    /**
     * 三个静态工厂方法
     * @param data
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.code = 200; // 状态码：200 成功，500 失败，404 找不到等
        result.msg = "success"; // 提示信息："success" 或错误原因
        result.data = data; // 实际返回的数据（泛型，可以是任何类型）
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<T>();
        result.code = 500; // 服务器内部错误
        result.msg = msg;  // 具体的错误信息
        // data 为 null（不设置）
        return  result;
    }

    // error重载方法
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<T>();
        result.code = code; // 自定义状态码
        result.msg = msg; // 错误信息
        return  result;
    }
}
