import request from '@/utils/request';

export async function sendSms(params) {
  return request('/api/register/sendSms', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function register(params) {
  return request('/api/register', {
    method: 'POST',
    data: {
      ...params
    }
  })
}

export async function createEntInfo(params) {
  return request('/api/register/createEntInfo', {
    method: 'POST',
    data: params
  })
}

export async function qryUser(params) {
  return request('/api/t1/account/qryUser', {
    method: 'GET',
    params: {
      ...params
    }
  });
}
export async function qryCurrentUser(params) {
  return request('/api/t1/account/qryCurrentUser', {
    method: 'GET',
    params: {
      ...params
    }
  });
}
export async function updateUser(params) {
  return request('/api/t1/account/updateUser', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}

export async function addUser(params) {
  return request('/api/t1/account/createUser', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function delUser(seq) {
  return request(`/api/t1/account/deleteUser/${seq}`, {
    method: 'DELETE'
  });
}

export async function updatePassword(params) {
  return request('/api/t1/account/updatePassword', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}