package com.cn.dc.biz.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @program: double-cache
 * @author: Hydra
 * @create: 2022-03-14 09:48
 **/
@Configuration
@MapperScan("com.cn.dc.biz.mapper")
public class MybatisConfig {
}
