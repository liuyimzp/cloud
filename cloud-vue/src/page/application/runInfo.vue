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
            <el-table-column prop="appName" label="应用名称" align="center"></el-table-column>
            <el-table-column prop="appCycle" label="状态" align="center">
              <template slot-scope="scope">
                <p>{{scope.row.appCycle === 'False'?'运行中':'已暂停'}}</p>
              </template>
            </el-table-column>
            <el-table-column prop="appNum" label="执行次数" align="center"></el-table-column>
            <el-table-column prop="appLastTime" label="上次执行" align="center">
              <template slot-scope="scope">
                <p>{{scope.row.appNum === '0'?'还未执行过':scope.row.appLastTime}}</p>
              </template>
            </el-table-column>
            <el-table-column prop="appAge" label="执行总时间" align="center"></el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import {getRunInfo, getPodLog} from '../../axios/api'
  var stackInterval;
  export default {
    name: 'runInfo',
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
      if (this.$route.meta.keepAlive === false && this.$route.name == 'runInfo') {
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
        this.fnGetAllInfo();
      }
    },
    beforeDestroy(){
      //销毁界面前清除定时函数
      clearInterval(stackInterval);
    },
    methods: {
      fnGetAllInfo(){
        clearInterval(stackInterval);
        this.fnGetAll();
        stackInterval = setInterval(()=>{
          this.fnGetAll();
        }, 5000);
      },
      fnGetAll() {
        getRunInfo({"id": this.appId, "appIdentify": this.appIdentify, "recordLog": this.recordLog}).then((res) => {
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
