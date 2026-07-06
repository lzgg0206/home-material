package com.hirain.material.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * AI 大模型配置（application.yml 的 ai.*）。
 *
 * <p>默认走硅基流动（SiliconFlow）OpenAI 兼容网关，{@code base-url} 一换即可切其他厂商。
 * 通过 {@code ai.mock} 开关在 Mock 与真实调用间切换，切换不改代码（同 {@link WxLoginProperties} 范式）。</p>
 *
 * @author lingzhi.Wang
 */
@Data
@Component
@ConfigurationProperties(prefix = "ai")
public class AiProperties {

  /** true=Mock 模式（不调大模型，返回占位 JSON），false=真实调用 */
  private boolean mock = true;

  /** OpenAI 兼容网关基地址（默认硅基流动） */
  private String baseUrl = "https://api.siliconflow.cn/v1";

  /** chat/completions 路径（相对 baseUrl） */
  private String chatPath = "/chat/completions";

  /** API Key（生产用环境变量 AI_API_KEY 注入） */
  private String apiKey;

  /** 模型名（按网关实际可用名填写，硅基流动上的智谱 GLM 系列） */
  private String model = "zhipuai/glm-4.5";

  /** 请求超时（毫秒），联网搜索较慢，默认 60s */
  private int timeout = 60000;

  /** 是否启用联网搜索（智谱 GLM web_search 工具） */
  private boolean webSearch = true;

  /** 采样温度 */
  private double temperature = 0.7;

  /** 默认城市（未登录或档案无城市时兜底） */
  private String defaultCity = "全国";

  /** 定时预热覆盖的品类ID集合 */
  private List<Long> preloadCategoryIds = List.of(10L, 13L, 16L);
}
