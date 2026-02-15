<template>
  <div class="weekly-page">
    <n-card title="周报管理">
      <template #header-extra>
        <n-space>
          <n-button v-if="userType === 1" type="primary" @click="openSubmitModal">提交周报</n-button>
        </n-space>
      </template>
      <n-data-table :columns="columns" :data="tableData" :loading="loading" :pagination="pagination" remote @update:page="handlePageChange" />
    </n-card>
    
    <n-modal v-model:show="showSubmitModal" preset="dialog" title="提交周报">
      <n-form ref="formRef" :model="submitForm" :rules="rules" label-placement="left" label-width="100">
        <n-form-item label="实习申请" path="applicationId">
          <n-select v-model:value="submitForm.applicationId" :options="applicationOptions" label-field="positionName" value-field="id" />
        </n-form-item>
        <n-form-item label="周次" path="weekNumber">
          <n-input-number v-model:value="submitForm.weekNumber" :min="1" :max="20" style="width: 100%" />
        </n-form-item>
        <n-form-item label="周报内容" path="content">
          <n-input v-model:value="submitForm.content" type="textarea" placeholder="请输入周报内容" :rows="6" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-button @click="showSubmitModal = false">取消</n-button>
        <n-button type="primary" @click="handleSubmit">提交</n-button>
      </template>
    </n-modal>
    
    <n-modal v-model:show="showCommentModal" preset="dialog" title="评阅周报">
      <n-descriptions :column="1" bordered>
        <n-descriptions-item label="学生">{{ currentReport.studentName }}</n-descriptions-item>
        <n-descriptions-item label="周次">第{{ currentReport.weekNumber }}周</n-descriptions-item>
        <n-descriptions-item label="周报内容">{{ currentReport.content }}</n-descriptions-item>
      </n-descriptions>
      <n-divider />
      <n-form :model="commentForm">
        <n-form-item label="评语">
          <n-input v-model:value="commentForm.comment" type="textarea" placeholder="请输入评语" :rows="4" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-button @click="showCommentModal = false">取消</n-button>
        <n-button type="primary" @click="handleComment">提交评语</n-button>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, h, onMounted, computed } from 'vue'
import { NButton, NSpace, useMessage } from 'naive-ui'
import { getWeeklyReportPage, getMyWeeklyReports, submitWeeklyReport, commentWeeklyReport } from '@/api/application'
import { getMyApplications } from '@/api/application'
import { useUserStore } from '@/stores/user'

const message = useMessage()
const userStore = useUserStore()
const userType = computed(() => userStore.userType)

const loading = ref(false)
const tableData = ref([])
const showSubmitModal = ref(false)
const showCommentModal = ref(false)
const formRef = ref(null)
const applicationOptions = ref([])
const currentReport = ref({})

const submitForm = ref({
  applicationId: null,
  weekNumber: 1,
  content: ''
})

const commentForm = ref({
  id: null,
  comment: ''
})

const rules = {
  applicationId: [{ required: true, type: 'number', message: '请选择实习申请', trigger: ['blur', 'change'] }],
  weekNumber: [{ required: true, type: 'number', message: '请输入周次', trigger: ['blur', 'change'] }],
  content: [{ required: true, message: '请输入周报内容', trigger: 'blur' }]
}

const pagination = ref({
  page: 1,
  pageSize: 10,
  itemCount: 0
})

const columns = computed(() => {
  const baseColumns = [
    { title: 'ID', key: 'id', width: 80 },
    { title: '学生姓名', key: 'studentName' },
    { title: '周次', key: 'weekNumber', render: (row) => `第${row.weekNumber}周` },
    { title: '周报内容', key: 'content', ellipsis: { tooltip: true } },
    { title: '教师评语', key: 'teacherComment', ellipsis: { tooltip: true } },
    { title: '评阅时间', key: 'commentTime' }
  ]
  
  if (userType.value === 2) {
    baseColumns.push({
      title: '操作',
      key: 'actions',
      width: 100,
      render: (row) => h(NButton, { size: 'small', onClick: () => openCommentModal(row) }, { default: () => '评阅' })
    })
  }
  
  return baseColumns
})

const loadData = async () => {
  loading.value = true
  try {
    const api = userType.value === 1 ? getMyWeeklyReports : getWeeklyReportPage
    const res = await api({
      pageNum: pagination.value.page,
      pageSize: pagination.value.pageSize
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

const openSubmitModal = async () => {
  const res = await getMyApplications({ pageNum: 1, pageSize: 100, status: 1 })
  if (res.code === 200) {
    applicationOptions.value = res.data.records
  }
  submitForm.value = { applicationId: null, weekNumber: 1, content: '' }
  showSubmitModal.value = true
}

const handleSubmit = async () => {
  await formRef.value?.validate()
  const res = await submitWeeklyReport(submitForm.value.applicationId, submitForm.value.weekNumber, submitForm.value.content)
  if (res.code === 200) {
    message.success('提交成功')
    showSubmitModal.value = false
    loadData()
  } else {
    message.error(res.message)
  }
}

const openCommentModal = (row) => {
  currentReport.value = row
  commentForm.value = { id: row.id, comment: row.teacherComment || '' }
  showCommentModal.value = true
}

const handleComment = async () => {
  const res = await commentWeeklyReport(commentForm.value.id, commentForm.value.comment)
  if (res.code === 200) {
    message.success('评阅成功')
    showCommentModal.value = false
    loadData()
  } else {
    message.error(res.message)
  }
}

onMounted(() => {
  loadData()
})
</script>
