<template>
  <div class="configMapManage">
    <div class="left-part">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="line-height: 32px;">
        <el-breadcrumb-item :to="{ path: '/appManage' }" :replace="true">应用管理</el-breadcrumb-item>
        <el-breadcrumb-item>{{this.$route.query.n}}-参数配置</el-breadcrumb-item>
      </el-breadcrumb>
      <div class="flex-content">
        <div class="fit">
          <el-table v-loading="loading" element-loading-text="拼命加载中" :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="100%">
            <el-table-column prop="propertyName" label="参数名称" :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <el-input v-model="scope.row.propertyName"  class="full-width"></el-input>
              </template>
            </el-table-column>
            <el-table-column prop="propertyValue" label="参数值" :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <el-input v-model="scope.row.propertyValue"  class="full-width" :type="scope.row.encrypt=='1'?'password':''"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="130px" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="editRow(scope.row)" size="small">修改</el-button>
                <el-button type="text" @click="deleteRow(scope.row.id)" size="small">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div>
          <el-button type="text" icon="el-icon-circle-plus-outline" size="small" @click="fnAppDownloadConfig()">配置下载</el-button>
        </div>
      </div>
    </div>
    <el-card class="box-card" v-show="rightMenu">
      <div slot="header">
        <span class="card-title">{{formEdit ? '修改':'新增'}}参数</span>
        <el-button style="float: right; padding: 3px 0;" type="text" icon="el-icon-close" @click="rightMenu = false; formEdit = false; $refs['formInfo'].resetFields()">关闭</el-button>
      </div>
      <el-form label-position="right" :model="formInfo" :rules="formInfoRules" ref="formInfo" label-width="120px">
        <el-form-item label="参数名称" prop="propertyName">
          <el-input v-model="formInfo.propertyName" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="参数值" prop="propertyValue">
          <el-input v-model="formInfo.propertyValue" class="full-width" :type="formInfo.encrypt=='1'?'password':''"></el-input>
        </el-form-item>
        <el-form-item label="是否为环境变量" prop="isEnv" >
          <el-select v-model="formInfo.isEnv" class="full-width" :disabled="formEdit" filterable>
            <el-option v-for="item in ynOptions" :key="item.value" :label="item.label" :value="item.value" ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否为隐蔽值" prop="encrypt">
          <el-select v-model="formInfo.encrypt" class="full-width" :disabled="formEdit" filterable >
            <el-option v-for="item in ynOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <div style="margin: 0 auto; text-align: center">
          <el-button type="primary" size="small" @click="fnSave('formInfo')" :disabled="submitLoading">保存<i v-if="submitLoading" class="el-icon-loading"></i></el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>
<script>
  import { getConfigMap,saveConfigMap,editConfigMap,removeConfigMap } from '../../axios/api'
  import {isChinese,isNumber} from '../../assets/js/utils'
  var validpropertyName = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入参数名称'))
    }else if (isChinese(value)){
      callback(new Error('不支持中文'))
    }else {
      callback()
    }
  };
  var validpropertyValue = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入参数值'))
    }else if (isChinese(value)){
      callback(new Error('不支持中文'))
    }else {
      callback()
    }
  };
  export default {
    name: 'configMapManage',
    data() {
      return {
        appId:'',
        rightMenu: false,
        tableData: [],
        formInfo:{
          id:'',
          appId:'',
          propertyName:'',
          propertyValue:'',
          isEnv:'0',
          encrypt:'0'
        },
        formInfoRules:{
          propertyName: [ {required: true, validator: validpropertyName, trigger: 'blur' } ],
          propertyValue: [ {required: true, validator: validpropertyValue, trigger: 'blur' } ],
        },
        ynOptions: [{ value: '0', label: '否' }, { value: '1', label: '是' }],
        formEdit:false,
        loading:false,
        submitLoading:false,
      }
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == 'configMapManage') {
        this.loading = true;
        if(this.$route.query.id != undefined){
          this.appId = this.$route.query.id;
        }
        this.fnGetAll();
      }
    },
    methods: {
        /*下载参数配置*/
        fnAppDownloadConfig(){
            window.location.href="/api/configMap/downloadConfigMap.do?id=" + this.appId;
        },
      fnGetAll(){
        getConfigMap({"appId":this.appId}).then((res)=>{
          if(res.success){
            this.tableData = res.data;
          }else {
            if(res.errorMsg){
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
          }
          this.loading = false;
        });
      },
      fnSave(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.submitLoading = true;
            this.formInfo.appId = this.appId;
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
        editConfigMap(this.formInfo).then((res)=>{
          if(res.success){
            this.$message({ showClose: true, message: '修改配置成功', type: 'success' });
            this.rightMenu = false;
            this.formEdit = false;
            this.$refs['formInfo'].resetFields();
            this.fnGetAll();
          }else {
            this.$message({ showClose: true, message: '修改失败:'+res.errorMsg, type: 'warning' });
            this.loading = false;
          }
          this.submitLoading = false;
        });
      },
      fnAdd(){
        saveConfigMap(this.formInfo).then((res)=>{
          if(res.success){
            this.$message({ showClose: true, message: '新增配置成功', type: 'success' });
            this.rightMenu = false;
            this.$refs['formInfo'].resetFields();
            this.fnGetAll();
          }else {
            this.$message({ showClose: true, message: '新增失败:'+res.errorMsg, type: 'error' });
            this.loading = false;
          }
          this.submitLoading = false;
        });
      },
      editRow(row){
        this.formInfo =  Object.assign({}, this.formInfo, {
          id:row.id,
          appId:row.appId,
          propertyName:row.propertyName,
          propertyValue:row.propertyValue,
          isEnv:row.isEnv,
          encrypt:row.encrypt
        });
     /*   this.rightMenu = true;
        this.formEdit = true;*/
        this.fnEdit();
      },
      deleteRow(id) {
        this.$confirm('此操作将永久删除该环境参数, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.envLoading = true;
          removeConfigMap({'id':id}).then((res)=>{
            if(res.success){
              this.$message({ showClose: true, message: '删除成功', type: 'success' });
              this.fnGetAll();
            }else {
              this.$message({ showClose: true, message: res.errorMsg || '删除失败', type: 'error' });
              this.envLoading = false
            }
          });
        }).catch(()=>{});
      }
    }
  }
</script>
<style lang="scss" scoped type="text/scss">
  .configMapManage,.fit { height: 100%; }
  .title { height: 32px; line-height: 32px; color: $main-ft-color; font-size: 18px;}
  .description { margin: 10px 0 20px; padding: 20px; color: $main-ft-color; background-color: $description-color; }
  .left-part { padding: 20px 25px 0 25px; box-sizing: border-box; height: 100%; @include flexbox-display-flex(); }
  .box-card { @include rightMenu(); }
  .el-form-item { margin-bottom: 16px; }
  .full-width { width: 100%; }
  .flex-content { overflow: auto; @include flexbox-display-flex(); @include flexbox-flex(); @include beautifyScrollbar();}
</style>
