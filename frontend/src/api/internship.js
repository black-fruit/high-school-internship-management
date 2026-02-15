import request from '@/utils/request'

export function getPlanPage(params) {
  return request({
    url: '/internship/plan/page',
    method: 'get',
    params
  })
}

export function createPlan(data) {
  return request({
    url: '/internship/plan',
    method: 'post',
    data
  })
}

export function updatePlanStatus(id, status) {
  return request({
    url: `/internship/plan/status/${id}`,
    method: 'put',
    params: { status }
  })
}

export function getPositionPage(params) {
  return request({
    url: '/internship/position/page',
    method: 'get',
    params
  })
}

export function getAvailablePositions(params) {
  return request({
    url: '/internship/position/available',
    method: 'get',
    params
  })
}

export function getPosition(id) {
  return request({
    url: `/internship/position/${id}`,
    method: 'get'
  })
}

export function createPosition(data) {
  return request({
    url: '/internship/position',
    method: 'post',
    data
  })
}

export function updatePositionStatus(id, status) {
  return request({
    url: `/internship/position/status/${id}`,
    method: 'put',
    params: { status }
  })
}
