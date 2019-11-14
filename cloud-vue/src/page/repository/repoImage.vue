<template>
  <div class="repoImage">
    <div class="top-info">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="line-height: 32px;">
        <el-breadcrumb-item :to="{ path: '/repoManage' }" :replace="true">镜像仓库</el-breadcrumb-item>
        <el-breadcrumb-item>{{businessAreaName}}</el-breadcrumb-item>
        <el-breadcrumb-item>{{groupName}}</el-breadcrumb-item>
        <el-breadcrumb-item>{{appName}}</el-breadcrumb-item>
      </el-breadcrumb>
      <div class="right-btn">
        <el-dropdown @command="addImage" trigger="click">
          <el-button type="primary" size="small">新增镜像<i class="el-icon-arrow-down el-icon--right"></i></el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="1">镜像制作</el-dropdown-item>
            <el-dropdown-item command="2">镜像上传</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <div class="description">介绍</div>
    <div class="flex-content">
      <div class="fit">
        <el-table :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="100%" :row-class-name="effectiveRow">
          <el-table-column prop="appName" label="应用名称" align="center"></el-table-column>
          <el-table-column prop="identify" label="英文标识" align="center"></el-table-column>
          <el-table-column prop="imagePath" label="镜像路径" align="center" :show-overflow-tooltip="true" min-width="150px"></el-table-column>
          <el-table-column prop="version" label="版本" align="center"></el-table-column>
          <el-table-column prop="effective" label="有效标识" align="center">
            <template slot-scope="scope">
              <span :class="scope.row.effective == '0' ? 'color-error':'color-success'">{{scope.row.effective == '0'?'无效':'有效'}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" align="center" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button type="text" :disabled="indexform===scope.$index" :class="{'invalidA':scope.row.effective == '0'}" @click="downloadImage(scope.row,scope.$index)" size="small">
                下载
              </el-button>
              <el-button type="text" :class="{'invalidA':scope.row.effective == '0'}" @click="deleteRow(scope.row)" size="small">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div class="pagination">
      <el-pagination @current-change="fnCurrentChange" background :current-page.sync="currentPage" :page-size="pageSize" layout="prev, pager, next, jumper, ->, total" :total="total"></el-pagination>
    </div>
    <el-card class="box-card" v-show="rightMenu">
      <div slot="header">
        <span class="card-title">{{formType == '1' ? '镜像制作' : '镜像上传'}}</span>
        <el-button style="float: right; padding: 3px 0;" type="text" icon="el-icon-close" @click="closeMenu">关闭</el-button>
      </div>
      <el-form label-position="right" v-loading="formLoading" :model="formInfo" :rules="formInfoRules" ref="formInfo" label-width="100px">
        <div v-if="formType == '1'">
          <el-form-item label="应用组" prop="groupName">
            <el-input v-model="formInfo.groupName" class="full-width" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="应用名称" prop="appName">
            <el-input v-model="formInfo.appName" class="full-width" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="英文标识" prop="identify">
            <el-input v-model="formInfo.identify" class="full-width" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="上传应用程序" class="is-required" label-width="114px">
            <el-upload class="uploadLeft-box" ref="upload1" :action="uploadUrl1" :auto-upload="false" :on-success="uploadSuccess1" :before-upload="uploadBefore1" :on-remove="uploadRemove1" :on-change="uploadChange1" :limit="1" :on-exceed="exceed">
              <el-button slot="trigger" size="small" type="primary" :disabled="submitLoading">选取文件</el-button>
            </el-upload>
          </el-form-item>
          <el-alert title="注：应用程序只能是 'war' 或 'jar' 文件" type="info" show-icon :closable="false" style="margin: -20px 0 16px 0"></el-alert>
          <el-form-item label="操作系统" prop="diyOperateSystem">
            <el-select v-model="formInfo.diyOperateSystem" class="full-width" filterable>
              <el-option v-for="item in osList" :key="item.id" :label="item.name"  :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="JDK版本" prop="diyJdkVersion">
            <el-select v-model="formInfo.diyJdkVersion" class="full-width" filterable>
              <el-option v-for="item in jdkList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="应用服务器" prop="diyWebContainer" v-if="showServer">
            <el-select v-model="formInfo.diyWebContainer" class="full-width" filterable>
              <el-option v-for="item in webContainerList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div v-else>
          <el-form-item label="上传镜像" class="is-required">
            <el-upload class="uploadLeft-box" ref="upload2" :action="uploadUrl2" :auto-upload="false" :before-upload="uploadBefore2" :on-change="uploadChange2" :on-remove="uploadRemove2" :limit="1" :on-exceed="exceed" multiple>
              <el-button slot="trigger" size="small" type="primary" id="btn-upload" :disabled="submitLoading">镜像选择</el-button>
            </el-upload>
          </el-form-item>
          <el-alert title="注：上传镜像只能是 'img' 文件" type="info" show-icon :closable="false" style="margin: -20px 0 16px 0"></el-alert>
        </div>
        <el-form-item label="版本号" prop="version">
          <el-input v-model="formInfo.version" class="full-width" placeholder="请输入如1.0.beta或1.0格式"></el-input>
        </el-form-item>
        <div style="margin: 0 auto; text-align: center">
          <el-button type="primary" size="small" @click="fnSubmit('formInfo')" :disabled="submitLoading">保存<i v-if="submitLoading" class="el-icon-loading el-icon--right"></i></el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>
<script>
  import {
    appImageGetAll,
    appImageRemove,
    appImageUpload,
    createDiyImage,
    downloadExcete,
    saveDiyImage,
    sshDownloadImage,
    checkFile
  } from '../../axios/api'
  import {isVersion} from '../../assets/js/utils'

  var interval;
  var validVersion = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入版本号'))
    }else  if (!isVersion(value)){
      callback(new Error('请输入“1.0.beta或1.0”格式'))
    }else {
      callback()
    }
  };
  export default {
    name: 'repoImage',
    data() {
      return {
        id:'',
        businessAreaName:'',
        groupName:'',
        appName:'',
        indexform:'',
        tableData: [],
        formType:'',
        rightMenu:false,
        formInfo:{
          groupName:'',
          appName:'',
          identify:'',
          diyOperateSystem:'',
          diyJdkVersion:'',
          diyWebContainer:'',
          version:'',
          uploadTmpFileName:'',
          diyType:'',
        },
        formInfoRules:{
          version: [
            { required: true, trigger: 'blur', validator: validVersion },
          ],
          diyJdkVersion: [
            { required: true, message: '请选择JDK版本', trigger: 'change' },
          ],
          diyOperateSystem: [
            { required: true, message: '请选择操作系统', trigger: 'change' },
          ],
          diyWebContainer: [
            { required: true, message: '请选择自定义镜像应用服务器', trigger: 'change' },
          ],
        },
        jdkList:[],
        osList:[],
        webContainerList:[],
        loading:false,
        loadingText:"拼命加载中",
        formLoading:false,
        uploadUrl1:'/api/diyImage/upload.do',
        uploadUrl2:'upload_url',
        uploadForm1: new FormData(),
        uploadForm2: new FormData(),
        submitLoading:false,
        currentPage:1,
        pageSize:5,
        downloadIng:false,
        downloadIng1:false,
        total:0,
        fileType:'',
        showServer:false,
      }
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == 'repoImage'){
        if(this.$route.query.id != undefined){
          this.id = this.$route.query.id;
          this.businessAreaName = this.$route.query.b;
          this.groupName = this.$route.query.g;
          this.appName = this.$route.query.a;
          this.$set(this.formInfo,"repertoryId",this.$route.query.id);
        }
        this.fnIntervalDown();
      }
    },
    destroyed: function () {
      this.fnClearDown();
    },
    methods: {
      /*分页查询*/
      fnCurrentChange(val) {
        this.currentPage = val;
        this.getAll();
      },
      fnIntervalDown(){
        this.getAll();
        this.fnClearDown();
        interval = setInterval(()=>{
          this.getAll();
        }, 5000);
      },
      fnClearDown(){
        clearInterval(interval);
      },
      /*查询所有*/
      getAll(){
        this.loading = true;
        appImageGetAll({'repertoryId':this.id,"currentPage":this.currentPage,"pageSize":this.pageSize}).then((res)=>{
          if(res.success){
            this.tableData = res.data.list
            this.total = res.data.total;
          }else {
            if(res.errorMsg){
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
          }
          this.loading = false;
        });
      },
      /*有效标识颜色区分*/
      effectiveRow({row, rowIndex}){
        if (row.effective == '0') {
          return 'effective-row';
        }
        return '';
      },
      /*打开新增面板*/
      addImage(command) {
        this.rightMenu = true;
        this.formType = command;
        if(command == '1'){
          this.fnGetImageInfo();
        }
      },
      /*获取表单信息*/
      fnGetImageInfo(){
        this.formLoading = true;
        createDiyImage({"id":this.id}).then((res)=>{
          if(res.success){
            this.formInfo = Object.assign({},this.formInfo,{
              "groupName": res.data.repository.groupName,
              "appName": res.data.repository.appName,
              "identify": res.data.repository.identify,
            });
            this.osList = res.data.osList;
            this.jdkList = res.data.jdkList;
            this.webContainerList = res.data.webContainerList;
          }
          this.formLoading = false;
        })
      },
      /*关闭菜单*/
      closeMenu(){
        this.rightMenu = false;
        this.submitLoading = false;
        this.$refs['formInfo'].resetFields();
        if(this.$refs.upload1){
          this.$refs.upload1.abort();
          this.resetInfo1();
        }else if(this.$refs.upload2){
          this.$refs.upload2.abort();
          this.resetInfo2();
        }
      },
      /*删除镜像*/
      deleteRow(rows){
        this.$confirm('此操作将永久删除该镜像, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.loading = true;
          appImageRemove({id:rows.id}).then((res)=>{
            if(res.success){
              this.$message({ showClose: true, message: '删除镜像成功', type: 'success' });
              this.currentPage = 1;
              this.getAll();
            }else {
              this.$message({ showClose: true, message: res.errorMsg || '删除镜像失败', type: 'error' });
              this.loading = false;
            }
          })
        }).catch(()=>{});
      },
      //下载镜像
      downloadImage(rows,index){
        let id = rows.id,imagePath = rows.imagePath,version = rows.version,appName = rows.appName;
        this.indexform = index;
        sshDownloadImage({id:id,imagePath:imagePath,version:version,appName:appName}).then((res) => {
          if (res.success) {
            this.$message({ showClose: true, message: '文件准备中...', type: 'success' });
            this.checkFile(rows,res.data);
          } else {
            if (res.errorMsg) {
              this.$message({showClose: true, message: res.errorMsg, type: 'error'});
            }
            this.indexform = '';
          }
        });
      },
      checkFile(rows,fileName){
        checkFile({fileName:fileName}).then((res) => {
          if (res.success) {
            this.downloading(rows,fileName);
          } else {
            if (res.errorMsg) {
              this.$message({showClose: true, message: res.errorMsg, type: 'error'});
            }
            this.indexform = '';
          }
        });
      },
      downloading(rows,fileName){
        window.location.href="/api/appImage/downloadImage.do?appName=" + rows.identify + "&id=" + rows.id + "&version=" + rows.version + "&fileName=" + fileName;
        this.indexform = '';
      },
      /*上传公共部分*/
      exceed(files){
        this.$message({ showClose: true, message: '当前限制一次只能选择 1 个镜像', type: 'warning' });
      },
      /*镜像制作*/
      uploadBefore1(file) {
        this.uploadForm1.append('file', file);
      },
      uploadChange1(file){
        this.fileType = file.name.substr(file.name.lastIndexOf(".")+1);
        if(this.fileType == 'war'){
          this.showServer = true;
        }else if(this.fileType == 'jar'){
          this.showServer = false;
        }else {
          this.resetInfo1();
          this.$message({ showClose: true, message: '应用程序只能是 war或jar 格式，请重新选择', type: 'error' });
        }
      },
      uploadRemove1(){
        this.resetInfo1();
      },
      resetInfo1(){
        this.showServer = false;
        this.fileType = '';
        this.formInfo.uploadTmpFileName = '';
        this.formInfo.diyType = '';
        this.submitLoading = false;
        this.uploadForm1.delete("file");
        this.$refs.upload1.clearFiles();
      },
      uploadSuccess1(res){
        if(res.success){
          this.submitLoading = true;
          this.formInfo.uploadTmpFileName = res.data.uploadTmpFileName;
          this.formInfo.diyType = this.fileType;
          saveDiyImage(this.formInfo).then((res)=> {
            if(res.success){
              this.rightMenu = false;
              this.currentPage = 1;
              this.getAll();
              this.$message({ showClose: true, message: '保存成功', type: 'success' });
            }else {
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
            this.resetInfo1();
            this.submitLoading = false;
          })
        }else {
          this.resetInfo1();
          this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
        }
      },
      /*镜像上传*/
      uploadBefore2(file) {
        this.uploadForm2.append('file', file);
        this.uploadForm2.append('fileType', file.name.substr(file.name.lastIndexOf(".")+1));
      },
      uploadChange2(file){
        if(file.name.substr(file.name.lastIndexOf(".")+1) != 'img'){
          this.$message({ showClose: true, message: '上传镜像只能是 img 格式，请重新选择', type: 'error' });
          this.resetInfo2();
        }
      },
      uploadRemove2(){
        this.resetInfo2();
      },
      resetInfo2(){
        this.uploadForm2.delete("file");
        this.uploadForm2.delete("fileType");
        this.uploadForm2.delete("version");
        this.uploadForm2.delete("repertoryId");
        this.$refs.upload2.clearFiles();
        this.submitLoading = false;
      },

      /*保存*/
      fnSubmit(formName){
        this.$refs[formName].validate((valid) => {
          if(valid) {
            if(this.formType == '1'){
              this.submitLoading = true;
              this.$refs.upload1.submit();

              if(this.uploadForm1.get("file") == null){
                this.$message({ showClose: true, message: '请选择应用程序', type: 'error' });
                this.resetInfo1();
                return false;
              }
            }else {
              this.$refs.upload2.submit();

              if(this.uploadForm2.get("file") == null){
                this.$message({ showClose: true, message: '请选择镜像', type: 'error' });
                this.resetInfo2();
                return false;
              }

              this.uploadForm2.append('version', this.formInfo.version);
              this.uploadForm2.append('repertoryId', this.id);

              this.submitLoading = true;
              appImageUpload(this.uploadForm2).then((res)=>{
                if(res.success){
                  this.rightMenu = false;
                  this.currentPage = 1;
                  this.getAll();
                  this.$message({ showClose: true, message: '上传镜像成功', type: 'success' });
                }else {
                  this.$message({ showClose: true, message: res.errorMsg || '上传失败', type: 'error' });
                }
                this.resetInfo2();
                this.formInfo.version = '';
                this.submitLoading = false;
              }).catch((res)=>{
                this.$message({ showClose: true, message: res.errorMsg || '上传失败', type: 'error' });
                this.resetInfo2();
                this.formInfo.version = '';
                this.submitLoading = false;
              })
            }
          }
        })
      },
    }
  }
</script>
<style lang="scss" type="text/scss">
  .uploadLeft-box {
    .el-upload { float: left; }
    .el-button { margin-top: 4px; float: left; }
    .el-upload-list { max-width: 160px; position: absolute; top:0; left: 90px; }
    .el-upload-list__item:first-child { margin-top: 8px; }
  }
  $effective-color:#fdf5e6;
  .el-table .effective-row {
    background-color: $effective-color!important;
    &.current-row>td,&.current-row:hover>td {
      background-color: $effective-color!important;
    }
    td { background: $effective-color!important; }
  }
</style>
<style lang="scss" scoped type="text/scss">
  .repoImage { padding: 20px 25px 0 25px; box-sizing: border-box; height: 100%; @include flexbox-display-flex(); }
  .fit { height: 100%; }
  .description { margin: 10px 0 20px; padding: 20px; color: $main-ft-color; background-color: $description-color; }
  .top-info { position: relative; height: 32px; }
  .right-btn { position: absolute; top: 0; right: 0; }
  .flex-content { overflow: auto; @include flexbox-display-flex(); @include flexbox-flex(); @include beautifyScrollbar();}
  .pagination { padding: 20px 0; text-align: center; }
  .box-card { @include rightMenu(); }
  .hidden { display: none }
  .el-form-item { margin-bottom: 16px; }
  .full-width { width: 100%; }
  .hidden { display: none }
  .color-success { color: $color-success!important; }
  .color-error { color: $color-error!important; }
  .invalidA{pointer-events:none; color:$color-disabled}
</style>
