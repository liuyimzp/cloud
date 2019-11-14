<template>
  <div class="author">
    <div class="title">
      <span>全局变量管理</span>
      <el-button type="primary" size="small" icon="el-icon-plus" @click="rightMenu = true;edit=false;" style="float: right">创建</el-button>
    </div>
    <div class="description">全局变量的增删查改，避免直接进入数据库进行修改。</div>
    <el-form label-position="right" :model="searchInfo" ref="searchInfo" label-width="104px" class="search-box">
      <el-form-item label="key" prop="imageName">
        <el-input v-model="formInfo.propKey" class="full-width"></el-input>
      </el-form-item>
      <el-form-item label="value" prop="identifyt">
        <el-input v-model="formInfo.propValue" class="full-width"></el-input>
      </el-form-item>
      <el-button type="primary" size="small" style="margin: 4px 0 0 10px;"  @click="fnSearchOn">查询</el-button>
      <el-button type="primary" size="small" style="margin: 4px 0 0 10px;"  @click="fnSearchReset">重置</el-button>
    </el-form>
    <div class="flex-content">
      <el-table v-loading="loading" :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="300px">
        <el-table-column prop="propKey" label="key" align="center"></el-table-column>
        <el-table-column prop="propValue" label="value" align="center"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="deleteRow(scope.row)" size="small">删除</el-button>
            <el-button type="text" @click="fnEdit(scope.row)" size="small">修改</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination @current-change="fnCurrentChange" background :current-page.sync="currentPage" :page-size="pageSize" layout="prev, pager, next, jumper, ->, total" :total="total"></el-pagination>
      </div>
    </div>
    <el-card class="box-card" v-show="rightMenu">
      <div slot="header">
        <span class="card-title">{{edit?'修改变量':'新增变量'}}</span>
        <el-button style="float: right; padding: 3px 0;" type="text" icon="el-icon-close" @click="rightMenu = false; $refs['formInfo'].resetFields(); submitLoading = false;">关闭</el-button>
      </div>
      <el-form label-position="right" :model="formInfo" :rules="formInfoRules" ref="formInfo" label-width="80px">
        <el-form-item label="key" prop="propKey">
          <el-input v-model="formInfo.propKey" class="full-width" :disabled="edit"></el-input>
        </el-form-item>
        <el-form-item label="value" prop="propValue">
          <el-input v-model="formInfo.propValue" class="full-width"></el-input>
        </el-form-item>
        <div style="margin: 0 auto; text-align: center">
          <el-button type="primary" size="small" @click="fnSave('formInfo')" :disabled="submitLoading">保存<i v-if="submitLoading" class="el-icon-loading el-icon--right"></i></el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>
<script>
  import { sysSave,sysRemove,sysEdit,getSysPropertyAll } from '../../axios/api'
  export default {
    name: 'systemproperty',
    data() {
      var validatePass = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入密码'));
        } else {
          if (this.formInfo.password !== '') {
            this.$refs.formInfo.validateField('password');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.formInfo.pwd) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        rightMenu: false,
        edit:false,
        tableData: [],
        loading: false,
        submitLoading: false,
        currentPage: 1,
        pageSize: 5,
        total: 0,
        formInfo:{
          propValue: "",
          propKey: ""
        },
        formInfoRules: {
            propKey: [ {required: true, message:'请输入key', trigger: 'blur' } ],
            propValue: [ {required: true, message:'请输入value', trigger: 'blur' } ],
        },
      }
    },
    mounted(){
      this.fnGetAll();
    },
    methods: {
      fnSearchOn(){
        this.currentPage = 1;
        this.fnGetAll();
      },
      fnSearchReset(){
        this.$refs['formInfo'].resetFields();
        this.currentPage = 1;
        this.fnGetAll();
      },
      fnGetAll(){
        this.loading = true;
        this.tableData = [];
        getSysPropertyAll({"currentPage":this.currentPage,"pageSize":this.pageSize,"propKey":this.formInfo.propKey,"propValue":this.formInfo.propValue}).then((res)=>{
          if(res.success){
            this.tableData = res.data.list;
            this.total = res.data.total;
          }else {
            if(res.errorMsg){
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
          }
          this.loading = false;
        })
      },
      fnSave(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.submitLoading = true;
            if (this.edit){
                sysEdit(this.formInfo).then((res)=>{
                    if(res.success){
                        this.currentPage = 1;
                        this.fnGetAll();
                        this.rightMenu = false;
                        this.$message({ showClose: true, message: '新增成功', type: 'success' });
                        this.$refs['formInfo'].resetFields();
                    }else {
                        this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                    }
                    this.submitLoading = false;
                });
            }else{
                sysSave(this.formInfo).then((res)=>{
                    if(res.success){
                        this.currentPage = 1;
                        this.fnGetAll();
                        this.rightMenu = false;
                        this.$message({ showClose: true, message: '新增成功', type: 'success' });
                        this.$refs['formInfo'].resetFields();
                    }else {
                        this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                    }
                    this.submitLoading = false;
                })
            }
          }
        });
      },
      fnEdit(row) {
        this.edit = true;
        this.formInfo.propKey = row.propKey;
        this.formInfo.propValue = row.propValue;
        this.rightMenu = true;
      },
      /*删除*/
      deleteRow(row){
        this.$confirm('此操作将永久删除该变量, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.loading = true;
            sysRemove({'propKey':row.propKey}).then((res)=>{
            if(res.success){
              this.$message({ showClose: true, message: '删除成功', type: 'success' });
              this.currentPage = 1;
              this.fnGetAll();
            }else {
              this.$message({ showClose: true, message: res.errorMsg || '删除失败', type: 'error' });
              this.loading = false
            }
          });
        }).catch(()=>{});
      },
      /*分页*/
      fnCurrentChange(val) {
        this.currentPage = val;
        this.fnGetAll();
      },
    }
  }
</script>
<style lang="scss" type="text/scss">
  .input-with-select {
    .el-input-group__prepend { background-color: $white-color; }
    .el-select .el-input { width: 130px; }
  }
  .tree-box { height: 220px; overflow-y: auto; @include beautifyScrollbar(); }
  .author-dialog { .el-dialog__body { padding: 10px 20px; } }
</style>
<style lang="scss" scoped type="text/scss">
  .author { padding: 20px 25px 0 25px; box-sizing: border-box; height: 100%; @include flexbox-display-flex(); }
  .title { height: 32px; line-height: 32px; color: $main-ft-color; font-size: 18px;}
  .description { margin: 10px 0 20px; padding: 20px; color: $main-ft-color; background-color: $description-color; }
  .flex-content { overflow: auto; @include flexbox-display-flex(); @include flexbox-flex(); @include beautifyScrollbar();}
  .search-box { .el-form-item { margin-right: 0; margin-bottom: 20px!important; width: 30%; float: left; } }
  .pagination { padding: 20px 0; text-align: center; }
  .box-card { @include rightMenu(); }
  .el-form-item { margin-bottom: 16px; }
  .full-width { width: 100%; }
</style>
