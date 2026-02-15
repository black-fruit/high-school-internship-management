<template>
  <div class="login-container">
    <n-card class="login-card" title="高校实习管理系统">
      <n-form ref="formRef" :model="form" :rules="rules" label-placement="left" label-width="80">
        <n-form-item label="用户名" path="username">
          <n-input v-model:value="form.username" placeholder="请输入用户名" />
        </n-form-item>
        <n-form-item label="密码" path="password">
          <n-input v-model:value="form.password" type="password" placeholder="请输入密码" @keyup.enter="handleLogin" />
        </n-form-item>
        <n-form-item>
          <n-button type="primary" block :loading="loading" @click="handleLogin">
            登录
          </n-button>
        </n-form-item>
        <n-form-item>
          <n-button text @click="router.push('/register')">
            没有账号？立即注册
          </n-button>
        </n-form-item>
      </n-form>
      <n-divider>测试账号</n-divider>
      <div class="test-accounts">
        <p>管理员: admin / admin123</p>
        <p>教师: teacher / teacher123</p>
        <p>学生: student / student123</p>
        <p>企业: enterprise / enterprise123</p>
      </div>
    </n-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const message = useMessage()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)
const form = ref({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  await formRef.value?.validate()
  loading.value = true
  try {
    await userStore.loginAction(form.value)
    message.success('登录成功')
    router.push('/dashboard')
  } catch (error) {
    message.error(error.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7f9;
}

.login-card {
  width: 400px;
}

.test-accounts {
  font-size: 12px;
  color: #666;
  line-height: 1.8;
}
</style>
