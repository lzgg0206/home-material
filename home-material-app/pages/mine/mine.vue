<template>
  <view class="container">
    <view class="section user">
      <view class="nickname">{{ nickname || '未登录' }}</view>
      <button v-if="!token" size="mini" type="primary" @click="goLogin">去登录</button>
      <button v-else size="mini" @click="logout">退出</button>
    </view>

    <view class="grid">
      <view class="grid-item" @click="go('/pages/profile/profile')">
        <view class="grid-name">家装档案</view>
        <view class="meta">编辑房屋/预算/偏好</view>
      </view>
      <view class="grid-item" @click="switchTab('/pages/list/list')">
        <view class="grid-name">选材清单</view>
        <view class="meta">管理已选型号</view>
      </view>
      <view class="grid-item" @click="go('/pages/search/search')">
        <view class="grid-name">搜索</view>
        <view class="meta">品牌/型号/避坑</view>
      </view>
      <view class="grid-item" @click="switchTab('/pages/index/index')">
        <view class="grid-name">回到首页</view>
        <view class="meta">榜单与避坑</view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'

const token = ref('')
const nickname = ref('')

const load = () => {
  token.value = uni.getStorageSync('token') || ''
  nickname.value = uni.getStorageSync('nickname') || ''
}
const goLogin = () => uni.navigateTo({ url: '/pages/login/login' })
const logout = () => {
  uni.removeStorageSync('token')
  uni.removeStorageSync('nickname')
  uni.removeStorageSync('userId')
  load()
  uni.showToast({ title: '已退出', icon: 'none' })
}
const go = (url) => uni.navigateTo({ url })
const switchTab = (url) => uni.switchTab({ url })

onShow(load)
</script>

<style scoped>
.user { display: flex; align-items: center; justify-content: space-between; }
.nickname { font-size: 34rpx; font-weight: 600; }
.grid { display: flex; flex-wrap: wrap; justify-content: space-between; }
.grid-item {
  width: 48%;
  background: #fff;
  border-radius: 16rpx;
  padding: 32rpx 24rpx;
  margin-bottom: 24rpx;
}
.grid-name { font-size: 30rpx; font-weight: 500; margin-bottom: 8rpx; }
</style>
