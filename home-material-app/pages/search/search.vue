<template>
  <view class="container">
    <view class="search-bar">
      <input
        v-model="kw"
        class="search-input"
        placeholder="搜索品牌、型号、避坑关键词"
        confirm-type="search"
        @confirm="doSearch"
      />
      <button size="mini" type="primary" @click="doSearch">搜索</button>
    </view>

    <view v-if="!searched">
      <view class="section" v-if="hot.length">
        <view class="title">热门搜索</view>
        <view class="tags">
          <text v-for="h in hot" :key="h" class="tag" @click="pickHot(h)">{{ h }}</text>
        </view>
      </view>
    </view>

    <view v-else>
      <view class="section" v-if="result.brands && result.brands.length">
        <view class="title">品牌</view>
        <view v-for="b in result.brands" :key="b.id" class="row" @click="goRankingByBrand(b)">{{ b.name }}</view>
      </view>
      <view class="section" v-if="result.models && result.models.length">
        <view class="title">型号</view>
        <view v-for="m in result.models" :key="m.id" class="row" @click="goModel(m.id)">{{ m.name }}</view>
      </view>
      <view class="section" v-if="result.categories && result.categories.length">
        <view class="title">品类</view>
        <view v-for="c in result.categories" :key="c.id" class="row">{{ c.name }}</view>
      </view>
      <view v-if="isEmpty" class="empty">无匹配结果</view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { search, getHotKeywords } from '../../api/index'

const kw = ref('')
const searched = ref(false)
const result = ref({})
const hot = ref([])

const isEmpty = computed(() =>
  searched.value &&
  !(result.value.brands && result.value.brands.length) &&
  !(result.value.models && result.value.models.length) &&
  !(result.value.categories && result.value.categories.length)
)

const doSearch = async () => {
  if (!kw.value.trim()) return
  try {
    result.value = await search(kw.value)
    searched.value = true
  } catch (e) {}
}

const pickHot = (h) => { kw.value = h; doSearch() }
const goModel = (id) => uni.navigateTo({ url: `/pages/model/detail?id=${id}` })
const goRankingByBrand = () => uni.navigateTo({ url: '/pages/brand/ranking' })

onShow(async () => {
  try { hot.value = await getHotKeywords() } catch (e) {}
})
</script>

<style scoped>
.search-bar { display: flex; align-items: center; margin-bottom: 24rpx; }
.search-input {
  flex: 1;
  background: #fff;
  border-radius: 32rpx;
  padding: 16rpx 28rpx;
  font-size: 28rpx;
  margin-right: 16rpx;
}
.tags { display: flex; flex-wrap: wrap; }
.tag {
  font-size: 26rpx;
  padding: 8rpx 24rpx;
  background: #f0faf3;
  color: #07c160;
  border-radius: 32rpx;
  margin: 8rpx 16rpx 8rpx 0;
}
.row { padding: 20rpx 0; border-bottom: 1rpx solid #f0f0f0; }
.row:last-child { border-bottom: none; }
</style>
