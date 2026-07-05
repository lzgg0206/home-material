# 家装选材避坑助手

> 帮装修业主「选材不踩坑」的一站式工具：品牌/型号口碑榜单、踩坑库、自选清单与预算管控。C 端为微信小程序（uni-app），B 端为后台管理（admin）。

## 技术栈

| 层 | 技术 |
| --- | --- |
| 后端 | Spring Boot 3.2.6、Java 21、MyBatis-Plus 3.5.7、Sa-Token 1.38.0（鉴权）、Knife4j 4.5.0（接口文档）、Hutool 5.8.27、POI 5.2.5（Excel 导出） |
| 数据库 | MySQL 8.0 |
| 前端 | uni-app + Vue 3（Composition API），可编译微信小程序与 H5 |
| 构建 | Maven 3.9+ |

## 目录结构

```
home-material/
├── pom.xml                          # Maven 配置
├── db/
│   ├── init.sql                     # 建表脚本（15 张表）
│   └── mock-data.sql                # 演示数据
├── src/main/
│   ├── java/com/hirain/material/
│   │   ├── controller/
│   │   │   ├── api/                 # C 端接口（/api/**）
│   │   │   └── admin/               # B 端管理接口（/admin/**）
│   │   ├── service/                 # 业务逻辑
│   │   ├── mapper/                  # 数据访问（MyBatis-Plus BaseMapper）
│   │   ├── entity/                  # 数据库实体（@Data + BaseEntity）
│   │   ├── vo/                      # 视图对象
│   │   ├── dto/                     # 请求对象
│   │   ├── enums/                   # 枚举（统一管理）
│   │   ├── common/                  # Result/BizException/全局异常处理
│   │   └── config/                  # MyBatis-Plus/Sa-Token/Knife4j/CORS 配置
│   └── resources/
│       ├── application.yml          # 主配置（端口 8090）
│       ├── application-dev.yml      # 开发环境（本地 MySQL）
│       ├── application-prod.yml     # 生产环境（环境变量注入）
│       └── mapper/                  # MyBatis XML（如有）
├── src/test/java/                   # 单元测试（JUnit 5 + Mockito）
└── home-material-app/               # uni-app 前端（详见其内部 README）
```

## 环境要求

- JDK **21**
- Maven **3.9+**
- MySQL **8.0**
- 前端：HBuilderX（最新版）或 微信开发者工具

## 快速开始

### 1. 初始化数据库

```sql
CREATE DATABASE home_material DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

依次执行：

```bash
mysql -uroot -p home_material < db/init.sql
mysql -uroot -p home_material < db/mock-data.sql
```

### 2. 启动后端

默认连接本地 MySQL（`localhost:3306`，用户名/密码 `root/root`）。若本机密码不同，修改 `src/main/resources/application-dev.yml`。

```bash
mvn spring-boot:run
# 或打包后运行
mvn clean package -DskipTests
java -jar target/home-material.jar
```

启动成功后服务监听 **http://localhost:8090**。

### 3. 启动前端

前端工程在 `home-material-app/`，详见 [`home-material-app/README.md`](home-material-app/README.md)。简要：

1. 用 HBuilderX 打开 `home-material-app` 目录
2. 「运行」→ 运行到小程序模拟器 → 微信开发者工具
3. H5 调试时，请求默认走 `/api` 前缀，需在 HBuilderX 的 `manifest.json` 或自定义 `vite.config.js` 里配置 devServer 把 `/api` 代理到 `http://localhost:8090`

## 配置说明

### Profile 切换

- `dev`（默认）：本地 MySQL，开启 SQL 日志
- `prod`：通过环境变量注入数据库连接

```bash
java -jar home-material.jar \
  --spring.profiles.active=prod \
  -DDB_URL=jdbc:mysql://生产地址:3306/home_material \
  -DDB_USERNAME=xxx -DDB_PASSWORD=xxx
```

### 微信登录

`application.yml` 中 `wx.login.mock=true` 为 Mock 模式：直接用 openid 换 token，便于本地联调。生产环境置 `false` 并填入真实 `appid` 与 `secret`（建议用环境变量）。

### 鉴权（Sa-Token）

- Token 字段：HTTP 头 `Authorization`
- 有效期：30 天
- `/api/**`：个人域（部分接口白名单，如首页、品类、搜索；清单/档案需登录）
- `/admin/**`：管理域，需登录且具备管理员角色（`hm_user.role=1`，普通用户访问返回 403）

> 管理员账号：mock-data 预置 `admin_openid_001`（role=1），本地可用它走 `/api/auth/login` 拿 token 后访问 B 端。

## 接口文档

启动后端后访问 Knife4j：

```
http://localhost:8090/doc.html
```

分组「家装选材避坑助手 API」聚合了 `/api/**` 与 `/admin/**` 全部接口。

## 运行测试

测试为纯逻辑单元测试（JUnit 5 + Mockito），不依赖数据库与 Spring 上下文：

```bash
mvn test
```

覆盖范围：统一响应 `Result`、预算计算与清单小计（`SelectionService`）、首页榜单与品类匹配（`HomeService`）、踩坑类型映射与分组（`ModelService`）、品牌标签（`BrandService`）、品类树构建（`CategoryService`）。

## 业务模块

| 模块 | 说明 |
| --- | --- |
| 首页聚合 | 快捷品类、核心品类 TOP3 榜单、今日避坑精选 |
| 品牌排行榜 | 按品类/维度/产地/价格档筛选 + 分页 |
| 型号详情 | 基础信息、口碑总览、关键词云、踩坑（分类+高危置顶）、相关推荐 |
| 搜索 | 品牌/型号/品类聚合 + 热门词 |
| 自选清单 | 清单与清单项 CRUD、预算统计（按品类/空间分布）、Excel 导出 |
| 家装档案 | 房屋信息、总预算、偏好（upsert） |
| 认证 | Mock 登录、微信小程序登录、当前用户 |
| 后台管理 | 品类/品牌/型号/踩坑/口碑的增删改查 |

## 编码规范摘要

- 依赖注入统一使用 `@Autowired` 字段注入
- 枚举统一放置于 `com.hirain.material.enums` 包
- 实体继承 `BaseEntity`（自带 id/createTime/updateTime/deleted 逻辑删除）
- 统一响应 `Result<T>`（code/message/data，成功 code=200）
- 业务异常抛 `BizException`，由全局异常处理器兜底
- 所有 public 方法需有 Javadoc

## 许可证

内部项目，未开放许可。
