import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

//默认静态路由
const routes = [
  {
    path: '/provider/login',
    name: 'login',
    component: () => import(/* webpackChunkName: "login" */ '@/views/Login.vue')
  },
  {
    path: '/provider/register',
    name: 'register',
    component: () => import(/* webpackChunkName: "register" */ '@/views/register/Register.vue')
  },
  {
    path: '*',
    name: '404',
    component: () => import(/* webpackChunkName: "common404" */ '@/views/exception/404.vue')
  }
]

const a = [
  {
    path: '/provider',
    name: 'provider',
    component: () => import(/* webpackChunkName: "layout" */ '@/layouts/Layout.vue'),
    children: [
      {
        path: 'dashboard',
        name: 'dashboard',
        meta: {
          icon: 'dashboard',
          label: 'dashboard',
          show: true
        },
        component: () => import(/* webpackChunkName: "dashboard" */ '@/views/dashboard/dashboard.vue')
      },
      {
        path: 'account_info',
        name: 'accountInfo',
        meta: {
          icon: 'person',
          label: '账号信息',
          show: true
        },
        component: () => import(/* webpackChunkName: "accountInfo" */ '@/views/mgmt/AccountInfo.vue')
      },
      {
        path: 'mgmt',
        name: 'mgmt',
        meta: {
          icon: 'admin_panel_settings',
          label: '系统管理',
          show: true
        },
        component: () => import(/* webpackChunkName: "commonRouterView" */ '@/views/router-view'),
        children: [
          {
            path: 'roles',
            name: 'roles',
            meta: {
              icon: 'code',
              label: '角色',
              show: true
            },
            component: () => import(/* webpackChunkName: "Role" */ '@/views/mgmt/role/RoleList.vue')
          },
          {
            path: 'users',
            name: 'users',
            meta: {
              icon: 'people_alt',
              label: '用户',
              show: true
            },
            component: () => import(/* webpackChunkName: "user" */ '@/views/mgmt/user/UserList.vue')
          },
          {
            path: 'dept',
            name: 'dept',
            meta: {
              icon: 'local_fire_department',
              label: '部门',
              show: true
            },
            component: () => import(/* webpackChunkName: "dept" */ '@/views/mgmt/dept/DeptList.vue')
          }
        ]
      },
      {
        path: 'apps',
        name: 'apps',
        meta: {
          icon: 'apps',
          label: '应用服务',
          show: true
        },
        component: () => import(/* webpackChunkName: "appsIndex" */ '@/views/apps/Index.vue')
      },
      {
        path: 'apps/:appId',
        name: 'app',
        meta: {
          icon: 'apps',
          label: '',
          show: false
        },
        component: () => import(/* webpackChunkName: "appLayout" */ '@/views/apps/AppLayout.vue'),
        children: [
          {
            path: 'dashboard',
            name: 'appDashboard',
            meta: {
              icon: 'dashboard',
              label: 'dashboard',
              show: true
            },
            component: () => import(/* webpackChunkName: "appsDashboard" */ '@/views/apps/Dashboard.vue')
          },
          {
            path: 'appEnv',
            name: 'appEnv',
            meta: {
              icon: 'eco',
              label: '生态环境',
              show: true
            },
            component: () => import(/* webpackChunkName: "AppEnv" */ '@/views/apps/env/AppEnv.vue')
          },
          {
            path: ':appEnvId/apis',
            name: 'apiGroup',
            meta: {
              icon: 'api',
              label: 'API服务',
              show: false
            },
            component: () => import(/* webpackChunkName: "ApiGroups" */ '@/views/apps/api/ApiGroups.vue'),
          },
          {
            path: ':appEnvId/api',
            name: 'api',
            meta: {
              icon: 'api',
              label: '原子服务',
              show: false
            },
            component: () => import(/* webpackChunkName: "Apis" */ '@/views/apps/api/Api.vue'),
          },
          {
            path: 'scene',
            name: 'scene',
            meta: {
              icon: 'developer_board',
              label: '场景服务',
              show: true
            },
            component: () => import(/* webpackChunkName: "scene" */ '@/views/apps/scene/Scene.vue')
          },
          {
            path: 'testMgmt',
            name: 'testMgmt',
            meta: {
              icon: 'check_box',
              label: '测试管理',
              show: true
            },
            component: () => import(/* webpackChunkName: "testRouterView" */ '@/views/router-view'),
            children: [
              {
                path: 'parameter',
                name: 'parameter',
                meta: {
                  icon: 'production_quantity_limits',
                  label: '参数变量',
                  show: true
                },
                component: () => import(/* webpackChunkName: "parameter" */ '@/views/apps/test/Parameter.vue'),
              },
              {
                path: 'case',
                name: 'case',
                meta: {
                  icon: 'cases',
                  label: '测试用例',
                  show: true
                },
                component: () => import(/* webpackChunkName: "case" */ '@/views/apps/test/Case.vue'),
              },
              {
                path: 'test',
                name: 'test',
                meta: {
                  icon: 'check',
                  label: '测试计划',
                  show: true
                },
                component: () => import(/* webpackChunkName: "testPlan" */ '@/views/apps/test/TestPlan.vue'),
              }
            ]
          },
          {
            path: 'changeMgmt',
            name: 'changeMgmt',
            meta: {
              icon: 'track_changes',
              label: '变更管理',
              show: true
            },
            component: () => import(/* webpackChunkName: "changeRouterView" */ '@/views/router-view'),
            children: [
              {
                path: 'track',
                name: 'track',
                meta: {
                  icon: 'change_history',
                  label: '变更足迹',
                  show: true
                },
                component: () => import(/* webpackChunkName: "change" */ '@/views/apps/change/Change.vue')
              }
            ]
          },
        ]
      },
      {
        path: 'logs',
        name: 'logs',
        meta: {
          icon: 'inbox',
          label: '操作日志',
          show: true
        },
        component: () => import(/* webpackChunkName: "logs" */ '@/views/log/Logs.vue')
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})


export default router
