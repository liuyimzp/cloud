<template>
  <div class="serviceConfig">
    <el-card class="box-card">
      <div slot="header">服务配置</div>
      <el-table v-loading="loading" :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="300px">
        <el-table-column prop="serviceType" label="服务类型" align="center">
          <template slot-scope="scope">
            {{scope.row.serviceType == '1' ? '外部':'内部'}}
          </template>
        </el-table-column>
        <el-table-column prop="innerPort" label="容器内部端口" align="center"></el-table-column>
        <el-table-column prop="mappingPort" label="外部服务端口" align="center"></el-table-column>
        <el-table-column prop="protocolType" label="协议类型" align="center"></el-table-column>
        <el-table-column label="操作" width="120px" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="editRow(scope.row)" size="small">修改</el-button>
            <el-button type="text" @click="deleteRow(scope.row.id)" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button type="text" icon="el-icon-circle-plus-outline" @click="editMenu = true; innerPort = 0">添加内部服务端口</el-button>
      <el-button type="text" icon="el-icon-circle-plus-outline" @click="editMenu = true; innerPort = 1">添加外部服务端口</el-button>
    </el-card>
    <div style="text-align: center; margin: 20px auto;">
      <el-button size="small" @click="$router.go(-1)" icon="el-icon-arrow-left">上一步</el-button>
      <el-button type="primary" size="small" @click="fnNext()">下一步<i class="el-icon-arrow-right"></i></el-button>
    </div>
    <el-dialog :visible.sync="editMenu" @close="closeMenu">
      <template slot="title">{{formEdit?'修改':'添加'}}{{innerPort == 0 ? '内部':'外部'}}服务端口</template>
      <el-form label-position="right" :model="formInfo" :rules="formInfoRules" ref="formInfo" label-width="110px">
        <el-form-item label="容器内部端口" prop="innerPort" >
          <el-input v-model="formInfo.innerPort" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="外部服务端口" prop="mappingPort" v-if="innerPort == 1">
          <el-input v-model="formInfo.mappingPort" class="full-width" placeholder="须在30000-32767范围内"></el-input>
        </el-form-item>
        <el-form-item label="协议类型" prop="protocolType" >
          <el-select v-model="formInfo.protocolType" filterable class="full-width">
            <el-option v-for="item in protocolTypeOption" :key="item.value" :value="item.value" :label="item.label"></el-option>
          </el-select>
        </el-form-item>
        <div style="text-align: center; margin-top: 20px;">
          <el-button type="primary" size="small" @click="fnSubmit('formInfo')" :disabled="saveLoading">{{formEdit?'修改':'添加'}}<i v-if="saveLoading" class="el-icon-loading el-icon--right"></i></el-button>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
  import {appServiceGet,appServiceSave,appServiceEdit,appServiceRemove,createYaml} from '../../axios/api'
  import {isNumber} from '../../assets/js/utils'
  var validInnerPort = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入容器内部端口'))
    }else if(!isNumber(value)) {
      callback(new Error('只允许输入数字'))
    } else {
      callback();
    }
  };
  var validMappingPort = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入外部服务端口'))
    }else if(!isNumber(value)) {
      callback(new Error('只允许输入数字'))
    }else if(!(value > 30000)) {
      callback(new Error('端口必须大于30000'))
    }else if(!(value < 32767)) {
      callback(new Error('端口必须小于32767'))
    }else {
      callback();
    }
  };
  export default {
    name: 'serviceConfig',
    data() {
      return {
        appId:'',
        loading:false,
        tableData:[],
        editMenu:false,
        formEdit:false,
        formInfo:{
          appId:'',
          id:'',
          serviceType:'',
          innerPort:8080,
          mappingPort:'',
          protocolType:'TCP',
        },
        formInfoRules:{
          innerPort: [{ required: true, validator: validInnerPort, trigger: 'blur' }],
          mappingPort: [ { required: true, validator: validMappingPort, trigger: 'blur' } ],
          protocolType: [{ required: true, message: '请输入协议类型', trigger: 'change' }],
        },
        protocolTypeOption: [{ value: 'TCP', label: 'TCP' }, { value: 'UDP', label: 'UDP' }],
        saveLoading:false,
        innerPort: 0,
      }
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == "serviceConfig") {
        if (this.$route.query.id != undefined) {
          this.appId = this.$route.query.id;
        }
        this.fnGetAll();
      }
    },
    methods: {
      fnGetAll() {
        this.loading = true;
        appServiceGet({'appId': this.appId}).then((res) => {
          if (res.success) {
            this.tableData = res.data;
          } else {
            if(res.errorMsg){
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
          }
          this.loading = false;
        })
      },
      closeMenu() {
        this.editMenu = false;
        this.formEdit = false;
        this.saveLoading = false;
        if (this.$refs['formInfo']) {
          this.$refs['formInfo'].resetFields();
          this.formInfo.serviceType = "";
          this.formInfo.mappingPort = "";
          this.formInfo.id = "";
        }
      },
      editRow(row) {
        this.formInfo = Object.assign({}, this.formInfo, {
          id:row.id,
          serviceType:row.serviceType,
          innerPort:row.innerPort,
          mappingPort:row.mappingPort,
          protocolType:row.protocolType,
        });
        this.innerPort = row.serviceType == '1' ? 1 : 0;
        this.formEdit = true;
        this.editMenu = true;
      },
      deleteRow(id){
        this.$confirm('此操作将永久删除该服务端口配置, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.loading = true;
          appServiceRemove({"id":id}).then((res)=>{
            if(res.success){
              this.$message({ showClose: true, message: '删除服务端口成功', type: 'success' });
              this.fnGetAll();
            }else {
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
              this.loading = false;
            }
          });
        }).catch(()=>{});
      },
      fnSubmit(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.formInfo.appId = this.appId;
            this.formInfo.serviceType = this.innerPort;
            this.saveLoading = true;
            if (this.formEdit) {
              appServiceEdit(this.formInfo).then((res) => {
                this.dealResult(res, this.formEdit)
              });
            } else {
              appServiceSave(this.formInfo).then((res) => {
                this.dealResult(res, this.formEdit)
              });
            }
          }
        })
      },
      dealResult(res, formEdit) {
        if (res.success) {
          this.editMenu = false;
          this.fnGetAll();
          this.$message({showClose: true, message: formEdit ? '修改成功' : '保存成功', type: 'success'});
        } else {
          this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
        }
        this.saveLoading = false;
      },
      fnNext() {
        if(this.tableData.length == 0){
          this.$message({ showClose: true, message: '请添加服务端口配置！', type: 'warning' });
        }else {
          createYaml({'id':this.appId}).then((res)=>{
            if(res.success) {
              this.$router.push({name: 'success', query:{'id':this.appId}})
            }else {
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
          });
        }
      }
    }
  }
</script>
<style lang="scss" scoped type="text/scss">
  .serviceConfig,.fit { height: 100%; }
  .full-width { width: 100%; }
  .btn-edit { color: $color-info; cursor: pointer; }
</style>
