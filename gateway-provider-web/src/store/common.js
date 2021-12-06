import router from '@/router'
import { login, logout, updateCurrentUser } from '@/services/common'
import { addRouters } from '@/utils/utils'

export default {
  namespaced: true,
  state: {
    status: undefined
  },
  mutations: {
    changeLoginStatus(state, payload) {
      sessionStorage.setItem('_user', JSON.stringify(payload))
      sessionStorage.setItem('_routers', payload.roleInfo.routers)
      state.status = payload.status
      addRouters(payload.roleInfo.routers)
    },
    changeLogoutStatus(state, payload) {
      state.status = payload.status
      sessionStorage.clear()
      // sessionStorage.removeItem('_user')
      // sessionStorage.removeItem('_routers')
    }
  },
  actions: {
    async login({ commit, state }, payload) {
      const response = await login(payload);
      const { status } = response;
      commit('changeLoginStatus', response);
      if ('error' !== status) {
        router.push('/provider/account_info');
      }
    },
    async logout({ commit, state }, payload) {
      const response = await logout();
      commit('changeLogoutStatus', response);
      router.push(
        '/provider/login'
      );
    },
    async updateCurrentUser({ commit, state }, payload) {
      const response = await updateCurrentUser(payload);
      const { status } = response;
      commit('changeLoginStatus', response.user);
      if ('error' !== status) {
        router.push('/provider/account_info');
      }
    },

  }
}
// export default common
