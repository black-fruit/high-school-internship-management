import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/register/index.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/index.vue'),
        meta: { title: '用户管理', roles: ['admin'] }
      },
      {
        path: 'enterprise',
        name: 'Enterprise',
        component: () => import('@/views/enterprise/index.vue'),
        meta: { title: '企业管理' }
      },
      {
        path: 'plan',
        name: 'Plan',
        component: () => import('@/views/plan/index.vue'),
        meta: { title: '实习计划' }
      },
      {
        path: 'position',
        name: 'Position',
        component: () => import('@/views/position/index.vue'),
        meta: { title: '实习岗位' }
      },
      {
        path: 'application',
        name: 'Application',
        component: () => import('@/views/application/index.vue'),
        meta: { title: '实习申请' }
      },
      {
        path: 'weekly',
        name: 'Weekly',
        component: () => import('@/views/weekly/index.vue'),
        meta: { title: '周报管理' }
      },
      {
        path: 'report',
        name: 'Report',
        component: () => import('@/views/report/index.vue'),
        meta: { title: '实习报告' }
      },
      {
        path: 'score',
        name: 'Score',
        component: () => import('@/views/score/index.vue'),
        meta: { title: '成绩管理' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 高校实习管理系统` : '高校实习管理系统'
  
  const userStore = useUserStore()
  const token = userStore.token || localStorage.getItem('token')
  
  if (!token && to.path !== '/login' && to.path !== '/register') {
    next('/login')
  } else if (token && (to.path === '/login' || to.path === '/register')) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
