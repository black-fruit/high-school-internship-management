<template>
  <div class="profile-page">
    <n-card title="个人中心">
      <template #header-extra>
        <n-space>
          <n-button @click="openPasswordModal">修改密码</n-button>
          <n-button type="primary" @click="openEditModal">编辑信息</n-button>
        </n-space>
      </template>
      <n-descriptions :column="2" bordered>
        <n-descriptions-item label="用户名">{{ userInfo?.username }}</n-descriptions-item>
        <n-descriptions-item label="真实姓名">{{ userInfo?.realName }}</n-descriptions-item>
        <n-descriptions-item label="手机号">{{ userInfo?.phone || '未设置' }}</n-descriptions-item>
        <n-descriptions-item label="邮箱">{{ userInfo?.email || '未设置' }}</n-descriptions-item>
        <n-descriptions-item label="用户类型">{{ userTypeMap[userInfo?.userType] }}</n-descriptions-item>
        <n-descriptions-item label="状态">{{ userInfo?.status === 1 ? '正常' : '禁用' }}</n-descriptions-item>
        <template v-if="userInfo?.userType === 1">
          <n-descriptions-item label="学号">{{ userInfo?.studentNo || '未设置' }}</n-descriptions-item>
          <n-descriptions-item label="班级">{{ userInfo?.className || '未设置' }}</n-descriptions-item>
          <n-descriptions-item label="专业">{{ userInfo?.major || '未设置' }}</n-descriptions-item>
          <n-descriptions-item label="学院">{{ userInfo?.college || '未设置' }}</n-descriptions-item>
          <n-descriptions-item label="指导教师">{{ userInfo?.teacherName || '未分配' }}</n-descriptions-item>
        </template>
        <template v-if="userInfo?.userType === 2">
          <n-descriptions-item label="工号">{{ userInfo?.teacherNo || '未设置' }}</n-descriptions-item>
          <n-descriptions-item label="学院">{{ userInfo?.college || '未设置' }}</n-descriptions-item>
          <n-descriptions-item label="系部">{{ userInfo?.department || '未设置' }}</n-descriptions-item>
        </template>
        <template v-if="userInfo?.userType === 3">
          <n-descriptions-item label="职位">{{ userInfo?.position || '未设置' }}</n-descriptions-item>
        </template>
      </n-descriptions>
    </n-card>
    
    <n-modal v-model:show="showEditModal" preset="dialog" title="编辑个人信息" style="width: 600px">
      <n-form ref="formRef" :model="editForm" :rules="rules" label-placement="left" label-width="100">
        <n-form-item label="真实姓名" path="realName">
          <n-input v-model:value="editForm.realName" placeholder="请输入真实姓名" />
        </n-form-item>
        <n-form-item label="手机号" path="phone">
          <n-input v-model:value="editForm.phone" placeholder="请输入手机号" />
        </n-form-item>
        <n-form-item label="邮箱" path="email">
          <n-input v-model:value="editForm.email" placeholder="请输入邮箱" />
        </n-form-item>
        
        <template v-if="userInfo?.userType === 1">
          <n-form-item label="学号" path="studentNo">
            <n-input v-model:value="editForm.studentNo" placeholder="请输入学号" />
          </n-form-item>
          <n-form-item label="班级" path="className">
            <n-input v-model:value="editForm.className" placeholder="请输入班级" />
          </n-form-item>
          <n-form-item label="专业" path="major">
            <n-input v-model:value="editForm.major" placeholder="请输入专业" />
          </n-form-item>
          <n-form-item label="学院" path="college">
            <n-input v-model:value="editForm.college" placeholder="请输入学院" />
          </n-form-item>
        </template>
        
        <template v-if="userInfo?.userType === 2">
          <n-form-item label="工号" path="teacherNo">
            <n-input v-model:value="editForm.teacherNo" placeholder="请输入工号" />
          </n-form-item>
          <n-form-item label="学院" path="college">
            <n-input v-model:value="editForm.college" placeholder="请输入学院" />
          </n-form-item>
          <n-form-item label="系部" path="department">
            <n-input v-model:value="editForm.department" placeholder="请输入系部" />
          </n-form-item>
        </template>
        
        <template v-if="userInfo?.userType === 3">
          <n-form-item label="职位" path="position">
            <n-input v-model:value="editForm.position" placeholder="请输入职位" />
          </n-form-item>
        </template>
      </n-form>
      <template #action>
        <n-button @click="showEditModal = false">取消</n-button>
        <n-button type="primary" :loading="loading" @click="handleSave">保存</n-button>
      </template>
    </n-modal>
    
    <n-modal v-model:show="showPasswordModal" preset="dialog" title="修改密码" style="width: 400px">
      <n-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-placement="left" label-width="100">
        <n-form-item label="旧密码" path="oldPassword">
          <n-input v-model:value="passwordForm.oldPassword" type="password" placeholder="请输入旧密码" />
        </n-form-item>
        <n-form-item label="新密码" path="newPassword">
          <n-input v-model:value="passwordForm.newPassword" type="password" placeholder="请输入新密码" />
        </n-form-item>
        <n-form-item label="确认密码" path="confirmPassword">
          <n-input v-model:value="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-button @click="showPasswordModal = false">取消</n-button>
        <n-button type="primary" :loading="passwordLoading" @click="handleChangePassword">确定</n-button>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useMessage } from 'naive-ui'
import { useUserStore } from '@/stores/user'
import { updateUser } from '@/api/user'
import { changePassword } from '@/api/auth'

const message = useMessage()
const userStore = useUserStore()
const userInfo = ref(null)
const showEditModal = ref(false)
const showPasswordModal = ref(false)
const loading = ref(false)
const passwordLoading = ref(false)
const formRef = ref(null)
const passwordFormRef = ref(null)

const userTypeMap = { 1: '学生', 2: '教师', 3: '企业', 4: '管理员' }

const editForm = ref({
  id: null,
  realName: '',
  phone: '',
  email: '',
  studentNo: '',
  className: '',
  major: '',
  college: '',
  teacherNo: '',
  department: '',
  position: ''
})

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const rules = {
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }]
}

const validateConfirmPassword = (rule, value) => {
  return value === passwordForm.value.newPassword
}

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, message: '两次密码不一致', trigger: 'blur' }
  ]
}

const loadUserInfo = async () => {
  await userStore.getUserInfo()
  userInfo.value = userStore.userInfo
}

const openEditModal = () => {
  editForm.value = {
    id: userInfo.value.id,
    realName: userInfo.value.realName || '',
    phone: userInfo.value.phone || '',
    email: userInfo.value.email || '',
    studentNo: userInfo.value.studentNo || '',
    className: userInfo.value.className || '',
    major: userInfo.value.major || '',
    college: userInfo.value.college || '',
    teacherNo: userInfo.value.teacherNo || '',
    department: userInfo.value.department || '',
    position: userInfo.value.position || ''
  }
  showEditModal.value = true
}

const handleSave = async () => {
  await formRef.value?.validate()
  loading.value = true
  try {
    const res = await updateUser(editForm.value)
    if (res.code === 200) {
      message.success('保存成功')
      showEditModal.value = false
      await loadUserInfo()
    } else {
      message.error(res.message || '保存失败')
    }
  } catch (error) {
    message.error(error.message || '保存失败')
  } finally {
    loading.value = false
  }
}

const openPasswordModal = () => {
  passwordForm.value = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
  showPasswordModal.value = true
}

const handleChangePassword = async () => {
  await passwordFormRef.value?.validate()
  passwordLoading.value = true
  try {
    const res = await changePassword({
      oldPassword: passwordForm.value.oldPassword,
      newPassword: passwordForm.value.newPassword
    })
    if (res.code === 200) {
      message.success('密码修改成功')
      showPasswordModal.value = false
    } else {
      message.error(res.message || '密码修改失败')
    }
  } catch (error) {
    message.error(error.message || '密码修改失败')
  } finally {
    passwordLoading.value = false
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>
