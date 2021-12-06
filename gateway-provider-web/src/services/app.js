import request from '@/utils/request';
export async function qryApp(params) {
  return request('/api/t1/service/qryApp', {
    method: 'GET',
    params: {
      ...params
    }
  });
}
export async function updateApp(params) {
  return request('/api/t1/service/updateApp', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}
export async function addApp(params) {
  return request('/api/t1/service/addApp', {
    method: 'POST',
    data: {
      ...params
    }
  });
}
export async function delApp(seq) {
  return request(`/api/t1/service/delApp/${seq}`, {
    method: 'DELETE'
  });
}

export async function updateAppEnv(params) {
  return request('/api/t1/service/updateAppEnv', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}

export async function delAppEnv(seq) {
  return request(`/api/t1/service/delAppEnv/${seq}`, {
    method: 'DELETE'
  });
}

export async function qryAppEnv(params) {
  return request('/api/t1/service/qryAppEnv', {
    method: 'GET',
    params: {
      ...params
    }
  });
}

export async function addAppEnv(params) {
  return request('/api/t1/service/addAppEnv', {
    method: 'POST',
    data: {
      ...params
    }
  });
}