<template>
  <div class="q-pa-md">
    <q-splitter separator-class="bg-white" v-model="splitterModel" style="height: 100%; min-height: 600px;">
      <template v-slot:before>
        <div class="q-pa-md">
          <div class="doc-heading doc-h1">用户列表</div>
          <div class="q-my-md">
            <q-table flat :data="userList" :columns="columns" :filter="filter" row-key="index" :pagination.sync="pagination" hide-pagination>
              <template v-slot:top-left>
                <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
                  <template v-slot:append>
                    <q-icon name="search" color="primary" />
                  </template>
                </q-input>
              </template>
              <template v-slot:top-right>
                <q-btn color="primary" unelevated class="table-head-btn" @click="addUserModelOpen">
                  新增
                  <q-icon name="add" class="q-ml-sm" />
                </q-btn>

              </template>

              <template v-slot:body-cell-operation="props">
                <q-td :class="props.col.__tdClass" class="q-gutter-x-sm">
                  <q-btn flat color="primary" label="删除" @click="delUserModelOpen(props.row)" />
                  <q-btn flat color="primary" label="详情" @click="mgmtUser(props.row)" />
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
          <div v-show="userModel.length === 0" class="doc-heading doc-h1">详情</div>
          <div>
            <q-form @submit="updateUser" ref="updateUserFrom">
              <q-toolbar class="text-primary">
                <q-input v-show="userModel.length !== 0" maxlength="30" class="doc-h1" :readonly="!editable" hint="数字和大小写字母组合" v-model="userModel.userId" dense :rules="[ val => val && val.length > 0 || '请输入用户ID' , val => val.length <=20 && val.length > 0 || '用户ID长度不超过20' ]" />
                <q-btn flat size="12px" round icon="edit" @click="editMode()" v-show="editable2" />
                <template v-if="editable">
                  <q-btn type="submit" flat size="12px" round icon="done" />
                  <q-btn flat size="12px" round icon="clear" @click="cancelMode()" />
                </template>
              </q-toolbar>
              <div v-if="userModel.length === 0" class="row q-col-gutter-sm">
                <span>请在左边选择一个用户,点击详情</span>
              </div>
              <div v-else>
                <q-card-section class="text-center">
                  <q-uploader @uploaded="handleUploaded" field-name="file" style="width:240px" :url="`/api/t1/account/uploadUserHeadIcon?userSeq=${userModel.seq}`" auto-upload flat class="uploader-container">
                    <template v-slot:header></template>
                    <template v-slot:list="">
                      <q-avatar size="88px" @mouseover="isUploadIconVisible = true" @mouseout="isUploadIconVisible = false" class="relative-position">
                        <q-img :src="userModel.headIcon === null || userModel.headIcon === undefined ? require('@/assets/logo.png') : staticDomain + userModel.headIcon.path + userModel.headIcon.local_name" class="absolute-center" />
                        <transition style="width:100%;height:100%" enter-active-class="animated fadeIn" leave-active-class="animated fadeOut">
                          <q-icon name="edit" v-show="isUploadIconVisible" class="avatar-upload">
                            <q-uploader-add-trigger />
                          </q-icon>
                        </transition>
                      </q-avatar>
                    </template>
                  </q-uploader>
                </q-card-section>
                <q-input label="密码" :readonly="!editable" v-model="userModel.password" type="password" dense >
                  <template v-slot:before>
                    <q-icon name="lock" class="q-mr-sm" />
                  </template>
                </q-input>
                <q-input :readonly="!editable" label="昵称" v-model="userModel.userName" type="text" autofocus>
                  <template v-slot:before>
                    <q-icon name="account_box" />
                  </template>
                </q-input>
                <q-input :readonly="!editable" label="邮件" v-model="userModel.email" type="email">
                  <template v-slot:before>
                    <q-icon name="email" />
                  </template>
                </q-input>
                <q-input mask="###########" unmasked-value :readonly="!editable" label="手机号" v-model="userModel.mobilePhone" type="text" autofocus>
                  <template v-slot:before>
                    <q-icon name="mobile_screen_share" />
                  </template>
                </q-input>
                <q-select :readonly="!editable" label="角色" :display-value="formatDicDisplay(roleOptions, userModel.roleInfo.seq)" v-model="userModel.roleInfo" :options="roleOptions" dense>
                  <template v-slot:before>
                    <q-icon name="business" class="q-mr-sm" />
                  </template>
                </q-select>
              </div>
            </q-form>
          </div>
        </div>
      </template>
    </q-splitter>
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
          <q-btn flat label="确定" color="primary" @click="delUser()" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="addUserOpened" transition-show="slide-up" transition-hide="slide-down">
      <div class="modal-content-sm" style="width:450px">
        <q-toolbar class="bg-primary text-white">
          <q-btn flat v-close-popup round dense icon="arrow_back" />
          <q-toolbar-title>新增用户</q-toolbar-title>
        </q-toolbar>
        <q-form ref="addUserForm" @submit="addUser">
          <q-stepper v-model="step" vertical color="primary" header-nav>
            <q-step :name="1" title="请输入邮件地址" icon="email" :done="step > 1">
              <q-input label="邮件" v-model="addUserModel.email" type="email" autofocus :rules="[
                  val => val && val.length > 0 || '请输入邮件地址'
                ]" @keydown.stop.prevent.enter="handleVerifyEmail">
                <template v-slot:before>
                  <q-icon name="email" />
                </template>
                <template v-slot:append>
                  <q-btn :readonly="addUserModel.email === undefined" flat round icon="send" color="primary" @click="handleVerifyEmail" :rules="[
                  val => val && val.length > 0 || '请输入邮件地址'
                ]" />
                </template>
              </q-input>
            </q-step>

            <q-step :name="2" title="请填写基本信息" icon="create_new_folder" :done="step > 2" :header-nav="false">
              <q-input label="用户ID" v-model="addUserModel.userId" hint="数字和大小写字母组合" type="text" dense autofocus :rules="[
                   val => val && val.length > 0 || '请输入用户ID' , val => val.length <=20 && val.length > 0 || '用户ID长度不超过20' 
                ]">
                <template v-slot:before>
                  <q-icon name="account_box" class="q-mr-sm" />
                </template>
              </q-input>
              <q-input label="密码" v-model="addUserModel.password" type="password" dense :rules="[
                  (val) => val && val.length > 0 || '请输入密码' , val => val.length >=6 && val.length <=18 || '密码为6~18位'
                ]">
                <template v-slot:before>
                  <q-icon name="lock" class="q-mr-sm" />
                </template>
              </q-input>
              <q-input label="确认密码" v-model="addUserModel.password2" type="password" dense :rules="[
                  (val) => val && val.length > 0  || '请输入确认密码' , val => val.length >=6 && val.length <=18 || '确认密码为6~18位'
                ]">
                <template v-slot:before>
                  <q-icon name="lock" class="q-mr-sm" />
                </template>
              </q-input>
              <q-select label="角色" :rules="[
                  (val) => val || '角色不能为空,请选择角色'
                ]" v-model="addUserModel.role" :options="roleOptions" dense>
                <template v-slot:before>
                  <q-icon name="business" class="q-mr-sm" />
                </template>
              </q-select>
              <q-stepper-navigation align="right">
                <q-btn unelevated type="submit" color="primary" label="提交" />
                <q-btn flat @click="step = 1" color="primary" label="上一步" class="q-ml-sm" />
              </q-stepper-navigation>
            </q-step>
          </q-stepper>
        </q-form>
      </div>
    </q-dialog>
  </div>

</template>

<script>
import { qryRole } from '@/services/role'
import { qryUser, updateUser, addUser, delUser } from '@/services/user'
import { userModel, dic } from '@/model'
import { positiveNotify, formatDicDisplay, negativeNotify, warningNotify } from '@/utils/utils'
export default {
  name: 'UserList',
  data() {
    return {
      staticDomain: 'http://localhost',
      addUserOpened: false,//增加用户的界面
      isUploadIconVisible: false,//鼠标移到log上显示
      userModel: userModel,
      tempUserModel: userModel,
      addUserModel: userModel,
      delUserModel: userModel,
      confirm: false,//删除的界面
      editable2: false,//笔的按钮
      editable: false,//编辑的界面
      pagination: {
        sortBy: 'desc',
        descending: false,
        page: 0,
        rowsPerPage: 5,
      },
      splitterModel: 75,
      filter: '',
      step: 1,
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
      userList: [],
      roleOptions: [],
    }
  },
  computed: {
    pagesNumber() {
      return Math.ceil(this.userList.length / this.pagination.rowsPerPage)
    },
  },
  methods: {
    formatDicDisplay,
    handleUploaded({ files, xhr }) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code !== '000000') {
        negativeNotify(resp.msg)
      } else {
        this.userModel.headIcon = resp.data.user.headIcon
        this.successUpUser(resp.data)
      }
    },
    async handleVerifyEmail() {
      if (this.addUserModel.email === undefined) {
        return
      }
      // email exist check
      var users = await qryUser({
        email: this.addUserModel.email,
      })
      if (users.length === 0) {
        this.step = 2
      } else {
        negativeNotify('该邮件地址已注册')
      }
    },
    mgmtUser(user) {
      this.splitterModel = 70
      this.userModel = { ...user }
      this.editable2 = true
      this.editable = false
    },
    async addUser() {
      if (this.addUserModel.password !== this.addUserModel.password2) {
        warningNotify('两次输入的密码不一致!')
        return
      }

      const re = await addUser(this.addUserModel)
      this.userList.unshift(re.user)
      this.addUserOpened = false
      positiveNotify('添加成功！')
    },
    addUserModelOpen() {
      this.clearAll()
      this.addUserOpened = true
    },
    async delUser() {
      this.confirm = false
      await delUser(this.delUserModel.seq)
      this.userList = this.userList.filter((user) => user.seq !== this.delUserModel.seq)

      positiveNotify('删除成功！')
      this.clearAll()
    },
    clearAll() {
      this.addUserModel = {}
      this.splitterModel = 75
      this.userModel = []
      this.tempUserModel = []
      this.editable2 = false
      this.step = 1
      this.delUserModel = {}
    },
    delUserModelOpen(user) {
      this.confirm = true
      this.delUserModel = user
    },
    cancelMode() {
      this.editable = false
      this.editable2 = true

      this.userModel = this.tempUserModel
    },
    editMode() {
      this.editable = true
      this.editable2 = false
      this.tempUserModel = {
        ...this.userModel,
      }
    },
    async updateUser() {
      const re = await updateUser(this.userModel)
      this.successUpUser(re)
    },
    successUpUser(re) {
      this.userList = this.userList.map((user) => {
        if (user.seq === re.user.seq) {
          return re.user
        } else {
          return user
        }
      })
      this.editable = false
      this.editable2 = true

      positiveNotify('修改成功!')
    },
  },
  async created() {
    //查询用户
    this.userList = await qryUser()
    //查询角色
    const response = await qryRole({})
    this.roleOptions = response.map((role) => {
      return {
        ...role,
        label: role.roleName,
        value: role.seq,
      }
    })
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
.avatar-upload
  display: flex
  justify-content: center
  align-items: center
  width: 100%
  height: 100%
  z-index: 10
  background: rgba(255, 255, 255, 0.6)
  border: 2px dashed $primary
  border-radius: inherit
  color: $primary
  cursor: pointer
</style>