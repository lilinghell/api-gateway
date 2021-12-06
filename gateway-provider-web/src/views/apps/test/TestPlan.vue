<template>
  <div class="q-pa-md">
    <q-splitter v-model="splitterModel"
                style="height: 100%; min-height: 600px;">
      <template v-slot:before>
        <div class="q-pa-md">
          <div class="doc-heading doc-h1">测试计划</div>
          <q-separator spaced />
          <div>
            <q-list>
              <template v-for="(testPlan, index) in testPlanList">
                <q-item @click="link = 'link_'+ index"
                        :active="link === 'link_'+index"
                        active-class="my-menu-link"
                        :key="'testPlan_'+index"
                        clickable
                        v-ripple>
                  <q-item-section @click="clickPlan(testPlan)">
                    <q-item-label>{{testPlan.name}}</q-item-label>
                    <q-item-label caption>
                      {{testPlan.planTimeShow}}
                    </q-item-label>
                    <q-item-label caption>{{testPlan.planRuleShow}}</q-item-label>
                  </q-item-section>
                  <q-item-section side>
                    <div class="text-grey-8 q-gutter-xs">
                      <q-toggle color="primary"
                                v-model="testPlan.usingFlg"
                                val="friend" />
                      <q-btn size="12px"
                             flat
                             dense
                             round
                             icon="more_horiz">
                        <q-menu transition-show="jump-down"
                                transition-hide="jump-up">
                          <q-list style="min-width: 100px">
                            <q-item @click="delTestPlanModel(testPlan)"
                                    clickable
                                    v-close-popup>
                              <q-item-section>删除</q-item-section>
                            </q-item>
                          </q-list>
                        </q-menu>
                      </q-btn>
                    </div>
                  </q-item-section>
                </q-item>
              </template>
            </q-list>
          </div>
          <div style="padding-top:50px;">
            <p>
              点击<a class="aClass text-h5"
                 @click="addTestPlanOpen">添加</a>测试计划
            </p>
          </div>
        </div>
      </template>
      <template v-slot:after>
        <div class="q-pa-md">
          <div v-if="link === ''"
               style="padding-top:20%"
               class="row q-col-gutter-sm">
            <q-icon color="grey"
                    size="28px"
                    class="q-ml-sm"
                    name="mdi-set mdi-hand-pointing-left" />
            <span style="color:grey"
                  class="text-h6">选中可查看测试用例，以及进行运行</span>
          </div>
          <div v-else>
            <div class="row">
              <div style="width:30%;">
                <q-input :borderless="!editable"
                         @focus="editable=true"
                         @blur="editable=false"
                         maxlength="30"
                         dense
                         v-model="testPlan.name"
                         type="input" />
              </div>
              <div style="width:70%"
                   class="text-right">
                <q-btn flat
                       color="primary"
                       @click="testCaseRunAll"
                       icon="playlist_play"
                       label="运行" />
                <q-btn flat
                       @click="editAutoTest"
                       color="primary"
                       icon="save"
                       label="保存" />
                <q-btn color="primary"
                       flat
                       dense
                       icon="more_horiz">
                  <q-menu transition-show="jump-down"
                          transition-hide="jump-up">
                    <q-list style="width:230px;">
                      <q-item-label header>测试报告</q-item-label>
                      <q-item clickable
                              @click="showTestPlanReportAll"
                              v-close-popup>
                        <q-item-section avatar>
                          <q-avatar icon="assignment"
                                    color="primary"
                                    text-color="white" />
                        </q-item-section>
                        <q-item-section>
                          <q-item-label>详细报告</q-item-label>
                          <q-item-label caption>查看详细报告</q-item-label>
                        </q-item-section>
                        <q-item-section side>
                          <q-icon name="info" />
                        </q-item-section>
                      </q-item>
                    </q-list>
                  </q-menu>
                </q-btn>
              </div>
            </div>
            <q-separator />
            <div class="row">
              <q-tabs style="width:30%"
                      class="text-left"
                      v-model="apiTestGroupSelectType"
                      narrow-indicator
                      dense
                      align="justify">
                <q-tab name="overView"
                       class="text-primary text-left"
                       label="概述" />
                <q-tab name="cases"
                       class="text-primary text-left"
                       label="用例列表" />
              </q-tabs>
              <q-tab-panels @transition="initEcharShow"
                            keep-alive
                            style="width:100%"
                            v-model="apiTestGroupSelectType"
                            animated
                            swipeable
                            vertical
                            transition-prev="jump-up"
                            transition-next="jump-up">
                <q-tab-panel name="overView">
                  <TestRunReportEchar ref="testRunReportEchar" />
                </q-tab-panel>
                <q-tab-panel name="cases">
                  <div class="q-mb-md">
                    <q-table flat
                             :data="testCasesList"
                             :columns="columns"
                             :filter="testCasesfilter"
                             row-key="index"
                             :pagination.sync="pagination"
                             hide-pagination>
                      <template v-slot:top-left>
                        <q-input borderless
                                 dense
                                 debounce="300"
                                 v-model="testCasesfilter"
                                 placeholder="Search">
                          <template v-slot:append>
                            <q-icon name="search"
                                    color="primary" />
                          </template>
                        </q-input>
                      </template>
                      <template v-slot:top-right>
                        <q-btn key="btn"
                               @click="addTestCasesModelOpen"
                               color="primary"
                               size="12px"
                               unelevated
                               class="table-head-btn">
                          添加用例
                          <q-icon name="add"
                                  class="q-ml-sm" />
                        </q-btn>

                      </template>
                      <template v-slot:body-cell-status="props">
                        <q-td :class="props.col.__tdClass"
                              class="q-gutter-x-sm">
                          <span class="text-teal"
                                v-if="props.row.status === '0'">执行成功</span>
                          <span class="text-red"
                                v-if="props.row.status === '1'">执行失败</span>
                          <span class="text-primary"
                                v-if="props.row.status === '2'">待执行</span>
                          <span class="text-orange"
                                v-if="props.row.status === '3'">执行中</span>
                          <span class="text-teal"
                                v-if="props.row.status === '4'">验证成功</span>
                          <span class="text-red"
                                v-if="props.row.status === '5'">验证失败</span>
                        </q-td>
                      </template>
                      <template v-slot:body-cell-operation="props">
                        <q-td :class="props.col.__tdClass"
                              class="q-gutter-x-sm">
                          <q-btn @click="showTestPlanReport(props.row)"
                                 :disabled="props.row.status === '2' || props.row.status === '3'"
                                 flat
                                 color="primary"
                                 label="详细报告" />
                          <q-btn color="primary"
                                 flat
                                 round
                                 dense
                                 icon="more_horiz">
                            <q-menu transition-show="jump-down"
                                    transition-hide="jump-up">
                              <q-list style="min-width: 100px">
                                <q-item @click="testCaseRun(props.row)"
                                        clickable
                                        v-close-popup>
                                  <q-item-section>运行</q-item-section>
                                </q-item>
                                <q-separator />
                                <q-separator />
                                <q-item @click="delTestCases(props.row)"
                                        clickable
                                        v-close-popup>
                                  <q-item-section>删除</q-item-section>
                                </q-item>
                              </q-list>
                            </q-menu>
                          </q-btn>
                        </q-td>
                      </template>
                    </q-table>
                    <div class="q-pa-lg flex flex-center">
                      <q-pagination color="primary"
                                    v-model="pagination.page"
                                    :max="pagesNumber"
                                    :direction-links="true">
                      </q-pagination>
                    </div>
                  </div>
                </q-tab-panel>
              </q-tab-panels>
            </div>
          </div>
        </div>
      </template>
    </q-splitter>
    <q-dialog v-model="showAddTestPlanDialog">
      <q-layout style="width: 1000px; max-width: 200vw;"
                view="Lhh lpR fff"
                container
                class="bg-white">
        <q-card>
          <div class="q-pa-md row q-col-gutter-sm">
            <div class="col-12 col-sm-5">
              <div class="text-h6 text-primary">»计划基本信息</div>
              <q-input style="width: 95%"
                       v-model="addTestPlan.name"
                       label="计划名称"
                       :rules="[ val => val && val.length > 0 || '']" />
              <q-input style="width: 95%"
                       label="描述"
                       filled
                       v-model="addTestPlan.planInfo"
                       type="textarea" />
            </div>
            <div class="col-12 col-sm-6 q-gutter-sm">
              <div class="text-h6 text-primary">»计划设置</div>
              <p>计划时间</p>
              <q-chip removable
                      v-for="timeItem in timeGroup"
                      :key="timeItem"
                      :val="timeItem"
                      :label="timeItem"
                      color="primary"
                      text-color="white"
                      @remove="removeTimeItem(timeItem)" />
              <q-btn dense
                     size="md"
                     rounded
                     color="primary"
                     icon-right="add"
                     class="cursor-pointer">
                <q-popup-proxy transition-show="scale"
                               transition-hide="scale">
                  <div class="column">
                    <q-time landscape
                            v-model="time">
                      <div class="row items-center justify-end q-gutter-sm">
                        <q-btn label="取消"
                               color="primary"
                               flat
                               v-close-popup />
                        <q-btn label="确定"
                               color="primary"
                               flat
                               @click="addPlanTime"
                               v-close-popup />
                      </div>
                    </q-time>
                  </div>
                </q-popup-proxy>
              </q-btn>
              <q-separator spaced />
              <p style="padding-top:50px">计划周期</p>
              <q-checkbox v-for="week in weekdays"
                          v-bind:key="week.value"
                          v-model="weekGroup"
                          :val="week.value"
                          :label="week.label"
                          color="primary" />
            </div>
          </div>
        </q-card>
        <q-footer class="bg-white text-white">
          <q-toolbar inset>
            <q-toolbar-title class="text-right">
              <q-btn @click="saveTestPlan()"
                     label="保存"
                     color="primary" />
            </q-toolbar-title>
          </q-toolbar>
        </q-footer>
      </q-layout>
    </q-dialog>

    <q-dialog v-model="confirm"
              persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="delete"
                    color="primary"
                    text-color="white" />
          <span class="q-ml-sm"
                style="padding-left:30px;width: 200px">
            是否确定删除？
          </span>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat
                 label="取消"
                 color="primary"
                 v-close-popup />
          <q-btn flat
                 label="确定"
                 color="primary"
                 @click="delTestPlanCases()" />
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-dialog v-model="delPlanConfirm"
              persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="delete"
                    color="primary"
                    text-color="white" />
          <span class="q-ml-sm"
                style="padding-left:30px;width: 200px">
            是否确定删除？
          </span>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat
                 label="取消"
                 color="primary"
                 v-close-popup />
          <q-btn flat
                 label="确定"
                 color="primary"
                 @click="delTestPlan()" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="showAddTestPlanCasesDialog">
      <q-layout style="width: 1000px; max-width: 200vw;"
                view="Lhh lpR fff"
                container
                class="bg-white">
        <q-card>
          <div class="q-pa-md row q-col-gutter-sm">
            <div class="col-12 col-sm-5">
              <q-input style="width:70%"
                       ref="filter"
                       dense
                       v-model="filter"
                       class="table-head-input">
                <template v-slot:append>
                  <q-icon color="primary"
                          name="search"
                          class="primary"
                          @click="resetFilter" />
                </template>
              </q-input>
              <q-scroll-area :thumb-style="thumbStyle"
                             :bar-style="barStyle"
                             style="height: 500px; max-width: 100%;">
                <q-tree :filter="filter"
                        :nodes="casesSimple"
                        label-key="label"
                        node-key="id"
                        @update:ticked="tickedCases"
                        tick-strategy="leaf"
                        :ticked.sync="ticked" />
              </q-scroll-area>
            </div>
            <div class="col-12 col-sm-6 q-gutter-sm">
              <div class="text-h6 text-primary">导入用例数({{selectCases.length}})</div>
              <q-scroll-area :thumb-style="thumbStyle"
                             :bar-style="barStyle"
                             style="height: 500px; max-width: 100%;">
                <div class="q-col-gutter-sm">
                  <div v-show="selectCases.length === 0">无</div>
                  <div v-for="testCase in selectCases"
                       :key="`ticked-${testCase.seq}`">
                    <div class="row">
                      <div style="width: 50%">{{ testCase.name }}</div>
                      <div style="width: 50%">{{ testCase.apiPath }}</div>
                    </div>
                  </div>
                </div>
              </q-scroll-area>
            </div>
          </div>
        </q-card>
        <q-footer class="bg-white text-white">
          <q-toolbar inset>
            <q-toolbar-title class="text-right">
              <q-btn @click="importCases"
                     label="确定"
                     color="primary" />
            </q-toolbar-title>
          </q-toolbar>
        </q-footer>
      </q-layout>
    </q-dialog>
    <TestRunReport ref="testRunReportRef" />
  </div>
</template>
<script>
import { testPlanModel, dic } from '@/model'
import {
  qryTestCases,
  testCaseRun,
  qryTestPlanCases,
  addTestPlan,
  qryTestPlan,
  addTestPlanCases,
  delTestPlanCases,
  delTestPlan,
} from '@/services/test'

import { date } from 'quasar'
import { positiveNotify, warningNotify, formatDicDisplay } from '@/utils/utils'

import TestRunReport from '@/views/apps/test/TestRunReport.vue'
import TestRunReportEchar from '@/views/apps/test/TestRunReportEchar.vue'

export default {
  name: 'CaseList',
  components: {
    TestRunReport,
    TestRunReportEchar,
  },
  data () {
    return {
      selectCases: [],
      ticked: [],
      casesSimple: [],
      filter: '',
      apiTestGroupSelectType: 'cases',
      editable: false,
      confirm: false,
      delPlanConfirm: false,
      link: '',
      weekdays: dic.weekdays,
      weekGroup: [],
      time: '01:00',
      timeGroup: [],
      showAddTestPlanCasesDialog: false,
      showAddTestPlanDialog: false,
      date: date,
      splitterModel: 25,
      addTestPlan: testPlanModel(),
      testPlanList: [],
      testPlan: testPlanModel(),
      testCasesList: [],
      testCasesfilter: '',
      pagination: {
        sortBy: 'desc',
        descending: false,
        page: 0,
        rowsPerPage: 5,
      },
      columns: [
        {
          name: 'name',
          align: 'center',
          label: '用例名称',
          style: 'width: 30%',
          field: (row) => row.name,
        },
        {
          name: 'key',
          label: 'key',
          align: 'center',
          style: 'width: 10%',
          field: (row) => row.uniKey,
        },
        {
          name: 'status',
          label: '状态',
          align: 'center',
          style: 'width: 5%',
          sortable: true,
          field: (row) => row.status,
        },
        {
          name: 'apiPath',
          align: 'center',
          label: '接口路径',
          style: 'width: 40%',
          sortable: true,
          field: (row) => row.apiPath,
        },
        {
          name: 'upTime',
          align: 'center',
          label: '最近时间',
          style: 'width: 10%',
          sortable: true,
          field: (row) => date.formatDate(row.updateTime, 'YYYY/MM/DD HH:mm:ss'),
        },
        {
          name: 'operation',
          align: 'center',
          label: '操作',
          style: 'width: 10%',
          field: (row) => row,
        },
      ],
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
      allTestCases: [],
      testCasesRunList: [],
      createReportTree: [],
    }
  },
  mounted () { },
  computed: {
    pagesNumber () {
      return Math.ceil(this.testCasesList.length / this.pagination.rowsPerPage)
    },
    appId () {
      return this.$route.params.appId
    },
  },
  methods: {
    formatDicDisplay,
    initEcharShow () {
      if (this.$refs.testRunReportEchar !== undefined) {
        this.$refs.testRunReportEchar.initEchar(this.testCasesList)
      }
    },
    async caseRun (caseSeqList) {
      await testCaseRun({ caseSeqList: caseSeqList, appSeq: this.appId })
      this.testCasesList = this.testCasesList.map((testCase) => {
        let tCase = caseSeqList.find((caseSeq) => caseSeq.toString() === testCase.seq.toString())
        if (tCase !== undefined) {
          return {
            ...testCase,
            status: '3', //执行中
          }
        } else {
          return testCase
        }
      })
    },
    async testCaseRun (row) {
      this.caseRun([row.seq])
    },
    async testCaseRunAll () {
      let caseSeqList = []
      this.testCasesList.map((testCase) => {
        caseSeqList.push(testCase.seq)
      })
      this.caseRun(caseSeqList)
    },
    editAutoTest () { },
    showTestPlanReportAll () {
      this.$refs.testRunReportRef.showReportAll(this.testCasesList)
    },
    showTestPlanReport (row) {
      this.$refs.testRunReportRef.showReport(row)
    },
    async importCases () {
      let caseSeqList = []
      this.selectCases.forEach((selectCase) => {
        caseSeqList.push(selectCase.seq)
      })
      let a = await addTestPlanCases({
        appSeq: this.appId,
        caseSeqList: caseSeqList,
        testPlanSeq: this.testPlan.seq,
      })
      this.testCasesList.unshift(...a.cases)
      positiveNotify('添加成功')
      this.showAddTestPlanCasesDialog = false
    },
    tickedCases (target) {
      this.selectCases = this.casesSimple.filter((testCase) => {
        var flg = false
        target.forEach((element) => {
          if (element.toString() === testCase.id.toString()) {
            flg = true
          }
        })
        return flg
      })
    },
    resetFilter () {
      this.filter = ''
      this.$refs.filter.focus()
    },
    async addTestCasesModelOpen () {
      this.casesSimple = []
      this.casesSimple = this.allTestCases
        .filter((tC) => {
          let reFind = this.testCasesList.find((cc) => cc.seq.toString() === tC.seq.toString())
          if (reFind === undefined) {
            return true
          } else {
            return false
          }
        })
        .map((testCase) => {
          return {
            ...testCase,
            label: testCase.name + ' ' + testCase.apiPath,
            id: testCase.seq,
          }
        })
      this.ticked = []
      this.selectCases = []
      this.showAddTestPlanCasesDialog = true
    },
    delTestCases (row) {
      this.confirm = true
      this.row = row
    },
    async delTestPlanCases () {
      await delTestPlanCases(this.row.seq)
      this.testCasesList = this.testCasesList.filter((testCases) => testCases.seq.toString() !== this.row.seq.toString())
      positiveNotify('删除成功')
      this.confirm = false
    },
    delTestPlanModel (testPlan) {
      this.delPlanConfirm = true
      this.dtp = testPlan
      console.log(this.dtp)
    },
    async delTestPlan () {
      await delTestPlan(this.dtp.seq)
      this.testPlanList = this.testPlanList.filter((testPlan) => testPlan.seq.toString() !== this.dtp.seq.toString())
      positiveNotify('删除成功')
      this.delPlanConfirm = false
    },
    async clickPlan (testPlan) {
      this.testPlan = testPlan
      //查询计划用例
      let testPlanCases = await qryTestPlanCases({
        appSeq: this.appId,
        testPlanSeq: testPlan.seq,
      })
      this.testCasesList = testPlanCases.cases
      this.initEcharShow()
    },
    async saveTestPlan () {
      if (this.addTestPlan.name === '') {
        warningNotify('计划名称不能为空')
      } else if (this.timeGroup.length === 0) {
        warningNotify('请设置计划时间')
      } else if (this.weekGroup.length === 0) {
        warningNotify('请设置计划周期')
      } else {
        let re = await addTestPlan({
          ...this.addTestPlan,
          planTime: this.timeGroup,
          planRule: this.weekGroup,
          appSeq: this.appId,
        })

        this.testPlanList.unshift(...re.testPlans)
        positiveNotify('添加成功')
        this.showAddTestPlanDialog = false
      }
    },
    addPlanTime () {
      this.timeGroup.push(this.time)
      this.timeGroup = Array.from(new Set(this.timeGroup))
      this.timeGroup.sort()
    },
    removeTimeItem (timeItem) {
      this.timeGroup.splice(this.timeGroup.indexOf(timeItem), 1)
    },
    addTestPlanOpen () {
      this.addTestPlan = testPlanModel()
      this.timeGroup = []
      this.weekGroup = []
      this.showAddTestPlanDialog = true
    },
    getMessage (mess) {
      if (mess === 'testRunOk') {
        //收到测试结束通知
        this.clickPlan(this.testPlan)
      }
    },
  },
  async created () {
    this.$root.$on('recTestRunResult', this.getMessage)

    let re = await qryTestPlan({ appSeq: this.appId })
    this.testPlanList = re.testPlans.map((testPlan) => {
      let timeGroup = JSON.parse(testPlan.planTime)
      let weekGroup = JSON.parse(testPlan.planRule)
      let planTime = ''
      let planWeek = ''
      for (let value of timeGroup) {
        planTime = planTime + value + '、'
      }
      for (let value of weekGroup) {
        planWeek = planWeek + this.formatDicDisplay(dic.weekdays, value) + '、'
      }
      return {
        ...testPlan,
        planTime: timeGroup,
        planTimeShow: planTime.substring(0, planTime.length - 1),
        planRule: weekGroup,
        planRuleShow: planWeek.substring(0, planWeek.length - 1),
      }
    })
    if (this.testPlanList.length > 0) {
      this.clickPlan(this.testPlanList[0])
      this.link = 'link_0'
    }

    let testPlanCases = await qryTestPlanCases({
      appSeq: this.appId,
      testPlanSeq: this.testPlan.seq,
    })
    this.testCasesList = testPlanCases.cases

    let allCases = await qryTestCases({ appSeq: this.appId })
    this.allTestCases = allCases.cases
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
.my-menu-link
  background: #E8E8E8
</style>
