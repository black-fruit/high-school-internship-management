<template>
  <div class="dashboard">
    <n-grid :cols="4" :x-gap="16" :y-gap="16">
      <n-gi>
        <n-card>
          <n-statistic label="学生总数" :value="statistics.studentCount">
            <template #prefix>
              <n-icon :component="PeopleOutline" />
            </template>
          </n-statistic>
        </n-card>
      </n-gi>
      <n-gi>
        <n-card>
          <n-statistic label="教师总数" :value="statistics.teacherCount">
            <template #prefix>
              <n-icon :component="PersonOutline" />
            </template>
          </n-statistic>
        </n-card>
      </n-gi>
      <n-gi>
        <n-card>
          <n-statistic label="合作企业" :value="statistics.enterpriseCount">
            <template #prefix>
              <n-icon :component="BusinessOutline" />
            </template>
          </n-statistic>
        </n-card>
      </n-gi>
      <n-gi>
        <n-card>
          <n-statistic label="实习岗位" :value="statistics.positionCount">
            <template #prefix>
              <n-icon :component="BriefcaseOutline" />
            </template>
          </n-statistic>
        </n-card>
      </n-gi>
    </n-grid>
    
    <n-card title="欢迎使用高校实习管理系统" style="margin-top: 16px">
      <n-p>本系统为高校实习管理提供一站式解决方案，支持学生、教师、企业和管理员四种角色。</n-p>
      <n-h3>功能介绍</n-h3>
      <n-ul>
        <n-li><strong>学生</strong>：浏览实习岗位、提交实习申请、填写周报、提交实习报告、查看成绩</n-li>
        <n-li><strong>教师</strong>：管理实习计划、审核实习申请、批阅周报、评定成绩</n-li>
        <n-li><strong>企业</strong>：发布实习岗位、筛选学生简历、评定学生成绩</n-li>
        <n-li><strong>管理员</strong>：用户管理、企业审核、系统配置</n-li>
      </n-ul>
    </n-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { PeopleOutline, PersonOutline, BusinessOutline, BriefcaseOutline } from '@vicons/ionicons5'

const statistics = ref({
  studentCount: 0,
  teacherCount: 0,
  enterpriseCount: 1,
  positionCount: 1
})

onMounted(() => {
  statistics.value = {
    studentCount: 1,
    teacherCount: 1,
    enterpriseCount: 1,
    positionCount: 1
  }
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}
</style>
