<template>
  <div class="report-page">
    <n-card title="实习报告">
      <template #header-extra>
        <n-space>
          <n-button v-if="userType === 1" type="primary" @click="openSubmitModal">提交报告</n-button>
        </n-space>
      </template>
      <n-form v-if="userType !== 1" :model="searchForm" inline>
        <n-form-item label="实习申请ID">
          <n-input v-model:value="searchForm.applicationId" placeholder="请输入申请ID" />
        </n-form-item>
        <n-form-item>
          <n-button type="primary" @click="loadReport">查询</n-button>
        </n-form-item>
      </n-form>
      
      <template v-if="currentReport">
        <n-descriptions :column="1" bordered>
          <n-descriptions-item label="学生姓名">{{ currentReport.studentName }}</n-descriptions-item>
          <n-descriptions-item label="报告标题">{{ currentReport.title }}</n-descriptions-item>
          <n-descriptions-item label="报告内容">
            <div style="white-space: pre-wrap">{{ currentReport.content }}</div>
          </n-descriptions-item>
          <n-descriptions-item label="提交时间">{{ currentReport.submitTime }}</n-descriptions-item>
        </n-descriptions>
      </template>
      <n-empty v-else description="暂无实习报告" />
    </n-card>
    
    <n-modal v-model:show="showSubmitModal" preset="dialog" title="提交实习报告" style="width: 600px">
      <n-form ref="formRef" :model="submitForm" :rules="rules" label-placement="left" label-width="100">
        <n-form-item label="实习申请" path="applicationId">
          <n-select v-model:value="submitForm.applicationId" :options="applicationOptions" label-field="positionName" value-field="id" />
        </n-form-item>
        <n-form-item label="报告标题" path="title">
          <n-input v-model:value="submitForm.title" placeholder="请输入报告标题" />
        </n-form-item>
        <n-form-item label="报告内容" path="content">
          <n-input v-model:value="submitForm.content" type="textarea" placeholder="请输入报告内容" :rows="10" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-button @click="showSubmitModal = false">取消</n-button>
        <n-button type="primary" @click="handleSubmit">提交</n-button>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useMessage } from 'naive-ui'
import { getInternshipReport, submitInternshipReport, getMyApplications } from '@/api/application'
import { useUserStore } from '@/stores/user'

const message = useMessage()
const userStore = useUserStore()
const userType = computed(() => userStore.userType)

const currentReport = ref(null)
const showSubmitModal = ref(false)
const formRef = ref(null)
const applicationOptions = ref([])
const searchForm = ref({ applicationId: '' })

const submitForm = ref({
  applicationId: null,
  title: '',
  content: ''
})

const rules = {
  applicationId: [{ required: true, type: 'number', message: '请选择实习申请', trigger: ['blur', 'change'] }],
  title: [{ required: true, message: '请输入报告标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入报告内容', trigger: 'blur' }]
}

const loadReport = async () => {
  if (!searchForm.value.applicationId) {
    message.warning('请输入申请ID')
    return
  }
  const res = await getInternshipReport(searchForm.value.applicationId)
  if (res.code === 200) {
    currentReport.value = res.data
  }
}

const openSubmitModal = async () => {
  const res = await getMyApplications({ pageNum: 1, pageSize: 100, status: 1 })
  if (res.code === 200) {
    applicationOptions.value = res.data.records
  }
  submitForm.value = { applicationId: null, title: '', content: '' }
  showSubmitModal.value = true
}

const handleSubmit = async () => {
  await formRef.value?.validate()
  const res = await submitInternshipReport(submitForm.value.applicationId, submitForm.value.title, submitForm.value.content, null)
  if (res.code === 200) {
    message.success('提交成功')
    showSubmitModal.value = false
    searchForm.value.applicationId = submitForm.value.applicationId
    loadReport()
  } else {
    message.error(res.message)
  }
}

onMounted(() => {
  if (userType.value === 1) {
    getMyApplications({ pageNum: 1, pageSize: 1, status: 1 }).then(res => {
      if (res.code === 200 && res.data.records.length > 0) {
        searchForm.value.applicationId = res.data.records[0].id
        loadReport()
      }
    })
  }
})
</script>
