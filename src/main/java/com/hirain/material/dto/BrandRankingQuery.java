package com.hirain.material.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 品牌排行榜查询参数。
 *
 * @author lingzhi.Wang
 */
@Data
@Schema(description = "品牌排行榜查询")
public class BrandRankingQuery {

  @Schema(description = "品类ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
  private Long categoryId;

  @Schema(description = "榜单维度 overall/cost/highend/lowpitfall/eco", example = "overall")
  private String dimension = "overall";

  @Schema(description = "产地 domestic/imported，空=全部")
  private String origin;

  @Schema(description = "价格档 economic/quality/high，空=全部")
  private String priceRange;

  @Schema(description = "页码", example = "1")
  private Integer page = 1;

  @Schema(description = "每页条数", example = "20")
  private Integer size = 20;
}
