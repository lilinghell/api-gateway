import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './quasar'
import { addRouters } from '@/utils/utils'
import axios from 'axios'
// 全局设置 axios 发送请求带上cookie
axios.defaults.withCredentials = true

Vue.config.productionTip = false

// 用户手动刷新页面，这是路由会被重设，要重新新增
if (sessionStorage.getItem('_user')) {
  addRouters(sessionStorage.getItem('_routers'))
}
// 登录状态判断
router.beforeEach((to, from, next) => {
  if (!sessionStorage.getItem('_user') && to.path !== '/provider/login' && to.path !== '/provider/register') {
    next({
      path: '/provider/login',
      query: { redirect: to.fullPath }
    })
  } else {
    next()
  }
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
