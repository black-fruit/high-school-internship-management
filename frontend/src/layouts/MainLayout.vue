<template>
  <n-layout has-sider style="height: 100vh">
    <n-layout-sider
      bordered
      collapse-mode="width"
      :collapsed-width="64"
      :width="220"
      :collapsed="collapsed"
      show-trigger
      @collapse="collapsed = true"
      @expand="collapsed = false"
    >
      <div class="logo">
        <span v-if="!collapsed">高校实习管理系统</span>
        <span v-else>实习</span>
      </div>
      <n-menu
        :collapsed="collapsed"
        :collapsed-width="64"
        :collapsed-icon-size="22"
        :options="menuOptions"
        :value="activeKey"
        @update:value="handleMenuSelect"
      />
    </n-layout-sider>
    <n-layout>
      <n-layout-header bordered class="header">
        <div class="header-left">
          <n-breadcrumb>
            <n-breadcrumb-item>首页</n-breadcrumb-item>
            <n-breadcrumb-item>{{ currentTitle }}</n-breadcrumb-item>
          </n-breadcrumb>
        </div>
        <div class="header-right">
          <n-dropdown :options="userOptions" @select="handleUserSelect">
            <n-button text>
              <template #icon>
                <n-icon><PersonOutline /></n-icon>
              </template>
              {{ userStore.userInfo?.realName || userStore.userInfo?.username || '用户' }}
            </n-button>
          </n-dropdown>
        </div>
      </n-layout-header>
      <n-layout-content class="content">
        <router-view />
      </n-layout-content>
    </n-layout>
  </n-layout>
</template>

<script setup>
import { ref, computed, h } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { NIcon } from 'naive-ui'
import {
  PersonOutline,
  HomeOutline,
  PeopleOutline,
  BusinessOutline,
  CalendarOutline,
  BriefcaseOutline,
  DocumentTextOutline,
  NewspaperOutline,
  StatsChartOutline,
  SettingsOutline,
  LogOutOutline
} from '@vicons/ionicons5'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const collapsed = ref(false)

const activeKey = computed(() => route.name)

const currentTitle = computed(() => route.meta.title || '')

const renderIcon = (icon) => {
  return () => h(NIcon, null, { default: () => h(icon) })
}

const menuOptions = computed(() => {
  const userType = userStore.userType
  const items = []
  
  items.push({
    label: '首页',
    key: 'Dashboard',
    icon: renderIcon(HomeOutline)
  })
  
  if (userType === 4) {
    items.push({
      label: '用户管理',
      key: 'User',
      icon: renderIcon(PeopleOutline)
    })
  }
  
  if (userType === 4 || userType === 2) {
    items.push({
      label: '企业管理',
      key: 'Enterprise',
      icon: renderIcon(BusinessOutline)
    })
  }
  
  if (userType === 4 || userType === 2) {
    items.push({
      label: '实习计划',
      key: 'Plan',
      icon: renderIcon(CalendarOutline)
    })
  }
  
  items.push({
    label: '实习岗位',
    key: 'Position',
    icon: renderIcon(BriefcaseOutline)
  })
  
  items.push({
    label: '实习申请',
    key: 'Application',
    icon: renderIcon(DocumentTextOutline)
  })
  
  items.push({
    label: '周报管理',
    key: 'Weekly',
    icon: renderIcon(NewspaperOutline)
  })
  
  items.push({
    label: '实习报告',
    key: 'Report',
    icon: renderIcon(DocumentTextOutline)
  })
  
  items.push({
    label: '成绩管理',
    key: 'Score',
    icon: renderIcon(StatsChartOutline)
  })
  
  return items
})

const userOptions = [
  { label: '个人中心', key: 'profile' },
  { label: '退出登录', key: 'logout' }
]

const handleMenuSelect = (key) => {
  router.push({ name: key })
}

const handleUserSelect = (key) => {
  if (key === 'logout') {
    userStore.logout()
    router.push('/login')
  } else if (key === 'profile') {
    router.push('/profile')
  }
}
</script>

<style scoped>
.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: bold;
  color: #18a058;
  border-bottom: 1px solid #e0e0e6;
}

.header {
  height: 64px;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-right {
  display: flex;
  align-items: center;
}

.content {
  padding: 24px;
  background-color: #f5f7f9;
  min-height: calc(100vh - 64px);
}
</style>
