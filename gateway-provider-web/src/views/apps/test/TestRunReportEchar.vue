<template>
  <div class="q-pa-md">
    <div id="echarMain" style="width: 100%; height: 500px"></div>
    <!-- <q-scroll-area :thumb-style="thumbStyle" :bar-style="barStyle" style="height: 600px; max-width: 100%;">
                        <div class="q-col-gutter-sm">
                          <div v-for="testCase in testCasesList" :key="`testCase-${testCase.seq}`">
                            <div class="row">
                              <div style="width: 25%">{{ testCase.name }}</div>
                              <div style="width: 35%">{{ testCase.apiPath }}</div>
                              <div style="width: 25%">{{date.formatDate(testCase.updateTime, 'YYYY/MM/DD HH:mm:ss')}}</div>
                              <div style="width: 15%">
                                <span class="text-teal" v-if="testCase.status === '0'">执行成功</span>
                                <span class="text-red" v-if="testCase.status === '1'">执行失败</span>
                                <span class="text-primary" v-if="testCase.status === '2'">待执行</span>
                                <span class="text-orange" v-if="testCase.status === '3'">执行中</span>
                                <span class="text-teal" v-if="testCase.status === '4'">验证成功</span>
                                <span class="text-red" v-if="testCase.status === '5'">验证失败</span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </q-scroll-area> -->
  </div>
</template>
<script>
import * as echarts from 'echarts'

export default {
  name: 'TestRunReportEchar',
  data() {
    return {
      myChart: '',
      echarData: [
        { value: 0, name: '执行成功' },
        { value: 0, name: '执行失败' },
        { value: 0, name: '待执行' },
        { value: 0, name: '执行中' },
        { value: 0, name: '验证成功' },
        { value: 0, name: '验证失败' },
      ],
      echarOption: {
        title: {
          text: '测试结果概览',
          subtext: '执行总用例数:0',
          left: 'center',
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
        },
        series: [
          {
            name: '数据来源',
            type: 'pie',
            radius: '50%',
            data: [],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            },
          },
        ],
      },
    }
  },
  computed: {},
  methods: {
    initEchar(testCasesList) {
      var chartDom = document.getElementById('echarMain')
      if (chartDom === null) {
        return
      }
      // console.log(chartDom)
      if (this.myChart === '') {
        this.myChart = echarts.init(chartDom)
      }

      let status_0 = 0
      let status_1 = 0
      let status_2 = 0
      let status_3 = 0
      let status_4 = 0
      let status_5 = 0
      testCasesList.forEach((testCase) => {
        if (testCase.status === '0') {
          status_0 = status_0 + 1
        }
        if (testCase.status === '1') {
          status_1 = status_1 + 1
        }
        if (testCase.status === '2') {
          status_2 = status_2 + 1
        }
        if (testCase.status === '3') {
          status_3 = status_3 + 1
        }
        if (testCase.status === '4') {
          status_4 = status_4 + 1
        }
        if (testCase.status === '5') {
          status_5 = status_5 + 1
        }
      })
      this.echarData = []
      if (status_0 > 0) {
        this.echarData.push({ value: status_0, name: '执行成功 ' + status_0 })
      }
      if (status_1 > 0) {
        this.echarData.push({ value: status_1, name: '执行失败 ' + status_1 })
      }
      if (status_2 > 0) {
        this.echarData.push({ value: status_2, name: '待执行 ' + status_2 })
      }
      if (status_3 > 0) {
        this.echarData.push({ value: status_3, name: '执行中 ' + status_3 })
      }
      this.echarData.push({ value: status_4, name: '验证成功 ' + status_4 })
      this.echarData.push({ value: status_5, name: '验证失败 ' + status_5 })
      this.echarOption.series[0].data = this.echarData

      this.echarOption.title.subtext = '执行总用例数: ' + testCasesList.length
      this.myChart.setOption(this.echarOption)
    },
  },
}
</script>