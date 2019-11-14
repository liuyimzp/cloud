<template>
  <div class="appManage">
    <div class="left-part">
      <div class="title">
        <span style="line-height:32px;">jenkins管理</span>
        <div style="float: right;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="rightMenu = true; formEdit = false;" style="float: right">创建</el-button>
        </div>
      </div>
      <div class="description">应用管理为用户提供了便捷的向导式应用发布流程。支持应用实例的动态伸缩、常用中间件的快速部署和应用状态监控。</div>
<!--      <el-form label-position="right" :model="searchInfo" ref="searchInfo" label-width="108px" class="search-box">-->
<!--        <el-form-item label="应用名称/标识" prop="field">-->
<!--          <el-input v-model="searchInfo.field" class="full-width" clearable @change="fnSearchInfo"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="所在集群" prop="clusterId">-->
<!--          <el-select v-model="searchInfo.clusterId" class="full-width" filterable @change="fnChangeSearch" clearable>-->
<!--            <el-option v-for="item in allClusters" :key="item.id" :label="item.name" :value="item.id"></el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        &lt;!&ndash;<el-button type="primary" size="small" style="margin: 4px 0 0 14px;" icon="el-icon-search" @click="fnSearchInfo">搜索</el-button>&ndash;&gt;-->
<!--        <el-button type="primary" size="small" style="margin: 4px 0 0 10px;"  @click="fnSearchReset">重置</el-button>-->
<!--      </el-form>-->
      <div class="flex-content">
        <el-table :data="tableData" v-loading="loading" element-loading-text="拼命加载中" :stripe="true" :highlight-current-row="true" style="width: 100%" height="100%">
          <el-table-column prop="name" :show-overflow-tooltip="true" min-width="80px" label="名称" align="center"></el-table-column>
          <el-table-column prop="describe" :show-overflow-tooltip="true" min-width="130px" label="描述" align="center"></el-table-column>
          <el-table-column prop="stat" label="运行状态" align="center">
            <template slot-scope="scope">
              <span :class="scope.row.stat === '1' ? 'color-success' : 'color-warning'">{{scope.row.statStr}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="lastSucces" label="上次成功" align="center">
            <template slot-scope="scope">
              <span>{{scope.row.lastSucces}}</span>
              <el-button type="text" size="small" @click="fnShowLogSussce(scope.row)" :disabled="false">{{scope.row.lastSuccesId}}</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="lastFailed" label="上次失败" align="center">
            <template slot-scope="scope">
              <span>{{scope.row.lastFailed}}</span>
              <el-button type="text" size="small" @click="fnShowLogFailed(scope.row)" :disabled="false">{{scope.row.lastFailedId}}</el-button>
            </template>
          </el-table-column>
<!--          <el-table-column prop="lastSucces" :show-overflow-tooltip="true" min-width="130px" label="上次成功" align="center"><el-button type="text" size="small" @click="" :disabled="false">{{scope.row.lastSuccesId}}</el-button></el-table-column>-->
<!--          <el-table-column prop="lastFailed" :show-overflow-tooltip="true" min-width="130px" label="上次失败" align="center"><el-button type="text" size="small" @click="" :disabled="false">{{scope.row.lastFailedId}}</el-button></el-table-column>-->
          <el-table-column prop="lastTime" :show-overflow-tooltip="true" min-width="130px" label="持续时间" align="center"></el-table-column>
          <el-table-column label="操作" width="200px" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="" :disabled="false">立即构建</el-button>
              <el-button type="text" size="small" @click="" :disabled="false">参数配置</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <el-dialog title="日志详情" width="800px" :visible.sync="showLog" class="log-box" :close-on-click-modal='false' @close="showLog=false">
      <div class="log-detail" v-html="logInfo" v-loading="logLoading"></div>
    </el-dialog>
  </div>
</template>
<script>
    import {getAllJenkinsJob} from '../../axios/api'
    import { isLowercaseMinus,isNumber } from '../../assets/js/utils'
    import AceEditor from 'ace-vue2'
    var validIdentify = (rule, value, callback) => {
        if (!value){
            callback(new Error('请输入应用标识'))
        }else  if (!isLowercaseMinus(value)){
            callback(new Error('请输入小写英文、数字、减号(减号不能在首、尾)'))
        }else {
            callback()
        }
    };
    export default {
        name: 'jenkinsIntegration',
        data() {
            return {
                tableData:[],
                loading:false,
                showLog:false,
                logInfo:""
            }
        },
        components:{
            AceEditor
        },
        mounted(){
            if(this.$route.meta.keepAlive === false && this.$route.name == 'jenkinsIntegration') {
                this.fnJobGetAll();
            }
        },
        methods: {
            fnJobGetAll(){
              this.loading = true;
              getAllJenkinsJob().then((res) => {
                if(res.success){
                  this.tableData = res.data;
                }else {
                  this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                }
                this.loading = false;
              })
            },
            fnShowLogFailed(row){
                this.logInfo = row.failedLog;
                this.showLog = true;
            },
            fnShowLogSussce(row){
                this.logInfo = row.sussceLog;
                this.showLog = true;
            }
        }
    }
</script>
<style lang="scss" type="text/scss">
  .el-textarea__inner { @include beautifyScrollbar($scrollbar-bgcolor: $textArea-scrollbar-bgcolor)}
  .el-table-filter__bottom { text-align: center!important; }
  .appManage {
    .el-table th { padding: 7px 0 }
    .el-input-number .el-input__inner { text-align: left; }
  }
  .search-box { .el-form-item { margin-right: 0; margin-bottom: 20px!important; width: 30%; float: left; } }
</style>
<style lang="scss" scoped type="text/scss">
  .el-table{
    color:#333
  }
  .appManage,.fit { height: 100%; }
  .title{height:32px;color:$main-ft-color;font-size:18px;}
  .description { margin: 10px 0 20px; padding: 20px; color: $main-ft-color; background-color: $description-color; }
  .left-part { padding: 20px 25px 0 25px; box-sizing: border-box; height: 100%; @include flexbox-display-flex(); }
  .box-card { @include rightMenu(); }
  .full-width { width: 100%; }
  .el-form-item { margin-bottom: 16px; }
  .flex-content { overflow: auto; @include flexbox-display-flex(); @include flexbox-flex(); @include beautifyScrollbar();}
  .pos-add { position: absolute; top: 0; right: -18px; }
  .icon-width { width: 20px; display: inline-block; }
  .btn-flex { cursor: pointer;
    &:hover { color: $color-info }
  }
  .flex-item { display: inline-block; width: 90px; line-height: 40px; text-align: right; margin-right: 10px; }
  .flex-width { width: 320px; box-sizing: border-box; }
  .pagination { padding: 20px 0; text-align: center; }
  .color-info { color: $color-info!important; }
  .color-loading { color: $color-info!important; }
  .color-success { color: $color-success!important; }
  .color-warning { color: $color-warning!important; }
  .color-error { color: $color-error!important; }
  .color-disabled { color: $color-disabled!important; }
  .mg-r2 { margin-right: -20px; }
  .slider-detail { margin-bottom: 10px; padding-left: 80px; }
  .slider-unit { margin: 0 15px; color: $color-info; font-weight: bold; }
  .describe { color: $text-l2-color; margin-top: 10px }
  .showFile{height: 300px; display: block}
  .onShowFile{display: none}
  .disable{pointer-events:none; color:$color-disabled}
</style>
