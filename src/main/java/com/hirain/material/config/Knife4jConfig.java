package com.hirain.material.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j / OpenAPI 文档配置。
 *
 * <p>访问入口：{@code http://localhost:8090/doc.html}</p>
 *
 * @author lingzhi.Wang
 */
@Configuration
public class Knife4jConfig {

  /**
   * 构建 OpenAPI 文档元信息。
   *
   * @return OpenAPI 实例
   */
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("家装选材避坑助手 API")
            .description("家装选材避坑助手小程序后端接口文档")
            .version("1.0.0")
            .contact(new Contact().name("home-material")));
  }
}
