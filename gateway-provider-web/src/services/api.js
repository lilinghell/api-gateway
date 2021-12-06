import request from '@/utils/request';
export async function qryApiGroup(params) {
  return request('/api/t1/service/qryApiGroup', {
    method: 'GET',
    params: {
      ...params
    }
  });
}
export async function updateApiGroup(params) {
  return request('/api/t1/service/updateApiGroup', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}
export async function addApiGroup(params) {
  return request('/api/t1/service/addApiGroup', {
    method: 'POST',
    data: {
      ...params
    }
  });
}
export async function delApiGroup(seq, appEnvSeq) {
  return request(`/api/t1/service/delApiGroup/${appEnvSeq}/${seq}`, {
    method: 'DELETE'
  });
}


export async function addApi(params) {
  return request('/api/t1/service/addApi', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function qryApi(params) {
  return request('/api/t1/service/qryApi', {
    method: 'GET',
    params: {
      ...params
    }
  });
}

export async function updateApi(params) {
  return request('/api/t1/service/updateApi', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}

export async function delApi(seq) {
  return request(`/api/t1/service/delApi/${seq}`, {
    method: 'DELETE'
  });
}

export async function qryApiExt(params) {
  return request('/api/t1/service/qryApiExt', {
    method: 'GET',
    params: {
      ...params
    }
  });
}

export async function updateApiExt(params) {
  return request('/api/t1/service/updateApiExt', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}

export async function qryApiPreview(params) {
  return request('/api/t1/service/qryApiPreview', {
    method: 'GET',
    params: {
      ...params
    }
  });
}
export async function qryApiHisPreview(params) {
  return request('/api/t1/service/qryApiHisPreview', {
    method: 'GET',
    params: {
      ...params
    }
  });
}

export async function syncGateway(params) {
  return request('/api/t1/service/syncGateway', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function qryApiHis(params) {
  return request('/api/t1/service/qryApiHis', {
    method: 'GET',
    params: {
      ...params
    }
  });
}

export async function qryApiExport(params) {
  return request('/api/t1/service/qryApiExport', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function apiImport(params) {
  return request('/api/t1/service/apiImport', {
    method: 'POST',
    data: {
      ...params
    }
  });
}
export async function exportToOtherEnv(params) {
  return request('/api/t1/service/exportToOtherEnv', {
    method: 'POST',
    data: {
      ...params
    }
  });
}