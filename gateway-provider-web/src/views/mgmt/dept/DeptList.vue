<template>
  <div class="q-pa-md">
    <q-splitter v-model="splitterModel" style="height: 100%; min-height: 600px;">
      <template v-slot:before>
        <div class="q-pa-md">
          <div class="doc-heading doc-h1">部门列表</div>
          <div>
            <q-input ref="filter" dense v-model="filter" class="table-head-input">
              <template v-slot:append>
                <q-icon color="primary" name="search" class="primary" @click="resetFilter" />
              </template>
            </q-input>
            <div>
              <q-tree :filter="filter" ref="tree" :nodes="simple" node-key="id" selected-color="primary" :selected.sync="selected" :expanded.sync="expanded">
                <template v-slot:default-header="prop">
                  <div @click="clickTree(prop.node)">
                    <span style="font-size:15px">{{prop.node.deptName}}</span>
                  </div>
                  <div class="text-right">
                    <!-- v-show判断是否等于，是则为true（显示），不是则为false（隐藏） -->
                    <q-icon v-show="prop.node.level === 1 " style="padding-left:10px" color="primary" size="xs" @click="addSubDept(prop.node)" flat name="add_box" />
                    <q-icon v-show="prop.node.children.length === 0" style="padding-left:10px" color="primary" size="xs" @click="delDept(prop.node)" flat name="delete" />
                  </div>
                </template>

              </q-tree>
            </div>
            <div style="padding-top:40px;">
              <p>
                点击<a class="aClass text-h5" @click="addDeptOpen">添加</a>一级部门
              </p>
            </div>
          </div>
        </div>
      </template>
      <template v-slot:after>
        <div class="q-pa-md">
          <div>
            <div v-if="node.id === undefined" style="padding-top:20%" class="row q-col-gutter-sm">
              <q-icon color="grey" size="28px" class="q-ml-sm" name="mdi-set mdi-hand-pointing-left" />
              <span style="color:grey" class="text-h6">选中部门可查看成员信息，以及进行修改</span>
            </div>
            <q-input style="width: 50%" v-show="node.id !== undefined" class="doc-h1" :readonly="!editable" v-model="node.deptName" dense :rules="[ val => val && val.length > 0 || '请输入部门名称' , val => val.length <=30 && val.length > 0 || '部门名长度不超过30' ]">
              <template v-slot:after>
                <!-- 🖊 -->
                <q-btn color="primary" flat size="10px" round icon="edit" @click="editMode()" v-show="!editable" />
                <!-- √ -->
                <q-btn color="primary" @click="updateDeptFrom()" v-show="editable" flat size="10px" round icon="done" />
                <!-- × -->
                <q-btn color="primary" flat size="10px" v-show="editable" round icon="clear" @click="cancelMode()" />
              </template>
            </q-input>
            <div>
              <q-input placeholder="描述" v-show="node.id !== undefined" style="width: 50%;height:50px" filled square bg-color="white" dense hide-bottom-space :disable="!editable" v-model="node.info" type="textarea" />
            </div>
          </div>
          <div style="padding-top:20px" v-if="node.id !== undefined" class="doc-heading doc-h1">
            <p>部门成员</p>
            <div>
              <q-table flat :data="memberList" :columns="columns" :filter="memberFilter" row-key="index" :pagination.sync="pagination" hide-pagination>
                <template v-slot:top-right>
                  <transition-group tag="div" class="row justify-end" enter-active-class="animated slideInLeft" leave-active-class="animated slideOutRight absolute">
                    <q-btn @click="addMemberModalOpened = !addMemberModalOpened" icon="mdi-set mdi-account-plus-outline" flat round color="primary" unelevated key="btn" />
                    <q-select class="fixed-head-input" v-show="addMemberModalOpened" key="input" use-input hide-dropdown-icon transition-show="jump-down" transition-hide="jump-down" v-model="addMemberModel" dense :options="users" @filter="userFilter" @input="handleAddMember" @blur="addMemberModalOpened = false">
                      <template v-slot:no-option>
                        <q-item dense>
                          <q-item-section side>
                            <q-icon name="warning" size="16px" />
                          </q-item-section>
                          <q-item-section>
                            <q-item-label class="text-grey-7">
                              没有可用数据
                            </q-item-label>
                          </q-item-section>
                        </q-item>
                      </template>
                      <template v-slot:option="scope">
                        <q-item v-bind="scope.itemProps" v-on="scope.itemEvents">
                          <q-item-section side>
                            <q-avatar v-if="scope.opt.headIcon === null || scope.opt.headIcon === undefined" size="24px">
                              <q-img :src="require('@/assets/logo.png')" />
                            </q-avatar>
                            <q-avatar v-else size="24px">
                              <q-img :src="staticDomain + scope.opt.headIcon.path + scope.opt.headIcon.local_name" />
                            </q-avatar>
                          </q-item-section>
                          <q-item-section>
                            <q-item-label>{{ scope.opt.userId }}</q-item-label>
                          </q-item-section>
                        </q-item>
                      </template>
                    </q-select>
                  </transition-group>
                </template>
                <template v-slot:body-cell-operation="props">
                  <q-td :class="props.col.__tdClass" class="q-gutter-x-sm">
                    <q-btn flat color="primary" label="删除" @click="delMemberModelOpen(props.row)" />
                  </q-td>
                </template>
              </q-table>
              <div class="q-pa-lg flex flex-center">
                <q-pagination color="primary" v-model="pagination.page" :max="pagesNumber" :direction-links="true">
                </q-pagination>
              </div>
            </div>
          </div>

        </div>
      </template>
    </q-splitter>

    <q-dialog v-model="handAddDeptMode" persistent>
      <q-card style="min-width: 450px">
        <q-toolbar class="bg-primary text-white">
          <q-btn flat v-close-popup round dense icon="arrow_back" />
          <!-- -- -->
          <q-toolbar-title>{{undefined != node && undefined != node.deptName && 0 !== node.children.length && null != node.parent ? node.deptName + "-" : ''}}新增部门</q-toolbar-title>
        </q-toolbar>
        <q-form @submit="addDept" ref="addDeptForm">
          <q-card-section style="width:430px;" class="text-right">
            <q-input filled autofocus v-model="addDeptModel.deptName" type="text" :rules="[ val => val && val.length > 0 || '请输入部门名称' , val => val.length <=30 && val.length > 0 || '部门名长度不超过30' ]">
              <template v-slot:before>
                <span style="font-size:15px; width:80px">名称：</span>
              </template>
            </q-input>
            <q-input filled v-model="addDeptModel.info" type="textarea">
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
    <q-dialog v-model="confirm" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="delete" color="primary" text-color="white" />
          <span class="q-ml-sm" style="padding-left:30px;width: 200px">
            是否确定删除？
          </span>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat label="取消" color="primary" v-close-popup />
          <q-btn flat label="确定" color="primary" @click="delConfirmDept()" />
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-dialog v-model="delMemberConfirm" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="delete" color="primary" text-color="white" />
          <span class="q-ml-sm" style="padding-left:30px;width: 200px">
            是否确定删除？
          </span>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat label="取消" color="primary" v-close-popup />
          <q-btn flat label="确定" color="primary" @click="delConfirmMember()" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>
<script>
import { deptModel, dic } from '@/model'
import {
  addDept,
  qryDept,
  delDept,
  updateDept,
  qryDeptMember,
  delDeptMember,
  addDeptMember,
} from '@/services/dept'
import { qryUser } from '@/services/user'
import { positiveNotify, formatDicDisplay } from '@/utils/utils'
export default {
  name: 'DeptList',
  data() {
    return {
      delMemberConfirm: false, //删除成员的界面
      staticDomain: 'http://localhost',
      users: [],
      addMemberModel: '',
      addMemberModalOpened: false, //增加部门成员的界面
      memberList: [],
      memberFilter: '',
      pagination: {
        sortBy: 'desc',
        descending: false,
        page: 0,
        rowsPerPage: 5,
      },
      splitterModel: 25,
      selected: '',
      filter: '',
      handAddDeptMode: false, //增加一级部门的界面
      confirm: false, //删除部门的界面
      editable: false, //部门编辑界面。。。笔
      addDeptModel: deptModel(),
      simple: [],
      expanded: [],
      node: {},
      tempNode: {},
      members: [],
      columns: [
        {
          name: 'userId',
          label: '用户ID',
          align: 'center',
          field: (row) => row.userId,
        },
        {
          name: 'email',
          label: '邮箱',
          align: 'center',
          field: (row) => row.email,
        },
        {
          name: 'roleName',
          label: '角色名称',
          align: 'center',
          field: (row) => row.roleInfo.roleName,
        },
        {
          name: 'userState',
          label: '状态',
          align: 'center',
          field: (row) => formatDicDisplay(dic.userState, row.userState),
        },
        {
          name: 'operation',
          align: 'center',
          label: '操作',
          field: (row) => row,
        },
      ],
      userOptions: [],
      memberModel: {},
    }
  },
  computed: {
    pagesNumber() {
      return Math.ceil(this.memberList.length / this.pagination.rowsPerPage)
    },
  },
  methods: {
    formatDicDisplay,
    async delConfirmMember() {
      let r = await delDeptMember(this.memberModel)
      this.memberList = r.members
      this.delMemberConfirm = false
      positiveNotify('删除成功!')
    },
    delMemberModelOpen(user) {
      this.memberModel = {
        deptSeq: this.node.seq,
        memberSeq: user.seq,
      }
      this.delMemberConfirm = true
    },
    async handleAddMember(user) {
      this.addMemberModel = ''
      let r = await addDeptMember({
        deptSeq: this.node.seq,
        memberSeq: user.seq,
      })
      this.memberList = r.members
      positiveNotify('添加成功!')
    },
    userFilter(val, update, abort) {
      if (val === '') {
        return abort()
      }
      update(() => {
        this.users = this.userOptions.filter(
          (user) => user.userId.toLowerCase().indexOf(val.toLowerCase()) > -1
        )
      })
    },
    resetFilter() {
      this.filter = ''
      this.$refs.filter.focus()
    },
    async updateDeptFrom() {
      await updateDept({
        seq: this.node.seq,
        deptName: this.node.deptName,
        info: this.node.info,
        status: this.node.status,
      })
      positiveNotify('更新成功!')
      this.editable = false
      this.createDeptTree()
    },
    editMode() {
      this.editable = true
      this.tempNode = { ...this.node }
    },
    cancelMode() {
      this.editable = false
      this.node = this.tempNode
      this.createDeptTree()
    },
    async clickTree(node) {
      this.editable = false
      this.node = node
      let re = await qryDeptMember({ deptSeq: this.node.seq })
      this.memberList = re.members
    },
    //增加二级部门
    addSubDept(node) {
      this.node = node
      this.addDeptOpen()
      this.addDeptModel.parentSeq = node.seq
    },
    async delConfirmDept() {
      await delDept(this.node.seq)
      this.createDeptTree()
      positiveNotify('删除成功!')
      this.confirm = false
      this.node = []
    },
    async delDept(node) {
      this.confirm = true
      this.node = node
    },
    addDeptOpen() {
      this.addDeptModel = {}
      this.handAddDeptMode = true
    },
    async addDept() {
      await addDept(this.addDeptModel)
      this.handAddDeptMode = false
      this.createDeptTree()
      positiveNotify('添加成功!')
    },
    async createDeptTree() {
      let a = await qryDept({})
      let depts = a.depts
      depts = depts.map((dept) => {
        return {
          ...dept,
          parentSeq: null != dept.parent ? dept.parent.seq : null,
        }
      })
      this.simple = this.subTree(null, depts, 1)
    },
    subTree(parent, list, level) {
      let sub = []
      list.forEach((el) => {
        let re = { ...el }
        re.level = level
        re.id = 'dept_' + el.seq
        re.label = el.deptName
        this.expanded.push(re.id)
        if (parent === el.parentSeq) {
          re.children = this.subTree(el.seq, list, level + 1)
          sub.push(re)
        }
      })
      return sub
    },
  },
  async created() {
    this.createDeptTree()
    this.userOptions = await qryUser({})
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
</style>
