import request from '@/utils/request'

export function getApplicationPage(params) {
  return request({
    url: '/application/page',
    method: 'get',
    params
  })
}

export function getMyApplications(params) {
  return request({
    url: '/application/my',
    method: 'get',
    params
  })
}

export function getEnterpriseApplications(params) {
  return request({
    url: '/application/enterprise',
    method: 'get',
    params
  })
}

export function getTeacherApplications(params) {
  return request({
    url: '/application/teacher',
    method: 'get',
    params
  })
}

export function applyPosition(positionId, resume) {
  return request({
    url: '/application/apply',
    method: 'post',
    params: { positionId, resume }
  })
}

export function approveApplication(id) {
  return request({
    url: `/application/approve/${id}`,
    method: 'post'
  })
}

export function rejectApplication(id, rejectReason) {
  return request({
    url: `/application/reject/${id}`,
    method: 'post',
    params: { rejectReason }
  })
}

export function getWeeklyReportPage(params) {
  return request({
    url: '/application/weekly/page',
    method: 'get',
    params
  })
}

export function getMyWeeklyReports(params) {
  return request({
    url: '/application/weekly/my',
    method: 'get',
    params
  })
}

export function submitWeeklyReport(applicationId, weekNumber, content) {
  return request({
    url: '/application/weekly',
    method: 'post',
    params: { applicationId, weekNumber, content }
  })
}

export function commentWeeklyReport(id, comment) {
  return request({
    url: `/application/weekly/comment/${id}`,
    method: 'post',
    params: { comment }
  })
}

export function getInternshipReport(applicationId) {
  return request({
    url: '/application/report',
    method: 'get',
    params: { applicationId }
  })
}

export function submitInternshipReport(applicationId, title, content, attachment) {
  return request({
    url: '/application/report',
    method: 'post',
    params: { applicationId, title, content, attachment }
  })
}

export function getScore(applicationId) {
  return request({
    url: '/application/score',
    method: 'get',
    params: { applicationId }
  })
}

export function teacherScore(applicationId, score, comment) {
  return request({
    url: '/application/score/teacher',
    method: 'post',
    params: { applicationId, score, comment }
  })
}

export function enterpriseScore(applicationId, score, comment) {
  return request({
    url: '/application/score/enterprise',
    method: 'post',
    params: { applicationId, score, comment }
  })
}
