<template>
  <div class="q-pa-md q-gutter-sm">
    <q-form @submit="updateUserForm" ref="updateUserForm">
      <q-splitter separator-class="bg-white" v-model="splitterModel" style="height: 100%; min-height: 600px;">
        <template v-slot:before>
          <div class="q-pa-md">
            <div class="doc-heading doc-h1">基本信息</div>
            <div class="q-my-md">
              <q-card-section class="text-center">
                <q-uploader @uploaded="handleUploaded" field-name="file" style="width:240px" :url="`/api/t1/account/uploadCUHeadIcon`" auto-upload flat class="uploader-container">
                  <template v-slot:header></template>
                  <template v-slot:list="">
                    <q-avatar size="88px" @mouseover="isUploadIconVisible = true" @mouseout="isUploadIconVisible = false" class="relative-position">
                      <q-icon v-if="userModel.headIcon === null || userModel.headIcon === undefined" color="primary" name="portrait" class="absolute-center" />
                      <q-img v-else :src="staticDomain + userModel.headIcon.path + userModel.headIcon.local_name" class="absolute-center" />
                      <transition style="width:100%;height:100%" enter-active-class="animated fadeIn" leave-active-class="animated fadeOut">
                        <q-icon name="edit" v-show="isUploadIconVisible" class="avatar-upload">
                          <q-uploader-add-trigger />
                        </q-icon>
                      </transition>
                    </q-avatar>
                  </template>
                </q-uploader>
              </q-card-section>
              <q-card-section>
                <q-input prefix="角色名：" v-if="userModel.roleInfo !== null && userModel.roleInfo !== undefined" readonly dense v-model="userModel.roleInfo.roleName" type="input" />
              </q-card-section>

              <q-card-section class="text-left">
                <q-input :borderless="!editable" maxlength="30" prefix="用户ID：" :readonly="!editable" dense v-model="userModel.userId" type="input">
                  <template v-slot:after>
                    <q-btn v-if="!editable" flat class="sizeSeven" round icon="edit" @click="editMode('1')" />
                    <div v-if="editable">
                      <q-btn color="primary" @click="updateUser" flat class="sizeSeven" round icon="done" />
                      <q-btn color="primary" flat class="sizeSeven" round icon="clear" @click="cancelMode('1')" />
                    </div>
                  </template>
                </q-input>
                <q-input :borderless="!editable2" prefix="昵称：" :readonly="!editable2" dense v-model="userModel.userName" type="input">
                  <template v-slot:after>
                    <q-btn v-if="!editable2" flat class="sizeSeven" round icon="edit" @click="editMode('2')" />
                    <div v-if="editable2">
                      <q-btn color="primary" @click="updateUser" flat class="sizeSeven" round icon="done" />
                      <q-btn color="primary" flat class="sizeSeven" round icon="clear" @click="cancelMode('2')" />
                    </div>
                  </template>
                </q-input>
              </q-card-section>

            </div>
          </div>
        </template>
        <template v-slot:after>
          <div class="q-pa-md">
            <div class="doc-heading doc-h1">安全设置</div>
            <div class="q-my-md" style="width:300px;">
              <q-card-section class="text-left">
                <q-input :borderless="!editable3" prefix="绑定手机：" :readonly="!editable3" dense v-model="userModel.mobilePhone" type="input" :rules="[ val => val && val.length > 0 || '请输入手机']">
                  <template v-slot:after>
                    <q-btn v-if="!editable3" flat class="sizeSeven" round icon="edit" @click="editMode('3')" />
                    <div v-if="editable3">
                      <q-btn color="primary" @click="updateUser" flat class="sizeSeven" round icon="done" />
                      <q-btn color="primary" flat class="sizeSeven" round icon="clear" @click="cancelMode('3')" />
                    </div>
                  </template>
                </q-input>
                <q-input prefix="绑定邮箱：" :readonly="!editable4" dense v-model="userModel.email" type="input">
                  <template v-slot:after>
                    <q-btn v-if="!editable4" flat class="sizeSeven" round icon="edit" @click="editMode('4')" />
                    <div v-if="editable4">
                      <q-btn color="primary" @click="updateUser" flat class="sizeSeven" round icon="done" />
                      <q-btn color="primary" flat class="sizeSeven" round icon="clear" @click="cancelMode('4')" />
                    </div>
                  </template>
                </q-input>
              </q-card-section>
              <q-card-section class="text-left">
                <span>登陆密码：</span>
                <a @click="openUpPass = true, passwordModel = {}" class="aClass text-weight-bold">点击修改</a>
              </q-card-section>
            </div>
          </div>
        </template>
      </q-splitter>
    </q-form>

    <q-dialog v-model="openUpPass">
      <q-card style="width:450px">
        <q-toolbar class="bg-primary text-white">
          <q-btn flat v-close-popup round dense icon="arrow_back" />
          <q-toolbar-title>登陆密码修改</q-toolbar-title>
        </q-toolbar>
        <q-form ref="upPassForm" @submit="upPass">
          <q-card-section style="width:95%" class="text-right">
            <q-input v-model="passwordModel.oldPassword" type="password" :rules="[ val => val && val.length > 0 || '']">
              <template v-slot:before>
                <span style="font-size:15px; width:80px">旧密码：</span>
              </template>
            </q-input>
            <q-input v-model="passwordModel.newPassword" type="password" :rules="[ val => val && val.length > 0 || '']">
              <template v-slot:before>
                <span style="font-size:15px; width:80px">新密码：</span>
              </template>
            </q-input>
            <q-input v-model="passwordModel.confirmPassword" type="password" :rules="[ val => val && val.length > 0 || '']">
              <template v-slot:before>
                <span style="font-size:15px; width:80px">确认密码：</span>
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
  </div>
</template>
<script>
import { userModel } from '@/model'
import { qryCurrentUser, updatePassword } from '@/services/user'
import { formatDicDisplay, positiveNotify, negativeNotify } from '@/utils/utils'
import { mapActions } from 'vuex'
export default {
  data() {
    return {
      file: null,
      isUploadIconVisible: false,
      userModel: userModel,
      tempUserModel: userModel,
      splitterModel: 30,
      editable: false,
      editable2: false,
      editable3: false,
      editable4: false,
      openUpPass: false,
      step: 1,
      passwordModel: {},
      staticDomain: 'http://localhost',
    }
  },
  methods: {
    formatDicDisplay,
    ...mapActions('common', {
      updateCurrentUser: 'updateCurrentUser',
    }),
    handleUploaded({ files, xhr }) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code !== '000000') {
        negativeNotify(resp.msg)
      } else {
        this.userModel = {
          ...resp.data,
        }
        positiveNotify('修改成功!')
      }
    },
    updateUser() {
      this.$refs.updateUserForm.submit()
    },
    async updateUserForm() {
      await this.updateCurrentUser({
        ...this.userModel,
        password: '',
      })
      positiveNotify('修改成功!')
      this.editable = false
      this.editable2 = false
      this.editable3 = false
      this.editable4 = false
    },
    async upPass() {
      await updatePassword(this.passwordModel)
      this.openUpPass = false
      positiveNotify('修改成功!')
    },
    editMode(num) {
      this.tempUserModel = { ...this.userModel }
      if ('1' === num) {
        this.editable = true
        this.editable2 = false
        this.editable3 = false
        this.editable4 = false
      }
      if ('2' === num) {
        this.editable = false
        this.editable2 = true
        this.editable3 = false
        this.editable4 = false
      }
      if ('3' === num) {
        this.editable = false
        this.editable2 = false
        this.editable3 = true
        this.editable4 = false
      }
      if ('4' === num) {
        this.editable = false
        this.editable2 = false
        this.editable3 = false
        this.editable4 = true
      }
    },
    cancelMode(num) {
      this.userModel = { ...this.tempUserModel }
      this.editable = false
      this.editable2 = false
      this.editable3 = false
      this.editable4 = false
    },
  },
  async created() {
    //查询用户
    let data = await qryCurrentUser({})
    this.userModel = data.user
  },
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
.sizeSeven
  font-size: 7px
.aClass
  cursor: pointer
  color: $primary
</style>