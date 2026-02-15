<template>
  <div class="application-page">
    <n-card title="实习申请管理">
      <template #header-extra>
        <n-space>
          <n-input v-model:value="searchKey" placeholder="搜索" clearable @keyup.enter="loadData" />
          <n-select v-model:value="statusFilter" :options="statusOptions" placeholder="状态筛选" clearable style="width: 120px" />
          <n-button type="primary" @click="loadData">搜索</n-button>
        </n-space>
      </template>
      <n-data-table :columns="columns" :data="tableData" :loading="loading" :pagination="pagination" remote @update:page="handlePageChange" />
    </n-card>
    
    <n-modal v-model:show="showRejectModal" preset="dialog" title="拒绝申请">
      <n-form :model="rejectForm">
        <n-form-item label="拒绝原因">
          <n-input v-model:value="rejectForm.rejectReason" type="textarea" placeholder="请输入拒绝原因" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-button @click="showRejectModal = false">取消</n-button>
        <n-button type="primary" @click="handleReject">确定</n-button>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, h, onMounted, computed } from 'vue'
import { NButton, NTag, NSpace, useMessage } from 'naive-ui'
import { getApplicationPage, getMyApplications, getEnterpriseApplications, getTeacherApplications, approveApplication, rejectApplication } from '@/api/application'
import { useUserStore } from '@/stores/user'

const message = useMessage()
const userStore = useUserStore()
const userType = computed(() => userStore.userType)

const loading = ref(false)
const tableData = ref([])
const searchKey = ref('')
const statusFilter = ref(null)
const showRejectModal = ref(false)
const rejectForm = ref({ id: null, rejectReason: '' })

const statusOptions = [
  { label: '待审核', value: 0 },
  { label: '已通过', value: 1 },
  { label: '已拒绝', value: 2 }
]

const statusMap = { 0: '待审核', 1: '已通过', 2: '已拒绝' }
const statusColor = { 0: 'warning', 1: 'success', 2: 'error' }

const pagination = ref({
  page: 1,
  pageSize: 10,
  itemCount: 0
})

const columns = computed(() => {
  const baseColumns = [
    { title: 'ID', key: 'id', width: 80 },
    { title: '学生姓名', key: 'studentName' },
    { title: '岗位名称', key: 'positionName' },
    { title: '企业名称', key: 'enterpriseName' },
    {
      title: '状态',
      key: 'status',
      render: (row) => h(NTag, { type: statusColor[row.status] }, { default: () => statusMap[row.status] })
    },
    { title: '指导教师', key: 'teacherName' }
  ]
  
  if (userType.value !== 1) {
    baseColumns.push({
      title: '操作',
      key: 'actions',
      width: 180,
      render: (row) => {
        if (row.status !== 0) return null
        return h(NSpace, null, {
          default: () => [
            h(NButton, { size: 'small', type: 'primary', onClick: () => handleApprove(row.id) }, { default: () => '通过' }),
            h(NButton, { size: 'small', type: 'error', onClick: () => openRejectModal(row.id) }, { default: () => '拒绝' })
          ]
        })
      }
    })
  }
  
  return baseColumns
})

const loadData = async () => {
  loading.value = true
  try {
    let api
    if (userType.value === 1) {
      api = getMyApplications
    } else if (userType.value === 3) {
      api = getEnterpriseApplications
    } else if (userType.value === 2) {
      api = getTeacherApplications
    } else {
      api = getApplicationPage
    }
    
    const res = await api({
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

const handleApprove = async (id) => {
  const res = await approveApplication(id)
  if (res.code === 200) {
    message.success('审核通过')
    loadData()
  } else {
    message.error(res.message)
  }
}

const openRejectModal = (id) => {
  rejectForm.value = { id, rejectReason: '' }
  showRejectModal.value = true
}

const handleReject = async () => {
  if (!rejectForm.value.rejectReason) {
    message.warning('请输入拒绝原因')
    return
  }
  const res = await rejectApplication(rejectForm.value.id, rejectForm.value.rejectReason)
  if (res.code === 200) {
    message.success('已拒绝')
    showRejectModal.value = false
    loadData()
  } else {
    message.error(res.message)
  }
}

onMounted(() => {
  loadData()
})
</script>
