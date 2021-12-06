<template>
  <div class="q-pa-md">
    <q-splitter separator-class="bg-white" v-model="splitterModel" style="height: 100%; min-height: 600px;">
      <template v-slot:before>
        <div class="q-pa-md">
          <div class="doc-heading doc-h1">服务标签列表</div>
          <div class="q-my-md">
            <q-table flat :data="serviceTagList" :columns="columns" :filter="filter" row-key="index" :pagination.sync="pagination" hide-pagination>
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
                <q-td :class="props.col.__tdClass">
                  <q-badge align="middle" :style="`background: ${props.row.color}`" class="q-mr-sm">{{ props.row.color }}</q-badge>{{ props.row.name }}
                </q-td>
              </template>
              <template v-slot:body-cell-operation="props">
                <q-td :class="props.col.__tdClass" class="q-gutter-x-sm">
                  <q-btn flat color="primary" label="删除" @click="delShow(props.row)" />
                  <q-btn flat color="primary" label="API场景" @click="apiShow(props.row)" />
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
          <div v-show="serviceTagModel.seq === ''" class="doc-heading doc-h1">API场景列表</div>
          <div>
            <q-form @submit="updateServiceTagSubmit" ref="updateServiceTagFrom">
              <q-toolbar class="text-primary">
                <q-input style="width:100%" v-show="serviceTagModel.seq !== ''" class="doc-h1" :readonly="!editable" v-model="serviceTagModel.name" dense :rules="[ val => val && val.length > 0 || '请输入名称' ,val => val.length <= 20 && val.length > 0 || '不能超过20' ]">
                  <template v-slot:after>
                    <q-btn color="primary" flat size="10px" round icon="edit" @click="editable = true" v-show="!editable" />
                    <q-btn color="primary" type="submit" v-show="editable" flat size="10px" round icon="done" @click="updateServiceTagSubmit"/>
                    <q-btn color="primary" flat size="10px" v-show="editable" round icon="clear" @click="cancelMode()" />
                  </template>
                </q-input>
              </q-toolbar>
              <div v-if="serviceTagModel.seq === ''" class="row q-col-gutter-sm">
                <q-icon color="grey" size="23px" class="q-ml-sm" name="mdi-set mdi-hand-pointing-left" />
                <span style="font-size:15px">选择一个标签,点击API场景</span>
              </div>
              <div v-else>
                <div class="q-pa-md row q-col-gutter-sm" style="width: 100%">
                  <div class="col-12 col-sm-16 q-gutter-sm">
                    <span v-if="tagApiList.length ===0" style="font-size:15px">暂无</span>
                    <q-scroll-area :thumb-style="thumbStyle" :bar-style="barStyle" style="height: 800px; max-width: 100%;">
                      <div class="q-col-gutter-sm">
                        <div v-for="(api,index) in tagApiList" :key="`ticked-${index}`">
                          <div class="row">
                            <div style="width: 30%">{{ api.apiName }}</div>
                            <div style="width: 70%">{{ api.apiUrl }}</div>
                          </div>
                        </div>
                      </div>
                    </q-scroll-area>
                  </div>
                </div>
              </div>
            </q-form>
          </div>
        </div>
      </template>

    </q-splitter>
    <q-dialog v-model="handAddServiceTagMode" persistent>
      <q-card style="min-width: 350px">
        <q-toolbar class="bg-primary text-white">
          <q-btn flat v-close-popup round dense icon="arrow_back" />
          <q-toolbar-title>新增服务标签</q-toolbar-title>
        </q-toolbar>
        <q-form @submit="addServiceTagSubmit" ref="addServiceTagForm">
          <q-card-section>
            <q-input label="标签名称" v-model="addServiceTagModel.name" type="text" autofocus dense :rules="[ val => val && val.length > 0 || '请输入标签名称' , val => val.length <= 20 && val.length > 0 || '标签名称不能超过20']" />
            <div class="q-gutter-x-sm">
              <q-btn v-for="each in serviceTagColors" :key="each.label" :style="`background: ${each.value}`" class="label-btn" @click="addServiceTagModel.color = each.value">
                <transition enter-active-class="animated fadeIn" leave-active-class="animated fadeOut">
                  <q-icon name="done" class="text-weight-bolder" color="white" v-show="addServiceTagModel.color === each.value" />
                </transition>
              </q-btn>
            </div>
          </q-card-section>
          <q-card-actions align="right" class="text-primary">
            <q-btn flat label="取消" v-close-popup />
            <q-btn flat label="保存" type="submit" />
          </q-card-actions>
        </q-form>
      </q-card>
    </q-dialog>
    <q-dialog v-model="delConfirm" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="delete" color="primary" text-color="white" />
          <span class="q-ml-sm" style="padding-left:30px;width: 200px">
            是否确定删除？
          </span>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat label="取消" color="primary" v-close-popup />
          <q-btn flat label="确定" color="primary" @click="delConfirmServiceTag()" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>
<script>
  import { serviceTagModel, dic } from '@/model'
  import { positiveNotify, formatDicDisplay } from '@/utils/utils'
  import { qryServiceTag, addServiceTag, delServiceTag ,updateServiceTag, qryApiByTag} from '@/services/tag'
  import { date } from 'quasar'
  export default {
    name: 'SceneList',
    data() {
      return {
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
        serviceTagColors: dic.serviceTagColors,
        splitterModel: 75,
        delConfirm: false,
        handAddServiceTagMode: false,
        editable: false,
        delServiceTagModel: serviceTagModel(),
        serviceTagModel: serviceTagModel(),
        tmpServiceTagModel: serviceTagModel(),
        addServiceTagModel: serviceTagModel(),
        serviceTagList: [],
        tagApiList: [],
        columns: [
          {
            name: 'name',
            label: '标签名',
            align: 'center',
            field: (row) => row.name,
            style: 'width: 45%',
          },
          {
            name: 'updateTime',
            label: '更新时间',
            align: 'center',
            field: (row) => date.formatDate(row.updateTime, 'YYYY/MM/DD HH:mm:ss'),
            style: 'width: 45%',
          },
          {
            name: 'operation',
            align: 'center',
            label: '操作',
            field: (row) => row,
            style: 'width: 45%',
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
        return Math.ceil(this.serviceTagList.length / this.pagination.rowsPerPage)
      },
      appId() {
        return this.$route.params.appId
      },
    },
    methods: {
      formatDicDisplay,
      addShow() {
        this.addServiceTagModel = serviceTagModel()
        this.handAddServiceTagMode = true
      },
      async apiShow(row) {
        this.serviceTagModel = { ...row }
        this.tmpServiceTagModel = { ...row }
        this.tagApiList = await qryApiByTag({
          serviceTagSeq: row.seq,
          appSeq: this.appId,
        })
        this.splitterModel = 60
        this.editable = false
      },
      delShow(tag) {
        this.delServiceTagModel = tag
        this.delConfirm = true
      },
      async delConfirmServiceTag() {
        await delServiceTag(this.delServiceTagModel.seq)
        this.delConfirm = false
        this.serviceTagList = this.serviceTagList.filter((tag) => tag.seq != this.delServiceTagModel.seq)
        positiveNotify('删除成功')
      },
      async updateServiceTagSubmit() {
        let re = await updateServiceTag({
          seq: this.serviceTagModel.seq,
          name: this.serviceTagModel.name
        } )
        this.serviceTagList = this.serviceTagList.map((item) => {
          if(item.seq.toString() === re.seq.toString()){
            return {...re}
          }else{
            return item;
          }
        })
        positiveNotify('修改成功')
        this.editable = false
      },
      async addServiceTagSubmit() {
        let re = await addServiceTag({
          ...this.addServiceTagModel,
          appSeq: this.appId,
        })
        this.serviceTagList.unshift(...re.serviceTags)
        this.handAddServiceTagMode = false
        positiveNotify('添加成功')
      },
      cancelMode() {
        this.serviceTagModel = { ...this.tmpServiceTagModel }
        this.editable = false
      },
    },
    async created() {
      let re = await qryServiceTag({ appSeq: this.appId })
      this.serviceTagList = re.serviceTags
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
  .label-btn
    width: 32px
    height: 32px
    min-height: 32px
</style>
