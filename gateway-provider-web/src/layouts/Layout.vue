<template>
  <q-layout view="hHh lpR fFf" container style="height: 100%; min-height: 700px;">
    <q-header elevated class="bg-primary text-white">
      <Header />
    </q-header>

    <q-drawer :mini="miniState" v-model="drawer" show-if-above :width="250" :breakpoint="500" bordered>
      <q-scroll-area class="fit bg-grey-1">
        <AppMenu />
      </q-scroll-area>
    </q-drawer>

    <q-page-container class="page-container">
      <q-page>
        <div class="flex-center">
          <router-view v-if="isRouteAlive" />
        </div>
        <q-page-sticky v-show="pageSticky" position="bottom-left" :offset="[18, 18]">
          <q-btn @click="showAppMenu" fab icon="menu" color="primary" />
        </q-page-sticky>
      </q-page>
    </q-page-container>
  </q-layout>
</template>
<script>
import Header from './Header'
import AppMenu from './AppMenu'
export default {
  name: 'Layout',
  components: {
    Header,
    AppMenu,
  },
  provide() {
    return { reload: this.reload }
  },
  data() {
    return {
      isRouteAlive: true,
      drawer: false,
      miniState: false,
      pageSticky: false,
    }
  },
  methods: {
    reload() {
      this.isRouteAlive = false
      this.$nextTick(() => {
        this.isRouteAlive = true
      })
    },
    showAppMenu() {
      this.drawer = !this.drawer
    },
    showPageSticky(pram, showDrawer) {
      this.drawer = showDrawer
      this.pageSticky = pram
    },
  },
  created() {
    this.$root.$on('showAppMenu', this.showAppMenu)
    this.$root.$on('showPageSticky', this.showPageSticky)
  },
}
</script>

<style lang="sass" scoped>
@import "~@/styles/quasar.sass"
</style>