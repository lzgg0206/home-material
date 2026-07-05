package com.hirain.material.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 配置：分页插件 + 审计字段自动填充。
 *
 * @author lingzhi.Wang
 */
@Configuration
public class MybatisPlusConfig {

  /**
   * 分页拦截器，单页上限 1000 防止误查全表。
   *
   * @return MyBatis-Plus 拦截器
   */
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    PaginationInnerInterceptor page = new PaginationInnerInterceptor();
    page.setMaxLimit(1000L);
    interceptor.addInnerInterceptor(page);
    return interceptor;
  }

  /**
   * 审计字段自动填充处理器，insertFill 填充 createTime/updateTime，updateFill 填充 updateTime。
   *
   * @return MetaObjectHandler 实例
   */
  @Bean
  public MetaObjectHandler metaObjectHandler() {
    return new MetaObjectHandler() {
      /**
       * 插入时填充 createTime 与 updateTime。
       *
       * @param metaObject MyBatis 元对象
       */
      @Override
      public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
      }

      /**
       * 更新时填充 updateTime。
       *
       * @param metaObject MyBatis 元对象
       */
      @Override
      public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
      }
    };
  }
}
