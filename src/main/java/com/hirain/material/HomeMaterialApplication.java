package com.hirain.material;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 家装选材避坑助手 启动类。
 *
 * <p>单模块 Spring Boot 应用，C 端接口 {@code /api/**}、B 端接口 {@code /admin/**}。</p>
 *
 * @author lingzhi.Wang
 */
@SpringBootApplication
@MapperScan("com.hirain.material.mapper")
public class HomeMaterialApplication {

  /**
   * 应用入口方法，启动 Spring 容器。
   *
   * @param args 启动命令行参数
   */
  public static void main(String[] args) {
    SpringApplication.run(HomeMaterialApplication.class, args);
  }
}
