import request from '@/utils/request'

export function getEnterprisePage(params) {
  return request({
    url: '/enterprise/page',
    method: 'get',
    params
  })
}

export function getEnterprise(id) {
  return request({
    url: `/enterprise/${id}`,
    method: 'get'
  })
}

export function auditEnterprise(id, auditStatus, auditRemark) {
  return request({
    url: '/enterprise/audit',
    method: 'post',
    params: { id, auditStatus, auditRemark }
  })
}

export function createEnterprise(data) {
  return request({
    url: '/enterprise',
    method: 'post',
    data
  })
}

export function updateEnterprise(data) {
  return request({
    url: '/enterprise',
    method: 'put',
    data
  })
}

export function getCurrentEnterprise() {
  return request({
    url: '/enterprise/current',
    method: 'get'
  })
}
