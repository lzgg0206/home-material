package com.hirain.material.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序登录配置（application.yml 的 wx.login.*）。
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx.login")
public class WxLoginProperties {

  /** true=Mock 模式（不调微信），false=真实 code2session */
  private boolean mock = true;

  private String appid;

  private String secret;

  /** code2session 请求超时（毫秒） */
  private int timeout = 5000;
}
