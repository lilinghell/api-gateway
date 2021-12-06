import request from '@/utils/request';
export async function qryEnv(params) {
  return request('/api/t1/service/qryEnv', {
    method: 'GET',
    params: {
      ...params
    }
  });
}
export async function qryEnvProfile(params) {
  return request('/api/t1/service/qryEnvProfile', {
    method: 'GET',
    params: {
      ...params
    }
  });
}
export async function updateEnv(params) {
  return request('/api/t1/service/updateEnv', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}
export async function addEnv(params) {
  return request('/api/t1/service/addEnv', {
    method: 'POST',
    data: {
      ...params
    }
  });

}
export async function delEnv(seq) {
  return request(`/api/t1/service/delEnv/${seq}`, {
    method: 'DELETE'
  });
}