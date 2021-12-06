export function roleModel() {
  return {
    seq: '',
    roleName: '',
    routers: '',
    entSeq: ''
  };
}

export function userModel() {
  return {
    seq: '',
    userId: '',
    userName: '',
    userState: '',
    roleInfo: roleModel(),
    email: '',
    mobilePhone: '',
    createTime: '',
    headIcon: null,
    entInfo: entModel(),
  }
}

export function registerUserModel() {
  return {
    seq: '',
    userId: '',
    userName: '',
    userState: '',
    roleInfo: roleModel(),
    email: '',
    mobilePhone: '',
    createTime: '',
    headIcon: Object,
    accept: false,
    password: '',
    _tokenName: ''
  }
}

export function entModel() {
  return {
    seq: '',
    entName: '',
    entShortName: '',
    entCode: '',
    corpName: '',
    corpPhone: '',
    corpIdType: '',
    corpIdNo: '',
    contactName: '',
    contactPhone: '',
    entDec: '',
    entShortDec: '',
    entLicenseFile: null,
    corpFile0: null,
    corpFile1: null,
    status: '',
    registerAddr: ''
  }
}

export function deptModel() {
  return {
    seq: '',
    deptName: '',
    parenSeq: '',
    info: '',
    status: ''
  }
}

export function apiGroupModel() {
  return {
    seq: '',
    name: '',
    appEnvSeq: ''
  }
}

export function apiModel() {
  return {
    seq: '',
    name: '',
    version: '1.0.0',
    schemaType: 'swagger_2.0',
    schemaFormat: 'YAML',
    url: '',
    method: 'POST',
    description: '',
    status: '0',
    groupSeq: '',
    info: '',
    detail: '',
    mockSwitch: '1',
    flowSwitch: '1',
    updateTime: null,
    appEnvSeq: '',
    serviceId: ''
  }
}

export function apiExtModel() {
  return {
    apiSeq: '',
    entSeq: '',
    connectTimeout: '',
    responseTimeout: '',
    replenishRate: '',
    burstCapacity: '',
    apiMock: ''
  }
}

export function parameterModel() {
  return {
    seq: '',
    entSeq: '',
    name: '',
    enName: '',
    value: '',
    type: '0'
  }
}

export function testGroupModel() {
  return {
    seq: '',
    entSeq: '',
    deptName: '',
    parentSeq: '',
    info: ''
  }
}

export function testCaseModel() {
  return {
    seq: '',
    entSeq: '',
    name: '',
    status: '2',
    apiPath: '',
    parameter: '',
    assertScript: '',
    testGroupSeq: '',
    caseDesc: '',
    caseType: '0'
  }
}

export function appModel() {
  return {
    seq: '',
    entSeq: '',
    name: '',
    type: '',
    info: '',
    serviceId: ''
  }
}
export function appEnvModel() {
  return {
    seq: '',
    entSeq: '',
    appSeq: '',
    name: '',
    type: '',
    info: '',
    envKey: '',
    address: '',
    serviceType: ''
  }
}
export function testPlanModel() {
  return {
    seq: '',
    entSeq: '',
    appSeq: '',
    name: '',
    planTime: '',
    planRule: '',
    usingFlg: true,
  }
}

export function serviceTagModel() {
  return {
    seq: '',
    name: '',
    color: '#027BE3'
  }
}


export const dic = {
  serviceType: [
    { label: 'HTTP服务', value: '0' },
    { label: '微服务', value: '1' },
  ],
  appEnvType: [
    { label: 'DEV', value: 'DEV' },
    { label: 'TEST', value: 'TEST' },
    { label: 'UAT', value: 'UAT' },
    { label: 'PRO', value: 'PRO' },
  ],
  appType: [
    { label: 'J2EE', value: '0' },
    { label: 'WEB', value: '1' },
    { label: 'Microservices', value: '2' },
    { label: 'BATCH', value: '3' },
  ],
  userState: [
    { label: '正常', value: '0' },
    { label: '锁定', value: '1' },
    { label: '停用', value: '2' }
  ],
  idType: [
    { label: '身份证', value: '0' },
    { label: '户口本', value: '1' }
  ],
  envState: [
    { label: '正常', value: '0' },
    { label: '停用', value: '1' }
  ],
  registryType: [
    { label: 'Eureka', value: 'Eureka' },
    { label: 'ZooKeeper', value: 'ZooKeeper' },
    { label: 'Nacos', value: 'Nacos' }
  ],
  apiSchemaType: [
    { label: 'swagger 2.0', value: 'swagger_2.0' },
  ],
  apiSchemaFormat: [
    { label: 'YAML', value: 'YAML' }
  ],
  apiMethodType: [
    { label: 'GET', value: 'GET' },
    { label: 'POST', value: 'POST' },
    { label: 'PUT', value: 'PUT' },
    { label: 'DELETE', value: 'DELETE' }
  ],
  parameterType: [
    { label: '常量', value: '0' },
    { label: '变量', value: '1' }
  ],
  testCaseStatus: [
    { label: '执行成功', value: '0' },
    { label: '执行失败', value: '1' },
    { label: '待执行', value: '2' },
    { label: '执行中', value: '3' },
    { label: '验证成功', value: '4' },
    { label: '验证失败', value: '5' }
  ],
  weekdays: [
    { label: '周一', value: '1' },
    { label: '周二', value: '2' },
    { label: '周三', value: '3' },
    { label: '周四', value: '4' },
    { label: '周五', value: '5' },
    { label: '周六', value: '6' },
    { label: '周日', value: '7' }
  ],
  serviceTagColors: [
    { label: 'primary', value: '#027BE3' },
    { label: 'secondary', value: '#26A69A' },
    { label: 'accent', value: '#9C27B0' },
    { label: 'positive', value: '#21BA45' },
    { label: 'negative', value: '#C10015' },
    { label: 'warning', value: '#F2C037' }
  ],
}