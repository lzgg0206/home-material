// 后端基地址：
// - H5：走 devServer proxy（/api → localhost:8090），见 manifest.json
// - 微信小程序：必须是已备案的完整 https 域名，并在小程序后台「开发设置-服务器域名」配为 request 合法域名
// #ifdef MP-WEIXIN
// 本地调试直连后端（微信开发者工具需勾"详情→本地设置→不校验合法域名"）；上线改回已备案 https 域名
const BASE_URL = 'http://127.0.0.1:8090/api'
// #endif
// #ifndef MP-WEIXIN
// H5 开发直连后端（后端 CorsConfig 已放开跨域）；生产部署改回 '/api' 走 nginx 反代
const BASE_URL = 'http://127.0.0.1:8090/api'
// #endif

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
