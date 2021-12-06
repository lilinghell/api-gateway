import request from '@/utils/request';
export async function login(params) {
  return request('/api/common/login', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function logout() {
  return request('/api/common/logout', {
    method: 'POST',
    data: {}
  });
}

export async function getToken() {
  return request('/api/common/token', {
    method: 'GET',
    params: {}
  });
}

export async function updateCurrentUser(params) {
  return request('/api/t1/account/updateCurrentUser', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}

export async function qryMessages() {
  return request('/api/t1/common/qryMessages', {
    method: 'GET',
    params: {}
  });
}

export async function updateMessage(params) {
  return request('/api/t1/common/updateMessage', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}