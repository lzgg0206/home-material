# home-material-app（uni-app 前端）

家装选材避坑助手小程序前端，基于 **uni-app + Vue3**，可编译微信小程序（`mp-weixin`）与 H5。

## 目录结构

```
home-material-app/
├── manifest.json        # uni-app 配置（mp-weixin.appid 在此填）
├── pages.json           # 页面注册 + tabBar
├── main.js              # Vue3 入口
├── App.vue              # 根组件 + 全局样式
├── api/
│   ├── request.js       # uni.request 封装（对齐后端 Result）
│   └── index.js         # 接口方法集合
└── pages/
    ├── index/index.vue  # 首页聚合
    ├── model/detail.vue # 型号详情
    ├── list/list.vue    # 自选清单 + 预算汇总
    ├── category/        # 品类（占位，P1 补）
    └── mine/            # 我的家装（占位，P1 补）
```

## 运行

### 方式一：HBuilderX（推荐）
1. HBuilderX 打开 `home-material-app` 目录
2. 顶部菜单「运行」→ 运行到小程序模拟器 → 微信开发者工具
3. 发行 → 小程序-微信，编译产物在 `unpackage/dist/build/mp-weixin`

### 方式二：CLI（Vite）
```bash
# 需先 npx degit dcloudio/uni-preset-vue#vite 迁移为 CLI 工程后再装依赖
npm install
npm run dev:mp-weixin    # 开发
npm run build:mp-weixin  # 打包
```

## 关键配置

- **mp-weixin AppID**：编辑 `manifest.json` → `mp-weixin.appid`，填注册的微信小程序 AppID（发布必需）。
- **后端基地址**：`api/request.js` 的 `BASE_URL`。
  - H5 开发：`/api`（走 `manifest.json` h5.devServer.proxy 到 `localhost:8090`）
  - 微信小程序生产：改已备案的 `https://api.yourdomain.com/api`（在小程序后台配 request 合法域名白名单）
- **登录**：`uni.login()` 拿 code → `wxLogin(code)`（后端 `wx.login.mock=false` 时走真实 `code2session`）。

## 对接的后端接口（C 端 `/api/**`）

首页聚合、品类树、品牌排行榜、型号详情、搜索、登录、家装档案、自选清单+预算。
