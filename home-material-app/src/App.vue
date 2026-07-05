<script>
export default {
  onLaunch() {
    console.log('home-material App Launch')
  },
  onShow() {},
  onHide() {}
}
</script>

<style>
/* ============ 设计令牌（收敛散落在各页的硬编码配色） ============ */
page {
  --color-primary: #07c160;
  --color-primary-bg: #f0faf3;
  --color-danger: #e64340;
  --color-danger-bg: #fdecec;
  --color-warn: #faad14;
  --color-text: #1a1a1a;
  --color-text-secondary: #666666;
  --color-text-placeholder: #999999;
  --color-bg: #f6f6f6;
  --color-card: #ffffff;
  --color-border: #f0f0f0;
  --radius-card: 16rpx;
  --radius-tag: 32rpx;
  background: var(--color-bg);
  color: var(--color-text);
  font-size: 28rpx;
}

/* ============ 通用容器与卡片 ============ */
.container { padding: 24rpx; }
.section {
  background: var(--color-card);
  border-radius: var(--radius-card);
  padding: 24rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.03);
}
.title { font-size: 32rpx; font-weight: 600; margin-bottom: 16rpx; color: var(--color-text); }
.meta { font-size: 26rpx; color: var(--color-text-secondary); margin-top: 8rpx; }
.green { color: var(--color-primary); }
.red { color: var(--color-danger); }

/* ============ 统一标签 pill（替代各页重复的 .tag/.cate-tag） ============ */
.tag {
  display: inline-block;
  font-size: 24rpx;
  padding: 6rpx 18rpx;
  background: var(--color-primary-bg);
  color: var(--color-primary);
  border-radius: var(--radius-tag);
  margin: 8rpx 16rpx 8rpx 0;
}

/* ============ 点击反馈（小程序里用 :active 替代 hover） ============ */
.tap { transition: opacity 0.15s ease; }
.tap:active { opacity: 0.65; }

/* ============ 骨架屏 ============ */
.skeleton {
  background: linear-gradient(90deg, #f0f0f0 25%, #fafafa 37%, #f0f0f0 63%);
  background-size: 400% 100%;
  animation: skeleton 1.4s ease infinite;
  border-radius: 8rpx;
}
.skeleton-line { height: 32rpx; margin-bottom: 16rpx; }
.skeleton-line.short { width: 40%; }

/* ============ 空状态 ============ */
.empty {
  text-align: center;
  color: var(--color-text-placeholder);
  padding: 80rpx 0;
  font-size: 28rpx;
}
.empty-icon {
  display: block;
  width: 120rpx;
  height: 120rpx;
  margin: 0 auto 24rpx;
  border-radius: 50%;
  background: var(--color-border);
}

@keyframes skeleton {
  0% { background-position: 100% 50%; }
  100% { background-position: 0 50%; }
}
@media (prefers-reduced-motion: reduce) {
  .skeleton { animation: none; }
  .tap { transition: none; }
}
</style>
