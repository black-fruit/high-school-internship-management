<template>
  <div class="score-page">
    <n-card title="成绩管理">
      <template v-if="userType === 1">
        <n-descriptions v-if="currentScore" :column="2" bordered>
          <n-descriptions-item label="学生姓名">{{ currentScore.studentName }}</n-descriptions-item>
          <n-descriptions-item label="教师评分">{{ currentScore.teacherScore || '未评分' }}</n-descriptions-item>
          <n-descriptions-item label="企业评分">{{ currentScore.enterpriseScore || '未评分' }}</n-descriptions-item>
          <n-descriptions-item label="总成绩">{{ currentScore.totalScore || '未计算' }}</n-descriptions-item>
          <n-descriptions-item label="教师评语" :span="2">{{ currentScore.teacherComment || '无' }}</n-descriptions-item>
          <n-descriptions-item label="企业评语" :span="2">{{ currentScore.enterpriseComment || '无' }}</n-descriptions-item>
        </n-descriptions>
        <n-empty v-else description="暂无成绩信息" />
      </template>
      
      <template v-else>
        <n-form :model="searchForm" inline style="margin-bottom: 16px">
          <n-form-item label="学生姓名">
            <n-input v-model:value="searchForm.keyword" placeholder="搜索学生姓名" clearable />
          </n-form-item>
          <n-form-item>
            <n-button type="primary" @click="handleSearch">搜索</n-button>
          </n-form-item>
        </n-form>
        <n-data-table :columns="columns" :data="tableData" :loading="loading" :pagination="pagination" remote @update:page="handlePageChange" />
      </template>
    </n-card>
    
    <n-modal v-model:show="showScoreModal" preset="dialog" :title="scoreType === 'teacher' ? '教师评分' : '企业评分'" style="width: 400px">
      <n-form :model="scoreForm" label-placement="left" label-width="80">
        <n-form-item label="学生姓名">{{ currentApplication.studentName }}</n-form-item>
        <n-form-item label="岗位">{{ currentApplication.positionName }}</n-form-item>
        <n-form-item label="评分" required>
          <n-input-number v-model:value="scoreForm.score" :min="0" :max="100" :precision="1" style="width: 100%" />
        </n-form-item>
        <n-form-item label="评语">
          <n-input v-model:value="scoreForm.comment" type="textarea" placeholder="请输入评语" :rows="3" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-button @click="showScoreModal = false">取消</n-button>
        <n-button type="primary" :loading="scoreLoading" @click="handleSubmitScore">提交</n-button>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, h, onMounted, computed } from 'vue'
import { NButton, NTag, useMessage } from 'naive-ui'
import { getScore, teacherScore, enterpriseScore, getMyApplications, getTeacherApplications, getEnterpriseApplications, getApplicationPage } from '@/api/application'
import { useUserStore } from '@/stores/user'

const message = useMessage()
const userStore = useUserStore()
const userType = computed(() => userStore.userType)

const loading = ref(false)
const scoreLoading = ref(false)
const tableData = ref([])
const currentScore = ref(null)
const showScoreModal = ref(false)
const scoreType = ref('')
const currentApplication = ref({})
const scoreForm = ref({ score: null, comment: '' })
const searchForm = ref({ keyword: '' })

const pagination = ref({
  page: 1,
  pageSize: 10,
  itemCount: 0
})

const columns = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '学生姓名', key: 'studentName' },
  { title: '岗位名称', key: 'positionName' },
  { title: '企业名称', key: 'enterpriseName' },
  { 
    title: '教师评分', 
    key: 'teacherScore',
    render: (row) => row.teacherScore ? h(NTag, { type: 'success' }, { default: () => row.teacherScore }) : h(NTag, { type: 'default' }, { default: () => '未评分' })
  },
  { 
    title: '企业评分', 
    key: 'enterpriseScore',
    render: (row) => row.enterpriseScore ? h(NTag, { type: 'success' }, { default: () => row.enterpriseScore }) : h(NTag, { type: 'default' }, { default: () => '未评分' })
  },
  {
    title: '操作',
    key: 'actions',
    width: 180,
    render: (row) => {
      const buttons = []
      if (userType.value === 2 && !row.teacherScore) {
        buttons.push(h(NButton, { size: 'small', type: 'primary', onClick: () => openScoreModal(row, 'teacher') }, { default: () => '教师评分' }))
      }
      if (userType.value === 3 && !row.enterpriseScore) {
        buttons.push(h(NButton, { size: 'small', type: 'primary', onClick: () => openScoreModal(row, 'enterprise') }, { default: () => '企业评分' }))
      }
      if (userType.value === 4) {
        if (!row.teacherScore) {
          buttons.push(h(NButton, { size: 'small', type: 'primary', onClick: () => openScoreModal(row, 'teacher') }, { default: () => '教师评分' }))
        }
        if (!row.enterpriseScore) {
          buttons.push(h(NButton, { size: 'small', type: 'primary', onClick: () => openScoreModal(row, 'enterprise') }, { default: () => '企业评分' }))
        }
      }
      return buttons.length > 0 ? buttons : h(NTag, { type: 'info' }, { default: () => '已评分' })
    }
  }
]

const handleSearch = () => {
  pagination.value.page = 1
  loadData()
}

const loadData = async () => {
  if (userType.value === 1) {
    const res = await getMyApplications({ pageNum: 1, pageSize: 1, status: 1 })
    if (res.code === 200 && res.data.records.length > 0) {
      const scoreRes = await getScore(res.data.records[0].id)
      if (scoreRes.code === 200) {
        currentScore.value = scoreRes.data
      }
    }
  } else {
    loading.value = true
    try {
      let api
      if (userType.value === 2) {
        api = getTeacherApplications
      } else if (userType.value === 3) {
        api = getEnterpriseApplications
      } else if (userType.value === 4) {
        api = getApplicationPage
      }
      const res = await api({
        pageNum: pagination.value.page,
        pageSize: pagination.value.pageSize,
        status: 1,
        keyword: searchForm.value.keyword
      })
      if (res.code === 200) {
        tableData.value = res.data.records
        pagination.value.itemCount = res.data.total
      }
    } finally {
      loading.value = false
    }
  }
}

const handlePageChange = (page) => {
  pagination.value.page = page
  loadData()
}

const openScoreModal = (row, type) => {
  currentApplication.value = row
  scoreType.value = type
  scoreForm.value = { score: null, comment: '' }
  showScoreModal.value = true
}

const handleSubmitScore = async () => {
  if (!scoreForm.value.score && scoreForm.value.score !== 0) {
    message.warning('请输入评分')
    return
  }
  scoreLoading.value = true
  try {
    let res
    if (scoreType.value === 'teacher') {
      res = await teacherScore(currentApplication.value.id, scoreForm.value.score, scoreForm.value.comment)
    } else {
      res = await enterpriseScore(currentApplication.value.id, scoreForm.value.score, scoreForm.value.comment)
    }
    if (res.code === 200) {
      message.success('评分成功')
      showScoreModal.value = false
      loadData()
    } else {
      message.error(res.message || '评分失败')
    }
  } catch (error) {
    message.error(error.message || '评分失败')
  } finally {
    scoreLoading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>
