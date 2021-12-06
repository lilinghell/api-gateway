<template>
  <div class="q-pa-md">
    <q-splitter separator-class="bg-white" v-model="splitterModel" style="height: 100%; min-height: 600px;">
      <template v-slot:before>
        <div class="q-pa-md">
          <div class="doc-heading doc-h1">角色列表</div>
          <div class="q-my-md">
            <q-table flat :data="roleList" :columns="columns" :filter="filter" row-key="index" :pagination.sync="pagination" hide-pagination>
              <template v-slot:top-left>
                <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
                  <template v-slot:append>
                    <q-icon name="search" color="primary" />
                  </template>
                </q-input>
              </template>
              <template v-slot:top-right>
                <q-btn color="primary" unelevated class="table-head-btn" @click="addRoleModelOpen">
                  新增
                  <q-icon name="add" class="q-ml-sm" />
                </q-btn>

              </template>

              <template v-slot:body-cell-operation="props">
                <q-td :class="props.col.__tdClass" class="q-gutter-x-sm">
                  <q-btn flat color="primary" label="删除" @click="delRoleModelOpen(props.row)" />
                  <q-btn flat color="primary" label="管理" @click="mgmtRole(props.row)" />
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
          <div v-show="roleModel.length === 0" class="doc-heading doc-h1">权限</div>
          <div>
            <q-form @submit="updateRole" ref="updateRoleFrom">
              <q-toolbar class="text-primary">
                <q-input v-show="roleModel.length !== 0" class="doc-h1" :readonly="!editable" v-model="roleModel.roleName" dense :rules="[ val => val && val.length > 0 || '请输入角色名称'
                  ,val => val.length<=20 && val.length > 0 || '角色长度为1~20']" />
                <q-btn flat size="12px" round icon="edit" @click="editMode()" v-show="editable2" />
                <template v-if="editable">
                  <q-btn type="submit" flat size="12px" round icon="done" />
                  <q-btn flat size="12px" round icon="clear" @click="cancelMode()" />
                </template>
              </q-toolbar>
              <q-scroll-area style="height: 590px;">
                <div class="row q-col-gutter-sm">
                  <q-tree ref="qTree" :nodes="simple" no-nodes-label="请在左边选择一个角色,点击管理" node-key="name" tick-strategy="leaf" :ticked.sync="ticked" :expanded.sync="expanded" />
                </div>
              </q-scroll-area>
            </q-form>
          </div>
        </div>
      </template>

    </q-splitter>
    <q-dialog v-model="prompt" persistent>
      <q-card style="min-width: 350px">
        <q-toolbar class="bg-primary text-white">
          <q-btn flat v-close-popup round dense icon="arrow_back" />
          <q-toolbar-title>新增角色</q-toolbar-title>
        </q-toolbar>
        <q-form @submit="addRole" ref="addRoleForm">
          <q-card-section>
            <div class="text-h6">角色名</div>
          </q-card-section>
          <q-card-section class="q-pt-none">
            <q-input :rules="[ val => val && val.length > 0 || '请输入角色名称'
                  ,val => val.length<=20 && val.length > 0 || '角色长度为1~20']" dense v-model="addRoleModel.roleName" autofocus />
          </q-card-section>
          <q-card-actions align="right" class="text-primary">
            <q-btn flat label="取消" v-close-popup />
            <q-btn flat label="保存" type="submit" />
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
          <q-btn flat label="确定" color="primary" @click="delRole()" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>

</template>

<script>
import { qryRole, updateRole, addRole, delRole } from '@/services/role'
import { roleModel } from '@/model'
import { getMenuList, positiveNotify } from '@/utils/utils'
export default {
  name: 'RoleList',
  data() {
    return {
      confirm: false,
      tempRoleName: '',
      tempSimple: [],
      tempTicked: [],
      editable2: false,
      editable: false,
      roleModel: roleModel,
      addRoleModel: roleModel,
      delRoleModel: roleModel,
      simple: [],
      ticked: [],
      expanded: [],
      pagination: {
        sortBy: 'desc',
        descending: false,
        page: 0,
        rowsPerPage: 5,
      },
      splitterModel: 75,
      filter: '',
      columns: [
        {
          name: 'index',
          label: '序号',
          align: 'center',
          field: (row) => row.index,
          style: 'width: 10%',
        },
        {
          name: 'roleName',
          label: '角色名称',
          align: 'center',
          field: (row) => row.roleName,
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
      roleList: [],
      allRouters: [],
      prompt: false,
    }
  },
  computed: {
    pagesNumber() {
      return Math.ceil(this.roleList.length / this.pagination.rowsPerPage)
    },
  },
  methods: {
    async addRole() {
      const re = await addRole(this.addRoleModel)
      this.roleList.unshift(re)
      this.roleList = this.roleList.map((row, index) => {
        if (row.seq === re.seq) {
          this.mgmtRole(row)
        }
        return {
          ...row,
          index: index + 1,
        }
      })
      this.prompt = false

      positiveNotify('添加成功！')
    },
    addRoleModelOpen() {
      this.addRoleModel = []
      this.prompt = true
    },
    async delRole() {
      this.confirm = false
      await delRole(this.delRoleModel.seq)
      this.roleList = this.roleList
        .filter((item) => item.seq !== this.delRoleModel.seq)
        .map((row, index) => {
          return { ...row, index: index + 1 }
        })
      positiveNotify('删除成功！')
      this.clearAll()
    },
    delRoleModelOpen(role) {
      this.confirm = true
      this.delRoleModel = role
    },
    cancelMode() {
      this.simple = []
      this.simple = this.tempSimple
      this.ticked = []
      this.ticked = this.tempTicked
      this.roleModel.roleName = this.tempRoleName
      this.editable = false
      this.editable2 = true
    },
    editMode() {
      this.tempSimple = this.simple
      this.tempTicked = this.ticked
      this.tempRoleName = this.roleModel.roleName
      this.simple = []
      this.roleTree(this.simple, getMenuList(this.allRouters), false)
      this.editable = true
      this.editable2 = false
    },
    mgmtRole(role) {
      this.editable = false
      this.editable2 = true
      this.splitterModel = 65
      this.simple = []
      this.expanded = []
      this.ticked = []
      this.roleModel = {
        seq: role.seq,
        roleName: role.roleName,
      }
      this.roleTree(this.simple, getMenuList(this.allRouters), true)

      let roleRouters = getMenuList(role.routers) //选中的角色
      this.getTickedData(roleRouters)
      // this.$refs.qTree.expandAll()
    },
    disabledAll() {
      this.editable = false
      this.editable2 = true
      this.simple = []
      this.roleTree(this.simple, getMenuList(this.allRouters), true)
    },
    clearAll() {
      this.simple = []
      this.ticked = []
      this.expanded = []
      this.tempRoleName = ''
      this.tempSimple = []
      this.tempTicked = []
      this.editable2 = false
      this.editable = false
      this.roleModel = roleModel
      this.addRoleModel = roleModel
      this.delRoleModel = roleModel
      this.splitterModel = 75
    },
    async updateRole() {
      let newRoleRouters = []
      this.newTree(newRoleRouters, this.allRouters, this.ticked)
      this.roleModel.routers = newRoleRouters
      const re = await updateRole(this.roleModel)

      this.roleList = this.roleList.map((item) => {
        if (Number(item.seq) === Number(re.seq)) {
          return {
            index: item.index,
            roleName: re.roleName,
            routers: re.routers,
            seq: item.seq,
          }
        } else {
          return item
        }
      })
      this.disabledAll()
      positiveNotify('修改成功!')
    },
    newTree(routers, data, ticked) {
      data.forEach((item) => {
        let r = Object.assign({}, item)
        r.component = item.component.toString()
        if (ticked.includes(item.name)) {
          routers.push(r)
        }
        if (item.children) {
          //判断子节点是否包含
          let a = false
          subFun(item.children)
          function subFun(data) {
            for (var i = 0; i < data.length; i++) {
              if (ticked.includes(data[i].name)) {
                a = true
                break
              }
              if (data[i].children) {
                subFun(data[i].children)
              }
            }
          }
          r.children = []
          if (a === true) {
            routers.push(r)
          }
          this.newTree(r.children, item.children, ticked)
        }
      })
    },
    roleTree(tree, data, disabled) {
      data.forEach((item) => {
        if (item.meta.show) {
          item = Object.assign({}, item)
          let r = {}
          r.label = item.meta.label
          r.name = item.name
          r.icon = item.meta.icon
          r.disabled = disabled

          if (item.children) {
            this.expanded.push(item.name)
            r.children = []
            this.roleTree(r.children, item.children, disabled)
          }
          tree.push(r)
        }
      })
    },
    getTickedData(data) {
      data.forEach((item) => {
        item = Object.assign({}, item)
        this.ticked.push(item.name)
        if (item.children) {
          // this.expanded.push(item.name)
          this.getTickedData(item.children)
        }
      })
      this.ticked.push('accountInfo')
    },
    async qryRole() {
      //查询角色
      const response = await qryRole({})
      this.roleList = response.map((row, index) => {
        return {
          ...row,
          index: index + 1,
        }
      })
      this.allRouters = eval(sessionStorage.getItem('_routers'))
    },
  },
  async created() {
    this.qryRole()
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