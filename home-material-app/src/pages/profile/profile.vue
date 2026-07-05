<template>
  <view class="container">
    <view class="section">
      <view class="field"><text class="label">城市</text><input v-model="form.city" placeholder="如 上海" /></view>
      <view class="field"><text class="label">面积(㎡)</text><input type="digit" v-model="form.area" /></view>
      <view class="field"><text class="label">户型</text><input v-model="form.layout" placeholder="如 三室两厅" /></view>
      <view class="field"><text class="label">房屋类型</text><input v-model="form.houseType" placeholder="如 新房" /></view>
      <view class="field"><text class="label">装修阶段</text><input v-model="form.stage" placeholder="如 硬装阶段" /></view>
      <view class="field"><text class="label">装修风格</text><input v-model="form.style" placeholder="如 现代简约" /></view>
      <view class="field"><text class="label">装修方式</text><input v-model="form.decorateWay" placeholder="如 半包" /></view>
      <view class="field"><text class="label">总预算(元)</text><input type="digit" v-model="form.totalBudget" /></view>
      <view class="field"><text class="label">核心偏好</text><input v-model="form.preference" placeholder="如 性价比,环保,耐用" /></view>
    </view>
    <button class="save" type="primary" :disabled="saving" @click="save">
      {{ saving ? '保存中...' : '保存档案' }}
    </button>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getProfile, saveProfile } from '../../api/index'

const form = ref({})
const saving = ref(false)

const load = async () => {
  try { form.value = (await getProfile()) || {} } catch (e) {}
}
const save = async () => {
  saving.value = true
  try {
    await saveProfile(form.value)
    uni.showToast({ title: '已保存', icon: 'success' })
  } catch (e) {} finally {
    saving.value = false
  }
}

onShow(load)
</script>

<style scoped>
.field { display: flex; align-items: center; padding: 20rpx 0; border-bottom: 1rpx solid #f0f0f0; }
.field:last-child { border-bottom: none; }
.label { width: 180rpx; color: #333; }
.field input { flex: 1; font-size: 28rpx; }
.save { margin-top: 32rpx; }
</style>
