package com.hirain.material.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hirain.material.common.BizException;
import com.hirain.material.config.WxLoginProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信 code2session 封装。
 *
 * <p>通过 {@code wx.login.mock} 开关在 Mock 与真实调用间切换，切换不改代码。</p>
 */
@Slf4j
@Service
public class WxLoginService {

  private static final String CODE2SESSION_URL =
      "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}"
          + "&js_code={code}&grant_type=authorization_code";

  @Autowired
  private WxLoginProperties props;

  /**
   * 用 code 换 openid。
   *
   * @param code 小程序 wx.login() 拿到的 code
   * @return openid
   */
  public String code2session(String code) {
    if (StrUtil.isBlank(code)) {
      throw new BizException("code 不能为空");
    }
    if (props.isMock()) {
      log.warn("[Mock登录] code={} 直接换 openid（生产关闭 wx.login.mock）", code);
      return "mock_" + code;
    }
    String url = CODE2SESSION_URL
        .replace("{appid}", props.getAppid())
        .replace("{secret}", props.getSecret())
        .replace("{code}", code);
    String resp = HttpUtil.get(url, 5000);
    JSONObject json = JSONUtil.parseObj(resp);
    Integer errcode = json.getInt("errcode");
    if (errcode != null && errcode != 0) {
      throw new BizException("微信登录失败: " + json.getStr("errmsg"));
    }
    return json.getStr("openid");
  }
}
