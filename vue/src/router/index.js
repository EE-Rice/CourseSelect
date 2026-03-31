// 路由配置：URL 路径对应哪个页面

import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/home',
    name: 'StudentHome',
    component: () => import('../views/StudentHome.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router