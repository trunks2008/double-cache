package com.cn.dc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-11 11:22
 **/
@SpringBootApplication
@EnableCaching
public class DcV2Application {
    public static void main(String[] args) {
        SpringApplication.run(DcV2Application.class);
    }
}
