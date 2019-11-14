<template>
  <div class="scriptManage">
    <div class="title">
      <span>脚本管理</span>
      <el-button type="primary" size="small" icon="el-icon-plus" @click="$router.push({name: 'scriptDetail', params:{ type: 'add' }})" style="float: right">新增</el-button>
    </div>
    <div class="description">系统脚本包含了一系列的Linux脚本文件，用于对集群环境的搭建和删除，适用对象为平台开发人员。</div>
    <div class="flex-content">
      <div class="fit">
        <el-table v-loading="loading" element-loading-text="拼命加载中" :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="100%">
          <el-table-column prop="shellName" label="脚本名称" align="center" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="shellUidConstant" label="脚本标识" align="center" min-width="120px" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="shellTypeDesc" label="脚本类型" align="center"></el-table-column>
          <el-table-column label="操作" width="120px" align="center">
            <template slot-scope="scope">
              <el-button type="text" @click="$router.push({name: 'scriptDetail', params:{ row: scope.row, edit: false, type: 'show' }})" size="small">查看</el-button>
              <el-button type="text" @click="$router.push({name: 'scriptDetail', params:{ row: scope.row, edit: true, type: 'edit' }})" size="small">修改</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>
<script>
  import {shellGetAll} from '../../axios/api'
  export default {
    name: 'scriptManage',
    data() {
      return {
        tableData: [],
        loading: false,
      }
    },
    watch: {
      $route (to, from) {
        if(to.name == 'scriptManage' && from.name == 'scriptDetail' && to.params.reload){
          this.fnGetAll();
        }
       }
    },
    mounted() {
      if(this.$route.name == 'scriptManage') {
        this.fnGetAll();
      }
    },
    methods: {
      fnGetAll() {
        this.loading = true;
        shellGetAll().then((res) => {
          if (res.success) {
            this.tableData = res.data;
          } else {
            if(res.errorMsg){
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
          }
          this.loading = false;
        })
      }
    }
  }
</script>
<style lang="scss" scoped type="text/scss">
  .scriptManage{padding:20px 25px 0 25px;box-sizing:border-box;height:100%;@include flexbox-display-flex()}
  .title { height: 32px; line-height: 32px; color: $main-ft-color; font-size: 18px;}
  .description { margin: 10px 0 20px; padding: 20px; color: $main-ft-color; background-color: $description-color; }
  .flex-content{ overflow:auto;@include flexbox-display-flex();@include flexbox-flex();@include beautifyScrollbar()}
  .fit{height:100%}
</style>
