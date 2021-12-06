<template>
  <div class="q-pa-md">
    <q-splitter v-show="apiInfoOpen" v-model="splitterModel" style="height: 100%; min-height: 600px;">
      <template v-slot:before>
        <div class="q-pa-md">
          <div class="doc-heading doc-h1">
            <q-avatar size="56px" class="q-mb-sm">
              <q-btn @click="goApiGroup" flat round dense icon="arrow_back" size="md" />
            </q-avatar> APIS
          </div>
          <div>
            <q-input ref="filter" dense v-model="filter" class="table-head-input">
              <template v-slot:before>
                <q-icon :disabled="appEnvType !== 'DEV'" color="primary" name="add" class="aClass" @click="addApiModelOpen" />
              </template>
              <template v-slot:append>
                <q-icon color="primary" name="search" class="primary" @click="resetFilter" />
              </template>
            </q-input>
            <q-scroll-area style="height: 450px;">
              <div>
                <q-tree :filter="filter" ref="tree" :nodes="simple" node-key="id" selected-color="primary" :selected.sync="selected" :expanded.sync="expanded">
                  <template v-slot:default-header="prop">
                    <div v-on:mouseover="showMenu(prop.node.seq)" v-on:mouseout="hiddenMenu(prop.node.seq)" @click="clickTree(prop.node)" style="width:90%;height:100%">
                      <span style="font-size:15px;">{{prop.node.name}}</span>
                    </div>
                    <div v-on:mouseover="showMenu(prop.node.seq)" v-on:mouseout="hiddenMenu(prop.node.seq)" style="width:10%;height:100%;" class="text-right">
                      <div :id="'node_' + prop.node.seq" style="display:none">
                        <q-btn color="primary" size="xs" flat round dense icon="more_horiz">
                          <q-menu v-on:mouseover="showMenu(prop.node.seq)" v-on:mouseout="hiddenMenu(prop.node.seq)" transition-show="jump-down" transition-hide="jump-up">
                            <q-list style="min-width: 100px">
                              <q-item @click="apiSelectType = 'define';splitterModel=15" clickable v-close-popup>
                                <q-item-section>编辑</q-item-section>
                              </q-item>
                              <q-separator />
                              <q-item @click="copyApi(prop.node)" clickable v-close-popup>
                                <q-item-section>复制</q-item-section>
                              </q-item>
                              <q-item @click="delConfirm=true, delApiSeq=prop.node.seq" clickable v-close-popup>
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
            </q-scroll-area>
          </div>
        </div>
      </template>
      <template v-slot:after>
        <div class="q-pa-md">
          <div v-if="apiModel.seq === ''" style="padding-top:20%" class="row q-col-gutter-sm">
            <q-icon color="grey" size="28px" class="q-ml-sm" name="mdi-set mdi-hand-pointing-left" />
            <span style="color:grey" class="text-h6">选中可查看API详情</span>
          </div>
          <div v-else>
            <div class="row">
              <div style="width:75%;" class="row">
                <div style="width:40%;">
                  <q-input label="接口名称" :readonly="appEnvType !== 'DEV'" @keyup.enter="editApi()" @focus="editApiMode('name')" @blur="editApiMode('name')" :borderless="!editable" maxlength="30" dense v-model="apiModel.name" type="input">
                    <template v-slot:before>
                      <q-icon color="primary" name="api" />
                    </template>
                  </q-input>
                </div>
                <div v-if="appEnvServiceType === '1'" style="width:30%;">
                  <q-input label="服务ID" :readonly="appEnvType !== 'DEV'" @keyup.enter="editApi()" @focus="editApiMode('name')" @blur="editApiMode('name')" :borderless="!editable" maxlength="30" dense v-model="apiModel.serviceId" type="input">
                    <template v-slot:before>
                      <q-icon color="primary" name="design_services" />
                    </template>
                  </q-input>
                </div>
                <div style="width:30%;">
                  <q-select :readonly="appEnvType !== 'DEV'" label="归属组" borderless square :display-value="formatDicDisplay(apiGroupOptions, apiModel.group === undefined || apiModel.group === null ? '' : apiModel.group.seq)" v-model="apiModel.group" :options="apiGroupOptions" dense>
                    <template v-slot:before>
                      <q-icon color="primary" name="widgets" />
                    </template>
                  </q-select>
                </div>
              </div>
              <div style="width:25%" class="text-right">
                <q-btn flat color="primary" icon="save" @click="editApi" label="保存" />
                <q-btn color="primary" flat dense icon="more_horiz">
                  <q-menu transition-show="jump-down" transition-hide="jump-up">
                    <q-list style="width:215px;">
                      <q-item-label header>参考规范</q-item-label>
                      <q-item clickable v-close-popup>
                        <q-item-section avatar>
                          <q-avatar icon="assignment" color="primary" text-color="white" />
                        </q-item-section>
                        <q-item-section>
                          <q-item-label>swagger规范</q-item-label>
                          <q-item-label caption>API定义规范</q-item-label>
                        </q-item-section>
                        <q-item-section side>
                          <q-icon name="info" />
                        </q-item-section>
                        <q-separator spaced />
                      </q-item>
                      <q-item-label header>接口导出</q-item-label>
                      <q-item clickable v-close-popup>
                        <q-item-section avatar>
                          <q-avatar icon="assignment" color="primary" text-color="white" />
                        </q-item-section>
                        <q-item-section>
                          <q-item-label>PDF</q-item-label>
                          <q-item-label caption>导出PDF格式</q-item-label>
                        </q-item-section>
                        <q-item-section side>
                          <q-icon name="info" />
                        </q-item-section>
                        <q-separator spaced />
                      </q-item>
                    </q-list>
                  </q-menu>
                </q-btn>
              </div>
            </div>
            <q-separator />
            <div class="row">
              <q-tabs class="text-left" v-model="apiSelectType" narrow-indicator dense align="justify">
                <q-tab @click="clickEditorFlag('overView')" name="overView" class="text-primary text-left" label="概述" />
                <q-tab v-if="appEnvType === 'DEV'" @click="clickEditorFlag('define')" name="define" class="text-primary text-left" label="接口定义" />
                <q-tab @click="clickEditorFlag('develop')" name="develop" class="text-primary text-left" label="扩展" />
                <q-tab @click="clickEditorFlag('observe')" name="observe" class="text-primary text-left" label="监视" />
              </q-tabs>
              <q-tab-panels style="width:100%" v-model="apiSelectType" keep-alive animated swipeable vertical transition-prev="jump-up" transition-next="jump-up">
                <q-tab-panel name="overView">
                  <div class="text-h6 q-mb-md">
                    <q-splitter separator-class="bg-white" v-model="splitterOverViewModel" style="height: 100%; min-height: 600px;">
                      <template v-slot:before>
                        <div class="q-pa-md row q-col-gutter-sm">
                          <div class="col-12 col-sm-6">
                            <q-input :readonly="true" v-model="apiModel.url" borderless dense></q-input>
                            <p>
                              <q-input :readonly="appEnvType !== 'DEV'" style="height:70px" placeholder="完整的API描述" @keyup.enter="editApiDetail()" @focus="editApiMode('detail')" @blur="editApiMode('detail')" :borderless="!editableDetail" maxlength="120" dense v-model="apiModel.detail" type="textarea" />
                            </p>
                          </div>
                          <div class="col-12 col-sm-6 q-gutter-sm">
                            <div :class="{ 'truncate-chip-labels': truncate }">
                              <template v-for="(serviceTag, index) in serviceTagAll">
                                <q-chip :class="!serviceTag.ticked ? 'chip-opacity' : ''" v-if="appEnvType === 'DEV'" @click="clickChip(serviceTag.ticked, serviceTag.seq)" :selected.sync="serviceTag.ticked" :key="'servcieTag_'+index" :style="`background: ${serviceTag.color}`" text-color="white" :label="serviceTag.name" :title="serviceTag.name" />
                                <q-chip disabled v-else :key="'servcieTag_'+index" :style="`background: ${serviceTag.color}`" text-color="white" :label="serviceTag.name" :title="serviceTag.name" />
                              </template>
                            </div>
                          </div>
                        </div>
                        <div class="row">
                          <q-list bordered padding style="width:45%;height:170px">
                            <q-item-label header>Schema</q-item-label>
                            <q-item>
                              <q-item-section>
                                <q-item-label style="font-size:15px">Type</q-item-label>
                                <q-item-label caption>
                                  {{apiModel.schemaType}}
                                </q-item-label>
                              </q-item-section>
                              <q-item-section>
                                <q-item-label style="font-size:15px">Format</q-item-label>
                                <q-item-label caption>
                                  {{apiModel.schemaFormat}}
                                </q-item-label>
                              </q-item-section>
                            </q-item>
                            <q-item>
                              <q-item-section>
                                <q-item-label style="font-size:15px">最近更新时间</q-item-label>
                                <q-item-label caption>
                                  {{apiModel.updateTime}}
                                </q-item-label>
                              </q-item-section>
                            </q-item>
                          </q-list>
                          <div style="width:5%"></div>
                          <q-list bordered padding style="width:45%;">
                            <q-item-label header>Settings</q-item-label>
                            <q-item>
                              <q-item-section>
                                <q-item-label style="font-size:15px">api 启用状态</q-item-label>
                                <q-item-label caption>Allow notification</q-item-label>
                              </q-item-section>
                              <q-item-section avatar>
                                <q-toggle @input="editApi('flg')" checked-icon="check" unchecked-icon="clear" true-value="0" false-value="1" color="red" v-model="apiModel.status" />
                              </q-item-section>
                            </q-item>
                            <q-item>
                              <q-item-section>
                                <q-item-label style="font-size:15px">API Mock</q-item-label>
                                <q-item-label caption>Allow notification</q-item-label>
                              </q-item-section>
                              <q-item-section avatar>
                                <q-toggle @input="editApi('flg')" checked-icon="check" unchecked-icon="clear" true-value="0" false-value="1" color="blue" v-model="apiModel.mockSwitch" />
                              </q-item-section>
                            </q-item>

                            <q-item>
                              <q-item-section>
                                <q-item-label style="font-size:15px">限流</q-item-label>
                                <q-item-label caption>Allow notification</q-item-label>
                              </q-item-section>
                              <q-item-section avatar>
                                <q-toggle @input="editApi('flg')" checked-icon="check" unchecked-icon="clear" true-value="0" false-value="1" color="green" v-model="apiModel.flowSwitch" />
                              </q-item-section>
                            </q-item>
                          </q-list>
                        </div>
                      </template>
                      <template v-slot:after>
                        Versions
                        <q-input @click="showApiDec()" :readonly="true" v-model="apiModel.version" dense></q-input>
                        <q-list>
                          <q-item-label header>历史版本</q-item-label>
                          <template v-for="(oldApi, index) in apiHis">
                            <q-item v-if="index < 3" :key="'oldApi_'+index" class="q-mb-sm" clickable v-ripple>
                              <q-item-section>
                                <q-item-label caption lines="1">V{{ oldApi.version }}</q-item-label>
                                <q-item-label caption lines="1">{{ oldApi.updateTime }}</q-item-label>
                              </q-item-section>
                              <q-item-section side>
                                <q-btn @click="showApiHisDec(oldApi.id)" color="blue" flat round icon="chat_bubble" />
                              </q-item-section>
                            </q-item>
                          </template>
                          <div class="q-mb-md">
                            <q-expansion-item header-class="text-primary" v-if="apiHis.length > 4" dense v-model="api_his_expanded" switch-toggle-side>
                              <q-card>
                                <template v-for="(oldApi, index) in apiHis">
                                  <q-item v-if="index >= 3" :key="'oldApi_'+index" class="q-mb-sm" clickable v-ripple>
                                    <q-item-section>
                                      <q-item-label caption lines="1">V{{ oldApi.version }}</q-item-label>
                                      <q-item-label caption lines="1">{{ oldApi.updateTime }}</q-item-label>
                                    </q-item-section>
                                    <q-item-section side>
                                      <q-btn @click="showApiHisDec(oldApi.id)" color="blue" flat round icon="chat_bubble" />
                                    </q-item-section>
                                  </q-item>
                                </template>
                              </q-card>
                            </q-expansion-item>
                          </div>
                        </q-list>
                      </template>
                    </q-splitter>
                  </div>
                </q-tab-panel>
                <q-tab-panel name="define" style="height:800px;">
                  <div id="swagger-editor"></div>
                </q-tab-panel>
                <q-tab-panel name="develop">
                  <div class="q-mb-md">
                    <q-expansion-item header-class="bg-grey-2" v-model="develop_timeOut_expanded" switch-toggle-side expand-separator label="超时时间(毫秒), 不设置将采用网关默认超时时间">
                      <q-card>
                        <q-card-section>
                          <div class="row">
                            connect-timeout：
                            <q-input maxlength="10" dense v-model="apiExtModel.connectTimeout" />
                            response-timeout：
                            <q-input maxlength="10" dense v-model="apiExtModel.responseTimeout" />
                          </div>
                        </q-card-section>
                      </q-card>
                    </q-expansion-item>
                    <q-expansion-item header-class="bg-grey-2" v-model="develop_flow_expanded" switch-toggle-side expand-separator label="限流(个数)">
                      <q-card>
                        <q-card-section>
                          <div class="row">
                            replenishRate：
                            <q-input placeholder="补充令牌速度" maxlength="10" dense v-model="apiExtModel.replenishRate" />
                            burstCapacity：
                            <q-input placeholder="令牌桶容量" maxlength="10" dense v-model="apiExtModel.burstCapacity" />
                          </div>
                        </q-card-section>
                      </q-card>
                    </q-expansion-item>
                    <q-expansion-item header-class="bg-grey-2" v-model="develop_mock_expanded" switch-toggle-side expand-separator label="API Mock">
                      <q-card>
                        <q-card-section>
                          <p>对API返回值进行Mock，以提供特定的响应结果，保证开发效率。<a v-if="apiExtModel.apiMock === undefined 
                          || apiExtModel.apiMock === null
                          || apiExtModel.apiMock === ''" @click="responseShili" class="aClass">导入示例</a></p>
                          <q-input rows="14" @focus="apiMockFocus" name="apiMockName" filled v-model="apiExtModel.apiMock" type="textarea" />
                        </q-card-section>
                      </q-card>
                    </q-expansion-item>
                  </div>
                </q-tab-panel>
                <q-tab-panel name="observe">
                  <div class="text-h6 q-mb-md">
                    observe
                  </div>
                </q-tab-panel>
              </q-tab-panels>
            </div>
          </div>
        </div>
      </template>
    </q-splitter>

    <AddApi :apiGroupSeq="apiGroupSeq" :apiList="apiList" apiFrom="api" ref="addApiRef" />

    <q-dialog v-model="delConfirm" persistent>
      <q-card style="width:300px">
        <q-card-section class="row items-center">
          <q-avatar icon="delete" color="primary" text-color="white" />
          <span class="q-ml-sm">确定删除?</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn label="删除" @click="delApi" color="primary" v-close-popup />
          <q-btn flat label="取消" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-dialog v-model="seamless" seamless position="bottom">
      <q-card style="width: 350px">
        <q-linear-progress :value="0.6" color="pink" />

        <q-card-section class="row items-center no-wrap">
          <div>
            <div class="text-red text-weight-bold">{{apiModel.name}} 需同步至网关</div>
            <div class="text-grey">API有修改，是否同步至网关</div>
          </div>
          <q-space />

          <q-btn color="red" @click="syncGateway(apiModel)" flat round icon="sync" />
          <q-btn flat color="red" round icon="close" v-close-popup />
        </q-card-section>
      </q-card>
    </q-dialog>
    <ApiPreview ref="apiPreviewRef" />
  </div>
</template>
<script>
import { date } from 'quasar'
import { dic, apiModel, apiExtModel } from '@/model'
import { formatDicDisplay, positiveNotify, warningNotify } from '@/utils/utils'
import ApiPreview from '@/views/apps/api/ApiPreview.vue'
import {
  qryApi,
  qryApiGroup,
  updateApi,
  delApi,
  qryApiExt,
  updateApiExt,
  syncGateway,
  qryApiHis,
  qryApiPreview,
} from '@/services/api'
import router from '@/router'
import YAML from 'js-yaml'
import { api_get, api_post, api_put, api_delete } from '@/utils/apiUtil'
import { qryServiceTag, qryApiServiceTag, addApiServiceTag } from '@/services/tag'

import AddApi from '@/views/apps/api/AddApi.vue'

import SwaggerEditorBundle from 'swagger-editor-dist/swagger-editor-bundle'
import SwaggerEditorStandalonePreset from 'swagger-editor-dist/swagger-editor-standalone-preset'
import 'swagger-editor-dist/swagger-editor.css'

export default {
  name: 'Api',
  components: {
    AddApi,
    ApiPreview,
  },
  computed: {
    appId() {
      return this.$route.params.appId
    },
    appEnvId() {
      return this.$route.params.appEnvId
    },
  },
  props: {},
  watch: {
    $route() {
      //跳转到该页面后需要进行的操作
    },
  },
  mounted() {},
  data() {
    return {
      appEnvType: '',
      envSeamless: false,
      api_his_expanded: false,
      apiHis: [],
      seamless: false,
      responseInfo: false,
      develop_flow_expanded: true,
      develop_timeOut_expanded: true,
      develop_mock_expanded: true,
      delConfirm: false,
      apiSelectType: 'overView',
      apiInfoOpen: true,
      apiModel: apiModel(),
      editable: false,
      deployEnvType: dic.deployEnvType,
      apiSchemaType: dic.apiSchemaType,
      apiSchemaFormat: dic.apiSchemaFormat,
      editor: null,
      splitterModel: 20,
      splitterOverViewModel: 75,
      filter: '',
      simple: [],
      selected: '',
      expanded: [],
      apiList: [],
      editableDetail: false,
      pageSticky: true,
      apiGroupSeq: '',
      apiGroupOptions: [],
      delApiSeq: '',
      apiInfoEditorFlag: false,
      apiExtModel: apiExtModel(),
      clickTabFlag: '',
      serviceTagAll: [],
      truncate: true,
      appEnvServiceType: '0',
    }
  },
  methods: {
    formatDicDisplay,
    async clickChip(ticked, seq) {
      if (this.appEnvType === 'DEV') {
        await addApiServiceTag({
          apiSeq: this.apiModel.seq,
          serviceTagSeq: seq,
          appSeq: this.appId,
          ticked: ticked,
        })
        positiveNotify('修改成功!')
      }
    },
    goApiGroup() {
      router.push({
        path: '/provider/apps/' + this.appId + '/' + this.appEnvId + '/apis',
        query: { groupSeq: this.apiGroupSeq },
      })
    },
    apiMockFocus(evt) {
      evt.value = this.apiExtModel.apiMock
    },
    async responseShili() {
      let apiTables = await qryApiPreview({
        apiSeq: this.apiModel.seq,
        appEnvSeq: this.appEnvId,
      })
      apiTables.forEach((apiTable) => {
        apiTable.forEach((api) => {
          document.getElementsByName('apiMockName')[0].value = JSON.stringify(
            api.responseDemo,
            null,
            2
          )
          document.getElementsByName('apiMockName')[0].dispatchEvent(new Event('input'))
        })
      })
    },
    showApiHisDec(id) {
      this.$refs.apiPreviewRef.showDialog = true
      this.$refs.apiPreviewRef.apiPreview('his', id)
    },
    showApiDec() {
      this.$refs.apiPreviewRef.showDialog = true
      this.$refs.apiPreviewRef.apiPreview('current', this.apiModel.seq)
    },
    async syncGateway(row) {
      let response = await syncGateway({ apiSeq: row.seq, appEnvSeq: this.appEnvId })
      this.apiModel = response.api
      this.showSeamless(this.apiModel.syncGateway)
      this.apiList = this.apiList.map((api) => {
        if (api.seq.toString() === response.api.seq.toString()) {
          return response.api
        } else {
          return api
        }
      })
      this.createApisTree(response.api.seq)
      positiveNotify('操作成功')
    },
    clickEditorFlag(value) {
      if (value === 'define') {
        this.apiInfoEditorFlag = true
        this.splitterModel = 16
        this.envSeamless = true
        this.$nextTick(() => {
          this.apiEditor()
        })
      } else {
        this.apiInfoEditorFlag = false
        this.splitterModel = 20
        this.envSeamless = false
      }
      this.clickTabFlag = value
    },
    async delApi() {
      await delApi(this.delApiSeq)
      this.apiList = this.apiList.filter((apiFilter) => {
        if (apiFilter.seq.toString() === this.delApiSeq.toString()) {
          return false
        }
        return true
      })
      this.createApisTree('')
      this.apiModel = apiModel()
      positiveNotify('删除成功')
      this.delConfirm = false
      this.editor = null
      this.clickEditorFlag('overView')
      this.apiSelectType = 'overView'
    },
    copyApi(api) {
      this.$refs.addApiRef.addApiModel = {
        ...api,
        name: api.name + ' copy',
        url: api.url + 'new',
        info: api.info.replace(api.url, api.url + 'new'),
      }
      this.$refs.addApiRef.addApi()
    },
    addApiModelOpen() {
      if (this.appEnvType === 'DEV') {
        this.$refs.addApiRef.addApiModel = apiModel()
        this.$refs.addApiRef.handAddApiMode = true
      }
    },
    showSeamless(syncGateway) {
      if (syncGateway === '1') {
        this.seamless = true
      } else {
        this.seamless = false
      }
    },
    async clickTree(node) {
      this.editable = false
      this.apiModel = { ...node }
      this.apiEditor()
      let a = await qryApiExt({
        apiSeq: node.seq,
        appEnvSeq: this.appEnvId,
      })
      this.apiExtModel = {
        apiSeq: node.seq,
        ...a,
      }
      this.showSeamless(node.syncGateway)
      this.qryApiHistory(node.seq)
      this.qryApiTag(node.seq)
    },
    async qryApiHistory(apiSeq) {
      let apiHistory = await qryApiHis({
        apiSeq: apiSeq,
        appEnvSeq: this.appEnvId,
      })
      this.apiHis = apiHistory.apis.map((api) => {
        return {
          ...api,
          updateTime: date.formatDate(api.updateTime, 'YYYY/MM/DD HH:mm:ss'),
        }
      })
    },
    showMenu(nodeSeq) {
      if (nodeSeq != -1 && this.appEnvType === 'DEV') {
        document.getElementById('node_' + nodeSeq).style.display = 'block'
      }
    },
    hiddenMenu(nodeSeq) {
      if (nodeSeq != -1) {
        document.getElementById('node_' + nodeSeq).style.display = 'none'
      }
    },
    resetFilter() {
      this.filter = ''
      this.$refs.filter.focus()
    },
    editApiMode(edit) {
      if (edit === 'detail') {
        this.editableDetail = !this.editableDetail
      }
      if (edit === 'name') {
        this.editable = !this.editable
      }
    },
    async editApi(flag) {
      if (this.clickTabFlag === 'develop') {
        let a = await updateApiExt(this.apiExtModel)
        this.showSeamless(a.syncGateway)
        positiveNotify('更新成功')
        return
      }

      if (this.apiModel.group === null) {
        warningNotify('请选择归属组')
        return
      }
      if (this.appEnvServiceType === '1' && this.apiModel.serviceId === '') {
        warningNotify('请输入服务ID')
        return
      }

      let str = this.editor === null ? '' : this.editor.specSelectors.specStr()
      if ('' !== str && this.apiInfoEditorFlag === true) {
        let jsContent = YAML.safeLoad(str)
        let path = Object.keys(jsContent.paths)
        // url
        this.apiModel.url = path[0]
        // method
        let method = Object.keys(jsContent.paths[path[0]])[0]
        this.apiModel.method = method.toUpperCase()
        // description
        this.apiModel.detail = jsContent.info.description
        // this.apiModel.detail = jsContent.paths[path[0]].description
        // version
        this.apiModel.version = jsContent.info.version
        // name
        this.apiModel.name = jsContent.info.title
        this.apiModel.info = str
      } else if ('' !== str && this.apiInfoEditorFlag === false) {
        let jsContent = YAML.safeLoad(str)
        str = str.replace(
          'description: "' + jsContent.info.description + '"',
          'description: "' + this.apiModel.detail + '"'
        )
        str = str.replace(
          'title: "' + jsContent.info.title + '"',
          'title: "' + this.apiModel.name + '"'
        )
        this.apiModel.info = str
      }

      let a = await updateApi({
        ...this.apiModel,
        groupSeq: this.apiModel.group.seq,
        clickTabFlag: flag === 'flg' ? 'define' : this.clickTabFlag,
      })
      this.editable = !this.editable
      this.apiList = this.apiList.map((api) => {
        if (api.seq.toString() === a.api.seq.toString()) {
          return a.api
        } else {
          return api
        }
      })
      this.createApisTree(a.api.seq)
      this.showSeamless(a.api.syncGateway)
      this.qryApiHistory(a.api.seq)
      positiveNotify('更新成功')
    },
    tryRequest(t) {
      // var x = t.url.indexOf('/')
      // for (var i = 0; i < 2; i++) {
      //   x = t.url.indexOf('/', x + 1)
      // }
      // let host = t.url.substring(x, t.url.length)
      // var testEnvProfile = document.getElementById('testEnvProfile').value
      // if (testEnvProfile.charAt(testEnvProfile.length - 1) === '/') {
      //   testEnvProfile = testEnvProfile.substring(0, testEnvProfile.length - 1)
      // }
      // let url = testEnvProfile + host
      // t.url = url
      return t
    },
    apiEditor() {
      if (this.editor === null && this.apiInfoEditorFlag === true) {
        try {
          this.editor = new SwaggerEditorBundle({
            dom_id: '#swagger-editor',
            layout: 'EditorLayout',
            presets: [SwaggerEditorStandalonePreset],
            requestInterceptor: this.tryRequest,
          })
          // window.editor = this.editor
          // var newNode = document.createElement('div')
          // newNode.innerHTML =
          //   '&nbsp;&nbsp;&nbsp;&nbsp;测试地址：<input style="width:400px;" type="text" name="testEnvProfile" id="testEnvProfile" value="http://localhost:8080/"/>'
          // var dom = document.getElementsByClassName('swagger-ui')[0]
          // dom.parentNode.insertBefore(newNode, dom)
        } catch (error) {
          console.log('ninis')
        }
      }
      if (this.apiInfoEditorFlag === true) {
        if (this.apiModel.info === '') {
          if (this.apiModel.schemaFormat === 'JSON') {
            //todo
            let test_json = api_test_json
            test_json = test_json.replace('{{version}}', this.apiModel.version)
            test_json = test_json.replace('{{title}}', this.apiModel.name)
            test_json = test_json.replace('{{url}}', this.apiModel.url)
            test_json = test_json.replace('{{description}}', this.apiModel.detail)
            this.editor.specActions.updateSpec(test_json)
          } else {
            let test_yaml = api_get
            if (this.apiModel.method === 'GET') {
              test_yaml = api_get
            }
            if (this.apiModel.method === 'POST') {
              test_yaml = api_post
            }
            if (this.apiModel.method === 'PUT') {
              test_yaml = api_put
            }
            if (this.apiModel.method === 'DELETE') {
              test_yaml = api_delete
            }
            test_yaml = test_yaml.replace('{{version}}', this.apiModel.version)
            test_yaml = test_yaml.replace('{{title}}', this.apiModel.name)
            test_yaml = test_yaml.replace('{{url}}', this.apiModel.url)
            test_yaml = test_yaml.replace('{{description}}', this.apiModel.detail)
            this.editor.specActions.updateSpec(test_yaml)
          }
        } else {
          this.editor.specActions.updateSpec(this.apiModel.info)
        }
      }
    },
    createApis(api) {
      this.apiList.unshift(api)
      this.createApisTree(api.seq)
      this.showSeamless(api.syncGateway)
    },
    async createApisTree(apiSeq) {
      this.apiList = this.apiList.map((api) => {
        if (api.seq.toString() === apiSeq.toString()) {
          this.apiModel = { ...api }
          this.selected = 'api_' + apiSeq
        }
        return {
          ...api,
          parentSeq: null,
        }
      })
      this.simple = this.subTree(null, this.apiList, 1)
    },
    subTree(parent, list, level) {
      let sub = []
      list.forEach((el) => {
        let re = { ...el }
        re.level = level
        re.id = 'api_' + el.seq
        re.label = el.name
        this.expanded.push(re.id)
        if (parent === el.parentSeq) {
          re.children = this.subTree(el.seq, list, level + 1)
          sub.push(re)
        }
      })
      return sub
    },
    async qryApiTag(apiSeq) {
      let tickedTag = await qryApiServiceTag({
        appSeq: this.appId,
        apiSeq: apiSeq,
      })
      let tickedTagList = tickedTag.apiServiceTags

      this.serviceTagAll = this.serviceTagAll.map((tag) => {
        let ti = tickedTagList.find((apiServiceTag) => tag.seq === apiServiceTag.serviceTagSeq)
        let ticked = false
        if (ti !== undefined) {
          ticked = true
        }
        return {
          ...tag,
          ticked: ticked,
        }
      })
    },
  },
  async created() {
    this.appEnvType = sessionStorage.getItem('_appEnvType')
    this.appEnvServiceType = sessionStorage.getItem('_appEnvServiceType')
    let apiSeq = this.$route.query.apiSeq
    let groupSeq = this.$route.query.groupSeq
    this.apiGroupSeq = groupSeq

    this.qryApiHistory(apiSeq)

    let a = await qryApi({
      groupSeq: groupSeq === '-1' ? '' : groupSeq,
      appEnvSeq: this.appEnvId,
    })
    this.apiList = a.apis.map((api) => {
      if (api.seq.toString() === apiSeq.toString()) {
        this.showSeamless(api.syncGateway)
      }
      return api
    })

    this.createApisTree(apiSeq)
    let c = await qryApiExt({
      apiSeq: apiSeq,
      appEnvSeq: this.appEnvId,
    })
    this.apiExtModel = {
      apiSeq: apiSeq,
      ...c,
    }

    let b = await qryApiGroup({
      appSeq: this.appId,
    })
    this.apiGroupOptions = b.apiGroups
    this.apiGroupOptions = this.apiGroupOptions.map((group) => {
      return {
        ...group,
        label: group.name,
        value: group.seq,
      }
    })

    let re = await qryServiceTag({ appSeq: this.appId })
    this.serviceTagAll = re.serviceTags

    this.qryApiTag(apiSeq)
  },
}
</script>

<style>
.swagger-editor .swagger-ui .info .title {
  font-size: 23px;
}
.Pane2 {
  overflow-y: scroll;
  /* display: none; */
}
.Pane {
  overflow-y: scroll;
}
#swagger-editor {
  font-size: 1.3em;
}
.swagger-editor .swagger-ui .info .title {
  font-size: 20px;
}
</style>
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
.truncate-chip-labels > .q-chip
  max-width: 100px
  font-size: 10px
.chip-opacity
  opacity: .6 !important
</style>
