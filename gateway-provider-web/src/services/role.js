import request from '@/utils/request';
export async function qryRole(params) {
  return request('/api/t1/account/qryRole', {
    method: 'GET',
    params: {
      ...params
    }
  });
}
export async function updateRole(params) {
  return request('/api/t1/account/updateRole', {
    method: 'PUT',
    data: {
      ...params,
      routers: JSON.stringify(params.routers)
    }
  });
}
export async function addRole(params) {
  return request('/api/t1/account/createRole', {
    method: 'POST',
    data: {
      ...params,
      routers: JSON.stringify(params.routers)
    }
  });

}
export async function delRole(seq) {
  return request(`/api/t1/account/deleteRole/${seq}`, {
    method: 'DELETE'
  });
}