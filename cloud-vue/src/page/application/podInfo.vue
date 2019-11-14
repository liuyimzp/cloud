<template>
  <div class="podInfo">
    <div class="left-part">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="line-height: 32px;">
        <el-breadcrumb-item :to="{ path: '/appManage' }" :replace="true">应用管理</el-breadcrumb-item>
        <el-breadcrumb-item>{{this.$route.query.n}}</el-breadcrumb-item>
      </el-breadcrumb>
      <div class="flex-content">
        <div class="fit">
          <el-table v-loading="loading" element-loading-text="拼命加载中" :data="tableData" :stripe="true"
                    :highlight-current-row="true" style="width: 100%" height="100%">
            <el-table-column prop="podName" label="pod名称" align="center"></el-table-column>
            <el-table-column prop="status" label="状态" align="center"></el-table-column>
            <el-table-column prop="nodeName" label="节点名称" align="center"></el-table-column>
            <el-table-column prop="nodeIP" label="节点IP" :show-overflow-tooltip="true" min-width="130px"
                             align="center"></el-table-column>
            <el-table-column prop="nodeId" label="日志" align="center">
              <template slot-scope="scope">
                <a v-if="scope.row.status == 'Running'">
                  <el-button type="text" @click="getLog(scope.row.status,scope.row.podName)" size="small">查看</el-button>
                </a>
              </template>
            </el-table-column>
            <el-table-column prop="containerIP" label="终端" align="center">
              <template slot-scope="scope">
                <a target="_blank" :href="scope.row.containerIP" v-if="scope.row.status == 'Running'">
                  <el-button type="text" size="small">访问</el-button>
                </a>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-dialog title="日志详情" :visible.sync="showLog" class="log-box" :fullscreen="true" :close-on-click-modal='false'
               @close="closeLog">
      <el-form :model="form" :inline="true">
        <el-form-item label="过滤类型">
          <el-select v-model="form.region" placeholder="请选日志过滤类型">
            <el-option label="行数" value="tail"></el-option>
            <el-option label="时间" value="since"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-if="form.region==='since'" label="最近:">
          <el-input placeholder="请输入内容" v-model="form.inputParam" clearable>
          </el-input>
        </el-form-item>
        <el-form-item v-if="form.region==='since'" label="单位:">
          <el-select v-model="form.timesUnit" placeholder="请选日志过滤类型">
            <el-option label="小时" value="h"></el-option>
            <el-option label="分钟" value="m"></el-option>
            <el-option label="秒" value="s"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-if="form.region==='tail'" label="最近:">
          <el-input placeholder="请输入内容" v-model="form.inputParam" clearable>
            <template slot="append">行</template>
          </el-input>
        </el-form-item>

      </el-form>
      <div class="log-detail" v-html="logInfo" v-loading="logLoading"></div>
      <span slot="footer" class="">
        <el-button size="small" type="primary" @click="doGetLog(selectPodName)">查看</el-button>
        <el-button size="small" @click="doDownLoadLog(selectPodName)">下载</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
  import {getPodInfo, getPodLog} from '../../axios/api'

  export default {
    name: 'podInfo',
    data() {
      return {
        appId: '',
        appIdentify: '',
        recordLog: '0',
        tableData: [],
        loading: false,
        logLoading: false,
        showLog: false,
        logInfo: '',
        t1: null,
        selectPodName: '',
        form: {
          name: '',
          region: 'tail',
          delivery: false,
          since: 'minute',
          inputParam: '200',
          timesUnit: '',
        },
        formLabelWidth: '120px'
      }
    },
    mounted() {
      if (this.$route.meta.keepAlive === false && this.$route.name == 'podInfo') {
        if (this.$route.query.id != undefined) {
          this.appId = this.$route.query.id;
        }
        if (this.$route.query.appIdentify != undefined) {
          this.appIdentify = this.$route.query.appIdentify;
        }
        if (this.$route.query.recordLog != undefined) {
          this.recordLog = this.$route.query.recordLog;
        }
        this.loading = true;
        this.fnGetAll();
      }
    },
    methods: {
      fnGetAll() {
        getPodInfo({"id": this.appId, "appIdentify": this.appIdentify, "recordLog": this.recordLog}).then((res) => {
          if (res.success) {
            this.tableData = res.data;
          } else {
            if (res.errorMsg) {
              this.$message({showClose: true, message: res.errorMsg, type: 'error'});
            }
          }
          this.loading = false;
        });
      },
      getLog(status, podName) {
        this.selectPodName = podName;
        if (status.trim() == "ContainerCreating") {
          this.$message({showClose: true, message: "节点还在准备中,请稍后再查看!", type: 'error'});
          return;
        }
        this.showLog = true;
        this.logLoading = true;
        this.doGetLog(podName);
      },
      doGetLog(podName) {
        this.logLoading = true;
        getPodLog({
          "appId": this.appId,
          "podName": podName,
          "filterType": this.form.region,
          "lineNum": this.form.inputParam,
          "inputTimes": this.form.inputParam + this.form.timesUnit
        }).then((res) => {
          if (res.success) {
            this.logInfo = res.data;
            this.logLoading = false;
          } else {
            if (res.errorMsg) {
              this.$message({showClose: true, message: res.errorMsg, type: 'error'});
            }
          }
        });
      },
      doDownLoadLog(podName) {
        getPodLog({
          "appId": this.appId,
          "podName": podName,
          "filterType": this.form.region,
          "lineNum": this.form.inputParam,
          "inputTimes": this.form.inputParam + this.form.timesUnit
        }).then((res) => {
          if (res.success) {
            this.logInfo = res.data;
            if (!this.logInfo) {
              return
            }
            let url = window.URL.createObjectURL(new Blob([this.logInfo]))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', podName + '-log.txt')

            document.body.appendChild(link)
            link.click()
          } else {
            if (res.errorMsg) {
              this.$message({showClose: true, message: res.errorMsg, type: 'error'});
            }
          }
        });
      },
      closeLog() {
        this.logInfo = '';
        this.showLog = false;
        this.selectPodName = '';
        this.since = 'minute';
        this.form.inputParam = '200';
        this.form.timesUnit='';
      }
    },


  }
</script>
<style lang="scss" type="text/scss">
  .log-box {
    width: 80%;
    margin: 0 auto !important;

    .is-fullscreen, .el-dialog__body {
      overflow: auto;
      @include flexbox-display-flex();
      @include flexbox-flex();
      @include beautifyScrollbar();
      box-sizing: border-box;
    }

    .el-dialog__body {
      padding: 10px 50px;
    }
  }
</style>
<style lang="scss" scoped type="text/scss">
  .podInfo, .fit {
    height: 100%;
  }

  .title {
    height: 32px;
    line-height: 32px;
    color: $main-ft-color;
    font-size: 18px;
  }

  .left-part {
    padding: 20px 25px 0 25px;
    box-sizing: border-box;
    height: 100%;
    @include flexbox-display-flex();
  }

  .flex-content {
    overflow: auto;
    @include flexbox-display-flex();
    @include flexbox-flex();
    padding-top: 20px;
  }

  .log-detail {
    overflow-y: scroll;
    @include beautifyScrollbar();
    min-height: 200px;
    word-break: break-all;
    white-space: pre-wrap;
    font-size: 14px;
    color: $text-l2-color;
    background-color: $description-color;
  }
</style>
