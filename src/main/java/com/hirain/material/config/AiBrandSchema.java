package com.hirain.material.config;

/**
 * 大模型品牌榜单响应的 JSON Schema 契约（单一来源）。
 *
 * <p>本常量既是 {@code LlmClient} 走 {@code response_format: json_schema} 强约束时下发给模型的 schema，
 * 也是前端回显渲染的数据契约。字段一旦定义勿轻易改动，确需变更须前后端同步。</p>
 *
 * <p><b>响应结构（顶层）</b>：
 * <pre>
 * {
 *   "brands": [ BrandItem ]   // 按维度排序，10-15 个
 * }
 * </pre>
 *
 * <p><b>BrandItem 字段</b>：
 * <table border="1">
 * <tr><th>字段</th><th>类型</th><th>必填</th><th>说明</th></tr>
 * <tr><td>name</td><td>string</td><td>是</td><td>品牌名称</td></tr>
 * <tr><td>origin</td><td>enum</td><td>是</td><td>产地 domestic国产 / imported进口</td></tr>
 * <tr><td>tier</td><td>enum</td><td>是</td><td>定位 high高端 / mid中端 / entry入门</td></tr>
 * <tr><td>praiseRate</td><td>number</td><td>是</td><td>好评率 0-100</td></tr>
 * <tr><td>pitfallCount</td><td>integer</td><td>是</td><td>踩坑反馈条数</td></tr>
 * <tr><td>priceMin</td><td>number</td><td>是</td><td>均价区间下限</td></tr>
 * <tr><td>priceMax</td><td>number</td><td>是</td><td>均价区间上限</td></tr>
 * <tr><td>tags</td><td>string[]</td><td>是</td><td>2-4 个中文标签</td></tr>
 * <tr><td>highlight</td><td>string</td><td>是</td><td>一句小红书口碑亮点</td></tr>
 * </table>
 *
 * @author lingzhi.Wang
 */
public final class AiBrandSchema {

  private AiBrandSchema() {
  }

  /** 品牌榜单响应 JSON Schema（智谱 GLM json_schema 强约束用）。 */
  public static final String BRAND_RANKING = """
      {
        "type": "object",
        "properties": {
          "brands": {
            "type": "array",
            "description": "品牌口碑榜单，按所给维度排序，10-15 个品牌",
            "items": {
              "type": "object",
              "properties": {
                "name": {"type": "string", "description": "品牌名称"},
                "origin": {"type": "string", "enum": ["domestic", "imported"], "description": "产地 国产/进口"},
                "tier": {"type": "string", "enum": ["high", "mid", "entry"], "description": "定位 高端/中端/入门"},
                "praiseRate": {"type": "number", "description": "好评率 0-100"},
                "pitfallCount": {"type": "integer", "description": "踩坑反馈条数"},
                "priceMin": {"type": "number", "description": "均价区间下限"},
                "priceMax": {"type": "number", "description": "均价区间上限"},
                "tags": {"type": "array", "items": {"type": "string"}, "description": "2-4 个中文标签"},
                "highlight": {"type": "string", "description": "一句小红书口碑亮点"}
              },
              "required": ["name", "origin", "tier", "praiseRate", "pitfallCount", "priceMin", "priceMax", "tags", "highlight"],
              "additionalProperties": false
            }
          }
        },
        "required": ["brands"],
        "additionalProperties": false
      }
      """;
}
