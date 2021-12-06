<template>
  <div>
    <div class="bg-white">
      <q-img width="200px" height="60px" :src="require('@/assets/logo.png')" alt="logo" />
    </div>
    <div class="WAL position-relative bg-grey-1" :style="style">
      <q-layout view="hHh Lpr lFf" class="WAL__layout bg-white" container>
        <q-header elevated>
          <q-toolbar style="height:100px" class="bg-primary text-h5 text-center">
            <div style="width:100%;">注册成为API服务提供商</div>
          </q-toolbar>
        </q-header>
        <q-page-container>
          <div class="q-pa-md">
            <q-stepper header-class="text-h6" flat v-model="step" ref="stepper" animated>
              <q-step style="padding-left:100px;" :name="1" active-color="primary" done-color="primary" title="开发者类型" icon="looks_one" :done="step > 1" :header-nav="step > 1">
                <q-card flat bordered class="my-card">
                  <q-card-section>
                    <div class="text-h6 text-center">企业开发者</div>
                  </q-card-section>
                  <q-card-section class="q-pt-none">
                    <q-icon color="primary" name="short_text" class="q-mr-sm" />适用群体<br />
                    <span class="text-grey-7" style="padding-left:20px;">有运营资质的企业主体，公司业务较为丰富</span>
                  </q-card-section>
                  <q-card-section class="q-pt-none">
                    <q-icon color="primary" name="short_text" class="q-mr-sm" />账号权益<br />
                    <span class="text-grey-7" style="padding-left:20px;">高级企业级功能</span>
                  </q-card-section>
                  <q-card-section class="q-pt-none">
                    <q-icon color="primary" name="short_text" class="q-mr-sm" />申请注册条件<br />
                    <span class="text-grey-7" style="padding-left:20px;">完成手机号认证</span><br />
                    <span class="text-grey-7" style="padding-left:20px;">完成邮箱认证(建议企业邮箱)</span><br />
                    <span class="text-grey-7" style="padding-left:20px;">完成企业信息认证</span><br />
                  </q-card-section>
                  <q-separator inset />
                  <q-card-section class="text-center">
                    <q-btn flat @click="entClick()" color="primary" label="成为企业开发者" />
                  </q-card-section>
                </q-card>
              </q-step>
              <q-step style="padding-left:100px;" :name="2" active-color="primary" done-color="primary" title="注册账号" icon="looks_two" :done="step > 2" :header-nav="step > 2">
                <div style="width:370px;">
                  <q-form ref="registerUserForm" @submit="registerUser">
                    <q-input maxlength="11" placeholder="请输入大陆手机号" v-model="userModel.mobilePhone" type="input" :rules="[ val => val && val.length > 0 || '']">
                      <template v-slot:before>
                        <span style="font-size:15px; width:80px"><span style="color:red">*</span>手机号：</span>
                      </template>
                    </q-input>
                    <q-input maxlength="6" placeholder="短信验证码" v-model="userModel.code" type="input" :rules="[ val => val && val.length > 0 || '']">
                      <template v-slot:before>
                        <span style="font-size:15px; width:80px"><span style="color:red">*</span>验证码：</span>
                      </template>
                      <template v-slot:append>
                        <a v-if="showFlag" @click="sendSms()" class="aClass" style="font-size:15px;">｜获取验证码</a>
                        <span v-else class="text-grey" style="font-size:15px;">｜获取验证码{{ count }}</span>
                      </template>
                    </q-input>
                    <q-input placeholder="输入登陆密码" v-model="userModel.password" :type="isPwd ? 'password' : 'text'" :rules="[ val => val && val.length > 0 || '']">
                      <template v-slot:before>
                        <span style="font-size:15px; width:80px"><span style="color:red">*</span>密码：</span>
                      </template>
                      <template v-slot:append>
                        <q-icon :name="isPwd ? 'visibility_off' : 'visibility'" class="cursor-pointer" @click="isPwd = !isPwd" />
                      </template>
                    </q-input>
                    <q-toggle color="primary" v-model="userModel.accept" label="接受许可证和条款" />
                    <q-stepper-navigation>
                      <q-btn type="submit" color="primary" label="下一步" />
                      <q-btn flat @click="()=>{step = 1; userModel.devType = ''}" color="primary" label="Back" class="q-ml-sm" />
                    </q-stepper-navigation>
                  </q-form>
                </div>
              </q-step>
              <q-step :name="3" active-color="primary" done-color="primary" title="完善认证信息" icon="looks_3" :done="step > 3" :header-nav="step > 3">
                <q-form action="/api/register/createEntInfo" enctype="multipart/form-data" method="post" ref="regEntInfoForm" @submit="regEntInfo">
                  <div class="row">
                    <q-input class="input-width-30" v-model="entModel.entShortName" type="input" :rules="[ val => val && val.length > 0 || '']">
                      <template v-slot:before>
                        <span class="input-title"><span style="color:red">*</span>企业简称：</span>
                      </template>
                    </q-input>
                    <q-input class="input-width-40" v-model="entModel.entName" type="input" :rules="[ val => val && val.length > 0 || '']">
                      <template v-slot:before>
                        <span class="input-title"><span style="color:red">*</span>企业全称：</span>
                      </template>
                    </q-input>
                    <q-input class="input-width-30" placeholder="企业组织机构代码" v-model="entModel.entCode" type="input" :rules="[ val => val && val.length > 0 || '']">
                      <template v-slot:before>
                        <span class="input-title"><span style="color:red">*</span>机构代码：</span>
                      </template>
                    </q-input>
                  </div>
                  <div class="row">
                    <q-input class="input-width-40" v-model="entModel.registerAddr" type="input" :rules="[ val => val && val.length > 0 || '']">
                      <template v-slot:before>
                        <span class="input-title"><span style="color:red">*</span>注册地址：</span>
                      </template>
                    </q-input>
                    <q-input class="input-width-30" v-model="entModel.corpName" type="input" :rules="[ val => val && val.length > 0 || '']">
                      <template v-slot:before>
                        <span class="input-title"><span style="color:red">*</span>法人名称：</span>
                      </template>
                    </q-input>
                    <q-input class="input-width-30" v-model="entModel.corpPhone" type="input" :rules="[ val => val && val.length > 0 || '']">
                      <template v-slot:before>
                        <span class="input-title"><span style="color:red">*</span>法人电话：</span>
                      </template>
                    </q-input>
                  </div>
                  <div class="row">
                    <q-select class="input-width-30" :rules="[(val) => val || '']" v-model="entModel.corpIdType" :options="idType" dense>
                      <template v-slot:before>
                        <span class="input-title"><span style="color:red">*</span>证件类型：</span>
                      </template>
                    </q-select>
                    <q-input class="input-width-30" placeholder="法人证件号码" v-model="entModel.corpIdNo" type="input" :rules="[ val => val && val.length > 0 || '']">
                      <template v-slot:before>
                        <span class="input-title"><span style="color:red">*</span>证件号码：</span>
                      </template>
                    </q-input>
                  </div>
                  <div class="row">
                    <span class="input-title"><span style="color:red">*</span>法人证件（正面）：</span>
                    <q-uploader @added="addcorpFile0" accept=".jpg,.png,.jpeg,.svg" field-name="corpFile0" style="width:20%" flat class="uploader-container">
                      <template v-slot:header></template>
                      <template v-slot:list="scope">
                        <q-avatar rounded size="80px" @mouseover="isUploadCorpFile0Visible = true" @mouseout="isUploadCorpFile0Visible = false" class="relative-position">
                          <q-icon v-if="scope.files.length === 0" name="photo" color="red-3" class="absolute-center" style="font-size: 1.5em;" />
                          <q-img v-else :src="scope.files[0].__img.src" class="absolute-center" />
                          <transition style="width:100%;height:100%" enter-active-class="animated fadeIn" leave-active-class="animated fadeOut">
                            <q-icon name="edit" v-show="isUploadCorpFile0Visible" @click="()=>{if(scope.files.length !== 0) {scope.removeFile(scope.files[0]);this.entModel.corpFile0=null}}" class="avatar-upload">
                              <q-uploader-add-trigger />
                            </q-icon>
                          </transition>
                        </q-avatar>
                      </template>
                    </q-uploader>
                    <span class="input-title"><span style="color:red">*</span>法人证件（反面）：</span>
                    <q-uploader @added="addcorpFile1" accept=".jpg,.png,.jpeg,.svg" field-name="corpFile1" style="width:20%" flat class="uploader-container">
                      <template v-slot:header></template>
                      <template v-slot:list="scope">
                        <q-avatar rounded size="80px" @mouseover="isUploadCorpFile1Visible = true" @mouseout="isUploadCorpFile1Visible = false" class="relative-position">
                          <q-icon v-if="scope.files.length === 0" name="photo" color="red-3" class="absolute-center" style="font-size: 1.5em;" />
                          <q-img v-else :src="scope.files[0].__img.src" class="absolute-center" />
                          <transition style="width:100%;height:100%" enter-active-class="animated fadeIn" leave-active-class="animated fadeOut">
                            <q-icon name="edit" v-show="isUploadCorpFile1Visible" @click="()=>{if(scope.files.length !== 0) {scope.removeFile(scope.files[0]);this.entModel.corpFile1=null}}" class="avatar-upload">
                              <q-uploader-add-trigger />
                            </q-icon>
                          </transition>
                        </q-avatar>
                      </template>
                    </q-uploader>
                    <span class="input-title"><span style="color:red">*</span>营业执照：</span>
                    <q-uploader @added="addEntLicenseFile" accept=".jpg,.png,.jpeg,.svg" field-name="entLicenseFile" style="width:20%" flat class="uploader-container">
                      <template v-slot:header=""></template>
                      <template v-slot:list="scope">
                        <q-avatar rounded size="80px" @mouseover="isUploadLicenseVisible = true" @mouseout="isUploadLicenseVisible = false" class="relative-position">
                          <q-icon v-if="scope.files.length === 0" name="photo" color="red-3" class="absolute-center" style="font-size: 1.5em;" />
                          <q-img v-else :src="scope.files[0].__img.src" class="absolute-center" />
                          <transition style="width:100%;height:100%" enter-active-class="animated fadeIn" leave-active-class="animated fadeOut">
                            <q-icon name="edit" v-show="isUploadLicenseVisible" @click="()=>{if(scope.files.length !== 0) {scope.removeFile(scope.files[0]);this.entModel.entLicenseFile=null}}" class="avatar-upload">
                              <q-uploader-add-trigger />
                            </q-icon>
                          </transition>
                        </q-avatar>
                      </template>
                    </q-uploader>
                  </div>
                  <q-stepper-navigation>
                    <q-btn type="submit" color="primary" label="下一步" />
                    <q-btn flat @click="step = 4" color="primary" label="跳过，稍后认证" class="q-ml-sm" />
                  </q-stepper-navigation>
                </q-form>
              </q-step>
              <q-step :name="4" active-color="primary" done-color="primary" title="注册完成" icon="looks_4" :header-nav="step > 4">
                注册成功，点击完成进行登陆
                <q-stepper-navigation>
                  <q-btn to="/login" color="primary" @click="done4 = true" label="完成" />
                </q-stepper-navigation>
              </q-step>
            </q-stepper>
          </div>
        </q-page-container>
      </q-layout>
    </div>
  </div>
</template>

<script>
import { registerUserModel, entModel, dic } from '@/model'
import { warningNotify } from '@/utils/utils'
import { sendSms, register, createEntInfo } from '@/services/user'
import { getToken } from '@/services/common'
export default {
  name: 'Register',
  data() {
    return {
      tab: 'devType',
      step: 1,
      userModel: registerUserModel(),
      entModel: entModel(),
      isPwd: true,
      idType: dic.idType,
      isUploadLicenseVisible: false,
      isUploadCorpFile0Visible: false,
      isUploadCorpFile1Visible: false,
      showFlag: true,
      count: '',
      timer: null,
    }
  },
  methods: {
    addcorpFile0(files) {
      this.entModel.corpFile0 = files[0]
    },
    addcorpFile1(files) {
      this.entModel.corpFile1 = files[0]
    },
    addEntLicenseFile(files) {
      this.entModel.entLicenseFile = files[0]
    },
    async regEntInfo(evt) {
      if (null === this.entModel.corpFile0) {
        warningNotify('请上传法人证件(正面)')
        return
      }
      if (null === this.entModel.corpFile1) {
        warningNotify('请上传法人证件(反面)')
        return
      }
      if (null === this.entModel.entLicenseFile) {
        warningNotify('请上传营业执照')
        return
      }
      //表单提交
      const formData = new FormData()
      for (var key in this.entModel) {
        if ('corpIdType' === key) {
          formData.append(key, this.entModel[key].value)
        } else {
          formData.append(key, this.entModel[key])
        }
      }
      await createEntInfo(formData)
      this.done3 = true
      this.step = 4
    },
    async entClick() {
      let token = await getToken()
      this.userModel = { ...this.userModel, ...token }
      this.userModel.devType = 1
      this.done1 = true
      this.step = 2
    },
    async registerUser() {
      if (this.userModel.accept !== true) {
        warningNotify('您需要先接受许可证和条款')
      } else {
        await register(this.userModel)
        this.done2 = true
        this.step = 3
      }
    },
    async sendSms() {
      if (this.userModel.mobilePhone === '') {
        warningNotify('手机号不能为空')
      } else {
        await sendSms({
          mobilePhone: this.userModel.mobilePhone,
        })

        const TIME_COUNT = 30
        if (!this.timer) {
          this.count = TIME_COUNT
          this.showFlag = false
          this.timer = setInterval(() => {
            if (this.count > 0 && this.count <= TIME_COUNT) {
              this.count--
            } else {
              this.showFlag = true
              clearInterval(this.timer)
              this.timer = null
            }
          }, 1000)
        }
      }
    },
  },
  computed: {
    style() {
      return {
        height: this.$q.screen.height - 60 + 'px',
      }
    },
  },
  async ccreated() {
    this.entModel.corpIdType = dic.idType[0]
  },
}
</script>

<style lang="sass" scoped>
@import "~@/styles/quasar.sass"
.WAL
  width: 100%
  height: 100%
  padding-top: 0px
  padding-bottom: 20px
  &:before
    content: ''
    height: 300px
    position: fixed
    top: 60px
    width: 100%
    background-color: $primary
  &__layout
    margin: 0 auto
    z-index: 4000
    height: 100%
    width: 90%
    max-width: 1000px
    border-radius: 5px
  &__field.q-field--outlined .q-field__control:before
    border: none
  .q-drawer--standard
    .WAL__drawer-close
      display: none
@media (max-width: 850px)
  .WAL
    padding: 0
    &__layout
      width: 100%
      border-radius: 0
@media (min-width: 691px)
  .WAL
    &__drawer-open
      display: none
.conversation__summary
  margin-top: 4px
.conversation__more
  margin-top: 0!important
  font-size: 1.4rem

.my-card
  width: 100%
  max-width: 300px
.input-width-45
  width: 45%
  padding-left: 10px
.input-width-40
  width: 40%
  padding-left: 10px
.input-width-35
  width: 35%
  padding-left: 10px
.input-width-30
  width: 30%
  padding-left: 10px
.input-title
  font-size: 13px
  width: 80px

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

.aClass
  cursor: pointer
  color: $primary
.bgColor
  background: #e6e6e6
</style>