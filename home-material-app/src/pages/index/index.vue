<template>
  <view class="container">
    <view class="section">
      <view class="title">快捷品类</view>
      <scroll-view scroll-x class="cate-list">
        <view
          v-for="c in home.quickCategories"
          :key="c.id"
          class="tag tap cate-pill"
          @click="goCategory(c.id)"
        >{{ c.name }}</view>
      </scroll-view>
    </view>

    <view v-for="r in home.topRankings" :key="r.categoryId" class="section">
      <view class="title">{{ r.categoryName }} TOP3</view>
      <view v-for="b in r.top3" :key="b.brandId" class="brand-card tap" @click="goCategory(r.categoryId)">
        <text :class="['rank', 'rank-' + b.rank]">{{ b.rank }}</text>
        <view class="brand-info">
          <text class="brand-name">{{ b.name }}</text>
          <view class="meta">好评 {{ b.praiseRate }}% · 踩坑 {{ b.pitfallCount }}</view>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="title">今日避坑精选</view>
      <view
        v-for="p in home.dailyPitfalls"
        :key="p.modelId"
        class="pit-card tap"
        @click="goModel(p.modelId)"
      >
        <view class="pit-tag">{{ p.categoryName }}</view>
        <view class="pit-desc">{{ p.description }}</view>
        <view class="meta">{{ p.modelName }} · {{ p.count }} 人反馈</view>
      </view>
      <view v-if="!home.dailyPitfalls || !home.dailyPitfalls.length" class="empty">
        <view class="empty-icon"></view>暂无避坑精选
      </view>
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
const goCategory = (id) => uni.navigateTo({ url: `/pages/brand/ranking?categoryId=${id}` })

onShow(load)
</script>

<style scoped>
.cate-list { white-space: nowrap; }
.cate-pill { display: inline-block; padding: 12rpx 28rpx; margin-right: 16rpx; font-size: 28rpx; }
.brand-card {
  display: flex;
  align-items: center;
  padding: 16rpx 0;
  border-bottom: 1rpx solid var(--color-border);
}
.brand-card:last-child { border-bottom: none; }
.brand-info { flex: 1; }
.brand-name { font-size: 30rpx; font-weight: 500; }
.rank {
  width: 48rpx;
  height: 48rpx;
  line-height: 48rpx;
  text-align: center;
  border-radius: 50%;
  margin-right: 20rpx;
  font-weight: 600;
  color: #fff;
}
.rank-1 { background: #faad14; }
.rank-2 { background: #bfbfbf; }
.rank-3 { background: #d48806; }
.pit-card { padding: 16rpx 0; border-bottom: 1rpx solid var(--color-border); }
.pit-card:last-child { border-bottom: none; }
.pit-tag {
  display: inline-block;
  font-size: 22rpx;
  color: var(--color-danger);
  background: var(--color-danger-bg);
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
  margin-bottom: 8rpx;
}
.pit-desc { font-size: 28rpx; line-height: 1.5; }
</style>
