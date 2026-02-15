<template>
  <div class="position-page">
    <n-card title="实习岗位">
      <template #header-extra>
        <n-space>
          <n-input v-model:value="searchKey" placeholder="搜索岗位名称" clearable @keyup.enter="loadData" />
          <n-button type="primary" @click="loadData">搜索</n-button>
          <n-button v-if="userType === 3" type="primary" @click="openCreateModal">发布岗位</n-button>
        </n-space>
      </template>
      <n-data-table :columns="columns" :data="tableData" :loading="loading" :pagination="pagination" remote @update:page="handlePageChange" />
    </n-card>
    
    <n-modal v-model:show="showDetailModal" preset="card" title="岗位详情" style="width: 600px">
      <n-descriptions :column="1" bordered>
        <n-descriptions-item label="岗位名称">{{ currentPosition.positionName }}</n-descriptions-item>
        <n-descriptions-item label="企业名称">{{ currentPosition.enterpriseName }}</n-descriptions-item>
        <n-descriptions-item label="岗位描述">{{ currentPosition.description }}</n-descriptions-item>
        <n-descriptions-item label="岗位要求">{{ currentPosition.requirements }}</n-descriptions-item>
        <n-descriptions-item label="专业要求">{{ currentPosition.majorRequired }}</n-descriptions-item>
        <n-descriptions-item label="招聘人数">{{ currentPosition.recruitCount }}</n-descriptions-item>
        <n-descriptions-item label="已申请">{{ currentPosition.appliedCount }}</n-descriptions-item>
        <n-descriptions-item label="实习时间">{{ currentPosition.startDate }} ~ {{ currentPosition.endDate }}</n-descriptions-item>
        <n-descriptions-item label="状态">
          <n-tag v-if="isPositionExpired" type="warning">已过期</n-tag>
          <n-tag v-else :type="currentPosition.status === 1 ? 'success' : 'default'">
            {{ currentPosition.status === 1 ? '开放' : '关闭' }}
          </n-tag>
        </n-descriptions-item>
      </n-descriptions>
      <template #footer v-if="userType === 1 && !isPositionExpired && currentPosition.status === 1">
        <n-button type="primary" @click="openApplyModal">申请实习</n-button>
      </template>
    </n-modal>
    
    <n-modal v-model:show="showApplyModal" preset="dialog" title="申请实习">
      <n-form :model="applyForm">
        <n-form-item label="申请说明">
          <n-input v-model:value="applyForm.resume" type="textarea" placeholder="请输入您的申请说明/简历" :rows="5" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-button @click="showApplyModal = false">取消</n-button>
        <n-button type="primary" @click="handleApply">提交申请</n-button>
      </template>
    </n-modal>
    
    <n-modal v-model:show="showCreateModal" preset="dialog" title="发布岗位">
      <n-form ref="formRef" :model="positionForm" :rules="rules" label-placement="left" label-width="100">
        <n-form-item label="岗位名称" path="positionName">
          <n-input v-model:value="positionForm.positionName" placeholder="请输入岗位名称" />
        </n-form-item>
        <n-form-item label="岗位描述" path="description">
          <n-input v-model:value="positionForm.description" type="textarea" placeholder="请输入岗位描述" />
        </n-form-item>
        <n-form-item label="岗位要求" path="requirements">
          <n-input v-model:value="positionForm.requirements" type="textarea" placeholder="请输入岗位要求" />
        </n-form-item>
        <n-form-item label="专业要求" path="majorRequired">
          <n-input v-model:value="positionForm.majorRequired" placeholder="请输入专业要求" />
        </n-form-item>
        <n-form-item label="招聘人数" path="recruitCount">
          <n-input-number v-model:value="positionForm.recruitCount" :min="1" style="width: 100%" />
        </n-form-item>
        <n-form-item label="开始日期" path="startDate">
          <n-date-picker v-model:value="positionForm.startDateTs" type="date" style="width: 100%" />
        </n-form-item>
        <n-form-item label="结束日期" path="endDate">
          <n-date-picker v-model:value="positionForm.endDateTs" type="date" style="width: 100%" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-button @click="showCreateModal = false">取消</n-button>
        <n-button type="primary" @click="handleCreate">发布</n-button>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, h, onMounted, computed } from 'vue'
import { NButton, NTag, NSpace, useMessage, useDialog } from 'naive-ui'
import { getPositionPage, getAvailablePositions, getPosition, createPosition } from '@/api/internship'
import { applyPosition } from '@/api/application'
import { getCurrentEnterprise } from '@/api/enterprise'
import { useUserStore } from '@/stores/user'

const message = useMessage()
const dialog = useDialog()
const userStore = useUserStore()
const userType = computed(() => userStore.userType)

const loading = ref(false)
const tableData = ref([])
const searchKey = ref('')
const showDetailModal = ref(false)
const showApplyModal = ref(false)
const showCreateModal = ref(false)
const currentPosition = ref({})
const applyForm = ref({ resume: '' })
const formRef = ref(null)

const isPositionExpired = computed(() => {
  if (!currentPosition.value.endDate) return false
  const today = new Date().toISOString().split('T')[0]
  return currentPosition.value.endDate < today
})
const currentEnterprise = ref(null)

const positionForm = ref({
  positionName: '',
  description: '',
  requirements: '',
  majorRequired: '',
  recruitCount: 1,
  startDateTs: null,
  endDateTs: null
})

const rules = {
  positionName: [{ required: true, message: '请输入岗位名称', trigger: 'blur' }],
  recruitCount: [{ required: true, type: 'number', message: '请输入招聘人数', trigger: ['blur', 'change'] }],
  startDateTs: [{ required: true, type: 'number', message: '请选择开始日期', trigger: ['blur', 'change'] }],
  endDateTs: [{ required: true, type: 'number', message: '请选择结束日期', trigger: ['blur', 'change'] }]
}

const pagination = ref({
  page: 1,
  pageSize: 10,
  itemCount: 0
})

const columns = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '岗位名称', key: 'positionName' },
  { title: '企业名称', key: 'enterpriseName' },
  { title: '专业要求', key: 'majorRequired' },
  { title: '招聘人数', key: 'recruitCount' },
  { title: '已申请', key: 'appliedCount' },
  { title: '开始日期', key: 'startDate' },
  { title: '结束日期', key: 'endDate' },
  {
    title: '状态',
    key: 'status',
    render: (row) => {
      const today = new Date().toISOString().split('T')[0]
      const isExpired = row.endDate && row.endDate < today
      if (isExpired) {
        return h(NTag, { type: 'warning' }, { default: () => '已过期' })
      }
      return h(NTag, { type: row.status === 1 ? 'success' : 'default' }, { default: () => row.status === 1 ? '开放' : '关闭' })
    }
  },
  {
    title: '操作',
    key: 'actions',
    width: 100,
    render: (row) => h(NButton, { size: 'small', onClick: () => viewDetail(row.id) }, { default: () => '查看' })
  }
]

const loadData = async () => {
  loading.value = true
  try {
    const api = userType.value === 1 ? getAvailablePositions : getPositionPage
    const res = await api({
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

const viewDetail = async (id) => {
  const res = await getPosition(id)
  if (res.code === 200) {
    currentPosition.value = res.data
    showDetailModal.value = true
  }
}

const openApplyModal = () => {
  applyForm.value = { resume: '' }
  showApplyModal.value = true
}

const handleApply = async () => {
  if (!applyForm.value.resume) {
    message.warning('请填写申请说明')
    return
  }
  const res = await applyPosition(currentPosition.value.id, applyForm.value.resume)
  if (res.code === 200) {
    message.success('申请成功')
    showApplyModal.value = false
    showDetailModal.value = false
    loadData()
  } else {
    message.error(res.message)
  }
}

const openCreateModal = async () => {
  if (!currentEnterprise.value) {
    const res = await getCurrentEnterprise()
    if (res.code === 200) {
      currentEnterprise.value = res.data
    }
  }
  if (!currentEnterprise.value) {
    message.warning('您还不是企业用户，无法发布岗位')
    return
  }
  positionForm.value = {
    positionName: '',
    description: '',
    requirements: '',
    majorRequired: '',
    recruitCount: 1,
    startDateTs: null,
    endDateTs: null
  }
  showCreateModal.value = true
}

const handleCreate = async () => {
  await formRef.value?.validate()
  const data = {
    ...positionForm.value,
    enterpriseId: currentEnterprise.value.id,
    startDate: positionForm.value.startDateTs ? new Date(positionForm.value.startDateTs).toISOString().split('T')[0] : null,
    endDate: positionForm.value.endDateTs ? new Date(positionForm.value.endDateTs).toISOString().split('T')[0] : null
  }
  delete data.startDateTs
  delete data.endDateTs
  
  const res = await createPosition(data)
  if (res.code === 200) {
    message.success('发布成功')
    showCreateModal.value = false
    loadData()
  } else {
    message.error(res.message)
  }
}

onMounted(() => {
  loadData()
})
</script>
