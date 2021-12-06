<template>
  <div class="q-pa-md">
    <div class="doc-heading doc-h1">参数列表</div>
    <div class="q-my-md">
      <q-table flat :data="parameterList" :columns="columns" :filter="filter" row-key="index" :pagination.sync="pagination" hide-pagination>
        <template v-slot:top-left>
          <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
            <template v-slot:append>
              <q-icon name="search" color="primary" />
            </template>
          </q-input>
        </template>
        <template v-slot:top-right>
          <q-btn color="primary" unelevated class="table-head-btn" @click="addModel">
            新增
            <q-icon name="add" class="q-ml-sm" />
          </q-btn>
        </template>
        <template v-slot:body-cell-operation="props">
          <q-td :class="props.col.__tdClass" class="q-gutter-x-sm">
            <q-btn flat color="primary" label="修改" @click="updateParameterModelOpen(props.row)" />
            <q-btn :disabled="props.row.enName === 'X-Request-EnvKey'" flat color="primary" label="删除" @click="delParameterModelOpen(props.row)" />
          </q-td>
        </template>
      </q-table>
      <div class="q-pa-lg flex flex-center">
        <q-pagination color="primary" v-model="pagination.page" :max="pagesNumber" :direction-links="true">
        </q-pagination>
      </div>
    </div>
    <q-dialog v-model="handAddParameterMode" persistent>
      <q-card style="min-width: 500px">
        <q-toolbar class="bg-primary text-white">
          <q-btn flat v-close-popup round dense icon="arrow_back" />
          <q-toolbar-title>新增参数变量</q-toolbar-title>
        </q-toolbar>
        <q-form @submit="addParameterSubmit" ref="addEnvForm">
          <q-card-section style="width:95%" class="text-right">
            <q-input dense filled v-model="parameterModel.name" type="text" :rules="[ val => val && val.length > 0 || '']">
              <template v-slot:before>
                <span style="font-size:15px; width:80px">名称：</span>
              </template>
            </q-input>
            <q-input dense filled v-model="parameterModel.enName" type="text" :rules="[ val => val && val.length > 0 || '']">
              <template v-slot:before>
                <span style="font-size:15px; width:80px">英文标识：</span>
              </template>
            </q-input>
            <q-input dense filled v-model="parameterModel.value" type="text" :rules="[ val => val && val.length > 0 || '']">
              <template v-slot:before>
                <span style="font-size:15px; width:80px">值：</span>
              </template>
            </q-input>
            <div class="row">
              <span class="text-grey-7" style="font-size:15px; width:80px;">类型：</span>
              <q-list class="row">
                <q-item tag="label" v-ripple>
                  <q-item-section avatar>
                    <q-radio v-model="parameterModel.type" val="0" color="orange">
                    </q-radio>
                  </q-item-section>
                  <q-item-section class="text-left">
                    <q-item-label>常量</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
          </q-card-section>
          <q-card-actions align="right">
            <q-btn style="width:80px" type="submit" color="primary" label="提交" />
            <q-btn style="width:80px" v-close-popup flat color="primary" label="取消" class="q-ml-sm" />
          </q-card-actions>
        </q-form>
      </q-card>
    </q-dialog>
    <q-dialog v-model="delParameterConfirm" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="delete" color="primary" text-color="white" />
          <span class="q-ml-sm" style="padding-left:30px;width: 200px">
            是否确定删除？
          </span>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat label="取消" color="primary" v-close-popup />
          <q-btn flat label="确定" color="primary" @click="delConfirmParameter()" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>
<script>
import { parameterModel, dic } from '@/model'
import { positiveNotify, formatDicDisplay } from '@/utils/utils'
import { qryParameter, addParameter, delParameter, updateParameter } from '@/services/test'
export default {
  name: 'ParameterList',
  data() {
    return {
      handAddParameterMode: false,
      parameterModel: parameterModel(),
      delParameterConfirm: false,
      handAddEnvMode: false,
      parameterList: [],
      delParameterMode: parameterModel(),
      addParameterFlag: false,
      columns: [
        {
          name: 'key',
          label: 'key',
          align: 'center',
          field: (row) => row.seq,
          style: 'width: 5%',
        },
        {
          name: 'name',
          label: '名称',
          align: 'center',
          field: (row) => row.name,
          style: 'width: 20%',
        },
        {
          name: 'enName',
          label: '英文标识',
          align: 'center',
          field: (row) => row.enName,
          style: 'width: 20%',
        },
        {
          name: 'value',
          label: '值',
          align: 'center',
          field: (row) => row.value,
          style: 'width: 20%',
        },
        {
          name: 'type',
          label: '类型',
          align: 'center',
          field: (row) => formatDicDisplay(dic.parameterType, row.type),
          style: 'width: 5%',
        },
        {
          name: 'operation',
          align: 'center',
          label: '操作',
          field: (row) => row,
          style: 'width: 30%',
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
      return Math.ceil(this.parameterList.length / this.pagination.rowsPerPage)
    },
    appId() {
      return this.$route.params.appId
    },
  },
  methods: {
    addModel() {
      this.parameterModel = parameterModel()
      this.handAddParameterMode = true
      this.addParameterFlag = true
    },
    async addParameterSubmit() {
      if (this.addParameterFlag) {
        let re = await addParameter({
          ...this.parameterModel,
          appSeq: this.appId,
        })
        this.parameterList.unshift(re)
        this.handAddParameterMode = false
        positiveNotify('添加成功')
      } else {
        let re = await updateParameter({
          ...this.parameterModel,
          appSeq: this.appId,
        })
        this.parameterList = this.parameterList.map((pa) => {
          if (re.seq.toString() === pa.seq.toString()) {
            return re
          } else {
            return pa
          }
        })
        this.handAddParameterMode = false
        positiveNotify('修改成功')
      }
    },
    delParameterModelOpen(row) {
      this.delParameterConfirm = true
      this.delParameterMode = row
    },
    async delConfirmParameter() {
      await delParameter({
        seq: this.delParameterMode.seq,
        appSeq: this.appId,
      })
      this.delParameterConfirm = false
      this.parameterList = this.parameterList.filter(
        (pa) => pa.seq.toString() !== this.delParameterMode.seq.toString()
      )
      positiveNotify('删除成功')
    },
    updateParameterModelOpen(row) {
      this.handAddParameterMode = true
      this.parameterModel = row
      this.addParameterFlag = false
    },
  },
  async created() {
    let response = await qryParameter({ appSeq: this.appId })
    this.parameterList = response.parameters
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
