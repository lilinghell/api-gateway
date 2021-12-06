import request from '@/utils/request';
export async function qryDept(params) {
  return request('/api/t1/account/qryDept', {
    method: 'GET',
    params: {
      ...params
    }
  });
}
export async function updateDept(params) {
  return request('/api/t1/account/updateDept', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}
export async function addDept(params) {
  return request('/api/t1/account/addDept', {
    method: 'POST',
    data: {
      ...params,
      status: 0
    }
  });

}
export async function delDept(seq) {
  return request(`/api/t1/account/delDept/${seq}`, {
    method: 'DELETE'
  });
}

export async function qryDeptMember(params) {
  return request('/api/t1/account/qryDeptMember', {
    method: 'GET',
    params: {
      ...params
    }
  });
}

export async function delDeptMember(params) {
  return request('/api/t1/account/delDeptMember', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}

export async function addDeptMember(params) {
  return request('/api/t1/account/addDeptMember', {
    method: 'POST',
    data: {
      ...params
    }
  });
}