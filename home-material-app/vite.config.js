import { defineConfig } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'

// uni-app Vue3 + Vite 工程配置
export default defineConfig({
  plugins: [uni()],
  server: {
    port: 5173,
    host: '0.0.0.0',
    proxy: {
      // 备用：若 request.js 改回相对 /api，走此代理到后端
      '/api': {
        target: 'http://localhost:8090',
        changeOrigin: true
      }
    }
  }
})
