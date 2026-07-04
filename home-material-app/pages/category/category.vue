<template>
  <view class="container">
    <view v-for="c1 in tree" :key="c1.id" class="section">
      <view class="title" @click="goRanking(c1)">{{ c1.name }} ›</view>
      <view v-if="c1.children && c1.children.length" class="sub">
        <text
          v-for="c2 in c1.children"
          :key="c2.id"
          class="cate-tag"
          @click="goRanking(c2)"
        >{{ c2.name }}</text>
      </view>
    </view>
    <view v-if="!tree.length" class="empty">加载中...</view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getCategoryTree } from '../../api/index'

const tree = ref([])

const load = async () => {
  try { tree.value = await getCategoryTree() } catch (e) {}
}
const goRanking = (c) => uni.navigateTo({ url: `/pages/brand/ranking?categoryId=${c.id}` })

onShow(load)
</script>

<style scoped>
.sub { display: flex; flex-wrap: wrap; }
.cate-tag {
  font-size: 26rpx;
  padding: 8rpx 24rpx;
  background: #f0faf3;
  color: #07c160;
  border-radius: 32rpx;
  margin: 8rpx 16rpx 8rpx 0;
}
</style>
