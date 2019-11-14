<template>
  <div class="repoManage">
    <div class="left-part">
      <div class="title">
        <span>镜像仓库管理</span>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="rightMenu = true; formEdit = false;" style="float: right">创建</el-button>
      </div>
      <div class="description">容器镜像服务提供安全可靠的容器镜像管理功能，并集成常用中间件镜像市场（如：Zookeeper、Kafka、Mysql、Oracle等），以支撑客户的容器化应用部署。</div>
      <el-form label-position="right" :model="searchInfo" ref="searchInfo" label-width="104px" class="search-box">
        <!--<el-form-item label="所属业务领域" prop="businessArea">-->
          <!--<el-select v-model="searchInfo.businessArea" class="full-width" filterable @change="fnChangeSearchB" clearable>-->
            <!--<el-option v-for="item in businessOptions" :key="item.codeValue" :label="item.codeDESC" :value="item.codeValue"></el-option>-->
          <!--</el-select>-->
        <!--</el-form-item>-->
        <!--<el-form-item label="应用组" prop="groupId">-->
          <!--<el-select v-model="searchInfo.groupId" class="full-width" filterable @change="fnChangeSearch" clearable :disabled="groupDisable">-->
            <!--<el-option v-for="item in appGroupOptions" :key="item.id" :label="item.groupName" :value="item.id"></el-option>-->
          <!--</el-select>-->
        <!--</el-form-item>-->
        <el-form-item label="应用名称" prop="imageName">
          <el-input v-model="searchInfo.imageName" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="英文标识" prop="identifyt">
          <el-input v-model="searchInfo.identifyt" class="full-width"></el-input>
        </el-form-item>
        <el-button type="primary" size="small" style="margin: 4px 0 0 10px;"  @click="fnSearchOn">查询</el-button>
        <el-button type="primary" size="small" style="margin: 4px 0 0 10px;"  @click="fnSearchReset">重置</el-button>
      </el-form>
      <div class="flex-content">
          <el-table v-loading="loading" element-loading-text="拼命加载中" :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="100%">
            <el-table-column type="expand">
              <template slot-scope="scope">
                <el-form label-position="left" inline class="demo-table-expand">
                  <el-form-item label="应用描述"><span>{{ scope.row.appDesc ? scope.row.appDesc : '暂无描述' }}</span></el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column prop="businessAreaName" label="所属业务领域" align="center" min-width="100px" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column align="center" :show-overflow-tooltip="true" prop="groupName" label="应用组"></el-table-column>
            <el-table-column prop="appName" label="应用名称" align="center" min-width="160px" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="identify" label="英文标识" align="center" min-width="140px" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="appType" label="应用服务类型" align="center" width="110px" :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <span v-for="item in appTypeOptions" v-if="item.codeValue == scope.row.appType">{{item.codeDESC}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="verCnt" label="版本数" align="center" width="80px"></el-table-column>
            <el-table-column prop="imagePath" label="镜像路径" min-width="180px" :show-overflow-tooltip="true" align="center"></el-table-column>
            <el-table-column label="操作" width="176px" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="toRepoImage(scope.row)" size="small">应用镜像管理</el-button>
                <el-button type="text" @click="editRow(scope.row)" size="small">编辑</el-button>
                <el-button type="text" @click="deleteRow(scope.row.id)" size="small">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination @current-change="fnCurrentChange" background :current-page.sync="currentPage" :page-size="pageSize" layout="prev, pager, next, jumper, ->, total" :total="total"></el-pagination>
          </div>
      </div>
    </div>
    <el-card class="box-card" v-show="rightMenu">
      <div slot="header">
        <span class="card-title">{{formEdit ? '修改':'新增'}}应用镜像库</span>
        <el-button style="float: right; padding: 3px 0;" type="text" icon="el-icon-close" @click="rightMenu = false; formEdit = false; $refs['formInfo'].resetFields(); submitLoading = false; formInfo.id = ''">关闭</el-button>
      </div>
      <el-form label-position="right" :model="formInfo" :rules="formInfoRules" ref="formInfo" label-width="115px">
        <el-form-item label="应用地址" prop="imagePath" v-show="formEdit">
          <el-input v-model="formInfo.imagePath" class="full-width" disabled></el-input>
        </el-form-item>
        <el-form-item label="所属业务领域" prop="businessArea">
          <el-select v-model="formInfo.businessArea" class="full-width" filterable @change="fnSelectChange" :disabled="formEdit">
            <el-option v-for="item in businessOptions" :key="item.codeValue" :label="item.codeDESC" :value="item.codeValue"></el-option>
          </el-select>
          <el-button type="text" class="pos-add" icon="el-icon-circle-plus-outline" @click="fnPosAdd('formBusiness')"></el-button>
        </el-form-item>
        <el-form-item label="应用组" prop="groupId">
          <el-select v-model="formInfo.groupId" class="full-width" filterable :disabled="formEdit">
            <el-option v-for="item in appGroupOptions" :key="item.id" :label="item.groupName" :value="item.id"></el-option>
          </el-select>
          <el-button type="text" class="pos-add" icon="el-icon-circle-plus-outline" @click="fnPosAdd('formAppGroup')"></el-button>
        </el-form-item>
        <el-form-item label="应用名称" prop="appName">
          <el-input v-model="formInfo.appName" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="英文标识" prop="identify">
          <el-input v-model="formInfo.identify" class="full-width" placeholder="小写英文、数字、减号" :disabled="formEdit"></el-input>
        </el-form-item>
        <el-form-item label="应用服务类型" prop="appType">
          <el-select v-model="formInfo.appType" class="full-width" filterable>
            <el-option v-for="item in appTypeOptions" :key="item.codeValue" :label="item.codeDESC" :value="item.codeValue"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="应用描述" prop="appDesc">
          <el-input v-model="formInfo.appDesc" type="textarea" :autosize="{ minRows: 3, maxRows: 5}"></el-input>
        </el-form-item>
        <div style="margin: 0 auto; text-align: center">
          <el-button type="primary" size="small" @click="fnSave('formInfo')" :disabled="submitLoading">保存<i v-if="submitLoading" class="el-icon-loading el-icon--right"></i></el-button>
        </div>
      </el-form>
    </el-card>
    <el-dialog title="新增所属业务领域" :visible.sync="businessVisible" @close="addLoading = false">
      <el-form :model="formBusiness" :rules="formBusinessRules" ref="formBusiness" label-width="120px">
        <el-form-item label="所属业务领域" prop="businessArea" style="margin-bottom: 16px">
          <el-input v-model="formBusiness.businessArea" class="full-width" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="businessVisible = false; $refs['formBusiness'].resetFields()">取消</el-button>
        <el-button size="small" type="primary" @click="fnSaveBusiness('formBusiness')" :disabled="addLoading">确定<i v-if="addLoading" class="el-icon-loading el-icon--right"></i></el-button>
      </div>
    </el-dialog>
    <el-dialog title="新增应用组" :visible.sync="dialogFormVisible" @close="addLoading = false">
      <el-form :model="formAppGroup" :rules="formAppGroupRules" ref="formAppGroup" label-width="120px">
        <el-form-item label="所属业务领域" prop="businessArea">
          <el-select v-model="formAppGroup.businessArea" class="full-width" filterable>
            <el-option v-for="item in businessOptions" :key="item.codeValue" :label="item.codeDESC" :value="item.codeValue"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="应用组名称" prop="groupName">
          <el-input v-model="formAppGroup.groupName" class="full-width" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="应用分组标识" prop="groupIdentify" style="margin-bottom: 0">
          <el-input v-model="formAppGroup.groupIdentify" class="full-width" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false; $refs['formAppGroup'].resetFields()">取消</el-button>
        <el-button size="small" type="primary" @click="fnSaveAppGroup('formAppGroup')" :disabled="addLoading">确定<i v-if="addLoading" class="el-icon-loading el-icon--right"></i></el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {appRepertoryGetAll,appRepertorySave,appRepertoryEdit,appRepertoryRemove,appCode,appBusinessCode, appGroupGetAll,appGroupSave,addBusinessArea } from '../../axios/api'
  import { isUnderlines,isLowercaseMinus } from '../../assets/js/utils'
  var validIdentify = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入英文标识'))
    }else  if (!isLowercaseMinus(value)){
      callback(new Error('请输入小写英文、数字、减号(减号不能在首、尾)'))
    }else {
      callback()
    }
  };
  var validGroupIdentify = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入应用分组标识'))
    }else  if (!isUnderlines(value)){
      callback(new Error('仅允许输入小写英文、下划线(下划线不能在首、尾)'))
    }else {
      callback()
    }
  };
  export default {
    name: 'repoManage',
    data() {
      return {
        rightMenu: false,
        formEdit: false,
        loading:false,
        dialogFormVisible: false,
        businessVisible: false,
        groupDisable:true,
        tableData: [],
        formInfo:{
          imagePath:'',
          businessArea:'',
          groupId:'',
          appName:'',
          identify:'',
          appType:'',
          appDesc:'',
          id:'',
        },
        formInfoRules:{
          identify: [
            { required: true, trigger: 'blur', validator: validIdentify },
            { min: 0, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur' }
          ],
          appName: [
            { required: true, message: '请输入应用名称', trigger: 'blur' },
            { min: 0, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur' }
          ],
          businessArea: [
            { required: true, message: '请选择所属业务领域', trigger: 'change' },
            { required: true, message: '请选择所属业务领域', trigger: 'blur' },
          ],
          groupId: [
            { required: true, message: '请选择应用组', trigger: 'change' },
            { required: true, message: '请选择应用组', trigger: 'blur' },
          ],
          appType: [
            { required: true, message: '请选择应用服务类型', trigger: 'change' },
          ],
        },
        appTypeOptions:[],
        businessOptions:[],
        appGroupOptions:[],
        formAppGroup:{groupName:'',groupIdentify:'',businessArea:''},
        formAppGroupRules:{
          groupName: [ { required: true, message: '请输入应用组名称', trigger: 'blur' } ],
          groupIdentify: [ { required: true, trigger: 'blur', validator: validGroupIdentify } ],
          businessArea: [
            { required: true, message: '请选择所属业务领域', trigger: 'change' },
            { required: true, message: '请选择所属业务领域', trigger: 'blur' },
          ],
        },
        formBusiness:{businessArea:''},
        formBusinessRules:{
          businessArea: [
            { required: true, message: '请输入业务领域', trigger: 'blur' },
            { min: 0, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur' } ],
        },
        addLoading:false,
        submitLoading:false,
        currentPage:1,
        pageSize: 5,
        total:0,
        searchInfo:{businessArea:'',groupId:'',identifyt:'',imageName:''},
      }
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == 'repoManage') {
        this.fnGetTypeCode();
        this.fnGetBusinessCode();
        this.fnGetAll();
      }
    },
    methods: {
      /*获取表格信息*/
      fnGetAll(){
        this.loading = true;
        this.tableData = [];
        appRepertoryGetAll({"currentPage":this.currentPage,"pageSize":this.pageSize,"businessArea":this.searchInfo.businessArea,"groupId":this.searchInfo.groupId,"appName":this.searchInfo.imageName,"identifyt":this.searchInfo.identifyt}).then((res)=>{
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
      fnSearchOn(){
        this.currentPage = 1;
        this.fnGetAll();
      },
      /*分页*/
      fnCurrentChange(val) {
        this.currentPage = val;
        this.fnGetAll();
      },
      /*change搜索*/
      fnChangeSearchB(businessArea){
        this.$set(this.searchInfo,'groupId','');
        if(businessArea.length){
          this.fnAppGroupGetAll(businessArea);
        }else {
          this.appGroupOptions = [];
          this.groupDisable = true;
        }
        this.fnSearchInfo();
      },
      fnChangeSearch(groupId){
        this.searchInfo.groupId = groupId;
        this.fnSearchInfo();
      },
      /*点击搜索*/
      fnSearchInfo(){
        this.currentPage = 1;
        this.fnGetAll();
      },
      fnSearchReset(){
        this.$refs['searchInfo'].resetFields();
        this.currentPage = 1;
        this.fnGetAll();
      },
      /*应用服务类型*/
      fnGetTypeCode(){
        appCode({codeType:"APPTYPE",yab003:"9999"}).then((res)=>{
          if(res.success){
            this.appTypeOptions = res.data;
          }
        });
      },
      /*业务邻域*/
      fnGetBusinessCode(){
        appBusinessCode({codeType:"BUSINESSAREA",yab003:"9999"}).then((res)=>{
          if(res.success){
            this.businessOptions = res.data;
          }
        });
      },
      fnSelectChange(businessArea){
        this.$set(this.formInfo,'groupId','');
        this.fnAppGroupGetAll(businessArea);
      },
      /*应用组*/
      fnAppGroupGetAll(businessArea){
        appGroupGetAll({"businessArea":businessArea}).then((res)=>{
          if(res.success){
            this.appGroupOptions = res.data;
            this.groupDisable = false;
          }
        });
      },
      /*保存表单*/
      fnSave(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.submitLoading = true;
            if(this.formEdit){
              appRepertoryEdit(this.formInfo).then((res) => {
                if(res.success){
                  this.currentPage = 1;
                  this.fnGetAll();
                  this.rightMenu = false;
                  this.formEdit = false;
                  this.$message({ showClose: true, message: '修改成功', type: 'success' });
                  this.$refs['formInfo'].resetFields();
                  this.formInfo.id = '';
                }else {
                  this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                }
                this.submitLoading = false;
              })
            }else {
                  appRepertorySave(this.formInfo).then((res) => {
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
      /*打开新增应用组*/
      fnPosAdd(formName){
        if(formName == 'formAppGroup'){
          this.dialogFormVisible = true;
          if(this.$refs['formAppGroup']){
            this.$refs['formAppGroup'].resetFields();
          }
        }else {
          this.businessVisible = true;
          if(this.$refs['formBusiness']){
            this.$refs['formBusiness'].resetFields();
          }
        }

      },
      /*保存应用组*/
      fnSaveAppGroup(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.addLoading = true;
            appGroupSave(this.formAppGroup).then((res) => {
              if (res.success) {
                this.dialogFormVisible = false;
                this.fnGetBusinessCode();
                this.fnAppGroupGetAll(res.data.businessArea);
                this.$set(this.formInfo,"businessArea",res.data.businessArea);
                this.$set(this.formInfo,"groupId",res.data.id);
                this.$message({ showClose: true, message: '新增应用组成功', type: 'success' });
              } else {
                this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
              }
              this.addLoading = false;
            })
          }
        })
      },
      /*保存所属业务领域*/

      fnSaveBusiness(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.addLoading = true;
            addBusinessArea({"codeDESC":this.formBusiness.businessArea}).then((res) => {
              if (res.success) {
                this.businessVisible = false;
                this.fnGetBusinessCode();
                this.fnAppGroupGetAll(res.data.codeValue);
                this.$set(this.formInfo,"businessArea",res.data.codeValue);
                this.$set(this.formInfo,'groupId','');
                this.$message({ showClose: true, message: '新增业务领域成功', type: 'success' });
              } else {
                this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
              }
              this.addLoading = false;
            })
          }
        })
      },
      /*跳转到镜像*/
      toRepoImage(row) {
        this.$router.push({name: 'repoImage', query:{id:row.id,b:row.businessAreaName,g:row.groupName,a:row.appName}});
      },
      /*编辑*/
      editRow(row){
        this.formInfo =  Object.assign({}, this.formInfo, {
          imagePath:row.imagePath,
          businessArea:row.businessArea,
          groupId:row.groupId,
          appName:row.appName,
          identify:row.identify,
          appType:row.appType,
          appDesc:row.appDesc,
          id:row.id
        });
        this.formEdit = true;
        this.rightMenu = true;
      },
      /*删除*/
      deleteRow(id){
        this.$confirm('此操作将永久删除该应用, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.loading = true;
          appRepertoryRemove({"id":id}).then((res)=>{
            if(res.success){
              this.$message({ showClose: true, message: '删除应用成功', type: 'success' });
              this.currentPage = 1;
              this.fnGetAll();
            }else {
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
              this.loading = false;
            }
          });
        }).catch(()=>{});
      },
    }
  }
</script>
<style lang="scss" type="text/scss">
  .el-textarea__inner { @include beautifyScrollbar($scrollbar-bgcolor: $textArea-scrollbar-bgcolor) }
  .repoManage {
    .demo-table-expand { font-size: 0; }
    .demo-table-expand label { width: 70px; color: #99a9bf; }
    .demo-table-expand .el-form-item { margin-right: 0; margin-bottom: 0!important; width: 100% }
    .el-table__expanded-cell { padding: 12px 20px 12px 60px!important; }
  }
  .el-table{
    color:#333 ;
  }
  .search-box { .el-form-item { margin-right: 0; margin-bottom: 20px!important; width: 30%; float: left; } }
</style>
<style lang="scss" scoped type="text/scss">
  .el-table{
    color:#333 ;
  }
  .repoManage,.fit { height: 100%; }
  .pos-add { position: absolute; top: 0; right: -18px; }
  .title { height: 32px; line-height: 32px; color: $main-ft-color; font-size: 18px;}
  .description { margin: 10px 0 20px; padding: 20px; color: $main-ft-color; background-color: $description-color; }
  .left-part { padding: 20px 25px 0 25px; box-sizing: border-box; height: 100%; @include flexbox-display-flex(); }
  .box-card { @include rightMenu(); }
  .el-form-item { margin-bottom: 16px; }
  .full-width { width: 100%; }
  .flex-content { overflow: auto; @include flexbox-display-flex(); @include flexbox-flex(); @include beautifyScrollbar();}
  .pagination { padding: 20px 0; text-align: center; }
</style>
