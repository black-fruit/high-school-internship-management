<template>
  <div class="register-container">
    <n-card class="register-card" title="用户注册">
      <n-form ref="formRef" :model="form" :rules="rules" label-placement="left" label-width="100">
        <n-form-item label="用户名" path="username">
          <n-input v-model:value="form.username" placeholder="请输入用户名" />
        </n-form-item>
        <n-form-item label="密码" path="password">
          <n-input v-model:value="form.password" type="password" placeholder="请输入密码" />
        </n-form-item>
        <n-form-item label="确认密码" path="confirmPassword">
          <n-input v-model:value="form.confirmPassword" type="password" placeholder="请再次输入密码" />
        </n-form-item>
        <n-form-item label="真实姓名" path="realName">
          <n-input v-model:value="form.realName" placeholder="请输入真实姓名" />
        </n-form-item>
        <n-form-item label="手机号" path="phone">
          <n-input v-model:value="form.phone" placeholder="请输入手机号" />
        </n-form-item>
        <n-form-item label="邮箱" path="email">
          <n-input v-model:value="form.email" placeholder="请输入邮箱" />
        </n-form-item>
        <n-form-item label="用户类型" path="userType">
          <n-radio-group v-model:value="form.userType">
            <n-radio :value="1">学生</n-radio>
            <n-radio :value="2">教师</n-radio>
            <n-radio :value="3">企业</n-radio>
          </n-radio-group>
        </n-form-item>
        
        <template v-if="form.userType === 1">
          <n-form-item label="学号" path="studentNo">
            <n-input v-model:value="form.studentNo" placeholder="请输入学号" />
          </n-form-item>
          <n-form-item label="班级" path="className">
            <n-input v-model:value="form.className" placeholder="请输入班级" />
          </n-form-item>
          <n-form-item label="专业" path="major">
            <n-input v-model:value="form.major" placeholder="请输入专业" />
          </n-form-item>
          <n-form-item label="学院" path="college">
            <n-input v-model:value="form.college" placeholder="请输入学院" />
          </n-form-item>
        </template>
        
        <template v-if="form.userType === 2">
          <n-form-item label="工号" path="teacherNo">
            <n-input v-model:value="form.teacherNo" placeholder="请输入工号" />
          </n-form-item>
          <n-form-item label="学院" path="college">
            <n-input v-model:value="form.college" placeholder="请输入学院" />
          </n-form-item>
          <n-form-item label="系部" path="department">
            <n-input v-model:value="form.department" placeholder="请输入系部" />
          </n-form-item>
        </template>
        
        <n-form-item>
          <n-button type="primary" block :loading="loading" @click="handleRegister">
            注册
          </n-button>
        </n-form-item>
        <n-form-item>
          <n-button text @click="router.push('/login')">
            已有账号？立即登录
          </n-button>
        </n-form-item>
      </n-form>
    </n-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { register } from '@/api/auth'

const router = useRouter()
const message = useMessage()

const formRef = ref(null)
const loading = ref(false)
const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  realName: '',
  phone: '',
  email: '',
  userType: 1,
  studentNo: '',
  className: '',
  major: '',
  college: '',
  teacherNo: '',
  department: ''
})

const validatePassword = (rule, value) => {
  return value === form.value.password
}

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validatePassword, message: '两次密码不一致', trigger: 'blur' }
  ],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  userType: [{ required: true, type: 'number', message: '请选择用户类型', trigger: ['blur', 'change'] }]
}

const handleRegister = async () => {
  await formRef.value?.validate()
  loading.value = true
  try {
    const data = { ...form.value }
    delete data.confirmPassword
    const res = await register(data)
    if (res.code === 200) {
      message.success('注册成功，请登录')
      router.push('/login')
    } else {
      message.error(res.message || '注册失败')
    }
  } catch (error) {
    message.error(error.message || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7f9;
}

.register-card {
  width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}
</style>
