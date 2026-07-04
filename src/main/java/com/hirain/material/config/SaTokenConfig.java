package com.hirain.material.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 权限拦截配置。
 *
 * <p>浏览类接口（首页/品类/品牌/型号/搜索/登录）游客可访问；
 * 个人域（档案/清单/预算）需登录。B 端 {@code /admin/**} 默认全部需登录。</p>
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new SaInterceptor(handler -> {
      // C 端：除白名单外需登录
      SaRouter.match("/api/**")
          .notMatch(
              "/api/home/**",
              "/api/category/**",
              "/api/brand/**",
              "/api/model/**",
              "/api/search/**",
              "/api/auth/**"
          ).check(r -> StpUtil.checkLogin());
      // B 端：全部需登录
      SaRouter.match("/admin/**").check(r -> StpUtil.checkLogin());
    })).addPathPatterns("/**")
        .excludePathPatterns(
            "/doc.html", "/swagger-ui.html", "/v3/api-docs/**",
            "/swagger-resources/**", "/webjars/**", "/favicon.ico",
            "/error"
        );
  }
}
