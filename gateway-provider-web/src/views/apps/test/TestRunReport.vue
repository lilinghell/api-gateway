<template>
  <div class="q-pa-md">
    <q-dialog v-model="showTestRunReportDialog">
      <q-layout style="width: 1100px; max-width: 200vw;" view="Lhh lpR fff" container class="bg-white">
        <q-card>
          <q-splitter v-model="splitterReportModel" style="height: 100%; min-height: 600px;">
            <template v-slot:before>
              <div class="q-pa-md">
                <div>
                  <q-input ref="filterReport" dense v-model="filterReport" class="table-head-input">
                    <template v-slot:append>
                      <q-icon color="primary" name="search" class="primary" @click="resetFilterReport" />
                    </template>
                  </q-input>
                  <div>
                    <q-tree :filter="filterReport" ref="treeReport" :nodes="simpleReport" node-key="id" selected selected-color="primary">
                      <template v-slot:default-header="prop">
                        <div style="width:100%" class="row" @click="treeReportClick(prop.node)">
                          <span style="font-size:15px;padding-right:20px;">{{prop.node.name}}</span>
                          <q-icon color="primary" v-if="prop.node.status === '0' || prop.node.status === '4'" name="done" />
                          <q-icon color="red" v-if="prop.node.status === '1' || prop.node.status === '5'" name="clear" />
                        </div>
                      </template>
                    </q-tree>
                  </div>
                </div>
              </div>
            </template>
            <template v-slot:after>
              <div class=" q-pa-md">
                <div class="q-col-gutter-sm">
                  <div class="text-h5">测试报告</div>
                  <div>总共
                    <span class="text-primary" style="padding-left:3px; padding-right:3px; ">{{testCasesRunList.length}}</span>
                    测试用例
                  </div>
                </div>
                <q-separator spaced />
                <q-tab-panels v-model="selectedReport" animated transition-prev="jump-up" transition-next="jump-up">
                  <template v-for="(testCaseRun, index) in testCasesRunList">
                    <q-tab-panel :key="'report_'+index" :name="testCaseRun.caseSeq">
                      <div class="q-col-gutter-sm">
                        <div class="row">
                          <div style="width:3%">
                            <q-icon v-if="testCaseRun.runType === '0'" name="auto_fix_high" color="orange" />
                            <q-icon v-if="testCaseRun.runType === '1'" name="autorenew" color="red" />
                          </div>
                          <div class="text-h6 text-primary">
                            {{testCaseNode.name}}
                          </div>
                        </div>
                        <div class="row">
                          <div style="width:20%">Path</div>
                          <div>{{testCaseNode.apiPath}}</div>
                        </div>
                        <div class="row">
                          <div style="width:20%">验证结果</div>
                          <div>
                            <span class="text-teal" v-if="testCaseRun.status === '0'">执行成功</span>
                            <span class="text-red" v-if="testCaseRun.status === '1'">执行失败</span>
                            <span class="text-primary" v-if="testCaseRun.status === '2'">待执行</span>
                            <span class="text-orange" v-if="testCaseRun.status === '3'">执行中</span>
                            <span class="text-teal" v-if="testCaseRun.status === '4'">验证成功</span>
                            <span class="text-red" v-if="testCaseRun.status === '5'">验证失败</span>
                          </div>
                        </div>
                        <div class="row">
                          <div style="width:20%">执行时间</div>
                          <div>{{testCaseRun.updateTime}}</div>
                        </div>
                        <div class="text-primary">Request</div>
                        <div>
                          <q-input readonly rows="10" dense filled v-model="testCaseRun.requestParameter" type="textarea" />
                        </div>
                        <div class="text-primary">Response</div>
                        <div>
                          <q-input readonly rows="10" dense filled v-model="testCaseRun.responseResult" type="textarea" />
                        </div>
                        <div class="text-primary">断言脚本</div>
                        <div>
                          <q-input readonly rows="5" dense filled v-model="testCaseNode.assertScript" type="textarea" />
                        </div>
                      </div>
                    </q-tab-panel>
                  </template>
                </q-tab-panels>
              </div>
            </template>
          </q-splitter>
        </q-card>
      </q-layout>
    </q-dialog>
  </div>
</template>
<script>
import { qryTestCases, qryTestCaseRun } from '@/services/test'

export default {
  name: 'TestRunReport',
  data() {
    return {
      showTestRunReportDialog: false,
      splitterReportModel: 25,
      filterReport: '',
      simpleReport: [],
      testCasesRunList: [],
      testCaseNode: '',
    }
  },
  computed: {
    appId() {
      return this.$route.params.appId
    },
  },
  methods: {
    async showReportAll(testCasesList) {
      let caseSeqList = []
      let ok = testCasesList.filter((testCase) => {
        if (testCase.status != '2') {
          caseSeqList.push(testCase.seq)
          return true
        } else {
          return false
        }
      })
      this.testCasesRunList = await qryTestCaseRun({ caseSeqList: caseSeqList, appSeq: this.appId })

      this.createReportTree(ok)

      this.showTestRunReportDialog = true
    },
    resetFilterReport() {
      this.filterReport = ''
      this.$refs.filterReport.focus()
    },
    treeReportClick(node) {
      this.selectedReport = node.seq
      this.testCaseNode = { ...node }
    },
    async showReport(row) {
      let re = await qryTestCases({ caseSeq: row.seq, appSeq: this.appId })
      let caseSeqList = []
      caseSeqList.push(row.seq)
      this.testCasesRunList = await qryTestCaseRun({ caseSeqList: caseSeqList, appSeq: this.appId })

      this.createReportTree([...re.cases])

      this.showTestRunReportDialog = true
    },
    createReportTree(testCases) {
      this.simpleReport = []
      this.simpleReport = testCases
      this.simpleReport = this.simpleReport.map((testCase) => {
        return {
          ...testCase,
          id: testCase.seq,
          label: testCase.name,
        }
      })
      this.treeReportClick(this.simpleReport[0])
    },
  },
}
</script>
<style lang="sass" scoped>
@import "~@/styles/quasar.sass"
.apiPreviewTable
  width: 100%
.doc-heading
  color: $primary
</style>
