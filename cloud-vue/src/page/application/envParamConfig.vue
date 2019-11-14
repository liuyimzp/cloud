<template>
  <div class="envParamConfig">
    <el-card class="box-card" style="margin-top: 20px;">
      <div slot="header">存储配置</div>
      <el-table v-loading="saveLoading" :data="existData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="300px">
        <el-table-column prop="volumeDisplayName" label="名称" align="center"></el-table-column>
        <el-table-column prop="volumeTypeDesc" label="类型" align="center"></el-table-column>
        <el-table-column prop="space" label="空间" align="center">
          <template slot-scope="scope">
            {{scope.row.space}}Gb
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路径" align="center"></el-table-column>
        <el-table-column label="操作" min-width="80px" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="deleteSaveRow(scope.row)" size="small" v-if="appStatus === '1'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button type="text" icon="el-icon-circle-plus-outline" @click="fnSaveAdd()" v-if="appStatus === '1'">添加</el-button>
    </el-card>
    <div style="text-align: center; margin: 20px auto;">
      <el-button size="small" @click="$router.go(-1)" icon="el-icon-arrow-left">上一步</el-button>
      <el-button type="primary" size="small" @click="fnNext()">下一步<i class="el-icon-arrow-right"></i></el-button>
    </div>
    <el-dialog :visible.sync="editMenu" width="70%" class="edit-menu" @close="submitLoading = false; editLoading = false">
      <template slot="title" >添加存储</template>
      <el-table v-loading="editLoading" :data="allData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="300px">
        <el-table-column align="center" prop="checked" label="选择" width="55">
          <template slot-scope="scope">
            <el-radio v-model="checked" :label="scope.row.volumeId" @change="checkedIndex = scope.$index; inputPath = 'opt/tomcat/logs'; inputSpace = ''; availableSpace = scope.row.volumeAvailableSpace; inputSpaceClass='';inputPathClass='';">&nbsp;</el-radio>
          </template>
        </el-table-column>
        <el-table-column prop="volumeDisplayName" label="名称" align="center"></el-table-column>
        <el-table-column prop="volumeTypeDesc" label="类型" align="center"></el-table-column>
        <el-table-column prop="volumeAvailableSpace" label="可用空间" align="center">
          <template slot-scope="scope">
            {{scope.row.volumeAvailableSpace}}Gb
          </template>
        </el-table-column>
        <el-table-column prop="path" label="空间" align="center" width="200px">
          <template slot-scope="scope">
            <el-input v-if="scope.$index === checkedIndex" v-model="inputSpace" class="input-path" :class="inputSpaceClass" placeholder="小于可用空间">
              <template slot="append">Gb</template>
            </el-input>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="容器内路径" align="center" width="300px">
          <template slot-scope="scope">
            <el-input v-if="scope.$index === checkedIndex" v-model="inputPath" class="input-path" :class="inputPathClass">
              <template slot="prepend">/</template>
            </el-input>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button size="small" @click="editMenu = false">取消</el-button>
        <el-button size="small" @click="fnSaveCheck()" type="primary" :disabled="submitLoading">确定<i v-if="submitLoading" class="el-icon-loading el-icon--right"></i></el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {getPVs,savePV,removePV,appEnvGet, appEnvSave, appEnvEdit, appEnvRemove, checkPath, getAppConfig} from '../../axios/api'
  import {isChinese,isNumber} from '../../assets/js/utils'
  var validEnvKey = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入参数名称'))
    }else if (isChinese(value)){
      callback(new Error('不支持中文'))
    }else {
      callback()
    }
  };
  var validEnvValue = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入参数值'))
    }else if (isChinese(value)){
      callback(new Error('不支持中文'))
    }else {
      callback()
    }
  };
  export default {
    name: 'envParamConfig',
    data() {
      return {
        appStatus:'',
        appId:'',
        recordLog:'1',
        saveLoading:false,
        editLoading: false,
        editMenu:false,
        allData:[],
        existData:[],
        checked: null,
        inputPath: 'opt/tomcat/logs',
        inputSpace: '',
        checkedIndex: '',
        submitLoading:false,
        formEdit:false,
        inputPathClass: '',
        inputSpaceClass: '',
        availableSpace: '',
      }
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == "envParamConfig") {
        if (this.$route.query.id != undefined) {
          this.appId = this.$route.query.id;
        }
        if(this.$route.query.recordLog != undefined){
          this.recordLog = this.$route.query.recordLog;
        }
        if(this.$route.query.appStatus != undefined){
          this.appStatus = this.$route.query.appStatus;
        }
        this.fnGetPVs();
      }
    },
    methods: {
      /*存储相关*/
      /*查询所有存储*/
      fnGetPVs(){
        this.saveLoading = true;
        getPVs({"id":this.appId}).then((res)=>{
          if(res.success){
            this.allData = res.data.allData;
            this.existData = res.data.existData;
          }
          this.saveLoading = false;
        })
      },
      /*打开存储面板*/
      fnSaveAdd(){
        if(this.recordLog=='0'){
          this.$message({ showClose: true, message: 'deployment无需添加', type: 'warn' });
          return;
        }
        this.editMenu = true;
        this.checkedIndex = '';
        this.inputPath = '';
        this.inputSpace = '';
        this.checked = '';
      },
      /*存储确定*/
      fnSaveCheck(){
        if(this.checked){
          if(this.inputSpace.length) {
            if(isNumber(this.inputSpace)){
              if(Number(this.availableSpace) < Number(this.inputSpace)){
                this.$message({ showClose: true, message: '空间需要比可用空间小', type: 'error' });
                this.inputSpaceClass = "error";
                return false;
              }else {
                this.inputSpaceClass = "success";
              }
            }else {
              this.$message({ showClose: true, message: '请输入数字', type: 'warning' });
              this.inputSpaceClass = "error";
              return false;
            }
          }else {
            this.$message({ showClose: true, message: '请输入空间', type: 'warning' });
            this.inputSpaceClass = "error";
            return false;
          }

          if(this.inputPath.length){
            this.editLoading = true;
            this.submitLoading = true;
            checkPath({"path":this.inputPath,"appId":this.appId}).then((r)=> {
              if(r.success){
                this.inputPathClass = "success";
                savePV({"appId":this.appId,"volumeId":this.checked,"path":"/"+this.inputPath,"space":this.inputSpace}).then((res)=>{
                  if(res.success){
                    this.$message({ showClose: true, message: '添加存储成功', type: 'success' });
                    this.editMenu = false;
                    this.fnGetPVs();
                  }else {
                    this.$message({ showClose: true, message: res.errorMsg || '添加存储失败', type: 'error' });
                  }
                  this.editLoading = false;
                  this.submitLoading = false;
                })
              }else {
                this.inputPathClass = "error";
                this.editLoading = false;
                this.$message({ showClose: true, message: r.errorMsg, type: 'error' });
              }
              this.submitLoading = false;
            })
          }else {
            this.$message({ showClose: true, message: '请输入容器内路径', type: 'warning' });
          }
        }else {
          this.$message({ showClose: true, message: '请选择存储', type: 'warning' });
        }
      },
      /*存储删除*/
      deleteSaveRow(row){
        this.$confirm('此操作将永久删除该应用存储, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.saveLoading = true;
          removePV({"id":row.id}).then((res)=>{
            if(res.success){
              this.$message({ showClose: true, message: '应用存储删除成功', type: 'success' });
              this.fnGetPVs();
            }else {
              this.$message({ showClose: true, message: res.errorMsg || '应用存储删除失败', type: 'error' });
              this.saveLoading = false;
            }
          })
        }).catch(()=>{});
      },
      /*下一步*/
      fnNext(){
        if(this.existData.length == 0 && this.recordLog=='1'){
          this.$message({ showClose: true, message: '请添加存储数据！', type: 'warning' });
          return false;
        }
        this.$router.push({name: 'configMap', query:{'id':this.appId}});
      }
    }
  }
</script>
<style lang="scss" type="text/scss">
  .input-path.success { .el-input__inner { border-color: #67c23a; } }
  .input-path.error { .el-input__inner { border-color: #f56c6c; } }
  .edit-menu {
    .el-input-group__prepend { padding: 0 14px; }
    .el-dialog__body { padding: 0 20px; }
  }
</style>
<style lang="scss" scoped type="text/scss">
  .envParamConfig { height: 100%; }
  .title { height: 32px; line-height: 32px; color: $main-ft-color; font-size: 14px;}
</style>
