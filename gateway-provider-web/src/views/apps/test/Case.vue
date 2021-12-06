<template>
  <div class="q-pa-md">
    <q-splitter v-model="splitterModel"
                style="height: 100%; min-height: 600px">
      <template v-slot:before>
        <div class="q-pa-md">
          <div class="doc-heading doc-h1">测试集合</div>
          <div>
            <q-input ref="filter"
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
            <div>
              <q-tree :filter="filter"
                      ref="tree"
                      :nodes="simple"
                      node-key="id"
                      selected-color="primary"
                      :selected.sync="selected"
                      :expanded.sync="expanded">
                <template v-slot:default-header="prop">
                  <div @click="clickTree(prop.node)">
                    <span style="font-size: 15px">{{ prop.node.name }}</span>
                  </div>
                  <div class="text-right">
                    <q-icon v-show="
                        prop.node.level === 1 &&
                        prop.node.seq != -1 &&
                        prop.node.seq != -2
                      "
                            style="padding-left: 10px"
                            color="green"
                            size="xs"
                            @click="addSubTestGroup(prop.node)"
                            flat
                            name="add_box" />
                    <q-icon v-show="
                        prop.node.children.length === 0 &&
                        prop.node.seq != -1 &&
                        prop.node.seq != -2
                      "
                            style="padding-left: 10px"
                            color="red"
                            size="xs"
                            @click="delConfirmTestGroup(prop.node)"
                            flat
                            name="delete" />
                  </div>
                </template>
              </q-tree>
            </div>
            <div style="padding-top: 40px">
              <p>
                点击<a class="aClass text-h5"
                   @click="addTestGroupOpen">添加</a>一级集合
              </p>
            </div>
          </div>
        </div>
      </template>
      <template v-slot:after>
        <div class="q-pa-md">
          <div v-if="node.id === undefined"
               style="padding-top: 20%"
               class="row q-col-gutter-sm">
            <q-icon color="grey"
                    size="28px"
                    class="q-ml-sm"
                    name="mdi-set mdi-hand-pointing-left" />
            <span style="color: grey"
                  class="text-h6">选中集合可查看测试用例，以及进行修改</span>
          </div>
          <div v-else>
            <div class="row">
              <div style="width: 30%">
                <q-input :readonly="node.seq === -1 || node.seq === -2"
                         @keyup.enter="editTestGroup()"
                         @focus="editTestGroupMode()"
                         @blur="editTestGroupMode()"
                         :borderless="!editable"
                         maxlength="30"
                         dense
                         v-model="node.name"
                         type="input" />
              </div>
              <div style="width: 70%"
                   class="text-right">
                <q-btn flat
                       color="primary"
                       @click="testCaseRunAll"
                       icon="playlist_play"
                       label="运行" />
                <q-btn flat
                       @click="editTestGroup"
                       color="primary"
                       icon="save"
                       label="保存" />
                <q-btn color="primary"
                       flat
                       dense
                       icon="more_horiz">
                  <q-menu transition-show="jump-down"
                          transition-hide="jump-up">
                    <q-list style="width: 230px">
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
                      <!-- <q-item clickable v-close-popup>
                        <q-item-section avatar>
                          <q-avatar icon="assignment" color="primary" text-color="white" />
                        </q-item-section>
                        <q-item-section>
                          <q-item-label>导出报告</q-item-label>
                          <q-item-label caption>导出概要报告</q-item-label>
                        </q-item-section>
                        <q-item-section side>
                          <q-icon name="info" />
                        </q-item-section>
                      </q-item> -->
                    </q-list>
                  </q-menu>
                </q-btn>
              </div>
            </div>
            <q-separator />
            <div class="row">
              <q-tabs style="width: 30%"
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
                            style="width: 100%"
                            v-model="apiTestGroupSelectType"
                            animated
                            swipeable
                            vertical
                            transition-prev="jump-up"
                            transition-next="jump-up">
                <q-tab-panel name="overView">
                  <div class="q-mb-md">
                    <div class="col-12 col-sm-6 q-gutter-sm">
                      <TestRunReportEchar ref="testRunReportEchar" />
                    </div>
                    <!-- <q-input style="width: 95%;height:70px" placeholder="用例集合描述" @focus="editTestGroupMode('info')" @blur="editTestGroupMode('info')" :borderless="!editableInfo" maxlength="120" dense v-model="node.info" type="textarea" /> -->
                  </div>
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
                        <q-btn color="primary"
                               size="12px"
                               unelevated
                               class="table-head-btn"
                               @click="addTestCasesModelOpen">
                          新增
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
                                 :disabled="
                              props.row.status === '2' ||
                              props.row.status === '3'
                            "
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
                                <q-item @click="copyTestCases(props.row)"
                                        clickable
                                        v-close-popup>
                                  <q-item-section>复制</q-item-section>
                                </q-item>
                                <q-item @click="mgmtTestCases(props.row)"
                                        clickable
                                        v-close-popup>
                                  <q-item-section>编辑</q-item-section>
                                </q-item>
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

    <q-dialog v-model="handAddTestGroupMode"
              persistent>
      <q-card style="min-width: 450px">
        <q-toolbar class="bg-primary text-white">
          <q-btn flat
                 v-close-popup
                 round
                 dense
                 icon="arrow_back" />
          <q-toolbar-title>{{
              undefined != node && undefined != node.name
                ? node.name + "-"
                : ""
            }}新增集合</q-toolbar-title>
        </q-toolbar>
        <q-form @submit="addTestGroup"
                ref="addTestGroupForm">
          <q-card-section style="width: 430px"
                          class="text-right">
            <q-input filled
                     autofocus
                     v-model="addTestGroupModel.name"
                     type="text"
                     :rules="[(val) => (val && val.length > 0) || '']">
              <template v-slot:before>
                <span style="font-size: 15px; width: 80px">集合名称：</span>
              </template>
            </q-input>
            <q-input filled
                     v-model="addTestGroupModel.info"
                     type="textarea">
              <template v-slot:before>
                <span style="font-size: 15px; width: 80px">描述：</span>
              </template>
            </q-input>
          </q-card-section>
          <q-card-actions align="right">
            <q-btn style="width: 80px"
                   type="submit"
                   color="primary"
                   label="提交" />
            <q-btn style="width: 80px"
                   v-close-popup
                   flat
                   color="primary"
                   label="取消"
                   class="q-ml-sm" />
          </q-card-actions>
        </q-form>
      </q-card>
    </q-dialog>
    <q-dialog v-model="confirm"
              persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="delete"
                    color="primary"
                    text-color="white" />
          <span class="q-ml-sm"
                style="padding-left: 30px; width: 200px">
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
                 @click="delTestGroup()" />
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-dialog v-model="delConfirm"
              persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="delete"
                    color="primary"
                    text-color="white" />
          <span class="q-ml-sm"
                style="padding-left: 30px; width: 200px">
            是否确定删除？
          </span>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn label="删除"
                 @click="delTest"
                 color="primary"
                 v-close-popup />
          <q-btn flat
                 label="取消"
                 color="primary"
                 v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-dialog v-model="showAddCasesDialog">
      <q-layout style="width: 1000px; max-width: 200vw"
                view="Lhh lpR fff"
                container
                class="bg-white">
        <q-card>
          <div class="q-pa-md row q-col-gutter-sm">
            <div class="col-12 col-sm-5">
              <div class="text-h6 text-primary">»用例基本信息</div>
              <div class="row">
                <q-input style="width: 60%"
                         v-model="addTestCaseModel.name"
                         label="用例名称"
                         :rules="[(val) => (val && val.length > 0) || '']" />
                <q-radio v-if="node.seq != -2"
                         dense
                         style="padding-left: 15px"
                         v-model="addTestCaseModel.caseType"
                         val="0"
                         label="正例"
                         color="orange" />
                <q-radio v-if="node.seq != -2"
                         dense
                         style="padding-left: 15px"
                         v-model="addTestCaseModel.caseType"
                         val="1"
                         label="反例"
                         color="red" />
                <q-radio v-if="node.seq === -2"
                         dense
                         style="padding-left: 15px"
                         v-model="addTestCaseModel.caseType"
                         val="2"
                         label="基础用例"
                         color="red" />
              </div>
              <q-select :display-value="
                  formatDicDisplay(apiOptions, addTestCaseModel.apiPath)
                "
                        :readonly="mgmtFlag"
                        @input="qryShili"
                        label="UAT环境的接口路径"
                        filled
                        v-model="addTestCaseModel.apiPath"
                        use-input
                        hide-selected
                        fill-input
                        input-debounce="0"
                        :options="apiOptions"
                        @filter="apiFilter"
                        :rules="[(val) => val || '']"
                        style="width: 95%; padding-bottom: 32px">
                <template v-slot:no-option>
                  <q-item dense>
                    <q-item-section side>
                      <q-icon name="warning"
                              size="16px" />
                    </q-item-section>
                    <q-item-section>
                      <q-item-label class="text-grey-7">
                        没有可用数据
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                </template>
                <template v-slot:option="scope">
                  <q-item v-bind="scope.itemProps"
                          v-on="scope.itemEvents">
                    <q-item-section side>
                      <q-item-label>{{ scope.opt.name }}</q-item-label>
                    </q-item-section>
                    <q-item-section>
                      <q-item-label>{{ scope.opt.url }}</q-item-label>
                    </q-item-section>
                  </q-item>
                </template>
              </q-select>
              <q-input style="width: 95%"
                       label="描述"
                       filled
                       v-model="addTestCaseModel.caseDesc"
                       type="textarea" />
              <pre><span v-if="showResponseDemo" style="color:grey">接口应答示例：{{ JSON.stringify(responseDemo, null, 2)  }}</span></pre>
              <div v-if="showParameter">
                <div>所有常量</div>
                <q-scroll-area :thumb-style="thumbStyle"
                               :bar-style="barStyle"
                               style="height: 400px; max-width: 100%">
                  <div class="q-col-gutter-sm">
                    <div v-for="(para, index) in parameterList"
                         :key="'para_' + index">
                      <div class="row">
                        <div style="width: 50%">{{ para.enName }}</div>
                        <div style="width: 50%">{{ para.name }}</div>
                      </div>
                    </div>
                  </div>
                </q-scroll-area>
              </div>
            </div>
            <div class="col-12 col-sm-6 q-gutter-sm">
              <div class="text-h6 text-primary">»请求参数设置</div>
              <p>格式：@{static}、${caseKey.res.body.xx}</p>
              <q-input @click.stop="showok"
                       @keydown.native="listen($event)"
                       name="testParameterName"
                       rows="14"
                       dense
                       filled
                       v-model="addTestCaseModel.parameter"
                       type="textarea" />

              <div class="text-h6 text-primary">»编写断言脚本</div>
              <p>格式：$res.body.xx == xx</p>
              <q-input @input="assertScriptFun"
                       dense
                       filled
                       v-model="addTestCaseModel.assertScript"
                       type="textarea" />
            </div>
          </div>
        </q-card>
        <q-footer class="bg-white text-white">
          <q-toolbar inset>
            <q-toolbar-title class="text-right">
              <q-btn @click="saveParaSet()"
                     label="保存"
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
import { testGroupModel, testCaseModel } from "@/model";
import {
  qryTestGroup,
  addTestGroup,
  delTestGroup,
  updateTestGroup,
  qryTestCases,
  qryParameter,
  addTestCases,
  updateTestCases,
  delTest,
  testCaseRun,
} from "@/services/test";
import { date } from "quasar";
import { qryAppEnv } from "@/services/app";
import { qryApi, qryApiPreview } from "@/services/api";
import {
  positiveNotify,
  warningNotify,
  formatDicDisplay,
  html2pdf,
} from "@/utils/utils";

import TestRunReport from "@/views/apps/test/TestRunReport.vue";
import TestRunReportEchar from "@/views/apps/test/TestRunReportEchar.vue";

export default {
  name: "CaseList",
  components: {
    TestRunReport,
    TestRunReportEchar,
  },
  data () {
    return {
      date: date,
      mgmtFlag: false,
      showResponseDemo: false,
      delConfirm: false,
      showParameter: false,
      parameterList: [],
      responseDemo: "",
      apiList: [],
      apiOptions: [],
      addApiPathModalOpened: false,
      addTestCaseModel: testCaseModel(),
      showAddCasesDialog: false,
      thumbStyle: {
        right: "4px",
        borderRadius: "5px",
        backgroundColor: "#027be3",
        width: "5px",
        opacity: 0.75,
      },

      barStyle: {
        right: "2px",
        borderRadius: "9px",
        backgroundColor: "#027be3",
        width: "9px",
        opacity: 0.2,
      },
      pagination: {
        sortBy: "desc",
        descending: false,
        page: 0,
        rowsPerPage: 5,
      },
      testCasesfilter: "",
      testCasesList: [],
      columns: [
        {
          name: "name",
          align: "center",
          label: "用例名称",
          style: "width: 30%",
          field: (row) => row.name,
        },
        {
          name: "key",
          label: "key",
          align: "center",
          style: "width: 10%",
          field: (row) => row.uniKey,
        },
        {
          name: "status",
          label: "状态",
          align: "center",
          style: "width: 5%",
          sortable: true,
          field: (row) => row.status,
        },
        {
          name: "apiPath",
          align: "center",
          label: "接口路径",
          style: "width: 40%",
          sortable: true,
          field: (row) => row.apiPath,
        },
        {
          name: "upTime",
          align: "center",
          label: "最近时间",
          style: "width: 10%",
          sortable: true,
          field: (row) =>
            date.formatDate(row.updateTime, "YYYY/MM/DD HH:mm:ss"),
        },
        {
          name: "operation",
          align: "center",
          label: "操作",
          style: "width: 10%",
          field: (row) => row,
        },
      ],
      editableInfo: false,
      apiTestGroupSelectType: "cases",
      splitterModel: 25,
      selected: "",
      selectedReport: "",
      filter: "",
      handAddTestGroupMode: false,
      confirm: false,
      editable: false,
      addTestGroupModel: testGroupModel(),
      simple: [],
      expanded: [],
      node: {},
      testGroupList: [],
      appEnvList: [],
      uatEnv: "",
    };
  },
  mounted () { },
  computed: {
    pagesNumber () {
      return Math.ceil(this.testCasesList.length / this.pagination.rowsPerPage);
    },
    appId () {
      return this.$route.params.appId;
    },
  },
  methods: {
    formatDicDisplay,
    showTestPlanReportAll () {
      this.$refs.testRunReportRef.showReportAll(this.testCasesList);
    },
    showTestPlanReport (row) {
      this.$refs.testRunReportRef.showReport(row);
    },
    html2pdf () {
      var view = document.getElementById("testCaseRunReportView");
      const fileName = date.formatDate(new Date(), "YYYYMMDDHHmmss");
      html2pdf(view, true, "caseRun_" + fileName + ".pdf");
    },
    async testCaseRunAll () {
      let caseSeqList = [];
      this.testCasesList.map((testCase) => {
        caseSeqList.push(testCase.seq);
      });
      await testCaseRun({ caseSeqList: caseSeqList, appSeq: this.appId });
      this.testCasesList = this.testCasesList.map((testCase) => {
        return {
          ...testCase,
          status: "3", //执行中
        };
      });
    },
    async testCaseRun (row) {
      await testCaseRun({ caseSeqList: [row.seq], appSeq: this.appId });
      this.testCasesList = this.testCasesList.map((testCase) => {
        if (row.seq.toString() === testCase.seq.toString()) {
          return {
            ...testCase,
            status: "3", //执行中
          };
        } else {
          return testCase;
        }
      });
    },
    async saveParaSet () {
      if (this.mgmtFlag) {
        let caseUp = await updateTestCases({
          ...this.addTestCaseModel,
          appSeq: this.appId,
        });
        this.testCasesList = this.testCasesList.map((row, index) => {
          if (row.seq.toString() === caseUp.seq.toString()) {
            return caseUp;
          } else {
            return row;
          }
        });
        positiveNotify("修改成功");
      } else {
        let caseRe = await addTestCases({
          ...this.addTestCaseModel,
          apiPath:
            this.addTestCaseModel.apiPath.url === undefined
              ? this.addTestCaseModel.apiPath
              : this.addTestCaseModel.apiPath.url,
          testGroupSeq: this.node.seq,
          appSeq: this.appId,
        });
        this.testCasesList.unshift(caseRe);
        positiveNotify("添加成功");
      }
      this.showAddCasesDialog = false;
    },
    assertScriptFun (val) {
      this.showParameter = false;
      this.showResponseDemo = true;
    },
    showok (event) {
      // console.log(event.y)
    },
    listen (event) {
      if (event.key === "@") {
        this.showResponseDemo = false;
        this.showParameter = true;
      }
    },
    async qryShili (api) {
      let apiTables = await qryApiPreview({
        apiSeq: api.seq,
        appEnvSeq: this.uatEnv.seq,
      });
      apiTables.forEach((apiTable) => {
        apiTable.forEach((api) => {
          if (
            !this.mgmtFlag ||
            document.getElementsByName("testParameterName")[0].value === ""
          ) {
            let reqStr = "";
            api.requestDemo.forEach((demo) => {
              reqStr = reqStr + demo + "\n";
            });
            if (Object.keys(api.requestObjectDemo).length !== 0) {
              reqStr =
                reqStr +
                " -d " +
                "'" +
                JSON.stringify(api.requestObjectDemo, null, 2) +
                "'";
            }
            document.getElementsByName("testParameterName")[0].value = reqStr;
            document
              .getElementsByName("testParameterName")[0]
              .dispatchEvent(new Event("input"));
          }
          this.responseDemo = api.responseDemo;
        });
      });
      this.showResponseDemo = false;
    },
    apiFilter (val, update, abort) {
      update(() => {
        this.apiOptions = this.apiList
          .filter(
            (api) =>
              api.name.toLowerCase().indexOf(val.toLowerCase()) > -1 ||
              api.url.toLowerCase().indexOf(val.toLowerCase()) > -1
          )
          .map((api) => {
            return {
              ...api,
              label: api.url,
              value: api.url,
            };
          });
      });
    },
    handleAddApiPath (api) { },
    delTestCases (row) {
      this.delConfirm = true;
      this.row = row;
      console.log(this.row);
    },
    async delTest () {
      await delTest(this.row.seq);
      this.testCasesList = this.testCasesList.filter(
        (testCases) => testCases.seq.toString() !== this.row.seq.toString()
      );
      positiveNotify("删除成功");
      this.delConfirm = false;
    },
    copyTestCases (row) {
      this.addTestCaseModel = {
        ...row,
        status: "2",
        entSeq: null,
        seq: null,
        uniKey: "",
        updateTime: "",
        createTime: "",
      };
      this.saveParaSet();
    },
    mgmtTestCases (row) {
      this.mgmtFlag = true;
      this.addTestCaseModel = { ...row };
      this.showAddCasesDialog = true;
      let fi = this.apiList.filter(
        (api) => api.url.toString() === row.apiPath.toString()
      );
      this.qryShili(fi[0]);
    },
    addTestCasesModelOpen () {
      this.mgmtFlag = false;
      this.addTestCaseModel = testCaseModel();
      if (this.node.seq === -2) {
        this.addTestCaseModel.caseType = "2";
      }
      this.responseDemo = "";
      this.showResponseDemo = false;
      this.showAddCasesDialog = true;
    },
    editTestGroupMode (edit) {
      if (edit === "info") {
        this.editableInfo = !this.editableInfo;
      }
    },
    async editTestGroup () {
      let a = await updateTestGroup({ ...this.node, appSeq: this.appId });
      this.testGroupList = this.testGroupList.map((group) => {
        if (a.seq.toString() === group.seq.toString()) {
          return a;
        } else {
          return group;
        }
      });
      this.createTestGroupTree();
      positiveNotify("更新成功!");
    },
    resetFilter () {
      this.filter = "";
      this.$refs.filter.focus();
    },
    initEcharShow () {
      if (this.$refs.testRunReportEchar !== undefined) {
        this.$refs.testRunReportEchar.initEchar(this.testCasesList);
      }
    },
    async clickTree (node) {
      this.editable = false;
      this.node = node;

      let re = await qryTestCases({
        testGroupSeq: node.seq,
        appSeq: this.appId,
      });
      this.testCasesList = re.cases;
      this.initEcharShow();
    },
    addSubTestGroup (node) {
      this.node = node;
      this.addTestGroupModel = {};
      this.addTestGroupModel.parentSeq = node.seq;
      this.handAddTestGroupMode = true;
    },
    async delTestGroup () {
      await delTestGroup(this.node.seq);
      this.testGroupList = this.testGroupList.filter(
        (testGroup) => testGroup.seq != this.node.seq
      );
      this.createTestGroupTree();
      positiveNotify("删除成功!");
      this.confirm = false;
      this.node = [];
    },
    async delConfirmTestGroup (node) {
      this.confirm = true;
      this.node = node;
    },
    addTestGroupOpen () {
      this.addTestGroupModel = {};
      this.handAddTestGroupMode = true;
    },
    async addTestGroup () {
      let a = await addTestGroup({
        ...this.addTestGroupModel,
        appSeq: this.appId,
      });
      this.testGroupList.push(a);

      this.handAddTestGroupMode = false;

      this.createTestGroupTree();

      positiveNotify("添加成功!");
    },
    async createTestGroupTree () {
      let groups = this.testGroupList;
      groups = groups.map((group) => {
        return {
          ...group,
          parentSeq: null != group.parent ? group.parent.seq : null,
        };
      });
      groups.unshift({
        ...testGroupModel(),
        seq: -1,
        name: "所有用例",
        parentSeq: null,
      });
      groups.unshift({
        ...testGroupModel(),
        seq: -2,
        name: "基础用例",
        parentSeq: null,
      });
      this.simple = this.subTree(null, groups, 1);
    },
    subTree (parent, list, level) {
      let sub = [];
      list.forEach((el) => {
        let re = { ...el };
        re.level = level;
        re.id = "testGroup_" + el.seq;
        re.label = el.name;
        this.expanded.push(re.id);
        if (parent === el.parentSeq) {
          re.children = this.subTree(el.seq, list, level + 1);
          sub.push(re);
          if (this.selected === re.id) {
            this.clickTree(re);
          }
        }
      });
      return sub;
    },
    getMessage (mess) {
      if (mess === "testRunOk") {
        //收到测试结束通知
        this.clickTree(this.node);
      }
    },
  },
  async created () {
    this.$root.$on("recTestRunResult", this.getMessage);
    let a = await qryTestGroup({ appSeq: this.appId });
    this.testGroupList = a.testGroups;

    this.selected = "testGroup_-1";
    this.createTestGroupTree();

    let re = await qryAppEnv({ appSeq: this.appId });
    this.appEnvList = re.appEnvs;
    let uatEnv = this.appEnvList.filter((appEnv) => appEnv.type === "UAT");
    this.uatEnv = uatEnv[0];
    if (uatEnv.length === 0) {
      warningNotify("该应用未定义UAT环境！");
    } else {
      let re = await qryApi({
        groupSeq: "",
        appSeq: this.appId,
        appEnvSeq: this.uatEnv.seq,
      });
      this.apiList = re.apis;
    }

    let response = await qryParameter({ appSeq: this.appId });
    this.parameterList = response.parameters;
  },
};
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
