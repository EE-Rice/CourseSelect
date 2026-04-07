// 功能：封装 axios，统一处理后端 API 请求
// 地位：所有 HTTP 请求的入口，统一配置 baseURL、超时、错误处理

import axios from 'axios'

// 创建 axios 实例
const request = axios.create({
  baseURL: 'http://localhost:8080/api',  // 后端地址
  timeout: 5000                          // 5秒超时
})

// 请求拦截器：每次请求前自动执行
// 后续添加 token 到请求头
request.interceptors.request.use(
  config => {
    // 从 localStorage 取 token，加到请求头
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

// 响应拦截器：每次响应后自动执行
// 统一处理错误
request.interceptors.response.use(
  response => {
    // 后端返回的数据在 response.data
    return response.data
  },
  error => {
    // 请求失败（网络错误、超时等）
    alert('请求失败：' + error.message)
    return Promise.reject(error)
  }
)

export default request