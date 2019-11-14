<template>
  <div class="addCluster">
    <div class="left-part">
      <div class="title">
        <span>集群管理</span>
        <div style="float: right">
          <div style="float:left; margin: -4px 10px 0 0">
            <span class="cluster-name">集群名称</span>
            <el-select v-model="selectClusterName" style="margin: 0 10px; width: 300px" filterable @change="fnSelectChange">
              <el-option v-for="item in clusterNameInfo" :key="item.id" :value="{id:item.id,value:item.name}" :label="item.name"></el-option>
            </el-select>
          </div>
          <el-button type="primary" size="small" icon="el-icon-plus" @click="fnOpenRight" style="float: right">新增</el-button>
        </div>
      </div>
      <div class="description">集群由管理节点和若干负载节点构成，所有应用运行需要的资源都需向集群申请，集群资源不足时需要及时添加负载节点</div>
      <div class="flex-content">
        <div class="fit">
          <el-table v-loading="loading" element-loading-text="拼命加载中" :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%" height="100%" @expand-change="expandChange" :row-key="getRowKeys" :expand-row-keys="expands">
            <el-table-column type="expand">
              <template slot-scope="scope">
                <el-form label-position="left" inline class="demo-table-expand">
                  <el-form-item label="集群备注"><span>{{ scope.row.comment }}</span></el-form-item>
                  <el-form-item v-if="scope.row.fsName != null" label="文件系统"><span>{{ scope.row.fsName }}</span></el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="集群名称" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column label="集群状态" align="center">
              <template slot-scope="scope">
                <span class="left-circle" :class="'bg-color'+scope.row.runningState"></span>
                <span :class="'color'+scope.row.runningState">{{scope.row.runningStateDesc}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="identify" label="英文标识" :show-overflow-tooltip="true" align="center" width="100px"></el-table-column>
            <el-table-column prop="poolSize" label="默认副本数" :show-overflow-tooltip="true" align="center" width="100px"></el-table-column>
            <el-table-column prop="minPoolSize" label="最小副本数" :show-overflow-tooltip="true" align="center" width="100px"></el-table-column>
            <el-table-column prop="totalDisk" label="可用/总磁盘" :show-overflow-tooltip="true" align="center" min-width="160px">
              <template slot-scope="scope">{{scope.row.memoryAvaiLabel | twoNumFilter}}/{{scope.row.memoryTotal | twoNumFilter}}Gb</template>
            </el-table-column>
            <el-table-column prop="numOfNodes" label="总节点数" :show-overflow-tooltip="true" align="center" width="120px">
              <template slot-scope="scope">
                {{scope.row.numOfNodes}}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="280px" align="center">
              <template slot-scope="scope">
                <el-button @click="toNodesManage(scope.row)" type="text" size="small">节点管理</el-button>
                <el-button @click="toPoolsManage(scope.row)" type="text" size="small">pool管理</el-button>
                <el-button @click="toCreateFs(scope.row)" type="text":disabled="(scope.row.fsName != null && scope.row.fsName != '') || refreshIndex === scope.$index" size="small">创建fs</el-button>
                <el-button @click="fnRefreshInfo(scope.row,scope.$index)" type="text" size="small" :disabled="refreshIndex === scope.$index">刷新资源信息</el-button>
              </template>
            </el-table-column>
            <el-table-column label="编辑" width="130px" align="center">
              <template slot-scope="scope" label-width="110px">
                <el-button type="text" @click="editRow(scope.row)" size="small" style="padding-left: 20px;" :disabled="scope.row.deleting !== 0 || refreshIndex === scope.$index">修改</el-button>
                <el-button type="text" @click="openDelete(scope.row,scope.$index)" size="small" :disabled="scope.row.deleting !== 0 || refreshIndex === scope.$index">删除<span class="icon-width"><i :class="{'el-icon-loading': (scope.$index===deleteIndex || scope.row.deleting === 1)}"></i></span></el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-card class="box-card" v-show="rightMenu">
      <div slot="header">
        <span class="card-title">{{formEdit ? '修改':'新增'}}集群</span>
        <el-button style="float: right; padding: 3px 0;" type="text" icon="el-icon-close" @click="rightMenu = false; formEdit = false; $refs['formInfo'].resetFields(); submitLoading = false; formInfo.id = ''">关闭</el-button>
      </div>
      <el-form label-position="right" :model="formInfo" :rules="formInfoRules" ref="formInfo" label-width="90px">
        <el-form-item prop="clusterId" class="hidden"></el-form-item>
        <el-form-item label="k8s集群" prop="clusterName">
          <el-input v-model="formInfo.clusterName" disabled class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="ceph集群" prop="name">
          <el-input v-model="formInfo.name" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="英文标识" prop="identify">
          <el-input v-model="formInfo.identify" :disabled="formEdit" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="默认副本" prop="poolSize">
          <el-input-number v-model="formInfo.poolSize" class="full-width" controls-position="right" :min="1" :disabled="formEdit"></el-input-number>
        </el-form-item>
        <el-form-item label="最小副本" prop="minPoolSize">
          <el-input-number v-model="formInfo.minPoolSize" class="full-width" controls-position="right" :min="1" :max="formInfo.poolSize" :disabled="formEdit"></el-input-number>
        </el-form-item>
        <el-form-item label="集群备注" prop="comment">
          <el-input v-model="formInfo.comment" type="textarea" :autosize="{ minRows: 3, maxRows: 5}"></el-input>
        </el-form-item>
        <div style="margin: 0 auto; text-align: center">
          <el-button type="primary" size="small" @click="fnSave('formInfo')" :disabled="submitLoading">保存<i v-if="submitLoading" class="el-icon-loading el-icon--right"></i></el-button>
        </div>
      </el-form>
    </el-card>
    <el-dialog title="删除提示" :visible.sync="dialogDelete" @close="fnDialogDelete">
      <span>此操作将<span style="color:#F56C6C">永久删除</span>该集群，请输入"delete/删除"</span>
      <el-form :model="deleteInfo" :rules="deleteInfoRules" ref="deleteInfo">
        <el-form-item prop="deleteInput">
          <el-input v-model="deleteInfo.deleteInput" class="full-width" style="margin-top: 20px" placeholder="请输入'delete'或者'删除'"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogDelete = false;">取消</el-button>
        <el-button size="small" type="primary" @click="deleteRow('deleteInfo')">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="创建文件系统" :visible.sync="fsCreate" @close="fnDialogClose">
      <el-form :model="formInfo" ref="formInfo" label-width="110px">
        <el-form-item label="文件系统名：" prop="fsNameP">
          <el-input v-model="formInfo.fsName" class="full-width" placeholder="文件系统名只能为英文"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="fsCreate = false;">取消</el-button>
        <el-button size="small" type="primary" @click="createFs('formInfo')">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
  import {cephClusterGetAllDev,cephClusterAdd,cephClusterUpdate,cephClusterCheck,cephClusterDelete,refreshCephInfo,createFsName,clusterGetAll} from '../../axios/api'
  import {isIP, isEnglish } from '../../assets/js/utils'
  var interval;
  var validIdentify = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入英文标识'))
    }else  if (!isEnglish(value)){
      callback(new Error('仅允许输入英文'))
    }else {
      callback()
    }
  };
  var validDelete = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入\'delete\'或者\'删除\''))
    }else  if (!(value == "delete" || value == "删除")){
      callback(new Error('请输入正确信息'))
    }else {
      callback()
    }
  };
  export default {
    name: 'addCephluster',
    data() {
      var validHaVirtualIp = (rule, value, callback) => {
        if (!value){
          callback(new Error('请输入HA虚拟IP'))
        }else  if (!isIP(value)){
          callback(new Error('HA虚拟IP地址不合法'))
        }else{
          callback();
          this.fnCheckIp();
        }
      };
      return {
        rightMenu: false,
        selectClusterName: {},
        dialogDelete: false,
        tableData: [],
        fsCreate:false,
        expands: [],
        expandsTemp: [],
        formInfo:{
          id:'',
          name:'',
          identify:'',
          comment:'',
          poolSize:'4',
          minPoolSize:'3',
          fsName:'',
          clusterId:'',
          clusterName:'',
        },
        formInfoRules:{
          name: [
            { required: true, message: '请输入集群名称', trigger: 'blur' },
            { min: 0, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur' }
          ],
          identify: [
            { required: true, trigger: 'blur', validator: validIdentify },
            { min: 0, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur' }
          ],
          clusterHaVirtualIP: [
            { required: true, trigger: 'blur', validator: validHaVirtualIp }
                    ]
        },
        formEdit:false,
        loading:false,
        submitLoading:false,
        deleteInfo: {
          deleteInput:''
        },
        deleteInfoRules:{
          deleteInput: [
            { required: true, trigger: 'blur', validator: validDelete },
          ]
        },
        deleteId:'',
        rowIndex:'',
        deleteIndex:'',
        refreshIndex: '',
        ip_exits:false,
      }
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == 'addCephCluster') {
        clusterGetAll().then((res) => {
          if (res.success) {
          this.clusterNameInfo = res.data;
          if(this.$route.params.id != undefined){
            this.selectClusterName = this.$route.params;
            this.fnIntervalTable(this.$route.params.id);
          }else {
            this.selectClusterName = {id: this.clusterNameInfo[0].id, value: this.clusterNameInfo[0].name};
            this.fnIntervalTable(this.clusterNameInfo[0].id);
          }
          this.formInfo.clusterId = this.selectClusterName.id;
          this.formInfo.clusterName = this.selectClusterName.value;
        }
      });
        // this.loading = true;
        // this.fnIntervalTable();
      }
    },
    destroyed: function () {
      this.fnClearTable();
    },
    methods: {
      fnIntervalTable(clusterId){
        this.fnGetAll(clusterId);
        this.fnClearTable();
        interval = setInterval(()=>{
          this.fnGetAll(clusterId);
        }, 10000);
      },
      fnClearTable(){
        clearInterval(interval);
      },
      /*打开右侧弹窗*/
      fnOpenRight(){
        if(this.selectClusterName.id == undefined){
          this.$message({ showClose: true, message: '请先选择集群名称', type: 'warning' });
        }else {
          this.formInfo = Object.assign({}, this.formInfo, {
            "clusterName": this.selectClusterName.value,
            "clusterId": this.selectClusterName.id,
          });
        }
        this.rightMenu = true;
        this.formEdit = false;
      },
      /*下拉框选择*/
      fnSelectChange(data){
        this.formInfo.clusterName = data.value;
        this.formInfo.clusterId = data.id;
        this.fnIntervalTable(data.id);
      },
      fnGetAll(clusterId){
        cephClusterGetAllDev({clusterId:clusterId}).then((res)=>{
          if(res.success){
            this.tableData = res.data;
            this.expands = this.expandsTemp;
          }else {
            if(res.errorMsg){
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
          }
          this.loading = false;
        });
      },
      /*获取表格展开项*/
      expandChange(row, expandedRows){
        this.expandsTemp = [];
        expandedRows.forEach((item)=>{
          this.expandsTemp.push(item.id);
        });
      },
      getRowKeys(row){
        return row.id;
      },
      fnAdd(){
        cephClusterCheck(this.formInfo).then((res)=>{
          if(!res.success){
            this.submitLoading = false;
            this.$message({ showClose: true, message: res.errorMsg, type: 'warning' });
          }else {
            this.loading = true;
            cephClusterAdd(this.formInfo).then((res)=>{
              if(res.success){
                this.$message({ showClose: true, message: '新增集群成功', type: 'success' });
                this.rightMenu = false;
                this.fnIntervalTable(this.formInfo.clusterId);
                this.$refs['formInfo'].resetFields();
              }else {
                this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                this.loading = false;
              }
              this.submitLoading = false;
            });
          }
        })
      },
      fnUpdate(){
        this.loading = true;
        cephClusterUpdate(this.formInfo).then((res)=>{
          if(res.success){
            this.$message({ showClose: true, message: '修改集群成功', type: 'success' });
            this.rightMenu = false;
            this.formEdit = false;
            this.fnIntervalTable(this.formInfo.clusterId);
            this.$refs['formInfo'].resetFields();
            this.formInfo.id = '';
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'warning' });
            this.loading = false;
          }
          this.submitLoading = false;
        });
      },
      fnSave(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.submitLoading = true;
            if(this.formEdit){
              if(this.formInfo.identify == this.tempRow.identify){
                this.fnUpdate();
              }else {
                cephClusterCheck(this.formInfo).then((res)=>{
                  if(!res.success){
                    this.submitLoading = false;
                    this.$message({ showClose: true, message: res.errorMsg, type: 'warning' });
                  }else {
                    this.fnUpdate();
                  }
                })
              }
            }else {
              this.fnAdd();
            }
          }
        });
      },
      toNodesManage(row) { this.$router.push({name: 'cephCluster',params:{"id": row.id, "value": row.name}}); },
      toPoolsManage(row) { this.$router.push({name: 'cephPool',params:{"id": row.id, "value": row.name}}); },
      toCreateFs(row){
        this.fsCreate = true;
        this.formInfo.id = row.id;
      },
      fnRefreshInfo(row,index){
        this.refreshIndex = index;
        refreshCephInfo(row).then((res)=>{
          if(res.success){
            this.fnIntervalTable(this.formInfo.clusterId);
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
          this.refreshIndex = '';
        });
      },
      editRow(row){
        this.tempRow = row;
        this.formInfo =  Object.assign({}, this.formInfo, {
          id:row.id,
          name:row.name,
          identify:row.identify,
          clusterHaVirtualIP:row.clusterHaVirtualIP,
          comment:row.comment,
          poolSize:row.poolSize,
          minPoolSize:row.minPoolSize
        });
        this.rightMenu = true;
        this.formEdit = true;
      },
      openDelete(row,index){
        this.dialogDelete = true;
        this.deleteId = row.id;
        this.rowIndex = index;
        if(this.$refs['deleteInfo']){
          this.$refs['deleteInfo'].resetFields();
        }
      },
      createFs(formName){
        this.$refs[formName].validate((valid) => {
          if (valid){
            if (!isEnglish(this.formInfo.fsName)){
              this.$message({ showClose: true, message: '文件系统名只能为英文', type: 'error' });
            }else {
              createFsName(this.formInfo).then((res)=>{
                if (res.success){
                  this.$message({ showClose: true, message: res.errorMsg, type: 'success' });
                  this.fnDialogClose();
                } else {
                  this.$message({ showClose: true, message: res.errorMsg || '创建文件系统失败', type: 'error' });
                  this.fnDialogClose();
                }
              });
            }
          }
        });
      },
      deleteRow(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.deleteIndex = this.rowIndex;
            this.refreshIndex = this.rowIndex;
            cephClusterDelete({'id':this.deleteId}).then((res)=>{
              if(res.success){
                this.fnIntervalTable(this.formInfo.clusterId);
                this.$set(this.tableData,this.deleteIndex,res.data);
                this.$message({ showClose: true, message: '正在删除集群，请等待', type: 'success' });
                this.dialogDelete = false;
              }else {
                this.$message({ showClose: true, message: res.errorMsg || '删除集群失败', type: 'error' });
                dialogDelete = false;
              }
              this.refreshIndex = '';
            });
          }
        })
      },
      fnDialogDelete(){
        this.deleteId = '';
        this.deleteIndex = '';
      },
      fnDialogClose(){
        this.fsCreate = false;
        this.formInfo.clusterId = '';
        this.formInfo.fsName = '';
      },
    }
  }
</script>
<style lang="scss" type="text/scss">
  .el-textarea__inner { @include beautifyScrollbar($scrollbar-bgcolor: $textArea-scrollbar-bgcolor)}
  .addCluster {
    .el-table__expanded-cell { padding: 12px 20px 12px 60px!important; }
    .el-dialog__body { padding: 30px 20px 0!important; }
    .demo-table-expand { font-size: 0; }
    .demo-table-expand label { width: 70px; color: #99a9bf; }
    .demo-table-expand .el-form-item { margin-right: 0; margin-bottom: 0!important; width: 50%;}
  }
</style>
<style lang="scss" scoped type="text/scss">
  .addCluster,.fit { height: 100%; }
  .title { height: 32px; line-height: 32px; color: $main-ft-color; font-size: 18px;}
  .description { margin: 10px 0 20px; padding: 20px; color: $main-ft-color; background-color: $description-color; }
  .left-part { padding: 20px 25px 0 25px; box-sizing: border-box; height: 100%; @include flexbox-display-flex(); }
  .box-card { @include rightMenu(); }
  .el-form-item { margin-bottom: 16px; }
  .full-width { width: 100%; }
  .flex-content { overflow: auto; @include flexbox-display-flex(); @include flexbox-flex(); @include beautifyScrollbar();}
  .icon-width { width: 20px; display: inline-block; }
  .left-circle { width: 12px; height: 12px; border-radius: 100%; display: inline-block; margin-right: 3px; }
  .bg-color1 { background-color: $color-disabled; }
  .bg-color2 { background-color: $color-success; }
  .bg-color3 { background-color: $color-error; }
  .cluster-name { color: $main-ft-color; font-size: 14px; text-align: center;}
  .color1 { color: $color-disabled; }
  .color2 { color: $color-success; }
  .color3 { color: $color-error; }
  .hidden { display: none; }
</style>
