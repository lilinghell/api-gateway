<template>
  <div class="q-pa-md">
    <q-dialog v-model="showDialog">
      <q-layout style="width: 1000px; max-width: 200vw;" view="Lhh lpR fff" container class="bg-white">
        <q-card>
          <div class="q-pa-md" id="apiPreviewView">
            <template v-for="(apiTable, indexs) in apiTables">
              <template v-for="(api, index) in apiTable">
                <q-card-section :key="'head'+indexs+index">
                  <div class="text-h6 doc-heading">{{api.apiName}}-v{{api.version}}</div>
                </q-card-section>
                <q-card-section :key="'sub'+indexs+index" class="q-pt-none">
                  <table class="apiPreviewTable" aria-describedby="接口预览">
                    <tr>
                      <td style="text-align: center;">URL</td>
                      <td colspan="6">{{api.url}}</td>
                    </tr>
                    <tr>
                      <td style="text-align: center;">method</td>
                      <td colspan="6">{{api.method}}</td>
                    </tr>
                    <tr>
                      <td style="text-align: center;">描述</td>
                      <td colspan="6">{{api.description}}</td>
                    </tr>
                    <tr class="tr-th">
                      <th style="width:15%" id="name">请求参数名</th>
                      <th style="width:15%" id="dec">描述</th>
                      <th style="width:10%" id="style">类型</th>
                      <th style="width:10%" id="requit">必输</th>
                      <th style="width:10%" id="in">in</th>
                      <th style="width:10%" id="format">格式</th>
                      <th style="width:30%" id="other">说明</th>
                    </tr>
                    <template v-for="(parameter, index) in api.parametersList">
                      <tr :key="'parameter'+indexs+index" class="tr-td">
                        <td>
                          <div v-html="parameter.name">
                            {{parameter.name}}
                          </div>
                        </td>
                        <td>{{parameter.description}}</td>
                        <td>{{parameter.type}}</td>
                        <td>{{parameter.required === true ? 'true' : ''}}</td>
                        <td>{{parameter.in}}</td>
                        <td>{{parameter.format}}</td>
                        <td>
                          <div v-if="parameter.otherInfo != null">
                            {{parameter.otherInfo.miniLength != null ? '最小长度:'+parameter.otherInfo.miniLength : ''}}
                            {{parameter.otherInfo.maxLength != null ? '最大长度:'+parameter.otherInfo.maxLength : ''}}
                            {{parameter.otherInfo.enumValue != null ? '枚举值:'+parameter.otherInfo.enumValue : ''}}
                            {{parameter.otherInfo.defaultValue != null ? '默认值:'+parameter.otherInfo.defaultValue : ''}}
                            {{parameter.otherInfo.minimum != null ? '最小值:'+parameter.otherInfo.minimum : ''}}
                            {{parameter.otherInfo.maximum != null ? '最大值:'+parameter.otherInfo.maximum : ''}}
                            {{parameter.otherInfo.type != null ? '类型:'+parameter.otherInfo.type : ''}}
                          </div>
                        </td>
                      </tr>
                    </template>
                    <tr class="tr-th">
                      <th id="name">应答参数名</th>
                      <th id="dec">描述</th>
                      <th id="style">类型</th>
                      <th id="requit">必输</th>
                      <th colspan="2" id="format">格式</th>
                      <th id="other">说明</th>
                    </tr>
                    <template v-for="(response, index) in api.responsesList">
                      <tr :key="'response'+indexs+index" class="tr-td">
                        <td>
                          <div v-html="response.name">
                            {{response.name}}
                          </div>
                        </td>
                        <td>{{response.description}}</td>
                        <td>{{response.type}}</td>
                        <td>{{response.required === true ? 'true' : ''}}</td>
                        <td colspan="2">{{response.format}}</td>
                        <td>
                          <div v-if="response.otherInfo != null">
                            {{response.otherInfo.miniLength != null ? '最小长度:'+response.otherInfo.miniLength : ''}}
                            {{response.otherInfo.maxLength != null ? '最大长度:'+response.otherInfo.maxLength : ''}}
                            {{response.otherInfo.enumValue != null ? '枚举值:'+response.otherInfo.enumValue : ''}}
                            {{response.otherInfo.defaultValue != null ? '默认值:'+response.otherInfo.defaultValue : ''}}
                            {{response.otherInfo.minimum != null ? '最小值:'+response.otherInfo.minimum : ''}}
                            {{response.otherInfo.maximum != null ? '最大值:'+response.otherInfo.maximum : ''}}
                            {{response.otherInfo.type != null ? '类型:'+response.otherInfo.type : ''}}
                          </div>
                        </td>
                      </tr>
                    </template>
                    <tr class="shili">
                      <th id="requestLiz">请求示例</th>
                      <td colspan="6">
                        <template v-for="(value, index) in api.requestDemo">
                          <pre :key="'requestDemo'+indexs+index">{{ value }}</pre>
                        </template>
                        <template v-if="Object.keys(api.requestObjectDemo).length > 0">
                          <pre> -d '{{ JSON.stringify(api.requestObjectDemo, null, 2)  }}'</pre>
                        </template>
                      </td>
                    </tr>
                    <tr class="shili">
                      <th id="responseLiz">应答示例</th>
                      <td colspan="6">
                        <pre>{{ JSON.stringify(api.responseDemo, null, 2)  }}</pre>
                      </td>
                    </tr>
                  </table>
                </q-card-section>
              </template>
            </template>
          </div>
        </q-card>
        <q-footer class="bg-white text-white">
          <q-toolbar inset>
            <q-toolbar-title class="text-right">
              <q-btn @click="html2pdf()" label="导出PDF" color="primary" v-close-popup />
            </q-toolbar-title>
          </q-toolbar>
        </q-footer>
      </q-layout>
    </q-dialog>
  </div>
</template>
<script>
import { html2pdf } from '@/utils/utils'
import { qryApiPreview, qryApiHisPreview } from '@/services/api'
import { date } from 'quasar'

export default {
  name: 'ApiPreview',
  data() {
    return {
      showDialog: false,
      apiTables: [],
    }
  },
  computed: {
    appId() {
      return this.$route.params.appId
    },
    appEnvId() {
      return this.$route.params.appEnvId
    },
  },
  methods: {
    html2pdf() {
      var view = document.getElementById('apiPreviewView')
      const fileName = date.formatDate(new Date(), 'YYYYMMDDHHmmss')
      html2pdf(view, true, 'apis_' + fileName + '.pdf')
    },
    async apiPreview(type, id) {
      if (type === 'his') {
        this.apiTables = await qryApiHisPreview({
          id: id,
          appEnvSeq: this.appEnvId,
        })
      } else if (type === 'current') {
        this.apiTables = await qryApiPreview({
          apiSeq: id,
          appEnvSeq: this.appEnvId,
        })
      } else if (type === 'group') {
        this.apiTables = await qryApiPreview({
          groupSeq: id,
          appEnvSeq: this.appEnvId,
        })
      }
    },
  },
}
</script>
<style scoped>
.q-pt-none table {
  border-collapse: collapse;
  border-spacing: 0;
}
.q-pt-none table td {
  border: 1px solid rgb(194, 187, 187);
}
.tr-th th {
  color: white;
  background-color: #1976d2;
  text-align: center;
}
.shili th {
  color: white;
  background-color: #1976d2;
  text-align: center;
  border: 1px solid rgb(194, 187, 187);
}
.tr-td td {
  text-align: left;
}
.trTemplate tr:nth-child(2n) {
  background-color: #1976d2;
}
</style>

<style lang="sass" scoped>
@import "~@/styles/quasar.sass"
.apiPreviewTable
  width: 100%
.doc-heading
  color: $primary
</style>
