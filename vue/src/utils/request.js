// ============================================
// request.js - axios 封装
//
// 功能：统一处理 HTTP 请求
// - 设置 baseURL（后端地址）
// - 请求时自动添加 token
// - 统一处理响应和错误
// ============================================

import axios from 'axios'

// 创建 axios 实例
const request = axios.create({
  baseURL: 'http://localhost:8080/api',  // 后端 API 地址
  timeout: 5000                         // 5秒超时
})

// 请求拦截器：每次请求前执行
request.interceptors.request.use(
  config => {
    // 从本地存储取 token，加到请求头
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.token = token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器：每次响应后执行
request.interceptors.response.use(
  response => {
    // 直接返回后端数据（res.data）
    return response.data
  },
  error => {
    // 请求失败（网络错误、超时等）
    console.error('请求错误：', error.message)
    return Promise.reject(error)
  }
)

export default request