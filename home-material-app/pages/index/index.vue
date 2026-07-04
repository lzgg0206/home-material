<template>
  <view class="container">
    <view class="section">
      <view class="title">快捷品类</view>
      <scroll-view scroll-x class="cate-list">
        <view
          v-for="c in home.quickCategories"
          :key="c.id"
          class="cate-item"
          @click="goCategory(c.id)"
        >{{ c.name }}</view>
      </scroll-view>
    </view>

    <view v-for="r in home.topRankings" :key="r.categoryId" class="section">
      <view class="title">{{ r.categoryName }} TOP3</view>
      <view v-for="b in r.top3" :key="b.brandId" class="brand-card">
        <text class="rank">{{ b.rank }}</text>
        <text class="brand-name">{{ b.name }}</text>
        <text class="meta">好评 {{ b.praiseRate }}% · 踩坑 {{ b.pitfallCount }}</text>
      </view>
    </view>

    <view class="section">
      <view class="title">今日避坑精选</view>
      <view
        v-for="p in home.dailyPitfalls"
        :key="p.modelId"
        class="pit-card"
        @click="goModel(p.modelId)"
      >
        <view class="pit-tag">{{ p.categoryName }}</view>
        <view class="pit-desc">{{ p.description }}</view>
        <view class="meta">{{ p.modelName }} · {{ p.count }} 人反馈</view>
      </view>
      <view v-if="!home.dailyPitfalls || !home.dailyPitfalls.length" class="empty">暂无内容</view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getHome } from '../../api/index'

const home = ref({ quickCategories: [], topRankings: [], dailyPitfalls: [] })

const load = async () => {
  try {
    home.value = await getHome()
  } catch (e) {
    // 错误已在 request 内 toast
  }
}

const goModel = (id) => uni.navigateTo({ url: `/pages/model/detail?id=${id}` })
const goCategory = (id) => uni.showToast({ title: '品类 ' + id, icon: 'none' })

onShow(load)
</script>

<style scoped>
.cate-list { white-space: nowrap; }
.cate-item {
  display: inline-block;
  padding: 12rpx 28rpx;
  margin-right: 16rpx;
  background: #f0faf3;
  color: #07c160;
  border-radius: 32rpx;
  font-size: 28rpx;
}
.brand-card {
  display: flex;
  align-items: center;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}
.brand-card:last-child { border-bottom: none; }
.rank {
  width: 48rpx;
  height: 48rpx;
  line-height: 48rpx;
  text-align: center;
  background: #ffeb3b;
  border-radius: 50%;
  margin-right: 20rpx;
  font-weight: 600;
}
.brand-name { flex: 1; }
.pit-card { padding: 16rpx 0; border-bottom: 1rpx solid #f0f0f0; }
.pit-card:last-child { border-bottom: none; }
.pit-tag {
  display: inline-block;
  font-size: 22rpx;
  color: #e64340;
  background: #fdecec;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
  margin-bottom: 8rpx;
}
.pit-desc { font-size: 28rpx; line-height: 1.5; }
</style>
