<template>
  <!-- 功能：登录成功后显示的页面 -->
  <div>
    <h1>学生首页</h1>
    <p>登录成功！</p>

    <!-- 关键：点击返回登录页 -->
    <el-button @click="logout">退出</el-button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

// 功能：退出登录，返回登录页
const router = useRouter()
const logout = () => router.push('/login')
</script>