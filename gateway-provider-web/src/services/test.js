import request from '@/utils/request';
export async function qryParameter(params) {
  return request('/api/t1/test/qryParameter', {
    method: 'GET',
    params: {
      ...params
    }
  });
}

export async function addParameter(params) {
  return request('/api/t1/test/addParameter', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function delParameter(params) {
  return request(`/api/t1/test/delParameter/${params.appSeq}/${params.seq}`, {
    method: 'DELETE'
  });
}

export async function updateParameter(params) {
  return request('/api/t1/test/updateParameter', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}

export async function qryTestGroup(params) {
  return request('/api/t1/test/qryTestGroup', {
    method: 'GET',
    params: {
      ...params
    }
  });
}

export async function addTestGroup(params) {
  return request('/api/t1/test/addTestGroup', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function delTestGroup(seq) {
  return request(`/api/t1/test/delTestGroup/${seq}`, {
    method: 'DELETE'
  });
}

export async function updateTestGroup(params) {
  return request('/api/t1/test/updateTestGroup', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}

export async function qryTestCases(params) {
  return request('/api/t1/test/qryTestCases', {
    method: 'GET',
    params: {
      ...params
    }
  });
}

export async function addTestCases(params) {
  return request('/api/t1/test/addTestCases', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function updateTestCases(params) {
  return request('/api/t1/test/updateTestCases', {
    method: 'PUT',
    data: {
      ...params
    }
  });
}

export async function delTest(seq) {
  return request(`/api/t1/test/delTest/${seq}`, {
    method: 'DELETE'
  });
}

export async function testCaseRun(params) {
  return request('/api/t1/test/testRun', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function qryTestCaseRun(params) {
  return request('/api/t1/test/qryTestCaseRun', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function addTestPlan(params) {
  return request('/api/t1/test/addTestPlan', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function delTestPlan(seq) {
  return request(`/api/t1/test/delTestPlan/${seq}`, {
    method: 'DELETE'
  });
}

export async function qryTestPlan(params) {
  return request('/api/t1/test/qryTestPlan', {
    method: 'GET',
    params: {
      ...params
    }
  });
}
export async function qryTestPlanCases(params) {
  return request('/api/t1/test/qryTestPlanCases', {
    method: 'GET',
    params: {
      ...params
    }
  });
}

export async function addTestPlanCases(params) {
  return request('/api/t1/test/addTestPlanCases', {
    method: 'POST',
    data: {
      ...params
    }
  });
}

export async function delTestPlanCases(seq) {
  return request(`/api/t1/test/delTestPlanCases/${seq}`, {
    method: 'DELETE'
  });
}