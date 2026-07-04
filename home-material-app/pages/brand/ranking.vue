<template>
  <view class="container">
    <view class="section filter">
      <picker :range="dimensions" range-key="name" @change="onDim">
        <view>榜单维度：<text class="green">{{ curDim.name }}</text> ▾</view>
      </picker>
    </view>

    <view v-for="b in list" :key="b.brandId" class="section brand-card">
      <text class="rank">{{ b.rank }}</text>
      <view class="info">
        <view class="brand-name">{{ b.name }} <text class="meta">· {{ (b.tags || []).join(' / ') }}</text></view>
        <view class="meta">好评 {{ b.praiseRate }}% · ¥{{ b.priceMin }}-{{ b.priceMax }} · 踩坑 {{ b.pitfallCount }}</view>
      </view>
    </view>
    <view v-if="!list.length" class="empty">暂无榜单数据（从首页品类入口进入）</view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getBrandRanking } from '../../api/index'

const dimensions = [
  { key: 'overall', name: '综合口碑' },
  { key: 'cost', name: '性价比' },
  { key: 'highend', name: '高端品质' },
  { key: 'lowpitfall', name: '低踩坑率' },
  { key: 'eco', name: '环保等级' }
]
const curDim = ref(dimensions[0])
const list = ref([])
const categoryId = ref(null)

const load = async () => {
  if (!categoryId.value) return
  try {
    list.value = await getBrandRanking({ categoryId: categoryId.value, dimension: curDim.value.key })
  } catch (e) {}
}
const onDim = (e) => { curDim.value = dimensions[e.detail.value]; load() }

onLoad((q) => { categoryId.value = (q && q.categoryId) || null; load() })
</script>

<style scoped>
.filter { display: flex; align-items: center; }
.brand-card { display: flex; align-items: center; }
.rank {
  width: 56rpx; height: 56rpx; line-height: 56rpx; text-align: center;
  background: #ffeb3b; border-radius: 50%; margin-right: 20rpx; font-weight: 600;
}
.info { flex: 1; }
.brand-name { font-size: 30rpx; font-weight: 500; }
</style>
