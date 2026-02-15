import request from '@/utils/request'

export function getUserPage(params) {
  return request({
    url: '/user/page',
    method: 'get',
    params
  })
}

export function getUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
}

export function createUser(data) {
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/user',
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
}

export function updateUserStatus(id, status) {
  return request({
    url: `/user/status/${id}`,
    method: 'put',
    params: { status }
  })
}

export function assignRole(userId, roleId) {
  return request({
    url: '/user/assignRole',
    method: 'post',
    params: { userId, roleId }
  })
}

export function getRoles() {
  return request({
    url: '/user/roles',
    method: 'get'
  })
}

export function getUserRoles(userId) {
  return request({
    url: `/user/roles/${userId}`,
    method: 'get'
  })
}
