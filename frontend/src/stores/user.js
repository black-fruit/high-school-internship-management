import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login, getCurrentUser } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)
  const userType = ref(localStorage.getItem('userType') ? Number(localStorage.getItem('userType')) : null)
  const roleName = ref(localStorage.getItem('roleName') || '')

  const setUser = (data) => {
    token.value = data.token
    userInfo.value = {
      userId: data.userId,
      username: data.username,
      realName: data.realName,
      userType: data.userType,
      roleName: data.roleName
    }
    userType.value = data.userType
    roleName.value = data.roleName
    localStorage.setItem('token', data.token)
    localStorage.setItem('userType', data.userType)
    localStorage.setItem('roleName', data.roleName)
  }

  const loginAction = async (loginForm) => {
    const res = await login(loginForm)
    if (res.code === 200) {
      setUser(res.data)
      return res
    }
    throw new Error(res.message)
  }

  const getUserInfo = async () => {
    const res = await getCurrentUser()
    if (res.code === 200) {
      userInfo.value = res.data
    }
    return res
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    userType.value = null
    roleName.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('userType')
    localStorage.removeItem('roleName')
  }

  return {
    token,
    userInfo,
    userType,
    roleName,
    setUser,
    loginAction,
    getUserInfo,
    logout
  }
})
