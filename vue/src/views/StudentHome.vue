<template>
  <div class="home">
    <h1>学生首页</h1>

    <!-- 显示登录用户信息 -->
    <div v-if="user">
      <p>欢迎，{{ user.name }}（{{ user.username }}）</p>
      <p>专业：{{ user.majorId }}</p>
    </div>

    <el-button @click="logout">退出登录</el-button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 读取本地存储的用户信息
const user = ref(null)

onMounted(() => {
  // 页面加载时执行
  const userStr = localStorage.getItem('user')
  if (userStr) {
    user.value = JSON.parse(userStr)
  }
})

const logout = () => {
  // 清除登录状态
  localStorage.removeItem('token')
  localStorage.removeItem('user')

  // 返回登录页
  router.push('/login')
}
</script>

<style scoped>
.home {
  padding: 40px;
  text-align: center;
}
</style>