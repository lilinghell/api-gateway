<template>
  <div class="q-pa-md">
    <div>
      <div v-if="apiGroupModel.seq === ''" style="padding-top:20%" class="row q-col-gutter-sm">
        <q-icon color="grey" size="28px" class="q-ml-sm" name="mdi-set mdi-hand-pointing-left" />
        <span style="color:grey" class="text-h6">选中API组可查看接口列表</span>
      </div>
      <div v-else>
        <div class="row">
          <div style="width:30%;">
            <q-input :disable="apiGroupModel.seq === -1 || appEnvType !== 'DEV'" @keyup.enter="editApiGroup()" @focus="editApiGroupMode()" @blur="editApiGroupMode()" :borderless="!editable" maxlength="30" dense v-model="apiGroupModel.name" type="input" />
          </div>
          <div style="width:70%" class="text-right">
            <q-btn v-show="apiGroupModel.seq != -1" flat color="primary" icon="playlist_play" label="运行" />
            <q-btn :disabled="appEnvType !== 'DEV'" v-show="apiGroupModel.seq != -1" flat @click="editApiGroup" color="primary" icon="save" label="保存" />
            <q-btn color="primary" flat dense icon="more_horiz">
              <q-menu transition-show="jump-down" transition-hide="jump-up">
                <q-list style="width:230px;">
                  <q-item-label header>接口导出</q-item-label>
                  <q-item @click="showGroupApiDec" clickable v-close-popup>
                    <q-item-section avatar>
                      <q-avatar icon="assignment" color="primary" text-color="white" />
                    </q-item-section>
                    <q-item-section>
                      <q-item-label>PDF</q-item-label>
                      <q-item-label caption>导出PDF文档</q-item-label>
                    </q-item-section>
                    <q-item-section side>
                      <q-icon name="info" />
                    </q-item-section>
                  </q-item>
                  <q-separator spaced />
                  <q-item-label header>导出至其它环境</q-item-label>
                  <template v-for="(appEnv, index) in appEnvList">
                    <q-item @click="exportToOtherEnv(appEnv, null)" :key="'toEnv_'+index" clickable v-close-popup>
                      <q-item-section avatar>
                        <q-avatar icon="eco" color="primary" text-color="white" />
                      </q-item-section>
                      <q-item-section>
                        <q-item-label>{{appEnv.name}}</q-item-label>
                      </q-item-section>
                    </q-item>
                  </template>
                </q-list>
              </q-menu>
            </q-btn>
          </div>
        </div>
        <q-separator />
        <div class="row">
          <q-tabs style="width:30%" class="text-left" v-model="apiGroupSelectType" narrow-indicator dense align="justify">
            <q-tab :disable="apiGroupModel.seq === -1" name="overView" class="text-primary text-left" label="概述" />
            <q-tab name="inters" class="text-primary text-left" label="接口列表" />
          </q-tabs>
          <q-tab-panels style="width:100%" v-model="apiGroupSelectType" animated swipeable vertical transition-prev="jump-up" transition-next="jump-up">
            <q-tab-panel name="overView">
              <div class="text-grey q-mb-md">
                新增分组所在的应用名称，用于区分API组所属应用,作为标识,便于管理.
              </div>
            </q-tab-panel>
            <q-tab-panel name="inters">
              <div class="q-mb-md">
                <q-table flat :data="apiList" :columns="columns" :filter="filter" row-key="index" :pagination.sync="pagination" hide-pagination>
                  <template v-slot:top-left>
                    <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
                      <template v-slot:append>
                        <q-icon name="search" color="primary" />
                      </template>
                    </q-input>
                  </template>
                  <template v-slot:top-right>
                    <q-btn color="secondary" size="12px" class="table-head-btn" @click="showApiExport">
                      导出
                      <q-icon name="label_important" class="q-ml-sm" />
                    </q-btn>
                    <pre> </pre>
                    <q-btn v-show="apiGroupModel.seq === -1" color="brown-5" size="12px" class="table-head-btn" @click="showApiImport">
                      导入
                      <q-icon name="import_contacts" class="q-ml-sm" />
                    </q-btn>
                    <pre> </pre>
                    <q-btn v-show="appEnvType === 'DEV'" color="primary" size="12px" unelevated class="table-head-btn" @click="addApiModelOpen">
                      新增
                      <q-icon name="add" class="q-ml-sm" />
                    </q-btn>
                  </template>

                  <template v-slot:body-cell-status="props">
                    <q-td :class="props.col.__tdClass" class="q-gutter-x-sm">
                      <q-toggle @input="changeStatus(props.row)" flat true-value="0" checked-icon="check" unchecked-icon="clear" false-value="1" v-model="props.row.status" color="red" />
                    </q-td>
                  </template>

                  <template v-slot:body-cell-operation="props">
                    <q-td :class="props.col.__tdClass" class="q-gutter-x-sm">
                      <q-btn flat :disabled="props.row.url === ''" color="primary" label="预览" @click="showApiDec(props.row)" />
                      <q-btn v-show="props.row.syncGateway === '1'" flat color="red" label="同步" @click="syncGateway(props.row)">
                        <q-tooltip content-class="bg-red" :offset="[10, 10]">
                          同步路由等信息到网关
                        </q-tooltip>
                      </q-btn>
                      <q-btn v-show="props.row.syncGateway !== '1'" flat color="primary" label="编辑" @click="mgmtApi(props.row)" />
                      <q-btn color="primary" flat round dense icon="more_horiz">
                        <q-menu transition-show="jump-down" transition-hide="jump-up">
                          <q-list style="min-width: 100px">
                            <q-item v-if="props.row.syncGateway === '0'" @click="syncGateway(props.row)" clickable v-close-popup>
                              <q-item-section>同步</q-item-section>
                            </q-item>
                            <q-separator />
                            <q-item clickable>
                              <q-item-section>
                                <q-item-label>导出至</q-item-label>
                                <q-item-label caption>其它环境</q-item-label>
                              </q-item-section>
                              <q-item-section side>
                                <q-icon name="keyboard_arrow_right" />
                              </q-item-section>
                              <q-menu auto-close anchor="top end" self="top start">
                                <q-list style="min-width: 100px">
                                  <q-item @click="exportToOtherEnv(env, props.row.seq)" v-for="(env, index) in appEnvList" :key="'envGo_'+index" clickable>
                                    <q-item-section>{{env.name}}</q-item-section>
                                  </q-item>
                                </q-list>
                              </q-menu>
                            </q-item>
                            <q-item v-if="props.row.syncGateway === '1'" @click="mgmtApi(props.row)" clickable v-close-popup>
                              <q-item-section>编辑</q-item-section>
                            </q-item>
                            <q-separator />
                            <q-item @click="delApiModelOpen(props.row)" clickable v-close-popup>
                              <q-item-section>删除</q-item-section>
                            </q-item>
                          </q-list>
                        </q-menu>
                      </q-btn>
                    </q-td>
                  </template>
                </q-table>
                <div class="q-pa-lg flex flex-center">
                  <q-pagination color="primary" v-model="pagination.page" :max="pagesNumber" :direction-links="true">
                  </q-pagination>
                </div>
              </div>
            </q-tab-panel>
          </q-tab-panels>
        </div>
      </div>
    </div>
    <q-dialog v-model="delConfirm" persistent>
      <q-card style="width:300px">
        <q-card-section class="row items-center">
          <q-avatar icon="delete" color="primary" text-color="white" />
          <span class="q-ml-sm">确定删除?</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn label="删除" @click="delApi" color="primary" v-close-popup />
          <q-btn flat label="取消" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
    <ApiPreview ref="apiPreviewRef" />
    <ApiExport ref="apiExportRef" />
    <ApiImport ref="apiImportRef" />
    <AddApi :apiGroupSeq="(apiGroupModel.seq).toString()" apiFrom="api" ref="addApiRef" />
  </div>
</template>
<script>
import { dic } from '@/model'
import { qryAppEnv } from '@/services/app'
import { formatDicDisplay, positiveNotify } from '@/utils/utils'
import {
  qryApi,
  updateApiGroup,
  delApi,
  updateApi,
  syncGateway,
  exportToOtherEnv,
} from '@/services/api'
import router from '@/router'
import ApiPreview from '@/views/apps/api/ApiPreview.vue'
import ApiExport from '@/views/apps/api/ApiExport.vue'
import ApiImport from '@/views/apps/api/ApiImport.vue'
import AddApi from '@/views/apps/api/AddApi.vue'

export default {
  name: 'ApiGroup',
  components: {
    ApiPreview,
    ApiExport,
    ApiImport,
    AddApi,
  },
  inject: ['reload'],
  props: {
    apiGroupModel: {
      type: Object,
    },
  },
  watch: {
    // apiGroupModel: {
    //   handler(newValue, oldValue) {},
    //   deep: true,
    // },
  },
  data() {
    return {
      appEnvType: '',
      delConfirm: false,
      pagination: {
        sortBy: 'desc',
        descending: false,
        page: 0,
        rowsPerPage: 5,
      },
      apiGroupSelectType: 'inters',
      editable: false,
      apiList: [],
      filter: '',
      appEnvList: [],
      columns: [
        {
          name: 'name',
          label: 'API名称',
          align: 'left',
          style: 'width: 30%',
          field: (row) => row.name,
        },
        {
          name: 'url',
          label: 'url',
          align: 'left',
          style: 'width: 30%',
          field: (row) => row.url,
        },
        {
          name: 'method',
          label: 'method',
          align: 'center',
          style: 'width: 5%',
          field: (row) => row.method,
        },
        {
          name: 'status',
          label: '启用开关',
          align: 'center',
          style: 'width: 5%',
          field: (row) => row,
        },
        {
          name: 'operation',
          align: 'center',
          label: '操作',
          style: 'width: 5%',
          field: (row) => row,
        },
      ],
    }
  },
  computed: {
    pagesNumber() {
      return Math.ceil(this.apiList.length / this.pagination.rowsPerPage)
    },
    appId() {
      return this.$route.params.appId
    },
    appEnvId() {
      return this.$route.params.appEnvId
    },
  },
  methods: {
    formatDicDisplay,
    async exportToOtherEnv(otherAppEnv, apiSeq) {
      let apiSeqList = []
      if (apiSeq === null) {
        this.apiList.map((api) => {
          apiSeqList.push(api.seq)
        })
      } else {
        apiSeqList.push(apiSeq)
      }
      await exportToOtherEnv({
        apiSeqList: apiSeqList,
        groupSeq: this.apiGroupModel.seq,
        appSeq: this.appId,
        envSeq: this.appEnvId,
        otherEnvSeq: otherAppEnv.seq,
      })

      positiveNotify('导入成功')
    },
    showApiImport() {
      this.$refs.apiImportRef.showApiImportDialog = true
      this.$refs.apiImportRef.initStatus()
    },
    showApiExport() {
      this.$refs.apiExportRef.showApiExportDialog = true
      this.$refs.apiExportRef.qryApis(this.apiList, this.apiGroupModel)
    },
    async syncGateway(row) {
      let response = await syncGateway({
        apiSeq: row.seq,
        appEnvSeq: this.appEnvId,
      })
      this.apiList = this.apiList.map((api) => {
        if (api.seq.toString() === response.api.seq.toString()) {
          return response.api
        } else {
          return api
        }
      })
      positiveNotify('操作成功')
    },
    async changeStatus(row) {
      console.log(row)
      let response = await updateApi({
        ...row,
        clickTabFlag: 'define',
        groupSeq: row.group.seq,
      })
      this.apiList = this.apiList.map((api) => {
        if (api.seq.toString() === response.api.seq.toString()) {
          return response.api
        } else {
          return api
        }
      })
      positiveNotify('修改成功')
    },
    async delApi() {
      await delApi(this.delApiSeq)
      this.apiList = this.apiList.filter((apiFilter) => {
        if (apiFilter.seq.toString() === this.delApiSeq.toString()) {
          return false
        }
        return true
      })
      positiveNotify('删除成功')
      this.delConfirm = false
    },
    showGroupApiDec() {
      this.$refs.apiPreviewRef.showDialog = true
      this.$refs.apiPreviewRef.apiPreview('group', this.apiGroupModel.seq)
    },
    showApiDec(row) {
      this.$refs.apiPreviewRef.showDialog = true
      this.$refs.apiPreviewRef.apiPreview('current', row.seq)
    },
    editApiGroupMode() {
      this.editable = !this.editable
    },
    async editApiGroup() {
      // this.editable = !this.editable
      await updateApiGroup({
        seq: this.apiGroupModel.seq,
        name: this.apiGroupModel.name,
        appSeq: this.appId,
      })
      positiveNotify('更新成功')
      this.$parent.$parent.createApiGroupsTree(this.apiGroupModel.seq)
    },
    addApiModelOpen() {
      this.$refs.addApiRef.showAddApiMode()
    },
    delApiModelOpen(row) {
      this.delConfirm = true
      this.delApiSeq = row.seq
    },
    mgmtApi(row) {
      router.push({
        path: '/provider/apps/' + this.appId + '/' + this.appEnvId + '/api',
        query: { apiSeq: row.seq, groupSeq: this.apiGroupModel.seq },
      })
    },
    createApis(api) {
      this.apiList.unshift(api)
    },
    async qryApi(node) {
      this.apiList = []
      this.filter = ''
      let re = await qryApi({
        groupSeq: node.seq === -1 ? '' : node.seq,
        appEnvSeq: this.appEnvId,
      })
      this.apiList = re.apis
    },
  },
  async created() {
    this.appEnvType = sessionStorage.getItem('_appEnvType')
    let re = await qryAppEnv({
      appSeq: this.appId,
    })
    this.appEnvList = re.appEnvs.filter((appEnv) => appEnv.seq != this.appEnvId)
  },
}
</script>
<style lang="sass" scoped>
@import "~@/styles/quasar.sass"
.doc-heading
  color: $primary
  // cursor: pointer
.doc-h1
  font-size: 1.5rem
  line-height: 2rem
  font-weight: 500
.aClass
  cursor: pointer
  color: $primary
.sizeSeven
  font-size: 7px
</style>
