package com.hirain.material.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置（前端 H5 / 微信开发者工具联调用）。
 *
 * @author lingzhi.Wang
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

  /**
   * 注册全局 CORS 映射，允许任意来源/方法/凭据。
   *
   * @param registry CORS 注册器
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOriginPatterns("*")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
  }
}
