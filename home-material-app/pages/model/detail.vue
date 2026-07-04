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
        <view class="advice" v-if="p.advice">💡 避坑建议：{{ p.advice }}</view>
      </view>
    </view>
  </view>
  <view v-else class="empty">加载中...</view>
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
.tag {
  display: inline-block;
  font-size: 24rpx;
  padding: 4rpx 16rpx;
  margin-right: 12rpx;
  background: #f0faf3;
  color: #07c160;
  border-radius: 8rpx;
}
.kw-line { display: flex; justify-content: space-between; padding: 12rpx 0; border-bottom: 1rpx solid #f0f0f0; }
.kw-line:last-child { border-bottom: none; }
.kw { font-weight: 500; }
.pit { padding: 16rpx 0; border-bottom: 1rpx solid #f0f0f0; }
.pit:last-child { border-bottom: none; }
.risk-tag {
  font-size: 22rpx;
  color: #fff;
  background: #e64340;
  padding: 2rpx 12rpx;
  border-radius: 8rpx;
  margin-right: 8rpx;
}
.advice {
  margin-top: 8rpx;
  font-size: 26rpx;
  color: #07c160;
  background: #f0faf3;
  padding: 12rpx;
  border-radius: 8rpx;
}
</style>
