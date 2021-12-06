<template>
  <q-list>
    <template v-for="(menuItem, index) in menuList">
      <q-item :key="index" v-if="!menuItem.children && menuItem.meta.show === true" @click="clickMenu('')" clickable v-ripple :to="'/provider/'+menuItem.path">
        <q-item-section avatar color="primary">
          <q-icon color="primary" :name="menuItem.meta.icon" />
        </q-item-section>
        <q-item-section>{{ menuItem.meta.label }}</q-item-section>
      </q-item>
      <q-expansion-item group="appMenu" :key="index" v-else-if="menuItem.children && menuItem.meta.show === true">
        <template v-slot:header>
          <q-item-section avatar>
            <q-icon color="primary" :name="menuItem.meta.icon" />
          </q-item-section>
          <q-item-section>
            {{ menuItem.meta.label }}
          </q-item-section>
        </template>
        <template v-for="(subMenuItem, subIndex) in menuItem.children">
          <q-item v-show="subMenuItem.meta.show === true" :key="'sub'+subIndex" @click="clickMenu(subMenuItem.path)" clickable v-ripple style="padding-left:50px;" :to="'/provider/'+menuItem.path+'/'+subMenuItem.path">
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
<script>
import { getMenuList } from '@/utils/utils'
export default {
  data() {
    return {
      menuList: [],
      clickPath: '',
    }
  },
  methods: {
    clickMenu(path) {
      sessionStorage.setItem('_clickPath', path)
      if ('dept' === path) {
        this.$root.$emit('showPageSticky', true, false)
      } else {
        this.$root.$emit('showPageSticky', false, true)
      }
    },
  },
  created() {
    this.menuList = getMenuList(sessionStorage.getItem('_routers'))
    this.clickPath = sessionStorage.getItem('_clickPath')
    // this.clickMenu(this.clickPath)
  },
}
</script>