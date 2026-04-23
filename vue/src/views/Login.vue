<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>学生选课系统</h2>

      <!-- 添加加载状态 -->
      <el-form :model="form">
        <el-form-item>
          <el-input v-model="form.username" placeholder="学号" />
        </el-form-item>

        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码" />
        </el-form-item>

        <!-- 加载中禁用按钮 -->
        <el-button
          type="primary"
          :loading="loading"
          @click="handleLogin"
        >
          登录
        </el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'           // 新增 ref
import { useRouter } from 'vue-router'
import request from '../utils/request'        // 导入封装的 axios

const router = useRouter()

// 新增：加载状态，请求时显示转圈，防止重复提交
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

// 修改：调用真实后端 API
const handleLogin = async () => {
  // 简单验证
  if (!form.username || !form.password) {
    alert('请输入学号和密码')
    return
  }

  // 开始加载
  loading.value = true

  try {
    // 关键：调用后端登录接口
    // request.post 返回 Promise，await 等待结果
    const res = await request.post('/user/login', {
      username: form.username,
      password: form.password
    })

    // 后端返回格式：{ code: 200, msg: 'success', data: { user, token } }
    if (res.code === 200) {
      // 登录成功
      alert('登录成功！')

      // 保存 token 到浏览器本地存储
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('user', JSON.stringify(res.data.user))

      // 跳转到首页
      router.push('/home')
    } else {
      // 登录失败（用户名密码错误等）
      alert(res.msg || '登录失败')
    }
  } catch (error) {
    // 网络错误、超时等
    console.error(error)
  } finally {
    // 无论成功失败，结束加载
    loading.value = false
  }
}
</script>

<style scoped>
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