package com.cn.dc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-11 11:22
 **/
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class DcV3Application {
    public static void main(String[] args) {
        SpringApplication.run(DcV3Application.class);
    }
}
