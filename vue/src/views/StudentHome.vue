<!-- ============================================
     StudentHome.vue - 学生首页

     功能：
     1. 显示可选课程列表（分页）
     2. 点击选课，调用后端 API
     3. 显示我的已选课程
     4. 点击退课

     后端接口：
     - GET /api/course?pageNum=1&pageSize=10  分页查课程
     - POST /api/student-course/select        选课
     - GET /api/student-course/student/{id}    查已选课程
     - POST /api/student-course/cancel        退课
     ============================================ -->

<template>
  <div class="student-home">

    <!-- 顶部导航栏 -->
    <div class="header">
      <h2>学生选课系统</h2>
      <div class="user-info">
        <span v-if="user">欢迎，{{ user.name }}</span>
        <el-button type="danger" size="small" @click="logout">退出</el-button>
      </div>
    </div>

    <!-- 主体内容 -->
    <div class="main-content">

      <!-- 左侧：可选课程 -->
      <el-card class="course-section">
        <template #header>
          <div class="section-title">可选课程</div>
        </template>

        <!-- 课程卡片网格 -->
        <el-row :gutter="20">
          <el-col
            v-for="course in courses"
            :key="course.id"
            :xs="24" :sm="12" :md="8"
          >
            <el-card class="course-card" shadow="hover">
              <h4>{{ course.name }}</h4>
              <p class="course-no">编号：{{ course.courseNo }}</p>
              <p>学分：{{ course.credit }}</p>
              <p>教师：{{ course.teacher }}</p>
              <p class="capacity">
                已选：{{ course.selectedCount }}/{{ course.capacity }}
              </p>

              <!-- 进度条显示选课比例 -->
              <el-progress
                :percentage="(course.selectedCount / course.capacity) * 100"
                :status="course.selectedCount >= course.capacity ? 'exception' : 'success'"
              />

              <el-button
                type="primary"
                class="select-btn"
                :disabled="course.selectedCount >= course.capacity || loading"
                @click="selectCourse(course.id)"
              >
                {{ course.selectedCount >= course.capacity ? '已满' : '选课' }}
              </el-button>
            </el-card>
          </el-col>
        </el-row>

        <!-- 分页 -->
        <el-pagination
          v-model:current-page="pageNum"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next, jumper"
          @current-change="loadCourses"
          class="pagination"
        />
      </el-card>

      <!-- 右侧：我的选课 -->
      <el-card class="my-course-section">
        <template #header>
          <div class="section-title">
            我的选课（共 {{ myCourses.length }} 门）
          </div>
        </template>

        <!-- 已选课程列表 -->
        <el-table :data="myCourses" v-if="myCourses.length > 0">
          <el-table-column prop="courseName" label="课程名称" />
          <el-table-column prop="credit" label="学分" width="80" />
          <el-table-column prop="teacher" label="教师" width="100" />
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button
                type="danger"
                size="small"
                @click="cancelCourse(scope.row.courseId)"
              >
                退课
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 空状态 -->
        <el-empty v-else description="还没有选课" />
      </el-card>
    </div>
  </div>
</template>

<script setup>
// ============================================
// 脚本：数据和逻辑
// ============================================

import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const router = useRouter()

// --- 用户数据 ---
const user = ref(null)

// --- 课程列表数据 ---
const courses = ref([])        // 课程数组
const pageNum = ref(1)         // 当前页
const pageSize = ref(6)       // 每页条数
const total = ref(0)           // 总条数

// --- 我的选课 ---
const myCourses = ref([])

// --- 加载状态 ---
const loading = ref(false)

// ============================================
// 页面加载时执行
// ============================================
onMounted(() => {
  // 从本地存储读取用户信息
  const userStr = localStorage.getItem('user')
  if (userStr) {
    user.value = JSON.parse(userStr)
    // 加载数据
    loadCourses()
    loadMyCourses()
  } else {
    // 未登录，跳回登录页
    router.push('/login')
  }
})

// ============================================
// 加载课程列表（分页）
// ============================================
const loadCourses = async () => {
  try {
    const res = await request.get('/course', {
      params: {
        pageNum: pageNum.value,
        pageSize: pageSize.value
      }
    })

    if (res.code === 200) {
      // MyBatis-Plus 分页返回格式：res.data.records
      courses.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    console.error('加载课程失败', error)
  }
}

// ============================================
// 加载我的选课
// ============================================
const loadMyCourses = async () => {
  if (!user.value) return

  try {
    // 简化：先用空数组，后续对接真实接口
    // const res = await request.get(`/student-course/student/${user.value.id}`)
    // myCourses.value = res.data

    myCourses.value = []  // 临时
  } catch (error) {
    console.error('加载我的选课失败', error)
  }
}

// ============================================
// 选课
// ============================================
const selectCourse = async (courseId) => {
  if (!user.value) return

  loading.value = true

  try {
    const res = await request.post('/student-course/select', null, {
      params: {
        studentId: user.value.id,
        courseId: courseId
      }
    })

    if (res.code === 200) {
      alert('选课成功！')
      loadCourses()      // 刷新课程列表（人数变化）
      loadMyCourses()    // 刷新我的选课
    } else {
      alert(res.msg || '选课失败')
    }
  } catch (error) {
    alert('选课失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// ============================================
// 退课
// ============================================
const cancelCourse = async (courseId) => {
  if (!user.value) return

  try {
    const res = await request.post('/student-course/cancel', null, {
      params: {
        studentId: user.value.id,
        courseId: courseId
      }
    })

    if (res.code === 200) {
      alert('退课成功！')
      loadCourses()
      loadMyCourses()
    } else {
      alert(res.msg || '退课失败')
    }
  } catch (error) {
    alert('退课失败：' + error.message)
  }
}

// ============================================
// 退出登录
// ============================================
const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}
</script>

<style scoped>
/* ============================================
   样式
   ============================================ */

.student-home {
  min-height: 100vh;
  background: #f5f7fa;
}

/* 顶部导航 */
.header {
  background: #409eff;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
}

.header h2 {
  margin: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

/* 主体内容 */
.main-content {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

/* 区域标题 */
.section-title {
  font-size: 18px;
  font-weight: bold;
}

/* 课程卡片 */
.course-section {
  margin-bottom: 20px;
}

.course-card {
  margin-bottom: 20px;
  text-align: center;
}

.course-card h4 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 16px;
}

.course-card p {
  margin: 5px 0;
  color: #666;
  font-size: 14px;
}

.course-no {
  color: #999;
  font-size: 12px;
}

.capacity {
  color: #409eff;
  font-weight: bold;
}

.select-btn {
  margin-top: 10px;
  width: 100%;
}

/* 分页 */
.pagination {
  margin-top: 20px;
  justify-content: center;
}

/* 我的选课 */
.my-course-section {
  margin-top: 20px;
}
</style>