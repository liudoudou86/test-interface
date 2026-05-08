package com.qa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author LiuZhen
 * @Date 2024/02/21
 * @Description 程序入口
 */

@SpringBootApplication
@EnableScheduling
public class App {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        // 设置懒加载
        app.setLazyInitialization(true);
        app.run(args);
    }
}
