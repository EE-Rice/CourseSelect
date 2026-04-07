// 路由配置：URL 路径对应哪个页面

// --- 从 vue-router 导入创建路由的函数 ---
// createRouter：创建路由实例
// createWebHistory：使用 HTML5 History 模式（URL 无 # 号）
import { createRouter, createWebHistory } from 'vue-router'

// 路由配置数组
// 每个对象定义一条路由规则
const routes = [
  // --- 路由 1：根路径重定向 ---
  {
    path: '/', // URL路径：跟路径
    redirect: '/login' // 重定向到/login
  },

  // --- 路由 2：登录页面 ---
  {
    path: '/login', // URL路径
    name: 'Login', // 路由名称，用于编程式导航
    component: () => import('../views/Login.vue') // 对应的组件

    // () => import(...) 是"懒加载"（动态导入）
        // 用户访问 /login 时才下载 Login.vue，首屏加载更快
        // 对比：import Login from '...' 是启动时全部加载
  },

  // --- 路由3:学生首页 ---
  {
    path: '/home',
    name: 'StudentHome',
    component: () => import('../views/StudentHome.vue')
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(), // 使用浏览器 History API（无 # 号）
  routes  // 挂载路由配置
})

// 导出路由实例
// 供 main.js 导入使用：import router from './router'
export default router

// 导出后，在 main.js 里 app.use(router) 就注册了路由