import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function register(data) {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}

export function getCurrentUser() {
  return request({
    url: '/auth/current',
    method: 'get'
  })
}

export function changePassword(data) {
  return request({
    url: '/auth/changePassword',
    method: 'post',
    data
  })
}
