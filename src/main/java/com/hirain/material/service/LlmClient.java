package com.hirain.material.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hirain.material.common.BizException;
import com.hirain.material.config.AiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大模型客户端：OpenAI 兼容（默认硅基流动）+ 智谱 GLM 联网搜索工具。
 *
 * <p>照抄 {@link WxLoginService} 的 Mock 开关范式，Hutool 一把梭，零新依赖。
 * 城市等上下文由调用方拼进 userPrompt，本类只负责收发与解析。</p>
 *
 * @author lingzhi.Wang
 */
@Slf4j
@Service
public class LlmClient {

  @Autowired
  private AiProperties props;

  /**
   * 调用大模型，返回 choices[0].message.content（原始字符串，通常为 JSON）。
   *
   * @param systemPrompt 系统提示
   * @param userPrompt   用户提示（应包含城市/品类等上下文）
   * @return 模型回复内容
   */
  public String chatJson(String systemPrompt, String userPrompt) {
    if (props.isMock()) {
      log.warn("[Mock大模型] 返回占位 JSON，生产关闭 ai.mock");
      return mockBrandJson();
    }
    Map<String, Object> body = buildRequestBody(systemPrompt, userPrompt);
    String resp;
    try {
      resp = HttpRequest.post(props.getBaseUrl() + props.getChatPath())
          .header("Authorization", "Bearer " + props.getApiKey())
          .header("Content-Type", "application/json")
          .body(JSONUtil.toJsonStr(body))
          .timeout(props.getTimeout())
          .execute()
          .body();
    } catch (Exception e) {
      log.error("调用大模型失败 model={}", props.getModel(), e);
      throw new BizException("AI 服务繁忙，请稍后重试");
    }
    return extractContent(resp);
  }

  /**
   * 构造 OpenAI 兼容请求体（JSON 模式 + 智谱 web_search 联网工具）。
   */
  private Map<String, Object> buildRequestBody(String systemPrompt, String userPrompt) {
    Map<String, Object> body = new HashMap<>();
    body.put("model", props.getModel());
    body.put("temperature", props.getTemperature());
    body.put("stream", false);
    body.put("response_format", Map.of("type", "json_object"));
    List<Map<String, String>> messages = new ArrayList<>();
    messages.add(Map.of("role", "system", "content", systemPrompt));
    messages.add(Map.of("role", "user", "content", userPrompt));
    body.put("messages", messages);
    if (props.isWebSearch()) {
      // 智谱 GLM web_search 工具格式，硅基流动透传；网关不支持时关闭 ai.web-search 即可
      body.put("tools", List.of(Map.of("type", "web_search",
          "web_search", Map.of("enable", true))));
    }
    return body;
  }

  /**
   * 从 OpenAI 兼容响应抽取 choices[0].message.content。
   */
  private String extractContent(String resp) {
    JSONObject json = JSONUtil.parseObj(resp);
    if (json.containsKey("error")) {
      Object err = json.get("error");
      throw new BizException("大模型返回错误: " + (err instanceof JSONObject ? ((JSONObject) err).getStr("message") : err));
    }
    JSONArray choices = json.getJSONArray("choices");
    if (choices == null || choices.isEmpty()) {
      throw new BizException("大模型返回为空");
    }
    return choices.getJSONObject(0).getJSONObject("message").getStr("content");
  }

  /**
   * Mock 占位 JSON，结构与真实返回一致（{brands:[...]}）。
   */
  private String mockBrandJson() {
    return """
        {"brands":[
          {"name":"东鹏","origin":"domestic","tier":"mid","praiseRate":93.5,"pitfallCount":8,"priceMin":80,"priceMax":300,"tags":["国产瓷砖","性价比","好评高"],"highlight":"小红书业主普遍认可耐磨度"},
          {"name":"马可波罗","origin":"domestic","tier":"mid","praiseRate":92.0,"pitfallCount":10,"priceMin":90,"priceMax":350,"tags":["国产瓷砖","花色多"],"highlight":"花色选择丰富是口碑主因"},
          {"name":"诺贝尔","origin":"domestic","tier":"high","praiseRate":94.0,"pitfallCount":5,"priceMin":150,"priceMax":500,"tags":["高端瓷砖","质感好"],"highlight":"质感与设计感受到高端业主推荐"}
        ]}""";
  }
}
