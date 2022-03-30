package com.cn.dc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

import java.util.Scanner;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-11 11:22
 **/
@EnableCaching
@SpringBootApplication
public class DcV4Application {
    public static void main(String[] args) {
        SpringApplication.run(DcV4Application.class);

//        -Dserver.port=9500
//        System.out.println("请输入启动端口：");
//        Scanner scanner = new Scanner(System.in);
//        String port = scanner.nextLine();
//        new SpringApplicationBuilder(DcV4Application.class)
//                .properties("server.port=" + port).run(args);
    }


}
