<template>
  <div class="user-page">
    <n-card title="用户管理">
      <template #header-extra>
        <n-space>
          <n-input v-model:value="searchKey" placeholder="搜索用户名/姓名" clearable @keyup.enter="loadData" />
          <n-select v-model:value="statusFilter" :options="statusOptions" placeholder="状态筛选" clearable style="width: 120px" />
          <n-button type="primary" @click="loadData">搜索</n-button>
          <n-button type="primary" @click="openCreateModal">添加用户</n-button>
        </n-space>
      </template>
      <n-data-table :columns="columns" :data="tableData" :loading="loading" :pagination="pagination" remote @update:page="handlePageChange" />
    </n-card>
    
    <n-modal v-model:show="showRoleModal" preset="dialog" title="分配角色">
      <n-form :model="roleForm">
        <n-form-item label="选择角色">
          <n-select v-model:value="roleForm.roleId" :options="roleOptions" label-field="roleName" value-field="id" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-button @click="showRoleModal = false">取消</n-button>
        <n-button type="primary" @click="handleAssignRole">确定</n-button>
      </template>
    </n-modal>
    
    <n-modal v-model:show="showCreateModal" preset="dialog" title="添加用户" style="width: 600px">
      <n-form ref="createFormRef" :model="createForm" :rules="createRules" label-placement="left" label-width="100">
        <n-form-item label="用户名" path="username">
          <n-input v-model:value="createForm.username" placeholder="请输入用户名" />
        </n-form-item>
        <n-form-item label="密码" path="password">
          <n-input v-model:value="createForm.password" type="password" placeholder="请输入密码" />
        </n-form-item>
        <n-form-item label="真实姓名" path="realName">
          <n-input v-model:value="createForm.realName" placeholder="请输入真实姓名" />
        </n-form-item>
        <n-form-item label="手机号" path="phone">
          <n-input v-model:value="createForm.phone" placeholder="请输入手机号" />
        </n-form-item>
        <n-form-item label="邮箱" path="email">
          <n-input v-model:value="createForm.email" placeholder="请输入邮箱" />
        </n-form-item>
        <n-form-item label="用户类型" path="userType">
          <n-select v-model:value="createForm.userType" :options="userTypeOptions" placeholder="请选择用户类型" />
        </n-form-item>
        
        <template v-if="createForm.userType === 1">
          <n-form-item label="学号" path="studentNo">
            <n-input v-model:value="createForm.studentNo" placeholder="请输入学号" />
          </n-form-item>
          <n-form-item label="班级" path="className">
            <n-input v-model:value="createForm.className" placeholder="请输入班级" />
          </n-form-item>
          <n-form-item label="专业" path="major">
            <n-input v-model:value="createForm.major" placeholder="请输入专业" />
          </n-form-item>
          <n-form-item label="学院" path="college">
            <n-input v-model:value="createForm.college" placeholder="请输入学院" />
          </n-form-item>
        </template>
        
        <template v-if="createForm.userType === 2">
          <n-form-item label="工号" path="teacherNo">
            <n-input v-model:value="createForm.teacherNo" placeholder="请输入工号" />
          </n-form-item>
          <n-form-item label="学院" path="college">
            <n-input v-model:value="createForm.college" placeholder="请输入学院" />
          </n-form-item>
          <n-form-item label="系部" path="department">
            <n-input v-model:value="createForm.department" placeholder="请输入系部" />
          </n-form-item>
        </template>
        
        <template v-if="createForm.userType === 3">
          <n-form-item label="企业" path="enterpriseId">
            <n-select v-model:value="createForm.enterpriseId" :options="enterpriseOptions" label-field="enterpriseName" value-field="id" placeholder="请选择企业" />
          </n-form-item>
          <n-form-item label="职位" path="position">
            <n-input v-model:value="createForm.position" placeholder="请输入职位" />
          </n-form-item>
        </template>
      </n-form>
      <template #action>
        <n-button @click="showCreateModal = false">取消</n-button>
        <n-button type="primary" :loading="createLoading" @click="handleCreate">确定</n-button>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, h, onMounted } from 'vue'
import { NButton, NTag, NSpace, useMessage, useDialog } from 'naive-ui'
import { getUserPage, updateUserStatus, assignRole, getRoles, createUser, deleteUser } from '@/api/user'
import { getEnterprisePage } from '@/api/enterprise'

const message = useMessage()
const dialog = useDialog()
const loading = ref(false)
const tableData = ref([])
const searchKey = ref('')
const statusFilter = ref(null)
const showRoleModal = ref(false)
const showCreateModal = ref(false)
const createLoading = ref(false)
const roleForm = ref({ userId: null, roleId: null })
const roleOptions = ref([])
const enterpriseOptions = ref([])
const createFormRef = ref(null)

const statusOptions = [
  { label: '启用', value: 1 },
  { label: '禁用', value: 0 }
]

const userTypeOptions = [
  { label: '学生', value: 1 },
  { label: '教师', value: 2 },
  { label: '企业', value: 3 },
  { label: '管理员', value: 4 }
]

const userTypeMap = { 1: '学生', 2: '教师', 3: '企业', 4: '管理员' }
const userTypeColor = { 1: 'info', 2: 'success', 3: 'warning', 4: 'error' }

const createForm = ref({
  username: '',
  password: '',
  realName: '',
  phone: '',
  email: '',
  userType: null,
  studentNo: '',
  className: '',
  major: '',
  college: '',
  teacherNo: '',
  department: '',
  enterpriseId: null,
  position: ''
})

const createRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  userType: [{ required: true, type: 'number', message: '请选择用户类型', trigger: ['blur', 'change'] }]
}

const validateEnterprise = () => {
  if (createForm.value.userType === 3 && !createForm.value.enterpriseId) {
    message.warning('请选择企业')
    return false
  }
  return true
}

const pagination = ref({
  page: 1,
  pageSize: 10,
  itemCount: 0,
  showSizePicker: true,
  pageSizes: [10, 20, 50]
})

const columns = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '用户名', key: 'username' },
  { title: '真实姓名', key: 'realName' },
  { title: '手机号', key: 'phone' },
  { title: '邮箱', key: 'email' },
  { 
    title: '用户类型', 
    key: 'userType',
    render: (row) => h(NTag, { type: userTypeColor[row.userType] }, { default: () => userTypeMap[row.userType] })
  },
  {
    title: '状态',
    key: 'status',
    render: (row) => h(NTag, { type: row.status === 1 ? 'success' : 'error' }, { default: () => row.status === 1 ? '启用' : '禁用' })
  },
  { title: '角色', key: 'roleName' },
  {
    title: '操作',
    key: 'actions',
    width: 280,
    render: (row) => h(NSpace, null, {
      default: () => [
        h(NButton, { size: 'small', onClick: () => handleStatusChange(row) }, { default: () => row.status === 1 ? '禁用' : '启用' }),
        h(NButton, { size: 'small', type: 'primary', onClick: () => openRoleModal(row) }, { default: () => '分配角色' }),
        h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row) }, { default: () => '删除' })
      ]
    })
  }
]

const loadData = async () => {
  loading.value = true
  try {
    const res = await getUserPage({
      pageNum: pagination.value.page,
      pageSize: pagination.value.pageSize,
      keyword: searchKey.value,
      status: statusFilter.value
    })
    if (res.code === 200) {
      tableData.value = res.data.records
      pagination.value.itemCount = res.data.total
    }
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page) => {
  pagination.value.page = page
  loadData()
}

const handleStatusChange = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  const res = await updateUserStatus(row.id, newStatus)
  if (res.code === 200) {
    message.success('操作成功')
    loadData()
  } else {
    message.error(res.message)
  }
}

const handleDelete = (row) => {
  dialog.warning({
    title: '确认删除',
    content: `确定要删除用户 "${row.realName}" 吗？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      const res = await deleteUser(row.id)
      if (res.code === 200) {
        message.success('删除成功')
        loadData()
      } else {
        message.error(res.message || '删除失败')
      }
    }
  })
}

const openRoleModal = async (row) => {
  roleForm.value.userId = row.id
  roleForm.value.roleId = null
  showRoleModal.value = true
  const res = await getRoles()
  if (res.code === 200) {
    roleOptions.value = res.data
  }
}

const handleAssignRole = async () => {
  if (!roleForm.value.roleId) {
    message.warning('请选择角色')
    return
  }
  const res = await assignRole(roleForm.value.userId, roleForm.value.roleId)
  if (res.code === 200) {
    message.success('分配成功')
    showRoleModal.value = false
    loadData()
  } else {
    message.error(res.message)
  }
}

const openCreateModal = async () => {
  createForm.value = {
    username: '',
    password: '',
    realName: '',
    phone: '',
    email: '',
    userType: null,
    studentNo: '',
    className: '',
    major: '',
    college: '',
    teacherNo: '',
    department: '',
    enterpriseId: null,
    position: ''
  }
  showCreateModal.value = true
  
  const res = await getEnterprisePage({ pageNum: 1, pageSize: 100 })
  if (res.code === 200) {
    enterpriseOptions.value = res.data.records
  }
}

const handleCreate = async () => {
  await createFormRef.value?.validate()
  if (!validateEnterprise()) return
  createLoading.value = true
  try {
    const res = await createUser(createForm.value)
    if (res.code === 200) {
      message.success('创建成功')
      showCreateModal.value = false
      loadData()
    } else {
      message.error(res.message || '创建失败')
    }
  } catch (error) {
    message.error(error.message || '创建失败')
  } finally {
    createLoading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>
