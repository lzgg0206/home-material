<template>
  <view class="container">
    <view class="section">
      <view class="title">登录（P0 Mock）</view>
      <input v-model="openid" class="input" placeholder="输入 openid 联调（生产用 uni.login 拿 code）" />
      <button class="btn" type="primary" :disabled="!openid" @click="doLogin">登录</button>
      <view class="meta">
        生产环境把按钮逻辑换成：
        uni.login → 拿 code → 调 /api/auth/wx-login → 后端 code2session 换 openid
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { loginByOpenid } from '../../api/index'

const openid = ref('')

const doLogin = async () => {
  try {
    const r = await loginByOpenid(openid.value)
    uni.setStorageSync('token', r.token)
    uni.setStorageSync('userId', r.userId)
    uni.setStorageSync('nickname', r.nickname)
    uni.showToast({ title: '登录成功', icon: 'success' })
    setTimeout(() => uni.navigateBack(), 800)
  } catch (e) {}
}
</script>

<style scoped>
.input {
  background: #f6f6f6;
  padding: 20rpx;
  border-radius: 12rpx;
  margin: 24rpx 0;
  font-size: 28rpx;
}
.btn { margin: 16rpx 0; }
</style>
