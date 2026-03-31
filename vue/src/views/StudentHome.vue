<template>
  <div>
    <h1>学生首页</h1>
    <p>登录成功！</p>
    <el-button @click="logout">退出</el-button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const logout = () => router.push('/login')
</script>