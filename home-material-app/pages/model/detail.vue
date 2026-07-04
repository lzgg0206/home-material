<template>
  <view class="container" v-if="d.modelId">
    <view class="section base">
      <view class="d-name">{{ d.brandName }} {{ d.modelName }}</view>
      <view class="meta">参考价 ¥{{ d.price }} · 规格 {{ d.spec }} · 环保 {{ d.ecoLevel }}</view>
      <view class="repu">
        好评 <text class="green">{{ d.praiseRate }}%</text>
        / 踩坑 <text class="red">{{ d.pitfallRate }}%</text>
        / 样本 {{ d.sampleCount }}
      </view>
      <view class="tags" v-if="d.sellingPoints && d.sellingPoints.length">
        <text v-for="t in d.sellingPoints" :key="t" class="tag">{{ t }}</text>
      </view>
    </view>

    <view class="section" v-if="d.pros && d.pros.length">
      <view class="title">业主公认优点</view>
      <view v-for="k in d.pros" :key="k.keyword" class="kw-line">
        <text class="kw">{{ k.keyword }}</text>
        <text class="meta">{{ k.mentionCount }} 位业主提及</text>
      </view>
    </view>

    <view class="section" v-for="g in d.pitfalls" :key="g.type">
      <view class="title">{{ g.typeName }}</view>
      <view v-for="p in g.items" :key="p.id" class="pit">
        <view>
          <text v-if="p.isHighRisk" class="risk-tag">高危踩坑</text>
          {{ p.description }}
          <text class="meta">（{{ p.count }} 人反馈）</text>
        </view>
        <view class="advice" v-if="p.advice">
          <text class="advice-label">避坑建议</text>{{ p.advice }}
        </view>
      </view>
    </view>
  </view>

  <view v-else class="container">
    <view class="section">
      <view class="skeleton skeleton-line"></view>
      <view class="skeleton skeleton-line short"></view>
      <view class="skeleton skeleton-line"></view>
    </view>
    <view class="section">
      <view class="skeleton skeleton-line"></view>
      <view class="skeleton skeleton-line"></view>
      <view class="skeleton skeleton-line short"></view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getModelDetail } from '../../api/index'

const d = ref({})

onLoad((q) => {
  getModelDetail(q.id).then((r) => { d.value = r }).catch(() => {})
})
</script>

<style scoped>
.d-name { font-size: 36rpx; font-weight: 600; }
.repu { margin-top: 12rpx; font-size: 28rpx; }
.tags { margin-top: 16rpx; }
.kw-line { display: flex; justify-content: space-between; padding: 12rpx 0; border-bottom: 1rpx solid var(--color-border); }
.kw-line:last-child { border-bottom: none; }
.kw { font-weight: 500; }
.pit { padding: 16rpx 0; border-bottom: 1rpx solid var(--color-border); }
.pit:last-child { border-bottom: none; }
.risk-tag {
  font-size: 22rpx;
  color: #fff;
  background: var(--color-danger);
  padding: 2rpx 12rpx;
  border-radius: 8rpx;
  margin-right: 8rpx;
}
.advice {
  margin-top: 8rpx;
  font-size: 26rpx;
  color: var(--color-primary);
  background: var(--color-primary-bg);
  padding: 12rpx;
  border-radius: 8rpx;
  line-height: 1.5;
}
.advice-label {
  display: inline-block;
  font-size: 22rpx;
  color: var(--color-primary);
  background: #fff;
  padding: 2rpx 12rpx;
  border-radius: 8rpx;
  margin-right: 8rpx;
  font-weight: 500;
}
</style>
