<template>
  <view class="container">
    <view v-if="!loaded" class="section">
      <view class="skeleton skeleton-line"></view>
      <view class="skeleton skeleton-line short"></view>
      <view class="skeleton skeleton-line"></view>
    </view>

    <template v-else>
      <view v-for="c1 in tree" :key="c1.id" class="section">
        <view class="title tap" @click="goRanking(c1)">{{ c1.name }} ›</view>
        <view v-if="c1.children && c1.children.length" class="sub">
          <text
            v-for="c2 in c1.children"
            :key="c2.id"
            class="tag tap cate-tag"
            @click="goRanking(c2)"
          >{{ c2.name }}</text>
        </view>
      </view>
      <view v-if="!tree.length" class="empty">
        <view class="empty-icon"></view>暂无品类
      </view>
    </template>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getCategoryTree } from '../../api/index'

const tree = ref([])
const loaded = ref(false)

const load = async () => {
  try { tree.value = await getCategoryTree() } catch (e) {}
  loaded.value = true
}
const goRanking = (c) => uni.navigateTo({ url: `/pages/brand/ranking?categoryId=${c.id}` })

onShow(load)
</script>

<style scoped>
.sub { display: flex; flex-wrap: wrap; }
.cate-tag { font-size: 26rpx; }
</style>
