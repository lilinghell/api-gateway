<template>
  <div class="q-pa-md">
    <q-splitter v-model="splitterModel" style="height: 100%; min-height: 600px;">
      <template v-slot:before>
        <div class="q-pa-md">
          <div class="doc-heading doc-h1">{{appEnv.name}}-API组</div>
          <div>
            <q-input ref="filter" dense v-model="filter" class="table-head-input">
              <template v-slot:append>
                <q-icon color="primary" name="search" class="primary" @click="resetFilter" />
              </template>
            </q-input>
            <div>
              <q-tree :filter="filter" ref="tree" :nodes="simple" node-key="id" selected-color="primary" :selected.sync="selected" :expanded.sync="expanded">
                <template v-slot:default-header="prop">
                  <div v-on:mouseover="showMenu(prop.node)" v-on:mouseout="hiddenMenu(prop.node)" @click="clickTree(prop.node)" style="width:90%;height:100%">
                    <span style="font-size:15px">{{prop.node.name}}</span>
                  </div>
                  <div v-on:mouseover="showMenu(prop.node)" v-on:mouseout="hiddenMenu(prop.node)" style="width:10%;height:100%;" class="text-right">
                    <div :id="'node_' + prop.node.seq" style="display:none">
                      <q-btn color="primary" size="xs" flat round dense icon="more_horiz">
                        <q-menu v-on:mouseover="showMenu(prop.node)" v-on:mouseout="hiddenMenu(prop.node)" transition-show="jump-down" transition-hide="jump-up">
                          <q-list style="min-width: 100px">
                            <q-item clickable @click="editApiGroupOpen(prop.node)" v-close-popup>
                              <q-item-section>编辑</q-item-section>
                            </q-item>
                            <q-item clickable v-if="prop.node.parent === null" @click="addApiGroupOpen(prop.node, 2)" v-close-popup>
                              <q-item-section>添加子组</q-item-section>
                            </q-item>
                            <q-separator />
                            <q-item clickable @click="addApiOpen(prop.node)" v-close-popup>
                              <q-item-section>添加API</q-item-section>
                            </q-item>
                            <q-separator />
                            <q-item clickable @click="fuzApiGroup(prop.node)" v-close-popup>
                              <q-item-section>复制</q-item-section>
                            </q-item>
                            <q-item clickable @click="delApiGroupOpen(prop.node)" v-close-popup>
                              <q-item-section>删除</q-item-section>
                            </q-item>

                          </q-list>
                        </q-menu>
                      </q-btn>
                    </div>
                  </div>
                </template>
              </q-tree>
            </div>
            <div style="padding-top:40px;">
              <p v-if="appEnv.type === 'DEV'">
                点击<a class="aClass text-h5" @click="addApiGroupOpen(null, 2)">添加</a>一级API组
              </p>
            </div>
          </div>
        </div>
      </template>
      <template v-slot:after>
        <div>
          <ApiGroupInfo v-show="apiGroupInfoOpened" :apiGroupModel="apiGroupModel" ref="apiGroupRef" />
        </div>
      </template>
    </q-splitter>
    <q-dialog v-model="handAddApiGroupMode" persistent>
      <q-card style="min-width: 450px">
        <q-toolbar class="bg-primary text-white">
          <q-btn flat v-close-popup round dense icon="arrow_back" />
          <q-toolbar-title>新增API组</q-toolbar-title>
        </q-toolbar>
        <q-form @submit="addApiGroup" ref="addApiGroupForm">
          <q-card-section style="width:430px;" class="text-right">
            <q-input v-model="addApiGroupModel.name" type="text" :rules="[ val => val && val.length > 0 || '']">
              <template v-slot:before>
                <span style="font-size:15px; width:90px"><span style="color:red">*</span>分组名称：</span>
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
    <q-dialog v-model="delConfirm" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="delete" color="primary" text-color="white" />
          <span class="q-ml-sm">确定删除:<span class="doc-heading">{{apiGroupModel.name}}</span>,若该组下有API,则不可删除.</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn label="删除" @click="delApiGroup" color="primary" v-close-popup />
          <q-btn flat label="取消" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
    <AddApi :apiGroupSeq="(apiGroupModel.seq).toString()" ref="addApiRef" />
  </div>
</template>
<script>
import { qryAppEnv } from '@/services/app'
import { apiGroupModel, apiModel, appEnvModel } from '@/model'
import { positiveNotify, formatDicDisplay } from '@/utils/utils'
import { qryApiGroup, addApiGroup, delApiGroup } from '@/services/api'
import ApiGroupInfo from '@/views/apps/api/ApiGroupInfo.vue'
import AddApi from '@/views/apps/api/AddApi.vue'
export default {
  name: 'ApiGroup',
  components: {
    ApiGroupInfo,
    AddApi,
  },
  data() {
    return {
      groupLevelFlag: 1,
      delConfirm: false,
      apiGroupInfoOpened: true,
      splitterModel: 25,
      simple: [],
      selected: '',
      filter: '',
      expanded: [],
      handAddApiGroupMode: false,
      addApiGroupModel: apiGroupModel(),
      apiGroupModel: apiGroupModel(),
      tempGroupSeq: '',
      parentGroup: null,
      appEnv: appEnvModel(),
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
    formatDicDisplay,
    fuzApiGroup(node) {
      this.addApiGroupModel = {
        ...node,
        name: 'new apiGroup',
      }
      this.addApiGroup()
    },
    editApiGroupOpen(node) {
      this.$refs.apiGroupRef.apiGroupSelectType = 'overView'
      this.clickTree(node)
    },
    delApiGroupOpen(node) {
      this.delConfirm = true
      this.clickTree(node)
    },
    async delApiGroup() {
      await delApiGroup(this.apiGroupModel.seq, this.appEnvId)
      this.createApiGroupsTree()
      this.apiGroupModel = apiGroupModel()
      positiveNotify('删除成功')
    },
    addApiOpen(node) {
      this.$refs.addApiRef.showAddApiMode()
      this.apiGroupModel = { ...node }
    },
    showMenu(node) {
      if (node.seq != -1 && this.appEnv.type === 'DEV') {
        document.getElementById('node_' + node.seq).style.display = 'block'
      }
    },
    hiddenMenu(node) {
      if (node.seq != -1) {
        document.getElementById('node_' + node.seq).style.display = 'none'
      }
    },
    resetFilter() {
      this.filter = ''
      this.$refs.filter.focus()
    },
    async clickTree(node) {
      this.editable = false
      this.apiGroupModel = { ...node }
      this.apiGroupInfoOpened = true
      this.$refs.apiGroupRef.qryApi(node)
      if (node.seq === -1) {
        this.$refs.apiGroupRef.apiGroupSelectType = 'inters'
      }
    },
    addApiGroupOpen(parentGroup, groupLevelFlag) {
      this.groupLevelFlag = groupLevelFlag
      this.parentGroup = parentGroup
      this.addApiGroupModel = apiGroupModel()
      this.handAddApiGroupMode = true
    },
    async addApiGroup() {
      let r = await addApiGroup({
        ...this.addApiGroupModel,
        parent: this.parentGroup,
        appSeq: this.appId,
      })
      positiveNotify('添加成功')
      //this.simple.push(r.apiGroup)
      this.createApiGroupsTree(r.apiGroup.seq)
      this.handAddApiGroupMode = false
    },
    async createApiGroupsTree(groupSeq) {
      if (groupSeq != undefined) {
        this.tempGroupSeq = groupSeq
      }
      let a = await qryApiGroup({ appSeq: this.appId })
      let groups = a.apiGroups
      groups = groups.map((group) => {
        return {
          ...group,
          parentSeq: null != group.parent ? group.parent.seq : null,
        }
      })
      groups.unshift({
        ...apiGroupModel(),
        seq: -1,
        name: '所有接口',
        parentSeq: null,
        appEnvSeq: this.appEnvId,
      })
      this.simple = this.subTree(null, groups, 1)
    },
    subTree(parent, list, level) {
      let sub = []
      list.forEach((el) => {
        let re = { ...el }

        re.level = level
        re.id = 'apiGroup_' + el.seq
        re.label = el.name
        this.expanded.push(re.id)

        if (parent === el.parentSeq) {
          re.children = this.subTree(el.seq, list, level + 1)
          sub.push(re)
          if (this.tempGroupSeq === el.seq + '') {
            this.clickTree(re)
            this.selected = 'apiGroup_' + el.seq
          }
        }
      })
      return sub
    },
    async init() {
      let groupSeq = this.$route.query.groupSeq === undefined ? '-1' : this.$route.query.groupSeq
      this.tempGroupSeq = groupSeq
      let re = await qryAppEnv({
        appSeq: this.appId,
        appEnvSeq: this.appEnvId,
      })
      this.appEnv = re.appEnvs[0]

      this.createApiGroupsTree()
    },
  },
  async created() {
    this.init()
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
