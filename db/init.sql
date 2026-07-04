-- ============================================================
-- 家装选材避坑助手 home_material 建库建表脚本
-- 对应 PRD P0 功能 F001-F014
-- ============================================================

DROP DATABASE IF EXISTS `home_material`;
CREATE DATABASE `home_material` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `home_material`;

-- ---------- F001 四级品类树（1大类/2品类/3细分，第4级即型号） ----------
DROP TABLE IF EXISTS `hm_category`;
CREATE TABLE `hm_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父级ID，0为根',
  `level` TINYINT NOT NULL COMMENT '层级 1大类/2品类/3细分',
  `name` VARCHAR(64) NOT NULL COMMENT '品类名称',
  `code` VARCHAR(64) NOT NULL COMMENT '品类编码',
  `icon` VARCHAR(255) DEFAULT NULL COMMENT '图标',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0未删/1已删',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_parent` (`parent_id`),
  KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家装品类树';

-- ---------- F002 品牌库 ----------
DROP TABLE IF EXISTS `hm_brand`;
CREATE TABLE `hm_brand` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(128) NOT NULL COMMENT '品牌名称',
  `logo` VARCHAR(255) DEFAULT NULL COMMENT 'logo',
  `origin` VARCHAR(32) DEFAULT NULL COMMENT '产地 domestic国产/imported进口',
  `tier` VARCHAR(32) DEFAULT NULL COMMENT '定位 high高端/mid中端/entry入门',
  `main_category_ids` VARCHAR(255) DEFAULT NULL COMMENT '主营品类ID集合(逗号分隔)',
  `price_min` DECIMAL(10,2) DEFAULT NULL COMMENT '均价区间下限',
  `price_max` DECIMAL(10,2) DEFAULT NULL COMMENT '均价区间上限',
  `after_sales` VARCHAR(255) DEFAULT NULL COMMENT '售后政策',
  `official_channel` VARCHAR(255) DEFAULT NULL COMMENT '官方渠道',
  `praise_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '好评率%',
  `pitfall_count` INT DEFAULT 0 COMMENT '踩坑反馈条数',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_origin` (`origin`),
  KEY `idx_tier` (`tier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品牌基础信息库';

-- ---------- F008 型号 ----------
DROP TABLE IF EXISTS `hm_model`;
CREATE TABLE `hm_model` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `brand_id` BIGINT NOT NULL COMMENT '品牌ID',
  `category_id` BIGINT NOT NULL COMMENT '品类ID(三级或二级)',
  `name` VARCHAR(128) NOT NULL COMMENT '型号全称',
  `spec` VARCHAR(255) DEFAULT NULL COMMENT '规格参数',
  `price` DECIMAL(10,2) DEFAULT NULL COMMENT '参考价',
  `eco_level` VARCHAR(32) DEFAULT NULL COMMENT '环保等级',
  `selling_points` VARCHAR(500) DEFAULT NULL COMMENT '核心卖点标签(逗号分隔)',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_brand` (`brand_id`),
  KEY `idx_category` (`category_id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='型号信息';

-- ---------- F005/F006 型号口碑聚合 ----------
DROP TABLE IF EXISTS `hm_model_reputation`;
CREATE TABLE `hm_model_reputation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `model_id` BIGINT NOT NULL COMMENT '型号ID',
  `praise_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '好评率%',
  `pitfall_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '踩坑率%',
  `sample_count` INT DEFAULT 0 COMMENT '有效样本数',
  `quality_score` DECIMAL(5,2) DEFAULT 0.00 COMMENT '质量评分',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_model` (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='型号口碑聚合';

-- ---------- 型号详情关键词云 ----------
DROP TABLE IF EXISTS `hm_model_keyword`;
CREATE TABLE `hm_model_keyword` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `model_id` BIGINT NOT NULL,
  `keyword` VARCHAR(64) NOT NULL COMMENT '关键词',
  `sentiment` TINYINT NOT NULL COMMENT '1正面/2负面',
  `mention_count` INT DEFAULT 0 COMMENT '提及频次',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_model` (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='型号口碑关键词';

-- ---------- F009 踩坑点 ----------
DROP TABLE IF EXISTS `hm_model_pitfall`;
CREATE TABLE `hm_model_pitfall` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `model_id` BIGINT NOT NULL,
  `type` VARCHAR(32) NOT NULL COMMENT '类型 quality/install/mismatch/experience',
  `description` VARCHAR(500) NOT NULL COMMENT '坑点描述',
  `count` INT DEFAULT 0 COMMENT '出现频次',
  `is_high_risk` TINYINT DEFAULT 0 COMMENT '是否高危 0/1',
  `advice` VARCHAR(500) DEFAULT NULL COMMENT '避坑建议',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_model_type` (`model_id`, `type`),
  KEY `idx_high_risk` (`is_high_risk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='型号踩坑点';

-- ---------- F007 品牌排行榜缓存(可选，首版动态算) ----------
DROP TABLE IF EXISTS `hm_brand_ranking`;
CREATE TABLE `hm_brand_ranking` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `category_id` BIGINT NOT NULL,
  `brand_id` BIGINT NOT NULL,
  `dimension` VARCHAR(32) NOT NULL COMMENT '维度 overall/cost/highend/lowpitfall/eco',
  `rank` INT NOT NULL COMMENT '名次',
  `score` DECIMAL(6,2) DEFAULT 0.00 COMMENT '得分',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_cat_dim` (`category_id`, `dimension`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品牌排行榜缓存';

-- ---------- F003 内容源(Mock) ----------
DROP TABLE IF EXISTS `hm_post`;
CREATE TABLE `hm_post` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `source_id` VARCHAR(64) DEFAULT NULL COMMENT '来源内容ID',
  `account` VARCHAR(128) DEFAULT NULL COMMENT '发布账号',
  `content` TEXT COMMENT '内容',
  `like_cnt` INT DEFAULT 0 COMMENT '点赞',
  `collect_cnt` INT DEFAULT 0 COMMENT '收藏',
  `comment_cnt` INT DEFAULT 0 COMMENT '评论',
  `is_ad` TINYINT DEFAULT 0 COMMENT '是否广告',
  `quality_score` DECIMAL(5,2) DEFAULT 0.00 COMMENT '质量分',
  `crawl_time` DATETIME DEFAULT NULL COMMENT '采集时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_source` (`source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小红书内容源(Mock)';

-- ---------- 用户 ----------
DROP TABLE IF EXISTS `hm_user`;
CREATE TABLE `hm_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `openid` VARCHAR(64) DEFAULT NULL COMMENT '微信openid',
  `nickname` VARCHAR(64) DEFAULT NULL,
  `phone` VARCHAR(20) DEFAULT NULL,
  `avatar` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';

-- ---------- F011 家装档案 ----------
DROP TABLE IF EXISTS `hm_user_profile`;
CREATE TABLE `hm_user_profile` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `city` VARCHAR(32) DEFAULT NULL COMMENT '城市',
  `area` DECIMAL(8,2) DEFAULT NULL COMMENT '面积㎡',
  `layout` VARCHAR(32) DEFAULT NULL COMMENT '户型',
  `house_type` VARCHAR(32) DEFAULT NULL COMMENT '房屋类型',
  `stage` VARCHAR(32) DEFAULT NULL COMMENT '装修阶段',
  `style` VARCHAR(32) DEFAULT NULL COMMENT '装修风格',
  `decorate_way` VARCHAR(32) DEFAULT NULL COMMENT '装修方式',
  `total_budget` DECIMAL(12,2) DEFAULT NULL COMMENT '总预算',
  `preference` VARCHAR(255) DEFAULT NULL COMMENT '核心偏好排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户家装档案';

-- ---------- F012 自选清单 ----------
DROP TABLE IF EXISTS `hm_selection_list`;
CREATE TABLE `hm_selection_list` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `name` VARCHAR(64) DEFAULT NULL COMMENT '清单名',
  `space` VARCHAR(32) DEFAULT NULL COMMENT '空间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自选清单';

-- ---------- F012 清单项 ----------
DROP TABLE IF EXISTS `hm_selection_item`;
CREATE TABLE `hm_selection_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `list_id` BIGINT NOT NULL,
  `model_id` BIGINT DEFAULT NULL,
  `category_id` BIGINT DEFAULT NULL,
  `space` VARCHAR(32) DEFAULT NULL COMMENT '空间',
  `spec` VARCHAR(255) DEFAULT NULL COMMENT '规格',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
  `unit_price` DECIMAL(10,2) DEFAULT 0.00 COMMENT '单价',
  `total_price` DECIMAL(12,2) DEFAULT 0.00 COMMENT '小计',
  `channel` VARCHAR(64) DEFAULT NULL COMMENT '购买渠道',
  `remark` VARCHAR(255) DEFAULT NULL,
  `purchase_status` VARCHAR(32) DEFAULT 'pending' COMMENT '待采购pending/已下单ordered/已收货received',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_list` (`list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='清单项';

-- ---------- F010 搜索历史 ----------
DROP TABLE IF EXISTS `hm_search_history`;
CREATE TABLE `hm_search_history` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT DEFAULT NULL,
  `keyword` VARCHAR(128) NOT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索历史';

-- ---------- F010 热搜词 ----------
DROP TABLE IF EXISTS `hm_hot_keyword`;
CREATE TABLE `hm_hot_keyword` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `keyword` VARCHAR(128) NOT NULL,
  `search_count` INT DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_keyword` (`keyword`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='热门搜索词';
