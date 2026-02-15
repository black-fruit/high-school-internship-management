<template>
  <div class="plan-page">
    <n-card title="实习计划管理">
      <template #header-extra>
        <n-space>
          <n-input v-model:value="searchKey" placeholder="搜索计划名称" clearable @keyup.enter="loadData" />
          <n-button type="primary" @click="loadData">搜索</n-button>
          <n-button type="primary" @click="openCreateModal">新增计划</n-button>
        </n-space>
      </template>
      <n-data-table :columns="columns" :data="tableData" :loading="loading" :pagination="pagination" remote @update:page="handlePageChange" />
    </n-card>
    
    <n-modal v-model:show="showCreateModal" preset="dialog" title="新增实习计划">
      <n-form ref="formRef" :model="planForm" :rules="rules" label-placement="left" label-width="100">
        <n-form-item label="计划名称" path="planName">
          <n-input v-model:value="planForm.planName" placeholder="请输入计划名称" />
        </n-form-item>
        <n-form-item label="学院名称" path="collegeName">
          <n-input v-model:value="planForm.collegeName" placeholder="请输入学院名称" />
        </n-form-item>
        <n-form-item label="专业" path="major">
          <n-input v-model:value="planForm.major" placeholder="请输入专业" />
        </n-form-item>
        <n-form-item label="开始日期" path="startDate">
          <n-date-picker v-model:value="planForm.startDateTs" type="date" style="width: 100%" />
        </n-form-item>
        <n-form-item label="结束日期" path="endDate">
          <n-date-picker v-model:value="planForm.endDateTs" type="date" style="width: 100%" />
        </n-form-item>
        <n-form-item label="计划描述" path="description">
          <n-input v-model:value="planForm.description" type="textarea" placeholder="请输入计划描述" />
        </n-form-item>
        <n-form-item label="实习要求" path="requirements">
          <n-input v-model:value="planForm.requirements" type="textarea" placeholder="请输入实习要求" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-button @click="showCreateModal = false">取消</n-button>
        <n-button type="primary" @click="handleCreate">确定</n-button>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, h, onMounted } from 'vue'
import { NButton, NTag, NSpace, useMessage } from 'naive-ui'
import { getPlanPage, createPlan, updatePlanStatus } from '@/api/internship'

const message = useMessage()
const loading = ref(false)
const tableData = ref([])
const searchKey = ref('')
const showCreateModal = ref(false)
const formRef = ref(null)

const planForm = ref({
  planName: '',
  collegeName: '',
  major: '',
  startDateTs: null,
  endDateTs: null,
  description: '',
  requirements: ''
})

const rules = {
  planName: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
  collegeName: [{ required: true, message: '请输入学院名称', trigger: 'blur' }],
  startDateTs: [{ required: true, type: 'number', message: '请选择开始日期', trigger: ['blur', 'change'] }],
  endDateTs: [{ required: true, type: 'number', message: '请选择结束日期', trigger: ['blur', 'change'] }]
}

const statusMap = { 0: '草稿', 1: '进行中', 2: '已结束' }
const statusColor = { 0: 'default', 1: 'success', 2: 'info' }

const pagination = ref({
  page: 1,
  pageSize: 10,
  itemCount: 0
})

const columns = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '计划名称', key: 'planName' },
  { title: '学院', key: 'collegeName' },
  { title: '专业', key: 'major' },
  { title: '开始日期', key: 'startDate' },
  { title: '结束日期', key: 'endDate' },
  {
    title: '状态',
    key: 'status',
    render: (row) => {
      const today = new Date().toISOString().split('T')[0]
      const isExpired = row.endDate && row.endDate < today && row.status === 1
      if (isExpired) {
        return h(NTag, { type: 'warning' }, { default: () => '已过期' })
      }
      return h(NTag, { type: statusColor[row.status] }, { default: () => statusMap[row.status] })
    }
  },
  {
    title: '操作',
    key: 'actions',
    width: 180,
    render: (row) => {
      const today = new Date().toISOString().split('T')[0]
      const isExpired = row.endDate && row.endDate < today && row.status === 1
      if (isExpired) {
        return h(NButton, { size: 'small', onClick: () => handleStatusChange(row.id, 2) }, { default: () => '结束' })
      }
      return h(NSpace, null, {
        default: () => [
          row.status === 0 ? h(NButton, { size: 'small', type: 'primary', onClick: () => handleStatusChange(row.id, 1) }, { default: () => '发布' }) : null,
          row.status === 1 ? h(NButton, { size: 'small', onClick: () => handleStatusChange(row.id, 2) }, { default: () => '结束' }) : null
        ]
      })
    }
  }
]

const loadData = async () => {
  loading.value = true
  try {
    const res = await getPlanPage({
      pageNum: pagination.value.page,
      pageSize: pagination.value.pageSize,
      keyword: searchKey.value
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

const openCreateModal = () => {
  planForm.value = {
    planName: '',
    collegeName: '',
    major: '',
    startDateTs: null,
    endDateTs: null,
    description: '',
    requirements: ''
  }
  showCreateModal.value = true
}

const handleCreate = async () => {
  await formRef.value?.validate()
  const data = {
    ...planForm.value,
    startDate: planForm.value.startDateTs ? new Date(planForm.value.startDateTs).toISOString().split('T')[0] : null,
    endDate: planForm.value.endDateTs ? new Date(planForm.value.endDateTs).toISOString().split('T')[0] : null
  }
  delete data.startDateTs
  delete data.endDateTs
  
  const res = await createPlan(data)
  if (res.code === 200) {
    message.success('创建成功')
    showCreateModal.value = false
    loadData()
  } else {
    message.error(res.message)
  }
}

const handleStatusChange = async (id, status) => {
  const res = await updatePlanStatus(id, status)
  if (res.code === 200) {
    message.success('操作成功')
    loadData()
  } else {
    message.error(res.message)
  }
}

onMounted(() => {
  loadData()
})
</script>
