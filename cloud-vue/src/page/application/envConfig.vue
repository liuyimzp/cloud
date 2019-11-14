<template>
  <div class="envConfig">
    <div class="left-part">
      <div class="title">
        <span>环境参数配置</span>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="rightMenu = true; formEdit = false;" style="float: right">新增</el-button>
      </div>
      <div class="description">环境参数配置-暂未开发</div>
      <div class="flex-content">
        <div class="fit">
          <el-table v-loading="loading" element-loading-text="拼命加载中" :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="100%">
            <el-table-column prop="name" label="名称" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="xxx" label="xxx" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column label="操作" width="130px" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="editRow(scope.row)" size="small">修改</el-button>
                <el-button type="text" @click="deleteRow(scope.row.id)" size="small">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-card class="box-card" v-show="rightMenu">
      <div slot="header">
        <span class="card-title">{{formEdit ? '修改':'新增'}}环境参数</span>
        <el-button style="float: right; padding: 3px 0;" type="text" icon="el-icon-close" @click="rightMenu = false; formEdit = false; $refs['formInfo'].resetFields()">关闭</el-button>
      </div>
      <el-form label-position="right" :model="formInfo" :rules="formInfoRules" ref="formInfo" label-width="80px">
        <el-form-item label="集群名称" prop="name">
          <el-input v-model="formInfo.name" class="full-width"></el-input>
        </el-form-item>
        <div style="margin: 0 auto; text-align: center">
          <el-button type="primary" size="small" @click="fnSave('formInfo')" :disabled="submitLoading">保存<i v-if="submitLoading" class="el-icon-loading"></i></el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>
<script>
  import {  } from '../../axios/api'
  export default {
    name: 'envConfig',
    data() {
      return {
        rightMenu: false,
        tableData: [],
        formInfo:{
        },
        formInfoRules:{

        },
        formEdit:false,
        loading:false,
        submitLoading:false,
      }
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == 'envConfig') {
        this.loading = true;
        this.fnGetAll();
      }
    },
    methods: {
      fnGetAll(){
//        Get().then((res)=>{
//          if(res.success){
//            this.tableData = res.data;
//          }else {
//            if(res.errorMsg){
//              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
//            }
//          }
//          this.loading = false;
//        });
      },
      fnSave(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.submitLoading = true;
            if(this.formEdit){
              this.fnEdit();
            }else {
              this.fnAdd();
            }
          }
        });
      },
      fnEdit(){
        this.loading = true;
//        clusterUpdate(this.formInfo).then((res)=>{
//          if(res.success){
//            this.$message({ showClose: true, message: '修改集群成功', type: 'success' });
//            this.rightMenu = false;
//            this.formEdit = false;
//            this.$refs['formInfo'].resetFields();
//            this.fnIntervalTable();
//          }else {
//            this.$message({ showClose: true, message: '英文标识已存在', type: 'warning' });
//            this.loading = false;
//          }
//        });
      },
      fnAdd(){
//        clusterAdd(this.formInfo).then((res)=>{
//          if(res.success){
//            this.$message({ showClose: true, message: '新增集群成功', type: 'success' });
//            this.rightMenu = false;
//            this.$refs['formInfo'].resetFields();
//            this.fnIntervalTable();
//          }else {
//            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
//            this.loading = false;
//          }
//        });
      },
      editRow(row){
        this.formInfo =  Object.assign({}, this.formInfo, {
          name:row.name,
        });
        this.rightMenu = true;
        this.formEdit = true;
      },
      deleteRow(id) {
//        clusterDelete({'id':id}).then((res)=>{
//          if(res.success){
//            this.fnIntervalTable();
//            this.$set(this.tableData,this.deleteIndex,res.data);
//            this.$message({ showClose: true, message: '正在删除节点，请等待', type: 'success' });
//            this.dialogDelete = false;
//          }else {
//            this.$message({ showClose: true, message: res.errorMsg || '删除集群失败', type: 'error' });
//          }
//        });
      }
    }
  }
</script>
<style lang="scss" scoped type="text/scss">
  .envConfig,.fit { height: 100%; }
  .title { height: 32px; line-height: 32px; color: $main-ft-color; font-size: 18px;}
  .description { margin: 10px 0 20px; padding: 20px; color: $main-ft-color; background-color: $description-color; }
  .left-part { padding: 20px 25px 0 25px; box-sizing: border-box; height: 100%; @include flexbox-display-flex(); }
  .box-card { @include rightMenu(); }
  .el-form-item { margin-bottom: 16px; }
  .full-width { width: 100%; }
  .flex-content { overflow: auto; @include flexbox-display-flex(); @include flexbox-flex(); @include beautifyScrollbar();}
</style>
