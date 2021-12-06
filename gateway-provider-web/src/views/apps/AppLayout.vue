<template>
  <div>
    <q-layout view="lHh Lpr lff" container style="height: 800px" class="shadow-2 rounded-borders">
      <q-drawer :mini="miniState" show-if-above :width="250" :breakpoint="500">
        <q-scroll-area style="height: calc(100% - 100px); margin-top: 100px; border-right: 1px solid #ddd">
          <template>
            <q-list>
              <template v-for="(menuItem, index) in menuList">
                <q-item @click="clickMenu('')" :key="index" v-if="!menuItem.children && menuItem.meta.show === true" clickable v-ripple :to="`/provider/apps/${appId}/`+menuItem.path">
                  <q-item-section avatar color="primary">
                    <q-icon color="primary" :name="menuItem.meta.icon" />
                  </q-item-section>
                  <q-item-section>{{ menuItem.meta.label }}</q-item-section>
                </q-item>
                <q-expansion-item group="appMenu" :key="index" v-else-if="menuItem.children && menuItem.meta.show === true">
                  <template v-slot:header>
                    <q-item-section v-on:mouseover="clickShowMenu(menuItem.path)" avatar>
                      <q-icon color="primary" :name="menuItem.meta.icon" />
                    </q-item-section>
                    <q-item-section>
                      {{ menuItem.meta.label }}
                    </q-item-section>
                  </template>
                  <template v-for="(subMenuItem, subIndex) in menuItem.children">
                    <q-item @click="clickMenu(subMenuItem.path)" v-show="subMenuItem.meta.show === true" :key="'sub'+subIndex" clickable v-ripple style="padding-left:50px;" :to="`/provider/apps/${appId}/`+menuItem.path+'/'+subMenuItem.path">
                      <q-item-section avatar>
                        <q-icon color="primary" :name="subMenuItem.meta.icon" />
                      </q-item-section>
                      <q-item-section>{{ subMenuItem.meta.label }}</q-item-section>
                    </q-item>
                  </template>
                </q-expansion-item>
              </template>
            </q-list>
          </template>
        </q-scroll-area>
        <q-img class="absolute-top" :src="require('@/assets/material.png')" style="height: 100px">
          <div class="absolute-bottom bg-transparent">
            <q-avatar size="56px" class="q-mb-sm">
              <q-btn @click="gobackApp()" flat round dense icon="arrow_back" size="lg" />
            </q-avatar>
            <div class="text-weight-bold h5">{{currentApp.name}}</div>
          </div>
        </q-img>
      </q-drawer>

      <q-page-container class="page-container">
        <q-page>
          <div class="flex-center">
            <router-view />
          </div>
        </q-page>
      </q-page-container>
    </q-layout>
  </div>
</template>
<script>
import router from '@/router'
import { qryApp } from '@/services/app'
import { appModel, dic } from '@/model'
export default {
  data() {
    return {
      menuList: [],
      miniState: false,
      pageSticky: true,
      currentApp: appModel(),
    }
  },
  computed: {
    appId() {
      return this.$route.params.appId
    },
  },
  methods: {
    toAppEnvMenu() {
      router.push({
        path: '/provider/apps/' + this.appId + '/appEnv',
      })
    },
    clickShowMenu(path) {
      if (path === 'testMgmt') {
        this.miniState = false
      }
    },
    clickMenu(path) {
      if (path === 'case' || path === 'test') {
        this.miniState = true
      } else {
        this.miniState = false
      }
      this.$root.$emit('showPageSticky', false, false)
    },
    gobackApp() {
      router.push({
        path: '/provider/apps/',
      })
      this.$root.$emit('showPageSticky', false, true)
    },
    getMenuList(data) {
      let routers = []
      if (null === data || data.length === 0) {
        return routers
      }
      routers = eval(data)
      routers = routers
        .filter((menu) => menu.path === '/provider')
        .map((menu) => {
          return menu.children
        })
      routers = routers[0]
        .filter((menu) => menu.path === 'apps/:appId')
        .map((menu) => {
          return menu.children
        })

      return routers[0]
    },
    appMiniStateSticky(miniState) {
      this.miniState = miniState
    },
  },
  async created() {
    this.menuList = this.getMenuList(sessionStorage.getItem('_routers'))
    let re = await qryApp({
      appSeq: this.appId,
    })
    this.currentApp = re.apps[0]
    this.$root.$on('appMiniStateSticky', this.appMiniStateSticky)
  },
}
</script>