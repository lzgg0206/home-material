// 后端基地址：H5 走 devServer proxy；微信小程序生产改为已备案 https 域名
const BASE_URL = '/api'

/**
 * 统一请求封装，对齐后端 Result {code,message,data}。
 * @returns {Promise<*>} 解析后的 data
 */
export function request({ url, method = 'GET', data, header }) {
  const token = uni.getStorageSync('token')
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + url,
      method,
      data,
      header: Object.assign(
        { 'Content-Type': 'application/json' },
        token ? { Authorization: token } : {},
        header || {}
      ),
      success: (res) => {
        const body = res.data || {}
        if (body.code === 200) {
          resolve(body.data)
        } else if (body.code === 401) {
          uni.removeStorageSync('token')
          uni.showToast({ title: '请先登录', icon: 'none' })
          reject(body)
        } else {
          uni.showToast({ title: body.message || '请求失败', icon: 'none' })
          reject(body)
        }
      },
      fail: (err) => {
        uni.showToast({ title: '网络异常', icon: 'none' })
        reject(err)
      }
    })
  })
}
