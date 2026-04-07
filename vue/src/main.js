// ============================================
// main.js - Vue 应用入口文件
// ============================================
// 这是最先执行的 JavaScript 文件
// 职责：创建 Vue 应用实例，加载全局插件，挂载到 DOM

// --- 导入 Vue 核心函数 ---
// createApp 是 Vue 3 创建应用实例的函数
// 类似 new Vue()，但 Vue 3 用函数式 API
import { createApp } from 'vue'

// --- 导入根组件 App.vue ---
// App.vue 是所有组件的"根容器"
import App from './App.vue'

// --- 导入 Element Plus（UI 组件库）---
// Element Plus 是饿了么团队开发的 Vue 3 UI 组件库
// 提供 Button、Input、Table 等美观的现成组件
import ElementPlus from 'element-plus'

// --- 导入 Element Plus 的 CSS 样式 ---
// 必须引入，否则组件显示不正确（没样式）
import 'element-plus/dist/index.css'

// --- 导入路由配置 ---
// 路由控制"显示哪个页面"
// 例如：URL 是 /login 显示登录页，/home 显示首页
import router from './router'

// ============================================
// 创建 Vue 应用实例
// ============================================
// createApp(App) 创建应用，App 是根组件
const app = createApp(App)

// ============================================
// 使用插件（全局注册）
// ============================================
// app.use(插件) 安装插件，所有子组件都能用

// 安装 Element Plus，全局注册所有组件
// 之后在任何 .vue 文件里都能直接用 <el-button> <el-input> 等
app.use(ElementPlus)

// 安装路由，启用页面跳转功能
// 之后可以用 <router-link> 和 this.$router
app.use(router)

// ============================================
// 挂载应用到 DOM
// ============================================
// mount('#app') 把 Vue 应用挂到 index.html 的 <div id="app"></div>
// 从这开始，Vue 接管页面渲染
app.mount('#app')