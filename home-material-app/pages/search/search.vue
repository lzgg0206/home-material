<template>
  <view class="container">
    <view class="search-bar">
      <view class="search-input-wrap">
        <input
          v-model="kw"
          class="search-input"
          placeholder="搜索品牌、型号、避坑关键词"
          confirm-type="search"
          @confirm="doSearch"
        />
        <text v-if="kw" class="clear-btn" @click="clearKw">×</text>
      </view>
      <button size="mini" type="primary" @click="doSearch">搜索</button>
    </view>

    <view v-if="!searched">
      <view class="section" v-if="hot.length">
        <view class="title">热门搜索</view>
        <view class="tags">
          <text v-for="h in hot" :key="h" class="tag tap" @click="pickHot(h)">{{ h }}</text>
        </view>
      </view>
    </view>

    <view v-else>
      <view class="section" v-if="result.brands && result.brands.length">
        <view class="title">品牌</view>
        <view v-for="b in result.brands" :key="b.id" class="row tap" @click="goRankingByBrand(b)">{{ b.name }}</view>
      </view>
      <view class="section" v-if="result.models && result.models.length">
        <view class="title">型号</view>
        <view v-for="m in result.models" :key="m.id" class="row tap" @click="goModel(m.id)">{{ m.name }}</view>
      </view>
      <view class="section" v-if="result.categories && result.categories.length">
        <view class="title">品类</view>
        <view v-for="c in result.categories" :key="c.id" class="row">{{ c.name }}</view>
      </view>
      <view v-if="isEmpty" class="empty">
        <view class="empty-icon"></view>无匹配结果，换个关键词试试
      </view>
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

const clearKw = () => { kw.value = ''; searched.value = false; result.value = {} }
const pickHot = (h) => { kw.value = h; doSearch() }
const goModel = (id) => uni.navigateTo({ url: `/pages/model/detail?id=${id}` })
const goRankingByBrand = () => uni.navigateTo({ url: '/pages/brand/ranking' })

onShow(async () => {
  try { hot.value = await getHotKeywords() } catch (e) {}
})
</script>

<style scoped>
.search-bar { display: flex; align-items: center; margin-bottom: 24rpx; }
.search-input-wrap { position: relative; flex: 1; margin-right: 16rpx; }
.search-input {
  background: #fff;
  border-radius: 32rpx;
  padding: 16rpx 56rpx 16rpx 28rpx;
  font-size: 28rpx;
}
.clear-btn {
  position: absolute;
  right: 16rpx;
  top: 50%;
  transform: translateY(-50%);
  width: 36rpx;
  height: 36rpx;
  line-height: 34rpx;
  text-align: center;
  background: #ddd;
  color: #fff;
  border-radius: 50%;
  font-size: 28rpx;
}
.tags { display: flex; flex-wrap: wrap; }
.row { padding: 20rpx 0; border-bottom: 1rpx solid var(--color-border); }
.row:last-child { border-bottom: none; }
</style>
