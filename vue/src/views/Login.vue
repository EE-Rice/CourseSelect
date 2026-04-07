<template>
  <!-- 功能：紫色背景居中显示登录卡片 -->
  <div class="login-container">
    <el-card class="login-card">
      <h2>学生选课系统</h2>

      <!-- 功能：表单输入，双向绑定数据 -->
      <!-- :model 绑定数据对象，v-model 双向绑定输入值 -->
      <el-form :model="form">
        <el-form-item>
          <!-- v-model="form.username" 输入变化自动更新 form.username -->
          <el-input v-model="form.username" placeholder="学号" />
        </el-form-item>

        <el-form-item>
          <!-- type="password" 密码显示圆点 -->
          <el-input v-model="form.password" type="password" placeholder="密码" />
        </el-form-item>

        <!-- @click 点击触发登录方法 -->
        <el-button type="primary" @click="handleLogin">登录</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
// 功能：引入路由用于页面跳转
// 关键：必须导入 reactive，否则报错 "Can't find variable: reactive"
import { reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// reactive 创建响应式对象，存储表单数据
const form = reactive({
  username: '',
  password: ''
})

// 处理登录，验证后跳转到首页
const handleLogin = () => {
  // 简单验证
  if (!form.username || !form.password) {
    alert('请输入学号和密码')
    return
  }

  console.log('登录', form.username, form.password)

  // router.push('/') 编程式跳转页面
  router.push('/home')
}
</script>

<style scoped>
/* 全屏紫色渐变背景，卡片居中 */
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 400px;
  padding: 20px;
}
</style>