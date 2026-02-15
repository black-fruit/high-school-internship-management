<template>
  <div class="enterprise-page">
    <n-card title="企业管理">
      <template #header-extra>
        <n-space>
          <n-input v-model:value="searchKey" placeholder="搜索企业名称" clearable @keyup.enter="loadData" />
          <n-select v-model:value="statusFilter" :options="statusOptions" placeholder="审核状态" clearable style="width: 120px" />
          <n-button type="primary" @click="loadData">搜索</n-button>
        </n-space>
      </template>
      <n-data-table :columns="columns" :data="tableData" :loading="loading" :pagination="pagination" remote @update:page="handlePageChange" />
    </n-card>
    
    <n-modal v-model:show="showAuditModal" preset="dialog" title="审核企业">
      <n-form :model="auditForm">
        <n-form-item label="审核结果">
          <n-radio-group v-model:value="auditForm.auditStatus">
            <n-radio :value="1">通过</n-radio>
            <n-radio :value="2">拒绝</n-radio>
          </n-radio-group>
        </n-form-item>
        <n-form-item label="审核备注">
          <n-input v-model:value="auditForm.auditRemark" type="textarea" placeholder="请输入审核备注" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-button @click="showAuditModal = false">取消</n-button>
        <n-button type="primary" @click="handleAudit">确定</n-button>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, h, onMounted } from 'vue'
import { NButton, NTag, NSpace, useMessage } from 'naive-ui'
import { getEnterprisePage, auditEnterprise } from '@/api/enterprise'

const message = useMessage()
const loading = ref(false)
const tableData = ref([])
const searchKey = ref('')
const statusFilter = ref(null)
const showAuditModal = ref(false)
const auditForm = ref({ id: null, auditStatus: 1, auditRemark: '' })

const statusOptions = [
  { label: '待审核', value: 0 },
  { label: '已通过', value: 1 },
  { label: '已拒绝', value: 2 }
]

const auditStatusMap = { 0: '待审核', 1: '已通过', 2: '已拒绝' }
const auditStatusColor = { 0: 'warning', 1: 'success', 2: 'error' }

const pagination = ref({
  page: 1,
  pageSize: 10,
  itemCount: 0
})

const columns = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '企业名称', key: 'enterpriseName' },
  { title: '信用代码', key: 'creditCode' },
  { title: '联系人', key: 'contactPerson' },
  { title: '联系电话', key: 'contactPhone' },
  { title: '邮箱', key: 'email' },
  {
    title: '审核状态',
    key: 'auditStatus',
    render: (row) => h(NTag, { type: auditStatusColor[row.auditStatus] }, { default: () => auditStatusMap[row.auditStatus] })
  },
  {
    title: '操作',
    key: 'actions',
    width: 120,
    render: (row) => row.auditStatus === 0 ? h(NButton, { size: 'small', type: 'primary', onClick: () => openAuditModal(row) }, { default: () => '审核' }) : null
  }
]

const loadData = async () => {
  loading.value = true
  try {
    const res = await getEnterprisePage({
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

const openAuditModal = (row) => {
  auditForm.value = { id: row.id, auditStatus: 1, auditRemark: '' }
  showAuditModal.value = true
}

const handleAudit = async () => {
  const res = await auditEnterprise(auditForm.value.id, auditForm.value.auditStatus, auditForm.value.auditRemark)
  if (res.code === 200) {
    message.success('审核成功')
    showAuditModal.value = false
    loadData()
  } else {
    message.error(res.message)
  }
}

onMounted(() => {
  loadData()
})
</script>
