<template>
  <div class="q-pa-md">
    <q-dialog v-model="showApiImportDialog">
      <q-layout style="width: 800px; max-width: 200vw;" view="Lhh lpR fff" container class="bg-white">
        <q-stepper v-model="step" flat ref="stepper" color="primary" animated>
          <q-step :name="1" title="选择文件" icon="settings" :done="done1">
            <p>请选择需要导入的JSON文件.</p>
            <q-file v-model="jsonFile" accept=".json" style="width:300px" dense color="teal" filled>
              <template v-slot:prepend>
                <q-icon name="cloud_upload" />
              </template>
            </q-file>

            <q-stepper-navigation>
              <q-btn @click="continueClick" color="primary" label="下一步" />
            </q-stepper-navigation>
          </q-step>

          <q-step :name="2" title="导入接口" icon="create_new_folder" :done="done2">
            <div v-if="importApis.apiInfoList" class="col-12 col-sm-6 q-gutter-sm">
              <q-scroll-area :thumb-style="thumbStyle" :bar-style="barStyle" style="height: 390px; max-width: 100%;">
                <div class="q-col-gutter-sm">
                  <div v-for="apiInfo in importApis.apiInfoList" :key="`ticked-${apiInfo.api.seq}`">
                    <div class="row">
                      <div style="width: 50%">{{ apiInfo.api.name }}</div>
                      <div style="width: 50%">{{ apiInfo.api.url }}</div>
                    </div>
                  </div>
                </div>
              </q-scroll-area>
              <q-separator spaced />
              <div class="text-h6">其它扩展信息导入</div>
              <div>
                <div class="q-gutter-sm">
                  <q-checkbox color="orange" left-label v-model="envFlg" label="生态环境" />
                  <q-checkbox color="red" left-label v-model="apiGroupFlg" label="接口组" />
                </div>
              </div>
            </div>

            <q-stepper-navigation>
              <q-btn @click="apiImport" color="primary" label="导入" />
              <q-btn flat @click="step = 1" color="primary" label="Back" class="q-ml-sm" />
            </q-stepper-navigation>
          </q-step>

          <q-step :name="3" title="导入成功" icon="assignment" :done="done3">
            <div class="row">导入成功</div>
            <div class="row">
              <q-checkbox disable v-model="result" label="API基本信息及扩展信息" color="orange" />
            </div>
            <div class="row">
              <q-checkbox disable v-model="envFlg" label="生态环境信息" color="orange" />
            </div>
            <div class="row">
              <q-checkbox disable v-model="apiGroupFlg" label="接口组信息" color="orange" />
            </div>
            <q-stepper-navigation>
              <q-btn color="primary" @click="finish" label="完成" />
            </q-stepper-navigation>
          </q-step>
        </q-stepper>

      </q-layout>
    </q-dialog>
  </div>
</template>
<script>
import { apiImport } from '@/services/api'
import { warningNotify } from '@/utils/utils'

export default {
  name: 'ApiImport',
  data() {
    return {
      result: true,
      envFlg: false,
      apiGroupFlg: true,
      showApiImportDialog: false,
      importApis: [],
      jsonFile: null,
      step: 1,
      done1: false,
      done2: false,
      done3: false,
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
    async finish() {
      var node = { seq: -1, appEnvSeq: this.appEnvId }
      this.$parent.qryApi(node)
      this.$parent.$parent.$parent.init()
      this.showApiImportDialog = false
    },
    async apiImport() {
      await apiImport({
        apiExportInfo: this.importApis,
        envFlg: this.envFlg,
        apiGroupFlg: this.apiGroupFlg,
        appEnvSeq: this.appEnvId,
      })
      this.done2 = true
      this.step = 3
    },
    async continueClick() {
      if (this.jsonFile === null) {
        warningNotify('请选择文件!')
      } else {
        if (window.FileReader) {
          var fr = new FileReader()
          fr.readAsText(this.jsonFile)
          fr.onloadend = () => {
            this.importApis = JSON.parse(fr.result)
            this.done1 = true
            this.step = 2
          }
        } else {
          console.log('Not supported by your browser!')
        }
      }
    },
    initStatus() {
      this.importApis = []
      this.jsonFile = null
      this.step = 1
      this.done1 = false
      this.done2 = false
      this.done3 = false
      this.envFlg = false
      this.apiGroupFlg = true
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
