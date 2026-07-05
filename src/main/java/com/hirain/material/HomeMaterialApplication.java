package com.hirain.material;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 家装选材避坑助手 启动类。
 *
 * <p>单模块 Spring Boot 应用，C 端接口 {@code /api/**}、B 端接口 {@code /admin/**}。
 * 启用本地缓存（{@link EnableCaching}）与定时任务（{@link EnableScheduling}）。</p>
 *
 * @author lingzhi.Wang
 */
@SpringBootApplication
@MapperScan("com.hirain.material.mapper")
@EnableCaching
@EnableScheduling
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
