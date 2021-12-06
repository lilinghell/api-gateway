import request from '@/utils/request';
export async function qryServiceTag(params) {
  return request('/api/t1/service/qryServiceTag', {
    method: 'GET',
    params: {
      ...params
    }
  });
}

export async function addServiceTag(params) {
  return request('/api/t1/service/addServiceTag', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function delServiceTag(seq) {
  return request(`/api/t1/service/delServiceTag/${seq}`, {
    method: 'DELETE'
  });
}

export async function updateService(params) {
  return request('/api/t1/service/updateServiceTag', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}

export async function qryApiServiceTag(params) {
  return request('/api/t1/service/qryApiServiceTag', {
    method: 'GET',
    params: {
      ...params
    }
  });
}

export async function addApiServiceTag(params) {
  return request('/api/t1/service/addApiServiceTag', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function qryApiByTag(params) {
  return request('/api/t1/service/qryApiByTag', {
    method: 'GET',
    params: {
      ...params
    }
  });
}

export async function updateServiceTag(params) {
  return request('/api/t1/service/updateServiceTag', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}