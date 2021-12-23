<template>
  <q-toolbar>
    <q-input v-show="!searchShow" dark borderless v-model="text" class="q-ml-md" style="width: 280px; padding-right:20px;">
      <template v-slot:append>
        <q-icon v-if="text === ''" name="search" />
        <q-icon v-else name="clear" class="cursor-pointer" @click="text = ''" />
      </template>
    </q-input>
    <q-btn flat @click="showAppMenu" round dense icon="menu" />
    <q-toolbar-title>
      HELL API网关服务Diga<q-badge align="top">v1.0.0</q-badge>
    </q-toolbar-title>
    <q-btn @click="clickMsg" flat label="" icon="notifications" title="公告">
      <q-badge v-show="messageCount > 0" color="red" floating>{{messageCount}}</q-badge>
      <q-popup-proxy>
        <q-banner>
          <q-radio @input="filterMess" v-model="messStatusRadio" val="1" label="未阅" color="orange" />
          <q-radio @input="filterMess" v-model="messStatusRadio" val="0" label="已阅" color="orange" />
          <q-separator />
          <q-list>
            <q-item v-if="messageList.length === 0" style="width: 300px;">
              <q-item-section>
                <q-item-label>没有未阅消息</q-item-label>
              </q-item-section>
            </q-item>
            <template v-for="(mess, index) in messageList">
              <q-item :key="index" clickable>
                <q-item-section avatar>
                  <q-avatar v-if="mess.status === '1'" icon="assignment" color="primary" text-color="white" />
                  <q-avatar v-else icon="assignment" color="grey" text-color="white" />
                </q-item-section>
                <q-item-section>
                  <q-item-label>{{mess.title}}-{{mess.info.apiUrl}}</q-item-label>
                  <q-item-label caption>by {{mess.info.hostIp}} {{mess.updateTime}}</q-item-label>
                  <q-item-label v-if="!mess.info.status" caption>{{mess.info.errorMsg}}</q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-icon @click="updateMessage(mess)" name="message" v-if="mess.status === '1' && mess.info.status" color="amber" />
                  <q-icon @click="updateMessage(mess)" name="error" v-if="mess.status === '1' && !mess.info.status" color="red" />
                  <q-icon name="message" v-if="mess.status === '0' && mess.info.status" color="grey" />
                  <q-icon name="error" v-if="mess.status === '0' && !mess.info.status" color="grey" />
                </q-item-section>
              </q-item>
            </template>
          </q-list>
        </q-banner>
      </q-popup-proxy>
    </q-btn>
    <q-btn flat label="" icon="events" title="事件">
      <q-badge v-show="eventCount > 0" color="red" floating>{{eventCount}}</q-badge>
    </q-btn>
    <q-btn-dropdown flat :label="userModel.userName">
      <div class="row no-wrap q-pa-md">
        <div class="column">
          <div class="text-h6 q-mb-md">通知</div>
          <q-toggle v-model="notice" label="公告" />
          <q-toggle v-model="event" label="事件" />
        </div>

        <q-separator vertical inset class="q-mx-lg" />

        <div class="column items-center">
          <q-avatar size="72px">
            <img alt="user" :src="userModel.headIcon === null || userModel.headIcon === undefined ? '' : staticDomain + userModel.headIcon.path + userModel.headIcon.local_name">
          </q-avatar>
          <div class="text-subtitle1 q-mt-md q-mb-xs">{{userModel.userId}}</div>
          <q-btn color="primary" @click="logout" label="Logout" size="sm" v-close-popup />
        </div>
      </div>
    </q-btn-dropdown>
  </q-toolbar>
</template>
<script>
import { date } from 'quasar'
import { mapActions } from 'vuex'
import { qryMessages, updateMessage } from '@/services/common'
export default {
  name: 'Header',
  data() {
    return {
      messStatusRadio: '1',
      messageCount: 0,
      eventCount: 0,
      text: '',
      searchShow: false,
      notice: false,
      event: true,
      userModel: {},
      staticDomain: 'http://localhost',
      socket: '',
      messageList: [],
      messageAll: [],
    }
  },
  mounted() {
    this.initWebSocket()
  },
  methods: {
    ...mapActions('common', {
      logout: 'logout',
    }),
    clickMsg() {
      this.messStatusRadio = '1'
      //未阅
      this.filterMess('1')
    },
    async updateMessage(message) {
      if (message.status === '0') {
        return
      }
      let re = await updateMessage({
        seq: message.seq,
        status: '0',
      })
      this.messageAll = this.messageAll.map((me) => {
        if (me.seq === re.seq) {
          return {
            ...re,
            info: JSON.parse(re.info),
            updateTime: date.formatDate(re.updateTime, 'MM/DD HH:mm:ss'),
          }
        } else {
          return me
        }
      })
      this.messageList = this.messageList.map((mesage) => {
        if (mesage.seq === re.seq) {
          return {
            ...re,
            info: JSON.parse(re.info),
            updateTime: date.formatDate(re.updateTime, 'MM/DD HH:mm:ss'),
          }
        } else {
          return mesage
        }
      })
      this.messageCount = this.messageCount - 1
    },
    initWebSocket() {
      if (typeof WebSocket === 'undefined') {
        console.log('Not support websocket')
      } else {
        var host = 'ws://' + location.host + '/api/websocket/message'
        console.log(host)
        this.socket = new WebSocket(host)
        this.socket.onmessage = this.getMessage
      }
    },
    getMessage(event) {
      let mess = JSON.parse(event.data)
      let type = mess.type
      let val = mess.val
      if (type === '0') {
        //router通知
        let data = {
          ...val,
          info: JSON.parse(val.info),
          updateTime: date.formatDate(val.updateTime, 'MM/DD HH:mm:ss'),
        }
        this.messageAll.unshift(data)
        this.messageCount = this.messageCount + 1
      }
      if (type === '1') {
        //testRunResult通知
        this.$root.$emit('recTestRunResult', val)
      }
    },
    showAppMenu() {
      this.$root.$emit('showAppMenu')
      this.searchShow = !this.searchShow
    },
    filterMess(status) {
      this.messageList = this.messageAll.filter((me) => me.status === status)
    },
  },
  async created() {
    this.userModel = JSON.parse(sessionStorage.getItem('_user'))
    this.messageAll = await qryMessages({})
    this.messageAll = this.messageAll.map((message) => {
      if (message.status === '1') {
        this.messageCount = this.messageCount + 1
      }
      return {
        ...message,
        info: JSON.parse(message.info),
        updateTime: date.formatDate(message.updateTime, 'MM/DD HH:mm:ss'),
      }
    })
    //未阅
    this.filterMess('1')
  },
}
window.onbeforeunload = function () {
  this.socket.close()
}
</script>