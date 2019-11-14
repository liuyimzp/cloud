<template>
  <div class="configMap">
    <el-card class="box-card">
      <div slot="header">参数配置</div>
      <el-table v-loading="envLoading" :data="envData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="300px">
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
      <el-button type="text" icon="el-icon-circle-plus-outline" @click="envMenu = true">添加</el-button>
      <el-button type="text" icon="el-icon-circle-plus-outline" @click="uploadFile">配置上传</el-button>
      <el-button type="text" icon="el-icon-circle-plus-outline" @click="showYaml">查看yaml</el-button>
    </el-card>
    <div style="text-align: center; margin: 20px auto;">
      <el-button size="small" @click="$router.go(-1)" icon="el-icon-arrow-left">上一步</el-button>
      <el-button type="primary" size="small" @click="fnNext()">下一步<i class="el-icon-arrow-right"></i></el-button>
    </div>
    <el-dialog title="上传文件" :visible.sync="uploadFileDialog" @close="uploadLoading = false;fnRemove">
      <el-form ref="formuploadFile"  label-width="110px">
        <el-form-item label="上传文件：" class="is-required" label-width="114px">
          <el-upload class="uploadLeft-box" ref="upload1"  :action="uploadUrl" :auto-upload="false" :limit="1" :on-change="uploadChange" :before-upload="beforeUpload" :on-remove="fnRemove" :on-exceed="fnMax">
            <el-button slot="trigger" size="small" type="primary" >选取文件</el-button>
          </el-upload>
        </el-form-item>
        <el-alert title="注：应用程序只能是 'properties' 文件" type="info" show-icon :closable="false" style="margin: -20px 0 16px 0"></el-alert>
      </el-form>
      <div :class="showFile ? 'showFile':'onShowFile'" >
        <ace-editor id="editor" lang="batchfile" theme="terminal" height="300px"></ace-editor>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="uploadFileDialog = false; $refs['formuploadFile'].resetFields()">取消</el-button>
        <el-button size="small" type="primary" @click="fnUploadFile" :disabled="uploadLoading">上传<i v-if="uploadLoading" class="el-icon-loading el-icon--right"></i></el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="yamlMenu" width="50%" class="env-menu" @close="yamlMenu = false">
      <el-input type="textarea" :rows="8" v-model="yamlText" :disabled="true"></el-input>
    </el-dialog>
    <el-dialog :visible.sync="envMenu" width="50%" class="env-menu" @close="closeEnvMenu">
      <template slot="title">新增参数配置</template>
      <el-form label-position="right" :model="envFormInfo" :rules="formInfoRules" ref="envFormInfo" label-width="120px">
        <el-form-item label="参数名称" prop="propertyName">
          <el-input v-model="envFormInfo.propertyName" class="full-width" placeholder="不支持中文"></el-input>
        </el-form-item>
        <el-form-item label="参数值" prop="propertyValue">
          <el-input v-model="envFormInfo.propertyValue" class="full-width" placeholder="不支持中文"></el-input>
        </el-form-item>
        <el-form-item label="是否为环境变量" prop="isEnv" >
          <el-select v-model="envFormInfo.isEnv" class="full-width"  filterable>
            <el-option v-for="item in ynOptions" :key="item.value" :label="item.label" :value="item.value" ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否为隐蔽值" prop="encrypt">
          <el-select v-model="envFormInfo.encrypt" class="full-width"  filterable >
            <el-option v-for="item in ynOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="envMenu = false">取消</el-button>
        <el-button type="primary" size="small" @click="fnSaveEnv('envFormInfo')" :disabled="submitLoading">保存<i v-if="submitLoading" class="el-icon-loading el-icon--right"></i></el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { getConfigMap,saveConfigMap,editConfigMap,removeConfigMap,showConfigMap,createConfigMap, filePreview,appConfigUpload} from '../../axios/api'
  import {isChinese,isNumber} from '../../assets/js/utils'
  import AceEditor from 'ace-vue2'
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
    name: 'configMap',
    data() {
      return {
        appId:'',
        envLoading:false,
        saveLoading:false,
        editLoading: false,
        uploadLoading:false,
        editMenu:false,
        envData:[],
        envMenu:false,
        yamlMenu:false,
        uploadFileDialog:false,
        showFile:false,
        uploadUrl:"/api/app/filePreview.do",
        formuploadFile:new FormData(),
        yamlText:'',
        envFormInfo:{
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
        submitLoading:false,
      }
    },
    components:{
      AceEditor
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == "configMap") {
        if (this.$route.query.id != undefined) {
          this.appId = this.$route.query.id;
        }
        this.fnGetEnv();
      }
    },
    methods: {
      /*参数配置相关*/
      /*查询所有配置*/
      fnGetEnv(){
        this.envLoading = true;
        getConfigMap({"appId":this.appId}).then((res)=>{
          if(res.success){
            this.envData = res.data;
          }
          this.envLoading = false;
        })
      },
      /*新增*/
      fnSaveEnv(formName) {
        this.$refs[formName].validate((valid) => {
          if(valid){
            this.submitLoading = true;
            this.envFormInfo.appId = this.appId;
            saveConfigMap(this.envFormInfo).then((res)=>{
              if(res.success){
                this.$message({ showClose: true, message: '新增参数配置成功', type: 'success' });
                this.fnGetEnv();
                this.envMenu = false;
              }else {
                this.$message({ showClose: true, message: res.errorMsg || '新增参数配置失败', type: 'error' });
              }
              this.submitLoading = false;
            })
          }
        });
      },
      /*修改*/
      editRow(row){
        this.envFormInfo = Object.assign({}, this.envFormInfo, {
          id:row.id,
          appId:row.appId,
          propertyName:row.propertyName,
          propertyValue:row.propertyValue,
          isEnv:row.isEnv,
          encrypt:row.encrypt
        });
        editConfigMap(this.envFormInfo).then((res)=>{
          if(res.success){
            this.$message({ showClose: true, message: '修改环境参数配置成功', type: 'success' });
            this.fnGetEnv();
            this.envMenu = false;
          }else {
            this.$message({ showClose: true, message: res.errorMsg || '修改环境参数配置失败', type: 'error' });
          }
          this.submitLoading = false;
        })
      },
      /*删除*/
      deleteRow(id){
        removeConfigMap({'id':id}).then((res)=>{
          if(res.success){
            this.$message({ showClose: true, message: '删除应用成功', type: 'success' });
            this.fnGetEnv();
          }else {
            this.$message({ showClose: true, message: res.errorMsg || '删除应用失败', type: 'error' });
            this.envLoading = false
          }
        });
      },
      /*关闭表单*/
      closeEnvMenu(){
        this.envMenu = false;
        this.submitLoading = false;
        if(this.$refs['envFormInfo']){
          this.$refs['envFormInfo'].resetFields();
          this.envFormInfo.id = '';
          this.envFormInfo.propertyName = '';
          this.envFormInfo.propertyValue = '';
        }
      },
      /*展示configMap创建文件*/
      showYaml(){
        showConfigMap({"appId":this.appId}).then((res)=>{
          if(res.success){
            this.yamlMenu = true;
            this.yamlText = res.data;
          }else {
            this.$message({ showClose: true, message: "生成失败:"+res.errorMsg, type: 'error' });
          }
        });
      },
      /*下一步*/
      fnNext(){
        createConfigMap({"appId":this.appId}).then((res)=>{
          if(res.success){
            this.$router.push({name: 'serviceConfig', query:{'id':this.appId}});
          }else {
            this.$message({ showClose: true, message: res.errorMsg || '创建失败', type: 'error' });
          }
        });

      },
        /*上传前执行函数*/
        uploadChange(file){
            let type = file.name.substr(file.name.lastIndexOf(".")+1);
            if(type != 'properties'){
                this.$message({ showClose: true, message: '上传镜像只能是 properties 格式，请重新选择', type: 'error' });
            }else{
                this.formuploadFile.append('file', file.raw);
                filePreview(this.formuploadFile).then((res)=> {
                    if(res.success){
                        let editor = ace.edit("editor");
                        editor.setValue(res.data);
                        editor.moveCursorTo(0, 0);
                        editor.setReadOnly(true);
                        this.showFile = true;
                    }else {
                        this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                    }
                });
            }
        },
        fnRemove(){
            this.showFile = false;
            this.formuploadFile.delete("file");
        },
        beforeUpload(file){
            this.formuploadFile.append('file', file);
        },
        fnMax(files){
            this.$message({ showClose: true, message: '当前限制一次只能选择 1 个文件', type: 'warning' });
        },
        uploadFile(){
            this.formuploadFile.append('id', this.appId);
            this.uploadFileDialog = true;
        },
        fnUploadFile(){
            appConfigUpload(this.formuploadFile).then((res)=> {
                if (res.success){
                    this.uploadFileDialog = false;
                    this.fnGetEnv();
                    this.$message({showClose: true, message:res.errorMsg?res.errorMsg:"上传成功！", type: res.errorMsg?'warning':'success'});
                }else {
                    if (res.errorMsg) {
                        this.$message({showClose: true, message: res.errorMsg, type: 'error'});
                    }
                }
            });
        },
    }
  }
</script>
<style lang="scss" type="text/scss">
  .input-path.success { .el-input__inner { border-color: #67c23a; } }
  .input-path.error { .el-input__inner { border-color: #f56c6c; } }
  .showFile{height: 300px; display: block}
  .onShowFile{display: none}
  .edit-menu {
    .el-input-group__prepend { padding: 0 14px; }
    .el-dialog__body { padding: 0 20px; }
  }
</style>
<style lang="scss" scoped type="text/scss">
  .configMap { height: 100%; }
  .title { height: 32px; line-height: 32px; color: $main-ft-color; font-size: 14px;}
</style>
