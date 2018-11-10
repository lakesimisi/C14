package com.conhj.server.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@AutoConfigureAfter(MyBatisConfig.class)
public class MyBatisMapperScannerConfig {
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mc=new MapperScannerConfigurer();
        mc.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mc.setBasePackage("com.conhj.server.mapper");
        return mc;
    }
}
