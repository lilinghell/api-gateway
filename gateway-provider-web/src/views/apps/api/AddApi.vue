<template>
  <div class="q-pa-md">
    <q-dialog v-model="handAddApiMode" persistent>
      <q-card style="min-width: 500px; height:550px;">
        <q-toolbar class="bg-primary text-white">
          <q-btn flat v-close-popup round dense icon="arrow_back" />
          <q-toolbar-title>创建新的API</q-toolbar-title>
        </q-toolbar>
        <q-form @submit="addApi" ref="addApiForm">
          <q-card-section class="text-left">
            <p class="doc-note_title text-primary">输入API信息</p>
            <div>名称</div>
            <q-input square filled dense v-model="addApiModel.name" placeholder="" type="text" :rules="[ val => val && val.length > 0 || '请输入API名称']" />
            <div class="row">
              <div style="width:50%">
                <div>版本</div>
                <q-input style="width:90%" square filled dense v-model="addApiModel.version" placeholder="e.g. 1.0.0" type="text" :rules="[ val => val && val.length > 0 || '请输入API版本']" />
              </div>
              <div style="width:50%">
                <div>method</div>
                <q-select style="width:90%" emit-value square filled :display-value="formatDicDisplay(apiMethodType, addApiModel.method)" v-model="addApiModel.method" :options="apiMethodType" dense />
              </div>
            </div>
          </q-card-section>
          <q-separator />
          <q-card-section class="text-left">
            <p class="doc-note_title text-primary">输入schema信息</p>
            <div class="row">
              <div style="width:50%">
                <div>schema类型</div>
                <q-select emit-value style="width:90%" square filled :display-value="formatDicDisplay(apiSchemaType, addApiModel.schemaType)" v-model="addApiModel.schemaType" :options="apiSchemaType" dense />
              </div>
              <div style="width:50%;">
                <div>schema格式</div>
                <q-select emit-value style="width:90%" square filled :display-value="formatDicDisplay(apiSchemaFormat, addApiModel.schemaFormat)" v-model="addApiModel.schemaFormat" :options="apiSchemaFormat" dense />
              </div>
            </div>
            <div style="width:45%">
              <p />
              <div>导入 API schema (可选)</div>
              <q-file dense color="teal" filled>
                <template v-slot:prepend>
                  <q-icon name="cloud_upload" />
                </template>
              </q-file>
            </div>
          </q-card-section>
          <q-separator />
          <q-card-actions align="right">
            <q-btn style="width:80px" type="submit" color="primary" label="提交" />
            <q-btn style="width:80px" v-close-popup flat color="primary" label="取消" class="q-ml-sm" />
          </q-card-actions>
        </q-form>
      </q-card>
    </q-dialog>
  </div>
</template>
<script>
import { dic, apiModel } from '@/model'
import { formatDicDisplay, positiveNotify } from '@/utils/utils'
import { addApi } from '@/services/api'
import router from '@/router'

export default {
  name: 'AddApi',
  components: {},
  props: {
    apiGroupSeq: {
      type: String,
    },
    apiFrom: {
      type: String,
    },
  },
  computed: {
    appId() {
      return this.$route.params.appId
    },
    appEnvId() {
      return this.$route.params.appEnvId
    },
  },
  watch: {},
  data() {
    return {
      handAddApiMode: false,
      addApiModel: apiModel(),
      apiSchemaType: dic.apiSchemaType,
      apiSchemaFormat: dic.apiSchemaFormat,
      apiMethodType: dic.apiMethodType,
    }
  },
  methods: {
    formatDicDisplay,
    showAddApiMode() {
      this.handAddApiMode = true
      this.addApiModel = apiModel()
    },
    async addApi() {
      let r = await addApi({
        ...this.addApiModel,
        groupSeq: this.addApiModel.group != null ? this.addApiModel.group.seq : this.apiGroupSeq,
        appEnvSeq: this.appEnvId,
        url: this.addApiModel.name,
      })

      if (this.apiFrom === 'api') {
        this.$parent.createApis(r.api)
        this.$parent.apiSelectType = 'overView'
      } else {
        this.$parent.$refs.apiGroupRef.createApis(r.api)
        // router.push({
        //   path: '/provider/apps/' + this.appId + '/' + this.appEnvId + '/apis',
        //   query: {
        //     apiSeq: r.api.seq,
        //     groupSeq: this.apiGroupSeq,
        //   },
        // })
      }

      this.$root.$emit('showPageSticky', false, false)
      this.handAddApiMode = false
      positiveNotify('添加成功')
    },
  },
  async created() {},
}
</script>
<style lang="sass" scoped>
@import "~@/styles/quasar.sass"
.doc-heading
  color: $primary
.doc-h1
  font-size: 1.5rem
  line-height: 2rem
  font-weight: 500
.aClass
  cursor: pointer
  color: $primary
.sizeSeven
  font-size: 7px
.doc-note_title
  font-weight: 500
</style>
