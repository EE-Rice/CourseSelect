package com.example.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 同源策略：浏览器有个安全机制：默认情况下，网页只能请求同源（相同协议、域名、端口）的数据。
 * 跨域问题：前端网页在 8081 端口，想请求 8080 端口的数据 → 违反同源策略，浏览器会拦截
 */
@Configuration // 告诉 Spring：这是一个配置类，启动时要加载
public class CorsConfig implements WebMvcConfigurer {
    // 实现 WebMvcConfigurer 接口，可以自定义 Spring MVC 的配置

    @Override
    public void addCorsMappings(CorsRegistry registry) { // addCorsMappings 是 Spring 提供的专门配置跨域的方法。
        registry.addMapping("/**") // 1. 匹配哪些路径：/** 是通配符，匹配所有路径
                .allowedOriginPatterns("*") // 2. 允许哪些来源：* 表示允许所有来源的请求
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 3. 允许哪些请求方法：获取提交更新删除数据，预检请求
                .allowCredentials(true) // 4. 是否允许携带凭证：是否允许携带凭证（Cookie、HTTP 认证等）
                .maxAge(3600); // 5. 预检请求的有效期，单位秒：第一次请求时会发送 OPTIONS 预检请求，1 小时内不会再发，提高性能
    }

}
