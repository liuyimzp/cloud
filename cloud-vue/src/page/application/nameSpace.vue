<template>
  <div class="envConfig">
    <div class="left-part">
      <div class="title">
        <span>命名空间</span>
        <div style="float: right">
          <div style="float:left; margin: -4px 10px 0 0">
            <span class="cluster-name">集群名称</span>
            <el-select v-model="selectClusterName" style="margin: 0 10px; width: 300px" filterable @change="fnSelectChange">
              <el-option v-for="item in clusterNameInfo" :key="item.id" :value="{id:item.id,value:item.name}" :label="item.name"></el-option>
            </el-select>
          </div>
          <el-button type="primary" size="small" icon="el-icon-plus" @click="fnOpenRightMenu" style="float: right">添加</el-button>
        </div>
      </div>
      <div class="description">命名空间增删改查</div>
      <div class="flex-content">
        <div class="fit">
          <el-table v-loading="loading" element-loading-text="拼命加载中" :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="100%">
            <el-table-column prop="namespaceName" label="集群名称" :show-overflow-tooltip="true" align="center"></el-table-column>
            <el-table-column prop="namespaceIdentify" label="标识" :show-overflow-tooltip="true" align="center"></el-table-column>
            <el-table-column prop="availableCPU" label="可用/最大CPU限制" min-width="130px" align="center" :show-overflow-tooltip="true">
              <template slot-scope="scope">{{ scope.row.availableCPU.toFixed(2) + '/' + scope.row.maxCPULimit.toFixed(2) }}</template>
            </el-table-column>
            <el-table-column prop="availableMemory" label="可用/最大内存限制" min-width="130px" align="center" :show-overflow-tooltip="true">
              <template slot-scope="scope">{{scope.row.availableMemory.toFixed(2) + 'Gb / ' + scope.row.maxMemoryLimit.toFixed(2) + 'Gb' }}</template>
            </el-table-column>
            <el-table-column label="操作" width="130px" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="editRow(scope.row)" :disabled="indexInfo===scope.$index" size="small">修改</el-button>
                <el-button type="text" @click="deleteRow(scope.row.id,scope.$index)" :disabled="indexInfo===scope.$index" size="small">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-card class="box-card" v-show="rightMenu">
      <div slot="header">
        <span class="card-title">{{formEdit ? '修改':'新增'}}命名空间</span>
        <el-button style="float: right; padding: 3px 0;" type="text" icon="el-icon-close" @click="rightMenu = false; formEdit = false; $refs['formInfo'].resetFields()">关闭</el-button>
      </div>
      <el-form label-position="right" :model="formInfo" :rules="formInfoRules" ref="formInfo" label-width="113px">
        <el-form-item label="命名空间名称" prop="namespaceName">
          <el-input v-model="formInfo.namespaceName" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="命名空间标识" prop="namespaceIdentify">
          <el-input v-model="formInfo.namespaceIdentify" class="full-width" :disabled="formEdit"></el-input>
        </el-form-item>
        <el-form-item label="最大内存限制" prop="maxMemoryLimit">
          <el-input v-model="formInfo.maxMemoryLimit" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="最大CPU限制" prop="maxCPULimit">
          <el-input v-model="formInfo.maxCPULimit" class="full-width"></el-input>
        </el-form-item>
        <div style="margin: 0 auto; text-align: center">
          <el-button type="primary" size="small" @click="fnSave('formInfo')" :disabled="submitLoading">保存<i v-if="submitLoading" class="el-icon-loading"></i></el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>
<script>
  import { namespaceGet, namespaceSave, namespaceEdit, namespaceDelete , clusterGetAll} from '../../axios/api'
  import { isNumber,islowerEnglish } from '../../assets/js/utils'
  var validIdentify = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入命名空间标识'))
    }else  if (!islowerEnglish(value)){
      callback(new Error('仅允许输入小写英文'))
    }else {
      callback()
    }
  };
  var validMemory = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入最大内存限制'))
    }else if(!isNumber(value)) {
      callback(new Error('只允许输入数字'))
    }else {
      callback()
    }
  };
  var validCPU = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入最大CPU限制'))
    }else if(!isNumber(value)) {
      callback(new Error('只允许输入数字'))
    }else {
      callback()
    }
  };
  export default {
    name: 'envConfig',
    data() {
      return {
        rightMenu: false,
        indexInfo:'',
        tableData: [],
        formInfo:{
          namespaceIdentify:'',
          namespaceName:'',
          maxMemoryLimit:'',
          maxCPULimit:'',
          id:'',
        },
        formInfoRules:{
          namespaceName: [ {required: true, message:'请输入命名空间名称', trigger: 'blur' } ],
          namespaceIdentify: [ { required: true, trigger: 'blur', validator: validIdentify } ],
          maxMemoryLimit: [ { required: true, trigger: 'blur', validator: validMemory } ],
          maxCPULimit: [ { required: true, trigger: 'blur', validator: validCPU } ],
        },
        formEdit:false,
        loading:false,
        submitLoading:false,
        selectClusterName: {},
        clusterNameInfo:[],
      }
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == 'nameSpace') {
        clusterGetAll().then((res) => {
          if (res.success) {
            this.clusterNameInfo = res.data;
            if(this.$route.params.id != undefined){
              this.selectClusterName = this.$route.params;
              this.fnGetAll(this.$route.params.id);
            }else {
              this.selectClusterName = {id: this.clusterNameInfo[0].id, value: this.clusterNameInfo[0].name};
              this.fnGetAll(this.clusterNameInfo[0].id);
            }
          }
        });
      }
    },
    methods: {
      fnGetAll(clusterId){
        this.loading = true;
        namespaceGet({'clusterId': clusterId }).then((res)=>{
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
      fnSelectChange(data){
        this.formInfo.clusterName = data.value;
        this.formInfo.clusterId = data.id;
        this.fnGetAll(data.id);
      },
      /*打开右侧菜单*/
      fnOpenRightMenu(){
        if(this.selectClusterName.id == undefined){
          this.$message({ showClose: true, message: '请先选择集群名称', type: 'warning' });
        }else {
          this.formInfo = Object.assign({},this.formInfo,{
            "clusterName":this.selectClusterName.value,
            "clusterId": this.selectClusterName.id,
          });
          this.rightMenu = true;
          this.formEdit = false;
        }
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
        namespaceEdit(this.formInfo).then((res)=>{
          if(res.success){
            this.$message({ showClose: true, message: '修改命名空间成功', type: 'success' });
            this.rightMenu = false;
            this.formEdit = false;
            this.$refs['formInfo'].resetFields();
            this.fnGetAll(this.selectClusterName.id);
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            this.loading = false;
          }
          this.submitLoading = false;
        });
      },
      fnAdd(){
        namespaceSave(this.formInfo).then((res)=>{
          if(res.success){
            this.$message({ showClose: true, message: '新增命名空间成功', type: 'success' });
            this.rightMenu = false;
            this.$refs['formInfo'].resetFields();
            this.fnGetAll(this.selectClusterName.id);
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            this.loading = false;
          }
          this.submitLoading = false;
        });
      },
      editRow(row){
        this.formInfo =  Object.assign({}, this.formInfo, {
          id:row.id,
          namespaceIdentify:row.namespaceIdentify,
          namespaceName:row.namespaceName,
          maxMemoryLimit:row.maxMemoryLimit,
          maxCPULimit:row.maxCPULimit,
        });
        this.rightMenu = true;
        this.formEdit = true;
      },
      deleteRow(id,index) {
        this.$confirm('此操作将永久删除该命名空间, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.indexInfo = index;
          namespaceDelete({'id':id}).then((res)=>{
            if(res.success){
              this.fnGetAll(this.selectClusterName.id);
              this.$message({ showClose: true, message: '删除命名空间成功', type: 'success' });
            }else {
              this.$message({ showClose: true, message: res.errorMsg || '删除命名空间失败', type: 'error' });
            }
            this.indexInfo = '';
          });
        }).catch(()=>{});
      }
    }
  }
</script>
<style lang="scss" scoped type="text/scss">
  .envConfig,.fit { height: 100%; }
  .cluster-name { color: $main-ft-color; font-size: 14px; text-align: center;}
  .title { height: 32px; line-height: 32px; color: $main-ft-color; font-size: 18px;}
  .description { margin: 10px 0 20px; padding: 20px; color: $main-ft-color; background-color: $description-color; }
  .left-part { padding: 20px 25px 0 25px; box-sizing: border-box; height: 100%; @include flexbox-display-flex(); }
  .box-card { @include rightMenu(); }
  .el-form-item { margin-bottom: 16px; }
  .full-width { width: 100%; }
  .flex-content { overflow: auto; @include flexbox-display-flex(); @include flexbox-flex(); @include beautifyScrollbar();}
</style>
