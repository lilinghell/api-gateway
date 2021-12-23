<template>
  <q-layout view="hHh lpR fFf">
    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-toolbar-title>
          <q-avatar style="width:200px;height:70px;" rounded>
            <q-img width="200px" height="60px" alt="logo" />
          </q-avatar>
          HELL API网关服务Diga
          <q-badge transparent align="middle" color="orange">v1.0.0</q-badge>
        </q-toolbar-title>
      </q-toolbar>
    </q-header>

    <q-page-container align="center" style="padding-top: 200px;">
      <div class="q-pa-md text-center" style="max-width: 400px">
        <q-form @submit="onSubmit" @reset="onReset" class="q-gutter-md">

          <q-input filled v-model="loginId" label="账号 *" hint="请输入手机号/邮箱/用户名" lazy-rules :rules="[ val => val && val.length > 0 || '请输入手机号/邮箱/用户名']" />

          <q-input filled type="password" v-model="loginPassword" label="密码 *" lazy-rules :rules="[
              val => val !== null && val !== '' || '请输入密码'
            ]" />

          <q-toggle v-model="accept" label="接受许可证和条款" />

          <div>
            <q-btn label="登陆" type="submit" color="primary" />
            <!-- <q-btn label="Reset" type="reset" color="primary" flat class="q-ml-sm" /> -->
            <q-btn label="注册" color="primary" to="/provider/register" flat class="q-ml-sm" />
          </div>
        </q-form>

      </div>
    </q-page-container>
  </q-layout>

</template>

<script>
import { warningNotify } from '@/utils/utils'
import { mapActions } from 'vuex'
export default {
  data() {
    return {
      loginId: null,
      loginPassword: null,
      accept: false,
    }
  },

  methods: {
    ...mapActions('common', {
      login: 'login',
    }),
    async onSubmit() {
      if (this.accept !== true) {
        warningNotify('您需要先接受许可证和条款')
      } else {
        await this.login({
          loginId: this.loginId,
          loginPassword: this.loginPassword,
        })
      }
    },

    onReset() {
      this.loginId = null
      this.loginPassword = null
      this.accept = false
    },
  },
}
</script>

