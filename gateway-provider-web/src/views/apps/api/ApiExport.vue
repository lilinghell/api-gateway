<template>
  <div class="q-pa-md">
    <q-dialog v-model="showApiExportDialog">
      <q-layout style="width: 1000px; max-width: 200vw;" view="Lhh lpR fff" container class="bg-white">
        <q-card>
          <div class="q-pa-md row q-col-gutter-sm">
            <div class="col-12 col-sm-5">
              <q-toggle keep-color @input="apiAllToggle" v-model="apiAll" color="pink" :label="`导出${group.name}所有的接口`" />
              <q-input v-show="!apiAll" style="width:70%" ref="filter" dense v-model="filter" class="table-head-input">
                <template v-slot:append>
                  <q-icon color="primary" name="search" class="primary" @click="resetFilter" />
                </template>
              </q-input>
              <q-scroll-area v-show="!apiAll" :thumb-style="thumbStyle" :bar-style="barStyle" style="height: 500px; max-width: 100%;">
                <q-tree :filter="filter" :nodes="apiSimple" label-key="label" node-key="id" @update:ticked="tickedApis" tick-strategy="leaf" :ticked.sync="ticked" />
              </q-scroll-area>
            </div>
            <div class="col-12 col-sm-6 q-gutter-sm">
              <div v-if="apiAll" class="text-h6 text-primary">导出接口({{apiSimple.length}})</div>
              <div v-else class="text-h6 text-primary">导出接口({{selectApis.length}})</div>
              <q-scroll-area :thumb-style="thumbStyle" :bar-style="barStyle" style="height: 450px; max-width: 100%;">
                <div class="q-col-gutter-sm">
                  <div v-if="apiAll">所有接口</div>
                  <div v-show="selectApis.length === 0 && !apiAll">无</div>
                  <div v-for="api in selectApis" :key="`ticked-${api.seq}`">
                    <div class="row">
                      <div style="width: 50%">{{ api.name }}</div>
                      <div style="width: 50%">{{ api.url }}</div>
                    </div>
                  </div>
                </div>
              </q-scroll-area>
              <q-separator spaced />
              <div class="text-h6 text-primary">其它扩展信息导出</div>
              <div>
                <div class="q-gutter-sm">
                  <q-checkbox color="orange" left-label v-model="timeOutFlg" label="超时时间" />
                  <q-checkbox color="red" left-label v-model="rateFlg" label="限流" />
                  <q-checkbox color="cyan" left-label v-model="apiMockFlg" label="API Mock" />
                </div>
              </div>
            </div>
          </div>
        </q-card>
        <q-footer class="bg-white text-white">
          <q-toolbar inset>
            <q-toolbar-title class="text-right">
              <q-btn @click="apiExport" label="导出" color="primary" />
            </q-toolbar-title>
          </q-toolbar>
        </q-footer>
      </q-layout>
    </q-dialog>
  </div>
</template>
<script>
import { qryApiExport } from '@/services/api'
import { apiGroupModel } from '@/model'
import { save2Json, warningNotify } from '@/utils/utils'
import { date } from 'quasar'

export default {
  name: 'ApiExport',
  data() {
    return {
      apiAll: false,
      showApiExportDialog: false,
      apiSimple: [],
      ticked: [],
      selectApis: [],
      timeOutFlg: true,
      rateFlg: true,
      apiMockFlg: true,
      filter: '',
      group: apiGroupModel(),
      thumbStyle: {
        right: '4px',
        borderRadius: '5px',
        backgroundColor: '#027be3',
        width: '5px',
        opacity: 0.75,
      },

      barStyle: {
        right: '2px',
        borderRadius: '9px',
        backgroundColor: '#027be3',
        width: '9px',
        opacity: 0.2,
      },
    }
  },
  computed: {
    appId() {
      return this.$route.params.appId
    },
    appEnvId() {
      return this.$route.params.appEnvId
    },
  },
  methods: {
    async apiExport() {
      if (this.apiAll) {
        this.selectApis = this.apiSimple
      }
      if (this.selectApis.length === 0) {
        warningNotify('请选择需要导出的接口!')
      } else {
        let response = await qryApiExport({
          timeOutFlg: this.timeOutFlg,
          rateFlg: this.rateFlg,
          apiMockFlg: this.apiMockFlg,
          selectApis: this.selectApis.map((api) => {
            return api.seq
          }),
          appEnvSeq: this.appEnvId,
        })

        const fileName = date.formatDate(new Date(), 'YYYYMMDDHHmmss')
        save2Json(response, 'apis_' + fileName + '.json')
        this.showApiExportDialog = false
      }
    },
    apiAllToggle(value, evt) {
      if (value) {
        this.selectApis = []
        this.ticked = []
      }
    },
    resetFilter() {
      this.filter = ''
      this.$refs.filter.focus()
    },
    tickedApis(target) {
      this.selectApis = this.apiSimple.filter((api) => {
        var flg = false
        target.forEach((element) => {
          if (element.toString() === api.id.toString()) {
            flg = true
          }
        })
        return flg
      })
    },
    async qryApis(apiList, group) {
      this.ticked = []
      this.selectApis = []
      this.timeOutFlg = true
      this.rateFlg = true
      this.apiMockFlg = true
      this.apiAll = false
      this.group = group
      // let response = await qryApi({
      //   groupSeq: groupSeq,
      // })
      let response = apiList
      this.apiSimple = response.map((api) => {
        return {
          ...api,
          label: api.name,
          id: api.seq,
        }
      })
    },
  },
}
</script>
<style lang="sass" scoped>
@import "~@/styles/quasar.sass"
.apiPreviewTable
  width: 100%
.doc-heading
  color: $primary
</style>
