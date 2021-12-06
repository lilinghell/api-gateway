<template>
  <div class="q-pa-md">
    <q-splitter separator-class="bg-white" v-model="splitterModel" style="height: 100%; min-height: 600px;">
      <template v-slot:before>
        <div class="q-pa-md">
          <div class="doc-heading doc-h1">应用列表</div>
          <div class="q-my-md">
            <q-table flat :data="appList" :columns="columns" :filter="filter" row-key="index" :pagination.sync="pagination" hide-pagination>
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
              <template v-slot:body-cell-name="props">
                <q-td :class="props.col.__tdClass" class="q-gutter-x-sm">
                  <q-btn @click="toApp(props.row)" no-caps flat color="primary" :label="props.row.name" />
                </q-td>
              </template>

              <template v-slot:body-cell-operation="props">
                <q-td :class="props.col.__tdClass" class="q-gutter-x-sm">
                  <q-btn flat color="primary" label="删除" @click="delAppModelOpen(props.row)" />
                  <q-btn flat color="primary" label="详情" @click="mgmtApp(props.row)" />
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
          <div v-show="appModel.seq === ''" class="doc-heading doc-h1">详情</div>
          <div>
            <q-form @submit="updateApp" ref="updateAppFrom">
              <q-toolbar class="text-primary">
                <q-input style="width:100%" v-show="appModel.seq !== ''" class="doc-h1" :readonly="!editable" v-model="appModel.name" dense :rules="[ val => val && val.length > 0 || '请输入app名称' , val => val.length <= 20 && val.length > 0 || 'app名称长度不能超过20' ]">
                  <template v-slot:after>
                    <q-btn color="primary" flat size="10px" round icon="edit" @click="editMode()" v-show="!editable" />
                    <q-btn color="primary" type="submit" v-show="editable" flat size="10px" round icon="done" @click="updateApp" />
                    <q-btn color="primary" flat size="10px" v-show="editable" round icon="clear" @click="cancelMode()" />
                  </template>
                </q-input>
              </q-toolbar>
              <div v-if="appModel.seq === ''" class="row q-col-gutter-sm">
                <q-icon color="grey" size="23px" class="q-ml-sm" name="mdi-set mdi-hand-pointing-left" />
                <span style="font-size:15px">请选择一个应用,点击详情</span>
              </div>
              <div v-else>
                <div class="row">
                  <q-select style="width:100%" readonly label="类型" :display-value="formatDicDisplay(options, appModel.type)" v-model="appModel.type" :options="options" dense  />
                </div>
                <div class="row">
                  <q-input style="height:70px; width:100%" label="描述" :readonly="!editable" v-model="appModel.info" type="textarea" dense :rules="[  val => val.length <= 256 || '描述长度不能超过256' ]"/>
                </div>
              </div>
            </q-form>
          </div>
        </div>
      </template>

    </q-splitter>
    <q-dialog v-model="handAddAppMode" persistent>
      <q-card style="min-width: 500px">
        <q-toolbar class="bg-primary text-white">
          <q-btn flat v-close-popup round dense icon="arrow_back" />
          <q-toolbar-title>新增应用</q-toolbar-title>
        </q-toolbar>
        <q-form @submit="addAppSubmit" ref="addAppForm">
          <q-card-section style="width:95%" class="text-right">
            <q-input autofocus v-model="addAppModel.name" type="text" :rules="[  val => val && val.length > 0 || '请输入app名称' , val => val.length <= 20 && val.length > 0 || 'app名称长度不能超过20']">
              <template v-slot:before>
                <span style="font-size:15px; width:80px">应用名称：</span>
              </template>
            </q-input>
            <q-select style="width:100%" :display-value="formatDicDisplay(options, addAppModel.type)" v-model="addAppModel.type" :options="options" :rules="[(val) => val || '请选择app类型']" dense>
              <template v-slot:before>
                <span style="font-size:15px; width:80px">类型：</span>
              </template>
            </q-select>
            <q-input v-model="addAppModel.info" type="textarea" :rules="[  val => val.length <= 256 || '描述长度不能超过256' ]">
              <template v-slot:before>
                <span style="font-size:15px; width:80px">描述：</span>
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
    <q-dialog v-model="delAppConfirm" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="delete" color="primary" text-color="white" />
          <span class="q-ml-sm" style="padding-left:30px;width: 200px">
            是否确定删除？
          </span>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat label="取消" color="primary" v-close-popup />
          <q-btn flat label="确定" color="primary" @click="delConfirmApp()" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>
<script>
import router from '@/router'
import { appModel, dic } from '@/model'
import { addApp, qryApp, updateApp, delApp } from '@/services/app'
import { positiveNotify, formatDicDisplay } from '@/utils/utils'
export default {
  name: 'AppList',
  data() {
    return {
      delAppConfirm: false,
      options: dic.appType,
      splitterModel: 75,
      appModel: appModel(),
      editable: false,
      addAppModel: appModel(),
      tempAppModel: appModel(),
      handAddAppMode: false,
      appList: [],
      columns: [
        {
          name: 'name',
          label: '应用名称',
          align: 'center',
          field: (row) => row.name,
          style: 'width: 25%',
        },
        {
          name: 'type',
          label: '应用种类',
          align: 'center',
          field: (row) => formatDicDisplay(dic.appType, row.type),
          style: 'width: 25%',
        },
        {
          name: 'operation',
          align: 'center',
          label: '操作',
          field: (row) => row,
          style: 'width: 25%',
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
      return Math.ceil(this.appList.length / this.pagination.rowsPerPage)
    },
  },
  methods: {
    formatDicDisplay,
    toApp(app) {
      router.push({
        path: '/provider/apps/' + app.seq + '/appEnv',
      })
      this.$root.$emit('showPageSticky', false, false)
    },
    addShow() {
      this.addAppModel = appModel()
      this.handAddAppMode = !this.handAddAppMode
    },
    cancelMode() {
      this.editable = false
      this.appModel = { ...this.tempAppModel }
    },
    editMode() {
      this.editable = true
    },
    async updateApp() {
      let a = await updateApp({
        appSeq: this.appModel.seq,
        name: this.appModel.name,
        type: this.appModel.type,
        info: this.appModel.info,
      })
      this.appList = this.appList.map((app) => {
        if (app.seq.toString() === a.apps[0].seq.toString()) {
          return a.apps[0]
        } else {
          return app
        }
      })
      this.editable = false
      positiveNotify('修改成功')
    },
    async addAppSubmit() {
      let re = await addApp({
        ...this.addAppModel,
        type: this.addAppModel.type.value,
      })
      this.appList.unshift(...re.apps)
      positiveNotify('添加成功')
      this.handAddAppMode = !this.handAddAppMode
    },
    async delConfirmApp() {
      await delApp(this.appModel.seq)
      this.appList = this.appList.filter((app) => app.seq !== this.appModel.seq)
      this.delAppConfirm = false
      this.appModel = appModel()
      positiveNotify('删除成功')
    },
    delAppModelOpen(app) {
      this.appModel = app
      this.delAppConfirm = true
    },
    mgmtApp(app) {
      this.appModel = { ...app }
      this.tempAppModel = { ...app }
      this.splitterModel = 65
      this.editable = false
    },
  },
  async created() {
    let re = await qryApp()
    this.appList = re.apps
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
