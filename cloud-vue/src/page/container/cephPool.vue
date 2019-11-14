<template>
  <div class="nodesManage">
    <div class="left-part">
      <div class="title">
        <span>pool信息</span>
        <div style="float: right">
          <div style="float:left; margin: -4px 10px 0 0">
            <span class="cluster-name">集群名称</span>
            <el-select v-model="selectClusterName" style="margin: 0 10px; width: 300px" filterable @change="fnSelectChange">
              <el-option v-for="item in clusterNameInfo" :key="item.id" :value="{id:item.id,value:item.name}" :label="item.name"></el-option>
            </el-select>
          </div>
          <el-button type="primary" size="small" icon="el-icon-plus" @click="fnOpenRightMenu" style="float: right">添加pool</el-button>
        </div>
      </div>
      <div class="description">pool是指ceph集群中的存储池，用于读写。</div>
      <div class="flex-content">
        <div class="fit">
          <el-table v-loading="loading" element-loading-text="努力获取信息" :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%;" height="100%" @expand-change="expandChange" :row-key="getRowKeys" :expand-row-keys="expands">
            <el-table-column type="expand">
              <template slot-scope="scope">
                <el-form label-position="left" inline class="demo-table-expand" label-width="110px">
                  <el-form-item label="创建人"><span>{{ scope.row.creatUser}}</span></el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column prop="poolName" label="pool名称" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="pgNum" label="pgNum" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="createDate" label="创建时间" :show-overflow-tooltip="true" align="center"></el-table-column>
            <el-table-column prop="createUser" label="创建人" :show-overflow-tooltip="true" align="center"></el-table-column>
            <el-table-column  prop="nodeStatus" label="状态" min-width="130px" align="center" :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <el-button type="text" size="medium"><i class="icon-right"></i>{{scope.row.poolIsUse==0?'未使用':scope.row.poolIsUse==1?'被占用':'被挂载'}}</el-button>
              </template>
            </el-table-column>
            <el-table-column label="编辑" width="100px" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="editRow(scope.row)" size="small" :disabled="iconLoading === scope.$index">修改</el-button>
                <el-button type="text" @click="deleteRow(scope.row,scope.$index)" size="small" :disabled="iconLoading === scope.$index || scope.row.poolIsUse!=0">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-card class="box-card" v-show="rightMenu">
      <div slot="header">
        <span class="card-title">{{formEdit ? '修改':'新增'}}pool</span>
        <el-button style="float: right; padding: 3px 0;" type="text" icon="el-icon-close" @click="rightMenu = false; formEdit = false; $refs['formInfo'].resetFields(); formInfo.poolId = ''; submitLoading = false;">关闭</el-button>
      </div>
      <el-form label-position="right" label-width="84px" :model="formInfo" ref="formInfo" :rules="rules">
        <el-form-item label="集群名称" prop="clusterName">
          <el-input v-model="formInfo.clusterName" disabled class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="poolName">
          <el-input v-model="formInfo.poolName" class="full-width" placeholder="英文和数字组合，数字不可在最前面"></el-input>
        </el-form-item>
        <el-form-item label="pg数" prop="pgNum">
          <el-input v-model="formInfo.pgNum" class="full-width" placeholder="填写pg数量，阿拉伯数字"></el-input>
        </el-form-item>
        <div style="margin: 20px auto 0; text-align: center">
          <el-button size="small" type="primary" :disabled="submitLoading" @click="submitForm('formInfo')">{{formEdit ? '修改':'添加'}}<i v-if="submitLoading" class="el-icon-loading el-icon--right"></i></el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>
<script>
  import {SaveCephPool,getAllCephPool,cephClusterGetAll,cephPoolDelete,poolUpdate,stopOsdNode,nodeToMds} from '../../axios/api'
  import {isMinus,isNumber} from '../../assets/js/utils'
  var interval;
  var validpoolName = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入节点名称'))
    }else if(!isMinus(value)) {
      callback(new Error('只允许英文、减号、数字，如vmnode-197'))
    } else {
      callback();
    }
  };
  export default {
    name: 'cephPool',
    data() {
      var validPgNum = (rule, value, callback) => {
        if (!value){
          callback(new Error('请输入pg数量'))
        }else if(!isNumber(value)) {
          callback(new Error('只允许输入数字'))
        }else {
          callback()
        }
      };
      return {
        rightMenu:false,
        dialogConsoleVisible:false,
        submitLoading:false,
        loading:false,
        iconLoading:'',
        formEdit:false,
        selectClusterName: {},
        clusterNameInfo:[],
        formInfo:{
          poolId:'',
          poolName:'',
          pgNum:'',
          clusterId:'',
          clusterName:''
        },
        rules: {
          poolName: [ {required: true, validator: validpoolName, trigger: 'blur' } ],
          pgNum: [ {required: true, validator: validPgNum, trigger: 'blur' } ],
        },
        tableData: [],
        expands: [],
        expandsTemp: [],
        consoleInput: '',
        websocket: null,
        showRight: false,
        start: 1,
        limit: 5,
        total: 0,
      }
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == 'cephPool') {
        cephClusterGetAll().then((res) => {
          if (res.success) {
            this.clusterNameInfo = res.data;
            if(this.$route.params.id != undefined){
              this.formInfo.clusterId = this.$route.params.id;
              this.selectClusterName = this.$route.params;
              this.fnIntervalTable(this.$route.params.id);
            }else {
              this.formInfo.clusterId = this.clusterNameInfo[0].id;
              this.selectClusterName = {id: this.clusterNameInfo[0].id, value: this.clusterNameInfo[0].name};
              this.fnIntervalTable(this.clusterNameInfo[0].id);
            }
            this.formInfo.clusterName = this.selectClusterName.value;
          }
        });
      }
    },
    destroyed: function () {
      this.fnClearTable();
      if(this.websocket) {
        this.websocketclose();
      }
    },
    methods: {
      /*打开右侧菜单*/
      fnOpenRightMenu(){
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
      /*表格节点类型筛选*/
      filterHandler(value, row, column) {
        const property = column['property'];
        return row[property] === value;
      },
      /*表格周期查询*/
      fnIntervalTable(id){
        this.fnQueryAll(id);
        this.fnClearTable();
        interval = setInterval(()=>{
          this.fnQueryAll(id);
        }, 10000);
      },
      /*下拉框选择*/
      fnSelectChange(data){
        this.formInfo.clusterId = data.id;
        this.formInfo.clusterName = data.value;
        this.fnIntervalTable(data.id);
      },
      /*表格查询周期停止*/
      fnClearTable(){
        clearInterval(interval);
      },
      /*表格查询*/
      fnQueryAll(id){
        getAllCephPool({clusterId:id}).then((res)=>{
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
          this.expandsTemp.push(item.nodeId);
        });
      },
      getRowKeys(row){
        return row.nodeId;
      },
      /*保存表单*/
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.loading = true;
            this.submitLoading = true;
            if(this.formEdit){
              poolUpdate(this.formInfo).then((res) => {
                if(res.success){
                  this.fnIntervalTable(this.formInfo.clusterId);
                  this.rightMenu = false;
                  this.formEdit = false;
                  this.$message({ showClose: true, message: '修改成功', type: 'success' });
                  this.$refs['formInfo'].resetFields();
                  this.formInfo.nodeId = '';
                }else {
                  this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                  this.loading = false;
                }
                this.submitLoading = false;
              })
            }else {
              SaveCephPool(this.formInfo).then((res) => {
                if(res.success){
                  this.fnIntervalTable(this.formInfo.clusterId);
                  this.rightMenu = false;
                  this.$refs['formInfo'].resetFields();
                }else {
                  this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                  this.loading = false;
                }
                this.submitLoading = false;
              })
            }
          }
        });
      },
      /*修改节点信息*/
      editRow(row){
        this.formInfo =  Object.assign({}, this.formInfo, {
          poolId:row.poolId,
          clusterId:row.clusterId,
          poolName:row.poolName,
          pgNum:row.pgNum,
        });
        this.formEdit = true;
        this.rightMenu = true;
      },
      /*删除节点信息*/
      deleteRow(rows,index){
        this.$confirm('此操作将永久删除该存储池, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.iconLoading = index;
          cephPoolDelete(rows).then((res)=>{
            if(res.success){
              this.fnIntervalTable(rows.clusterId);
              this.$set(this.tableData,index,res.data);
              this.$message({ showClose: true, message: '删除成功', type: 'success' });
            }else {
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
            this.iconLoading = '';
          });
        }).catch(()=>{});
      },
      /*密码不保存*/
      removeReadonly(el){
        document.getElementById("nodePassword").removeAttribute('readonly');
        document.getElementById("pwd").removeAttribute('readonly');
      },
      setReadonly(el){
        document.getElementById("nodePassword").setAttribute('readonly' ,'readonly');
        document.getElementById("pwd").setAttribute('readonly' ,'readonly');
      },

      /*控制台相关*/

      /*控制台回车*/
      enterEvent(){
        this.websocketsend(this.consoleInput);
        let container = document.createElement("div");
        container.innerHTML = "> "+ this.consoleInput;
        this.$refs.console.appendChild(container);
        this.consoleInput = '';
      },
      /*控制台显示*/
      fnShowDialog(row){
        this.dialogConsoleVisible = true;
        this.initWebSocket(row.nodeId);
        if(this.$refs.console){
          this.$refs.console.innerHTML = "";
        }
        this.consoleInput = '';
      },
      /*控制台关闭*/
      fnCloseDialog(){
        this.websocketclose();
      },
      initWebSocket(nodeId){ //初始化weosocket
        const wsuri = "ws://127.0.0.1:8080/cloud/websocket/socketServer/webShellSocket.do?nodeId="+nodeId;//ws地址
        this.websocket = new WebSocket(wsuri);
        this.websocket.onopen = this.websocketonopen;
        this.websocket.onerror = this.websocketonerror;
        this.websocket.onmessage = this.websocketonmessage;
        this.websocket.onclose = this.websocketclose;
      },
      websocketonopen() {
        console.log("WebSocket连接成功");
      },
      websocketonerror(e) {
        console.log("WebSocket连接发生错误");
      },
      websocketonmessage(e){
        let container = document.createElement("div");
        container.innerHTML = e.data;
        this.$refs.console.appendChild(container);
        this.scrollToBottom();
      },
      websocketsend(agentData){
        this.websocket.send(agentData);
      },
      websocketclose(){
        console.log("WebSocket连接关闭");
        this.websocket.close();
      },
      /*控制台滚动到最下方*/
      scrollToBottom(){
        this.$nextTick(() => {
          var div = document.getElementById('scroll');
          div.scrollTop = div.scrollHeight;
        })
      },
      /*控制台右键菜单*/
      showRightMenu(e) {
        this.showRight = true;
        let scroll = document.getElementById('scroll');
        let rightMenu = document.getElementById('rightMenu');
        let top = scroll.parentNode.parentNode.offsetTop;
        let left = scroll.parentNode.parentNode.offsetLeft;
        let curX, curY, menuWidth = 160, dialogWidth = 1000;
        if(e.clientX - left + menuWidth > dialogWidth){
          curX = dialogWidth - menuWidth;
        }else {
          curX = e.clientX - left;
        }
        curY = e.clientY - top;

        rightMenu.style.left = curX + 'px';
        rightMenu.style.top = curY + 'px';
      },
      /*关闭控制台*/
      fnClearConsole(){
        this.showRight = false;
        this.$refs.console.innerHTML = "";
        document.getElementById('DialogInput').focus();
      },
    }
  }
</script>
<style lang="scss" type="text/scss">
  .el-table-filter__bottom { text-align: center!important; }
  .nodesManage {
    .el-table__expanded-cell { padding: 12px 20px 12px 60px!important; }
    .el-table th { padding: 7px 0 }
    .demo-table-expand { font-size: 0; }
    .demo-table-expand label { width: 70px; color: #99a9bf; }
    .demo-table-expand .el-form-item { margin-right: 0; margin-bottom: 0!important; width: 50%; }
  }
  .step-box{
    width: 80%; margin: 0 auto!important;
    .is-fullscreen,.el-dialog__body { overflow:auto;@include flexbox-display-flex();@include flexbox-flex();@include beautifyScrollbar(); box-sizing: border-box; }
    .el-dialog__body { padding: 10px 10px 10px 50px;}
    .el-step__description { padding-right:0 }
    .el-scrollbar__wrap { overflow-x: hidden; }
    .description-box { padding: 2px 40px 10px 10px; }
    .slot-state { font-size: 16px; padding: 4px 0; }
    .slot-time { font-size: 12px; color: $text-l2-color; padding: 4px 0; }
    .slot-log { position: relative; font-size: 14px; color: $text-l2-color; background-color: $description-color; border-top-left-radius: 4px; border-top-right-radius: 4px; margin: 14px auto 0; word-break: break-all; white-space: pre-wrap; height: 94px; overflow-y: hidden; padding: 14px; box-sizing: border-box;
      .slot-log-content { position: absolute; bottom: 0; }
      &.show-detail { min-height: 94px; height: auto;
        .slot-log-content { position: relative; }
      }
    }
    .log-detail { font-size: 13px; color: $text-l2-color; width: 100%; height: 30px; text-align: center; cursor: pointer; line-height: 30px; box-sizing: border-box; border-bottom-left-radius: 4px; border-bottom-right-radius: 4px; border: solid 1px $description-color;
      -webkit-user-select: none; -moz-user-select: none; -ms-user-select: none; user-select: none;
      &:hover { box-shadow: 0 0 8px 0 rgba(232,237,250,0.6),0 2px 4px 0 rgba(232,237,250,0.5); }
    }
  }
  .right-menu { width: 160px; position: absolute; left: 0px; top: 0px;
    .el-menu-item, .el-submenu__title { height: 40px; line-height: 40px; } }
</style>
<style lang="scss" scoped type="text/scss">
  .nodesManage,.fit { height: 100%; }
  .title { height: 32px; line-height: 32px; color: $main-ft-color; font-size: 18px; }
  .description { margin: 10px 0 20px; padding: 20px; color: $main-ft-color; background-color: $description-color; }
  .left-part { padding: 20px 25px 0 25px; box-sizing: border-box; height: 100%; @include flexbox-display-flex(); }
  .cluster-name { color: $main-ft-color; font-size: 14px; text-align: center;}
  .box-card { @include rightMenu(); }
  .system-title { color: $text-l0-color; font-size: 16px; padding:2px 0 18px; text-align: center; }
  .system-title1 { color: $text-l0-color; font-size: 16px;  padding:15px 0 5px; text-align: center; }
  .flex { @include flexbox-display-flex(); }
  .el-form-item { margin-bottom: 16px; }
  .full-width { width: 100%; }
  .flex-content { overflow: auto; @include flexbox-display-flex(); @include flexbox-flex(); @include beautifyScrollbar();}
  .hidden { display: none; }
  $color1:#1fd99e;
  $color2:#419ff4;
  $color3:#f59f3d;
  $circle-height: 80px;
  .circle { width: $circle-height; height: $circle-height; line-height: $circle-height; border-radius: 100%; margin: 5px auto 0; text-align: center; font-size: 12px; @include transition();}
  .color1 { color: $color1; border: solid 1px $color1; }
  .color2 { color: $color2; border: solid 1px $color2; }
  .color3 { color: $color3; border: solid 1px $color3; }
  .title1 { color: $color1; }
  .title2 { color: $color2; }
  .title3 { color: $color3; }
  .color-info { color: $color-info!important; }
  .color-loading { color: $color-info!important; }
  .color-success { color: $color-success!important; }
  .color-warning { color: $color-warning!important; }
  .color-error { color: $color-error!important; }
  .color-disabled { color: $color-disabled!important; }
  .icon-right { margin-right: 5px; }
  .icon-width { width: 20px; display: inline-block; }
  .icon-left { margin-left: 5px; }
  .console-body { height: 400px; background-color: $console-color; color: $white-color; padding: 10px; box-sizing: border-box; @include beautifyScrollbar(); overflow-y: auto; overflow-x: hidden; position: relative; }
  .console-show { word-break: break-all; white-space: pre-wrap; }
  .console-edit { background-color: $console-color; color: $white-color; padding: 0; width: 100%; height: 100px; outline: none; border: none; font-size: 14px; line-height: 24px; resize: none; font-family: unset; }
  $tag-height: 18px;
</style>
