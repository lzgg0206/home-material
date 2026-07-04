import { request } from './request'

// 首页聚合
export const getHome = () => request({ url: '/home' })

// 品类树
export const getCategoryTree = () => request({ url: '/category/tree' })

// 品牌排行榜
export const getBrandRanking = (params) => request({ url: '/brand/ranking', data: params })

// 型号详情
export const getModelDetail = (id) => request({ url: `/model/${id}/detail` })

// 搜索
export const search = (keyword) => request({ url: '/search', data: { keyword } })
export const getHotKeywords = () => request({ url: '/search/hot' })

// 登录
export const loginByOpenid = (openid) =>
  request({ url: '/auth/login', method: 'POST', data: { openid } })
export const wxLogin = (code) =>
  request({ url: '/auth/wx-login', method: 'POST', data: { code } })

// 家装档案
export const getProfile = () => request({ url: '/profile' })
export const saveProfile = (profile) =>
  request({ url: '/profile', method: 'POST', data: profile })

// 自选清单
export const getMyLists = () => request({ url: '/list' })
export const createList = (list) => request({ url: '/list', method: 'POST', data: list })
export const getListItems = (id) => request({ url: `/list/${id}/items` })
export const addListItem = (listId, item) =>
  request({ url: `/list/${listId}/item`, method: 'POST', data: item })
export const getBudget = (id) => request({ url: `/list/${id}/budget` })
