<template>
  <div class="q-pa-md">
    <q-splitter separator-class="bg-white" v-model="splitterModel" style="height: 100%; min-height: 600px;">
      <template v-slot:before>
        <div class="q-pa-md">
          <div class="doc-heading doc-h1">环境列表</div>
          <div class="q-my-md">
            <q-table flat :data="appEnvList" :columns="columns" :filter="filter" row-key="index" :pagination.sync="pagination" hide-pagination>
              <template v-slot:top-left>
                <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
                  <template v-slot:append>
                    <q-icon name="search" color="primary" />
                  </template>
                </q-input>
              </template>
              <template v-slot:top-right>
                <q-btn color="primary" unelevated class="table-head-btn" @click="addShow">
                  新增
                  <q-icon name="add" class="q-ml-sm" />
                </q-btn>
              </template>
              <template v-slot:body-cell-operation="props">
                <q-td :class="props.col.__tdClass" class="q-gutter-x-sm">
                  <q-btn flat color="primary" :label="props.row.type === 'DEV' ? 'API定义' : 'API管理'" @click="apiShow(props.row)" />
                  <q-btn color="primary" flat round dense icon="more_horiz">
                    <q-menu transition-show="jump-down" transition-hide="jump-up">
                      <q-list style="min-width: 100px">
                        <q-item @click="mgmtAppEnv(props.row)" clickable v-close-popup>
                          <q-item-section>详情</q-item-section>
                        </q-item>
                        <q-item @click="delAppEnvModelOpen(props.row)" clickable v-close-popup>
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
        </div>
      </template>
      <template v-slot:after>
        <div class="q-pa-md">
          <div v-show="appEnvModel.seq === ''" class="doc-heading doc-h1">详情</div>
          <div>
            <q-form @submit="updateAppEnv" ref="updateAppFrom">
              <q-toolbar class="text-primary">
                <q-input style="width:100%" v-show="appEnvModel.seq !== ''" class="doc-h1" :readonly="!editable" v-model="appEnvModel.name" dense :rules="[ val => val && val.length > 0 || '名称不能为空' , val => val.length <= 20 && val.length > 0 || '名称长度不能超过20']" />
                <q-btn v-show="appEnvModel.seq !== '' && !editable" color="primary" flat size="10px" round icon="edit" @click="editMode()" />
                <q-btn color="primary" v-show="editable" type="submit" flat size="10px" round icon="done" />
                <q-btn color="primary" flat size="10px" v-show="editable" round icon="clear" @click="cancelMode()" />
              </q-toolbar>
              <div v-if="appEnvModel.seq === ''" class="row q-col-gutter-sm">
                <q-icon color="grey" size="23px" class="q-ml-sm" name="mdi-set mdi-hand-pointing-left" />
                <span style="font-size:15px">请选择一个环境,点击详情</span>
              </div>
              <div v-else>
                <div class="row">
                  <q-select style="width:100%;opacity: .6 !important" readonly label="环境类型" :display-value="formatDicDisplay(options, appEnvModel.type)" v-model="appEnvModel.type" :options="options" />
                </div>
                <div class="row">
                  <q-select style="width:100%;opacity: .6 !important" readonly label="服务类型" :display-value="formatDicDisplay(serviceTypeOptions, appEnvModel.serviceType)" v-model="appEnvModel.serviceType" :options="serviceTypeOptions" />
                </div>
                <div class="row">
                  <q-input style="width:100%" v-if="appEnvModel.serviceType === '0'" label="地址" :readonly="!editable" v-model="appEnvModel.address" type="text" />
                </div>
                <div class="row">
                  <q-input style="width:100%" label="描述" :readonly="!editable" v-model="appEnvModel.info" type="textarea" />
                </div>
              </div>
            </q-form>
          </div>
        </div>
      </template>

    </q-splitter>
    <q-dialog v-model="handAddAppEnvMode" persistent>
      <q-card style="min-width: 500px">
        <q-toolbar class="bg-primary text-white">
          <q-btn flat v-close-popup round dense icon="arrow_back" />
          <q-toolbar-title>新增环境</q-toolbar-title>
        </q-toolbar>
        <q-form @submit="addAppEnvSubmit" ref="addAppForm">
          <q-card-section style="width:95%" class="text-right">
            <q-input autofocus v-model="addAppEnvModel.name" type="text" :rules="[ val => val && val.length > 0 || '名称不能为空' , val => val.length <= 20 && val.length > 0 || '名称长度不能超过20']">
              <template v-slot:before>
                <span style="font-size:15px; width:80px">环境名称：</span>
              </template>
            </q-input>
            <q-select style="width:100%" :display-value="formatDicDisplay(options, addAppEnvModel.type)" v-model="addAppEnvModel.type" :options="options" :rules="[(val) => val || '请选择环境类型']" dense>
              <template v-slot:before>
                <span style="font-size:15px; width:80px">环境类型：</span>
              </template>
            </q-select>
            <q-input v-if="addAppEnvModel.serviceType === '0'" v-model="addAppEnvModel.address" type="text" :rules="[ val => val && val.length > 0 || '地址不能为空']">
              <template v-slot:before>
                <span style="font-size:15px; width:80px">URL地址：</span>
              </template>
            </q-input>
          </q-card-section>
          <q-card-actions align="right">
            <q-btn style="width:80px" type="submit" color="primary" label="提交" />
            <q-btn style="width:80px" v-close-popup flat color="primary" label="取消" class="q-ml-sm" />
          </q-card-actions>
        </q-form>
      </q-card>
    </q-dialog>
    <q-dialog v-model="delAppEnvConfirm" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="delete" color="primary" text-color="white" />
          <span class="q-ml-sm" style="padding-left:30px;width: 200px">
            是否确定删除？
          </span>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat label="取消" color="primary" v-close-popup />
          <q-btn flat label="确定" color="primary" @click="delConfirmAppEnv()" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>
<script>
import router from '@/router'
import { appEnvModel, dic, appModel } from '@/model'
import { addAppEnv, qryAppEnv, updateAppEnv, delAppEnv, qryApp } from '@/services/app'
import { positiveNotify, formatDicDisplay } from '@/utils/utils'
export default {
  name: 'AppEnvList',
  data() {
    return {
      appModel: appModel(),
      delAppEnvConfirm: false,
      options: dic.appEnvType,
      serviceTypeOptions: dic.serviceType,
      splitterModel: 75,
      appEnvModel: appEnvModel(),
      editable: false,
      addAppEnvModel: appEnvModel(),
      tempAppEnvModel: appEnvModel(),
      handAddAppEnvMode: false,
      appEnvList: [],
      columns: [
        {
          name: 'name',
          label: '名称',
          align: 'center',
          field: (row) => row.name,
          style: 'width: 25%',
        },
        {
          name: 'type',
          label: '环境类型',
          align: 'center',
          field: (row) => formatDicDisplay(dic.appEnvType, row.type),
          style: 'width: 5%',
        },
        {
          name: 'serviceType',
          label: '服务类型',
          align: 'center',
          field: (row) => formatDicDisplay(dic.serviceType, row.serviceType),
          style: 'width: 5%',
        },
        {
          name: 'envKey',
          label: 'EnvKey',
          align: 'center',
          field: (row) => row.envKey,
          style: 'width: 25%',
        },
        {
          name: 'operation',
          align: 'center',
          label: '操作',
          field: (row) => row,
          style: 'width: 40%',
        },
      ],
      filter: '',
      pagination: {
        sortBy: 'desc',
        descending: false,
        page: 0,
        rowsPerPage: 5,
      },
    }
  },
  computed: {
    pagesNumber() {
      return Math.ceil(this.appEnvList.length / this.pagination.rowsPerPage)
    },
    appId() {
      return this.$route.params.appId
    },
  },
  methods: {
    formatDicDisplay,
    apiShow(appEnv) {
      sessionStorage.setItem('_appEnvType', appEnv.type)
      sessionStorage.setItem('_appEnvServiceType', appEnv.serviceType)
      router.push({
        path: '/provider/apps/' + this.appId + '/' + appEnv.seq + '/apis',
      })
      this.$root.$emit('appMiniStateSticky', true)
    },
    async delConfirmAppEnv() {
      await delAppEnv(this.appEnvModel.seq)
      this.delAppEnvConfirm = false
      this.appEnvList = this.appEnvList.filter((appEnv) => appEnv.seq !== this.appEnvModel.seq)
      this.appEnvModel = appEnvModel()
      positiveNotify('删除成功！')
    },
    addShow() {
      this.addAppEnvModel = {
        ...appEnvModel(),
        serviceType: this.appModel.type === '2' ? '1' : '0',
      }
      this.handAddAppEnvMode = !this.handAddAppEnvMode
    },
    cancelMode() {
      this.editable = false
      this.appEnvModel = { ...this.tempAppEnvModel }
    },
    editMode() {
      this.editable = true
    },
    async updateAppEnv() {
      let re = await updateAppEnv({
        seq: this.appEnvModel.seq,
        name: this.appEnvModel.name,
        info: this.appEnvModel.info,
        address: this.appEnvModel.address,
        appSeq: this.appId,
      })
      this.appEnvList = this.appEnvList.map((appEnv) => {
        if (appEnv.seq === re.appEnvs[0].seq) {
          return re.appEnvs[0]
        } else {
          return appEnv
        }
      })
      positiveNotify('修改成功')
      this.editable = false
    },
    async addAppEnvSubmit() {
      let re = await addAppEnv({
        ...this.addAppEnvModel,
        type: this.addAppEnvModel.type.value,
        serviceType: this.addAppEnvModel.serviceType,
        appSeq: this.appId,
      })
      this.appEnvList.unshift(...re.appEnvs)
      this.handAddAppEnvMode = !this.handAddAppEnvMode
      positiveNotify('添加成功')
    },
    async delConfirmApp() {
      positiveNotify('删除成功')
    },
    delAppEnvModelOpen(appEnv) {
      this.appEnvModel = appEnv
      this.splitterModel = 70
      this.delAppEnvConfirm = true
    },
    mgmtAppEnv(appEnv) {
      this.appEnvModel = { ...appEnv }
      this.tempAppEnvModel = { ...appEnv }
      this.splitterModel = 70
    },
  },
  async created() {
    let re = await qryAppEnv({ appSeq: this.appId })
    this.appEnvList = re.appEnvs

    let reApp = await qryApp({ appSeq: this.appId })
    this.appModel = { ...reApp.apps[0] }
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
</style>
