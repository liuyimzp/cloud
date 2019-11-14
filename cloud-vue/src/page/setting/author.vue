<template>
  <div class="author">
    <div class="title">
      <span>人员及权限管理</span>
      <el-button type="primary" size="small" icon="el-icon-plus" @click="rightMenu = true;" style="float: right">创建</el-button>
    </div>
    <div class="description">权限管理提供了多用户环境下资源、应用、仓库的操作和访问限制，预防用户越权操作造成应用不可用。</div>
    <el-form label-position="right" label-width="0px">
      <el-row :span="24">
        <el-col :span="8">
          <el-form-item label="" prop="">
            <el-input v-model="searchInfo" class="input-with-select" clearable>
              <el-select v-model="select" slot="prepend" placeholder="请选择">
                <el-option label="用户名" value="1"></el-option>
                <el-option label="登录账号" value="2"></el-option>
              </el-select>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" size="small" style="margin: 6px 0 0 14px;" icon="el-icon-search" @click="fnSearchInfo">搜索</el-button>
        </el-col>
      </el-row>
    </el-form>
    <div class="flex-content">
      <el-table v-loading="loading" :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="300px">
        <el-table-column prop="loginId" label="登录账号" align="center"></el-table-column>
        <el-table-column prop="name" label="用户名" align="center"></el-table-column>
        <el-table-column label="权限管理" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="openManage(scope.row,true)" size="small">人员应用权限</el-button>
            <el-button type="text" @click="openManage(scope.row)" size="small">人员镜像权限</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="deleteRow(scope.row)" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination @current-change="fnCurrentChange" background :current-page.sync="currentPage" :page-size="pageSize" layout="prev, pager, next, jumper, ->, total" :total="total"></el-pagination>
      </div>
    </div>
    <el-dialog :title="authorTitle" :visible.sync="authorVisible" @close="submitLoading = false; addList = []; removeList = []" class="author-dialog">
      <div class="tree-box">
        <el-tree :data="treeData" show-checkbox node-key="resourceId" :props="defaultProps" ref="tree"></el-tree>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="authorVisible = false;">取消</el-button>
        <el-button size="small" type="primary" @click="fnSaveAuthor" :disabled="submitLoading">确定<i v-if="submitLoading" class="el-icon-loading el-icon--right"></i></el-button>
      </div>
    </el-dialog>
    <el-card class="box-card" v-show="rightMenu">
      <div slot="header">
        <span class="card-title">新增人员</span>
        <el-button style="float: right; padding: 3px 0;" type="text" icon="el-icon-close" @click="rightMenu = false; $refs['formInfo'].resetFields(); submitLoading = false;">关闭</el-button>
      </div>
      <el-form label-position="right" :model="formInfo" :rules="formInfoRules" ref="formInfo" label-width="80px">
        <el-form-item label="用户名" prop="name">
          <el-input v-model="formInfo.name" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="登录账号" prop="loginId">
          <el-input v-model="formInfo.loginId" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pwd">
          <el-input type="password" v-model="formInfo.pwd" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="password">
          <el-input type="password" v-model="formInfo.password" class="full-width"></el-input>
        </el-form-item>
        <div style="margin: 0 auto; text-align: center">
          <el-button type="primary" size="small" @click="fnSave('formInfo')" :disabled="submitLoading">保存<i v-if="submitLoading" class="el-icon-loading el-icon--right"></i></el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>
<script>
  import { getUsers,userSave,userRemove,getAllAppAuthoritys,getAllImageAuthoritys,saveAuthoritys } from '../../axios/api'
  export default {
    name: 'author',
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
        tableData: [],
        loading: false,
        authorVisible: false,
        submitLoading: false,
        currentPage: 1,
        pageSize: 5,
        total: 0,
        select: "1",
        searchInfo: "",
        formInfo:{
          name: "",
          loginId: "",
          pwd: "",
          password: ""
        },
        formInfoRules: {
          name: [ {required: true, message:'请输入用户名', trigger: 'blur' } ],
          loginId: [ {required: true, message:'请输入登录账号', trigger: 'blur' } ],
          pwd: [ {required: true, validator: validatePass, trigger: 'blur' } ],
          password: [ {required: true, validator: validatePass2, trigger: 'blur' } ],
        },
        authorTitle:'',
        userId:'',
        treeData: [],
        defaultProps: {
          children: 'children',
          label: 'resourceName'
        },
        checkedId:[],
        addList:[],
        removeList:[],
      }
    },
    mounted(){
      this.fnGetAll();
    },
    methods: {
      fnGetAll(){
        this.loading = true;
        this.tableData = [];
        let param = {};
        param.start = this.currentPage - 1;
        param.limit = this.pageSize;
        if(this.select == '1'){
          param.name = this.searchInfo;
        }else if(this.select == '2'){
          param.loginId = this.searchInfo;
        }
        getUsers(param).then((res)=>{
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
            userSave(this.formInfo).then((res)=>{
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
        });
      },
      openManage(row,type){
        this.authorVisible = true;
        this.userId = row.userId;
        if(type){
          this.authorTitle = row.name + ' --> 应用权限';
          this.getAllApp(row.userId);
        }else {
          this.authorTitle = row.name + ' --> 镜像权限';
          this.getAllImage(row.userId);
        }
      },
      getAllApp(userId){
        getAllAppAuthoritys({'userId':userId}).then((res)=>{
          if(res.success){
            this.treeData = res.data;
            this.setChecked(res.data);
          }
        });
      },
      getAllImage(userId){
        getAllImageAuthoritys({'userId':userId}).then((res)=>{
          if(res.success){
            this.treeData = res.data;
            this.setChecked(res.data);
          }
        });
      },
      setChecked(data){
        this.checkedId = [];
        data.forEach((item)=>{
          if(item.checked){
            this.checkedId.push(item.resourceId);
          }
          if(item.children){
            item.children.forEach((citem)=>{
              if(citem.checked){
                this.checkedId.push(citem.resourceId);
              }
            })
          }
        })
        this.$refs.tree.setCheckedKeys(this.checkedId);
      },
      fnSaveAuthor(){
        this.submitLoading = true;
        let getCheckedNodes = this.$refs.tree.getCheckedNodes();
        let getCheckedKeys = this.$refs.tree.getCheckedKeys();
        this.addList = [];
        this.removeList = [];
        getCheckedNodes.forEach((item)=>{
          if(item.children){
            this.addList.push({
              "resourceId": item.resourceId,
              "resourceType": item.resourceType,
            });
          }else {
            if(!item.checked){
              this.addList.push({
                "resourceId": item.resourceId,
                "resourceType": item.resourceType,
              });
            }
          }
        });

        for(let i=0; i<this.checkedId.length; i++){
          if(getCheckedKeys.length){
            for(let j=0; j<getCheckedKeys.length; j++){
              if(this.checkedId[i]  ==  getCheckedKeys[j]){
                break;
              }
              if(j == getCheckedKeys.length-1){
                this.removeList.push({
                  "resourceId": this.checkedId[i]
                });
              }
            }
          }else {
            this.removeList.push({
              "resourceId": this.checkedId[i]
            });
          }
        }
        saveAuthoritys({userId:this.userId,addList:this.addList,removeList:this.removeList}).then((res)=>{
          if(res.success){
            this.submitLoading = false;
            this.authorVisible = false;
            this.$message({ showClose: true, message: '权限保存成功', type: 'success' });
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
        });
      },
      /*删除*/
      deleteRow(row){
        this.$confirm('此操作将永久删除该人员, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.loading = true;
          userRemove({'userId':row.userId}).then((res)=>{
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
      /*点击搜索*/
      fnSearchInfo(){
        this.currentPage = 1;
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
  .pagination { padding: 20px 0; text-align: center; }
  .box-card { @include rightMenu(); }
  .el-form-item { margin-bottom: 16px; }
  .full-width { width: 100%; }
</style>
