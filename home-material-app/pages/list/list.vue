<template>
  <view class="container">
    <view v-if="!items.length" class="empty">清单还是空的，去首页加些型号吧</view>

    <view v-for="it in items" :key="it.id" class="section item">
      <view class="item-name">{{ it.modelName || '自定义型号' }}</view>
      <view class="meta">{{ it.categoryName }} · {{ it.space }} · {{ it.spec }}</view>
      <view class="item-price">
        {{ it.quantity }} × ¥{{ it.unitPrice }} = ¥{{ it.totalPrice }}
        <text class="status">{{ statusText(it.purchaseStatus) }}</text>
      </view>
    </view>

    <view class="budget-bar" v-if="budget.totalSpent !== undefined">
      <view>已选 {{ items.length }} 件 · 合计 ¥{{ budget.totalSpent }}</view>
      <view>
        预算 ¥{{ budget.totalBudget }} ·
        <text :class="budget.overspent ? 'red' : 'green'">
          {{ budget.overspent ? '超支' : '剩余' }} ¥{{ Math.abs(budget.remaining) }}
        </text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getMyLists, getListItems, getBudget } from '../../api/index'

const items = ref([])
const budget = ref({})

const statusText = (s) => ({ pending: '待采购', ordered: '已下单', received: '已收货' }[s] || s)

const load = async () => {
  try {
    const lists = await getMyLists()
    if (!lists || !lists.length) return
    const id = lists[0].id
    items.value = await getListItems(id)
    budget.value = await getBudget(id)
  } catch (e) {
    // 未登录或错误已在 request 内 toast
  }
}

onShow(load)
</script>

<style scoped>
.item-name { font-size: 30rpx; font-weight: 500; }
.item-price { margin-top: 8rpx; font-size: 28rpx; }
.status {
  float: right;
  font-size: 24rpx;
  color: #07c160;
  background: #f0faf3;
  padding: 2rpx 12rpx;
  border-radius: 8rpx;
}
.budget-bar {
  position: fixed;
  left: 0; right: 0; bottom: 0;
  background: #fff;
  padding: 24rpx;
  border-top: 1rpx solid #eee;
  display: flex;
  justify-content: space-between;
  font-size: 28rpx;
}
</style>
