<template>
  <div class="nodesManage">
    <div class="left-part">
      <div class="title">
        <span>ceph节点信息</span>
        <div style="float: right">
          <div style="float:left; margin: -4px 10px 0 0">
            <span class="cluster-name">集群名称</span>
            <el-select v-model="selectClusterName" style="margin: 0 10px; width: 300px" filterable @change="fnSelectChange">
              <el-option v-for="item in clusterNameInfo" :key="item.id" :value="{id:item.id,value:item.name}" :label="item.name"></el-option>
            </el-select>
          </div>
          <el-button type="primary" size="small" icon="el-icon-plus" @click="fnOpenRightMenu" style="float: right">添加节点</el-button>
        </div>
      </div>
      <div class="description">节点是指加入到集群的计算资源，包括虚拟机、物理机等；ceph节点区分为mon和osd，可通过添加节点来实现动态扩容。</div>
      <div class="flex-content">
        <div class="fit">
          <el-table v-loading="loading" element-loading-text="努力获取集群信息" :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%;" height="100%" @expand-change="expandChange" :row-key="getRowKeys" :expand-row-keys="expands">
            <el-table-column type="expand">
              <template slot-scope="scope">
                <el-form label-position="left" inline class="demo-table-expand" label-width="110px">
                  <el-form-item label="创建人"><span>{{ scope.row.creatUser}}</span></el-form-item>
                  <el-form-item label="可用磁盘/总量">{{scope.row.nodeDiskToTalMbIdealState}}/{{scope.row.nodeDiskToTalMb}}Gb </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column prop="nodeName" label="节点名称" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="nodeType" label="节点类型" width="100px" :filters="[{text: 'mon节点', value: '0'}, {text: 'osd节点', value: '1'}]" :filter-method="filterHandler">
              <template slot-scope="scope">{{scope.row.nodeType === '0'? 'mon节点':'osd节点'}}</template>
            </el-table-column>
            <el-table-column  prop="nodeStatus" label="节点状态" min-width="130px" align="center" :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <el-button type="text" size="medium" :class="'color-'+scope.row.nodeRunningStely"><i class="icon-right" :class="'el-icon-'+scope.row.nodeRunningStely"></i>{{scope.row.nodeRunningText}}</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="nodeIP" label="IP" :show-overflow-tooltip="true" align="center"></el-table-column>
            <el-table-column prop="nodePath" label="目录位置" :show-overflow-tooltip="true" align="center"></el-table-column>
            <el-table-column label="操作" width="160px" align="center">
              <template slot-scope="scope">
                <!--<el-button @click="fnShowDialog(scope.row)" type="text" size="small" style="padding-left: 20px;">控制</el-button>-->
                <!--<el-button @click="fnReInitNode(scope.row)" type="text" size="small" :disabled="scope.row.nodeRunningStely == 'loading' || scope.$index===iconLoading">{{scope.row.nodeInitState == 10? '强制初始化':'初始化'}}</el-button>-->
                <el-button v-if="scope.row.nodeRunningState === 1" @click="fnNodeState(scope.row,scope.$index)"  type="text" size="small" :disabled="scope.row.nodeDisplayStateStyle == 'loading' || scope.row.nodeType == '0' || scope.$index===iconLoading">停用<i class="icon-width" :class="{'el-icon-loading': (scope.$index===iconLoading)}"></i></el-button>
                <el-button v-else @click="fnNodeState(scope.row,scope.$index)" type="text" size="small" :disabled="scope.row.nodeRunningStely == 'loading' || scope.$index===iconLoading">启用<i class="icon-width" :class="{'el-icon-loading': (scope.$index===iconLoading)}"></i></el-button>
                <el-button v-if="scope.row.nodeRunningState === 1 && scope.row.nodeType == '0'" @click="fnToMds(scope.row,scope.$index)"  type="text" size="small" :disabled="scope.row.nodeDisplayStateStyle == 'loading' || scope.row.isMds != 0 || scope.$index===iconLoading">mds<i class="icon-width" :class="{'el-icon-loading': (scope.$index===iconLoading)}"></i></el-button>
              </template>
            </el-table-column>
            <el-table-column label="编辑" width="100px" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="editRow(scope.row)" size="small" :disabled="scope.$index===iconLoading || scope.row.nodeRunningStely == 'loading'">修改</el-button>
                <el-button type="text" @click="deleteRow(scope.row,scope.$index)" size="small" :disabled="scope.$index===iconLoading || scope.row.nodeRunningStely == 'loading'">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-card class="box-card" v-show="rightMenu">
      <div slot="header">
        <span class="card-title">{{formEdit ? '修改':'新增'}}节点</span>
        <el-button style="float: right; padding: 3px 0;" type="text" icon="el-icon-close" @click="rightMenu = false; formEdit = false; checkout = false; $refs['formInfo'].resetFields(); formInfo.nodeId = ''; submitLoading = false;">关闭</el-button>
      </div>
      <el-form label-position="right" label-width="84px" :model="formInfo" ref="formInfo" :rules="rules">
        <el-form-item label="集群名称" prop="clusterName">
          <el-input v-model="formInfo.clusterName" disabled class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="节点名称" prop="nodeName">
          <el-input v-model="formInfo.nodeName" :disabled="formEdit" class="full-width" placeholder="英文、数字、减号，如vmnode-197"></el-input>
        </el-form-item>
        <el-form-item label="节点类型" prop="nodeType">
          <el-select v-model="formInfo.nodeType" class="full-width" filterable :disabled="formEdit">
            <el-option v-for="item in NodeOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目录位置" prop="nodePath">
          <el-input v-model="formInfo.nodePath" :disabled="formEdit" class="full-width" placeholder="以根目录'/'开头"></el-input>
        </el-form-item>
        <el-form-item label="主机IP" prop="nodeIP">
          <el-input v-model="formInfo.nodeIP" class="full-width" :disabled="formEdit"></el-input>
        </el-form-item>
        <el-form-item label="SSH端口" prop="nodeSSHPort">
          <el-input v-model="formInfo.nodeSSHPort" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="账号" prop="nodeUser">
          <el-input v-model="formInfo.nodeUser" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pwd">
          <el-input type="password" v-model="formInfo.pwd" id="pwd" readonly auto-complete="off" @focus="removeReadonly()" @blur="setReadonly()"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="nodePassword">
          <el-input type="password" v-model="formInfo.nodePassword" id="nodePassword" readonly auto-complete="off" @focus="removeReadonly()" @blur="setReadonly()"></el-input>
        </el-form-item>
        <el-form-item prop="nodeSystem" class="hidden"></el-form-item>
        <el-form-item prop="nodeCpuInfo" class="hidden"></el-form-item>
        <el-form-item prop="nodeMemoryInfo" class="hidden"></el-form-item>
        <el-form-item prop="nodeDiskInfoTotal" class="hidden"></el-form-item>
        <div v-show="checkout">
          <el-row justify="center" type="flex">
            <el-col :span="8" style="text-align: center">
              <div class="title3">磁盘</div>
              <div class="circle color3">{{formInfo.nodeDiskToTalMb}}Gb</div>
            </el-col>
          </el-row>
        </div>
        <div style="margin: 20px auto 0; text-align: center">
          <el-button size="small" type="primary" @click="fnCheckout('formInfo')" :disabled="checkLoading">测试<span v-if="checkLoading">中 <i class="el-icon-loading"></i></span></el-button>
          <el-button size="small" type="primary" v-if="checkout" :disabled="submitLoading" @click="submitForm('formInfo')">{{formEdit ? '修改':'添加'}}<i v-if="submitLoading" class="el-icon-loading el-icon--right"></i></el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>
<script>
  import {SaveCephNode,getAllCephNode,cephNodeTest,cephClusterGetAll,cephNodeDelete,nodeUpdate,stopOsdNode,nodeToMds,reInitNode,initStep,fromFailed,operateStack} from '../../axios/api'
  import {isIP,isMinus,isNumber} from '../../assets/js/utils'
  var interval,stepInterval,stackInterval;
  var validNodeIP = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入主机IP'))
    }else  if (!isIP(value)){
      callback(new Error('您输入的IP地址格式不正确'))
    }else {
      callback()
    }
  };
  var validNodeName = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入节点名称'))
    }else if(!isMinus(value)) {
      callback(new Error('只允许英文、减号、数字，如vmnode-197'))
    } else {
      callback();
    }
  };
  export default {
    name: 'cephCluster',
    data() {
      var validatePass = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入密码'));
        } else {
          if (this.formInfo.nodePassword !== '') {
            this.$refs.formInfo.validateField('nodePassword');
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
      var validSSH = (rule, value, callback) => {
        if (!value){
          callback(new Error('请输入主机SSH连接端口'))
        }else if(!isNumber(value)) {
          callback(new Error('只允许输入数字'))
        }else if(value < 0 || value > 65535){
          callback(new Error('端口范围0-65535'))
        }else {
          callback()
        }
      };
      var validNodePath = (rule, value, callback) => {
        if (!value){
          callback(new Error('请输入地址'))
        }else if(value.substring(0,1) != '/') {
          callback(new Error('路径需要以/开始'))
        }else if(value.substring(value.length-1) == '/'){
          callback(new Error('路径书写格式不规范'))
        }else {
          callback()
        }
      };
      return {
        rightMenu:false,
        dialogFormVisible:false,
        dialogConsoleVisible:false,
        checkout:false,
        submitLoading:false,
        loading:false,
        iconLoading:'',
        checkLoading: false,
        formEdit:false,
        selectClusterName: {},
        clusterNameInfo:[],
        curClusterId:'',
        formInfo:{
          nodeId:'',
          nodeName:'',
          nodeType:'',
          nodeIP:'',
          nodePath:'',
          nodeSSHPort:'',
          nodeUser:'',
          nodePassword:'',
          nodeDiskToTalMb:'',
          nodeVarDisInfoAlwas:'',
          clusterId:'',
          clusterName:''
        },
        NodeOptions: [{ value: '0', label: 'Mon节点' }, { value: '1', label: 'OSD节点' }],
        rules: {
          pwd: [ {required: true, validator: validatePass, trigger: 'blur' } ],
          nodePassword: [ {required: true, validator: validatePass2, trigger: 'blur' } ],
          nodeIP: [ {required: true, validator: validNodeIP, trigger: 'blur' } ],
          nodeName: [ {required: true, validator: validNodeName, trigger: 'blur' } ],
          nodeUser: [ {required: true, message:'请输入账号', trigger: 'blur' } ],
          nodeSSHPort: [ {required: true, validator: validSSH, trigger: 'blur' } ],
          nodePath: [ {required: true, validator: validNodePath, trigger: 'blur' } ],
        },
        tableData: [],
        expands: [],
        expandsStack: [],
        expandsTemp: [],
        expandsTempStack: [],
        baseStepList:[],
        stepList:[],
        stepTemp:{
          '-1':'error',
          '0':'wait',//disabled
          '1':'finish',//loading
          '2':'success',
        },
        detailTemp: '',
        detailClickTemp: '',
        reStep: false,
        reStepLoading: false,
        consoleInput: '',
        websocket: null,
        showRight: false,
        forceDelete: false,
        forceLoading: false,
        forceInfo: {
          forceInput:'',
          nodeId:'',
          clusterId:''
        },
        isScroll: false,
        stackVisible: false,
        stackLoading: false,
        stackData: [],
        start: 1,
        limit: 5,
        total: 0,
        operateStackNodeId:''
      }
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == 'cephCluster') {
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
      this.fnClearStep();
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
            "nodeSSHPort": 22
          });
        }
        this.rightMenu = true;
        this.formEdit = false;
        this.checkout = false;
        this.checkLoading = false;
      },
      /*表格节点类型筛选*/
      filterHandler(value, row, column) {
        const property = column['property'];
        return row[property] === value;
      },
      /*测试*/
      fnCheckout(formName){
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.checkLoading = true;
            cephNodeTest(this.formInfo).then((res)=>{
              if(res.success) {
                this.formInfo.nodeSystem = res.data.nodeSystem;
                this.formInfo.nodeCpuInfo = res.data.nodeCpuInfo;
                this.formInfo.nodeMemoryInfo = res.data.nodeMemoryInfo;
                this.formInfo.nodeDiskToTalMb = res.data.nodeDiskToTalMb;
                this.formInfo.nodeVarDisInfoUsable = res.data.nodeVarDisInfoUsable;
                this.formInfo.nodeVarDisInfoAlwas = res.data.nodeVarDisInfoAlwas;
                this.checkout = true;
              }else{
                this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
              }
              this.checkLoading = false;
            })
          }
        });
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
        getAllCephNode({clusterId:id}).then((res)=>{
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
              nodeUpdate(this.formInfo).then((res) => {
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
                this.checkout = false;
                this.submitLoading = false;
              })
            }else {
              SaveCephNode(this.formInfo).then((res) => {
                if(res.success){
                  this.fnIntervalTable(this.formInfo.clusterId);
                  this.rightMenu = false;
                  this.$message({ showClose: true, message: '正在初始化节点请稍后', type: 'success' });
                  this.$refs['formInfo'].resetFields();
                }else {
                  this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                  this.loading = false;
                }
                this.checkout = false;
                this.submitLoading = false;
              })
            }
          }
        });
      },
      /*修改节点信息*/
      editRow(row){
        this.formInfo =  Object.assign({}, this.formInfo, {
          nodeId:row.nodeId,
          clusterId:row.clusterId,
          clusterName:row.clusterName,
          nodeName:row.nodeName,
          nodeType:row.nodeType,
          nodeAsChild:row.nodeAsChild,
          nodeIP:row.nodeIP,
          nodeSSHPort:row.nodeSSHPort,
          nodeUser:row.nodeUser,
          pwd:row.nodePassword,
          nodePassword:row.nodePassword,
          nodeSystem:row.nodeSystem,
          nodeCpuInfo:row.nodeCpuInfo,
          nodeMemoryInfo:row.nodeMemoryInfo,
          nodeDiskInfoTotal:row.nodeDiskInfoTotal
        });
        this.formEdit = true;
        this.rightMenu = true;
      },
      /*删除节点信息*/
      deleteRow(rows,index){
        this.$confirm('此操作将永久删除该节点, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.iconLoading = index;
          cephNodeDelete(rows).then((res)=>{
            if(res.success){
              this.fnIntervalTable(rows.clusterId);
              this.$set(this.tableData,index,res.data);
              this.$message({ showClose: true, message: '正在删除节点，请等待', type: 'success' });
            }else {
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
            this.iconLoading = '';
          });
        }).catch(()=>{});
      },
      /*节点初始化*/
      fnReInitNode(row){
        if(row.nodeInitState == 10){
          this.forceDelete = true;
          this.forceInfo.nodeId = row.nodeId;
          this.forceInfo.clusterId = row.clusterId;
        }else {
          reInitNode({"nodeId":row.nodeId}).then((res)=>{
            if(res.success){
              this.fnIntervalTable(row.clusterId);
            }
          });
        }
      },
      /*强制初始化确定*/
      fnForceInfo(formName) {
        this.$refs[formName].validate((valid) => {
          if(valid){
            this.forceLoading = true;
            reInitNode({"nodeId":this.forceInfo.nodeId}).then((res)=>{
              this.forceDelete = false;
              this.forceLoading = false;
              if(res.success){
                this.fnIntervalTable(this.forceInfo.clusterId);
              }else {
                if(res.errorMsg){
                  this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                }
              }
            });
          }
        })
      },
      /*状态修改*/
      fnNodeState(row,index){
        this.iconLoading = index;
        stopOsdNode({nodeId:row.nodeId}).then((res)=>{
          if(res.success){
            this.$message({ showClose: true, message: res.errorMsg, type: 'success' });
            this.fnIntervalTable(row.clusterId);
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
          this.iconLoading = '';
        });
      },
      fnToMds(row,index){
        this.iconLoading = index;
        nodeToMds({nodeId:row.nodeId}).then((res)=>{
          if(res.success){
            this.$message({ showClose: true, message: res.errorMsg, type: 'success' });
            this.fnIntervalTable(row.clusterId);
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
          this.iconLoading = '';
        });
      },
      /*创建详情显示*/
      fnGetNodeState(row){
        this.dialogFormVisible = true;
        this.curNodeId = row.nodeId;
        this.fnIntervalStep(row.nodeId,true);
      },
      /*进度周期查询*/
      fnIntervalStep(nodeId,first){
        this.fnInitStep(nodeId,first);
        this.fnClearStep();
        stepInterval = setInterval(()=>{
          if(!this.reStep){
            this.fnInitStep(nodeId);
          }
        }, 5000);
      },
      /*创建详情方法*/
      fnInitStep(nodeId,first){
        initStep({nodeId:nodeId}).then(res=>{
          if(res.success){
            this.baseStepList = res.data.baseStepList;
            this.stepList = res.data.stepList;
            this.reStep = false;
            if(this.stepList[this.stepList.length - 1].stepState === 2){
              this.fnClearStep();
            }else {
              for(let i=0; i<this.stepList.length; i++){
                if(this.stepList[i].stepState === -1){
                  this.reStep = true;
                  this.fnClearStep();
                  break;
                }
              }
            }
            this.scrollToInit(first);

            if(first){
              this.isScroll = false;
            }

            var _self = this;
            var scrollFunc = function (e) {
              e = e || window.event;
              _self.isScroll = Boolean(e.wheelDelta || e.detail);
            };
            if (document.getElementById("stepScroll").addEventListener) {
              document.getElementById("stepScroll").addEventListener('DOMMouseScroll', scrollFunc, false);
            }
            document.getElementById("stepScroll").onmousewheel = scrollFunc;
          }
        })
      },
      /*清除循环*/
      fnClearStep(){
        clearInterval(stepInterval);
      },
      /*步骤页关闭*/
      fnClose(){
        this.dialogFormVisible = false;
        this.detailClickTemp = '';
        this.fnClearStep();
      },
      /*显示更多*/
      showDetail(index){
        if(this.detailClickTemp === index){
          this.detailClickTemp = '';
        }else {
          this.detailClickTemp = index;
        }
      },
      /*步骤重试*/
      fnReStep(){
        this.fnClearStep();
        this.reStepLoading = true;
        fromFailed({nodeId:this.curNodeId}).then(res=>{
          if(res.success){
            this.fnIntervalStep(this.curNodeId);
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
          this.reStepLoading = false;
        })
      },
      /*显示当前步骤*/
      scrollToInit(first){
        this.$nextTick(() => {
          var stepScroll = document.getElementById('stepScroll').childNodes[0];
          var stepInit = document.getElementById('stepInit');
          if(!this.isScroll && stepInit){
            stepScroll.scrollTop = stepInit.offsetTop;
            return false;
          }

          if(first){
            stepScroll.scrollTop = 0;
          }
        })
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
      /*操作记录*/
      fnOperateStack(nodeId){
        this.operateStackNodeId = nodeId;
        this.stackVisible = true;
        this.start = 1;
        this.stackLoading = true;
        this.fnQueryStack(nodeId);
      },
      fnQueryStack(nodeId){
        this.fnClearStack();
        this.fnQueryStackInfo(nodeId);
        stackInterval = setInterval(()=>{
          this.fnQueryStackInfo(nodeId);
        }, 5000);
      },
      fnQueryStackInfo(nodeId){
        operateStack({"nodeId":nodeId,"start":this.start,"limit":this.limit}).then((res)=>{
          if(res.success){
            this.stackData = res.data.list;
            this.expandsStack = this.expandsTempStack;
            this.total = res.data.total;
            this.stackLoading = false;
          }
        })
      },
      fnCurrentChange(val) {
        this.start = val;
        this.stackLoading = true;
        this.fnQueryStack(this.operateStackNodeId);
      },
      /*清除stack循环*/
      fnClearStack(){
        clearInterval(stackInterval);
      },
      fnCloseStack(){
        this.fnClearStack();
      },
      expandChangeStack(row, expandedRows){
        this.expandsTempStack = [];
        expandedRows.forEach((item)=>{
          this.expandsTempStack.push(item.operateId);
        });
      },
      getRowKeysStack(row){
        return row.operateId;
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
  .consoleBox {
    .el-dialog__header { padding: 10px 20px; }
    .el-dialog__body { padding: 0; }
    .el-dialog__headerbtn { top: 15px; }
  }
  .stack-box {
    .el-dialog__body { padding: 10px 20px 0; }
    .el-table::before { background-color: #fff; }
    .stack-log { word-break: break-all; white-space: pre-wrap; }
    .el-table--scrollable-x .el-table__body-wrapper { overflow-x: hidden; }
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
  .ops-stack { font-size: 0; letter-spacing: -3px; cursor: pointer; color: $white-color; height: $tag-height; line-height: $tag-height; display: inline-block;
    .ops-stack-triangle {  width:0px; height:0px; display: inline-block; border-top: transparent 4px solid; border-bottom: transparent 4px solid; border-right: #ccc 5px solid; }
    .ops-stack-word { background-color: #ccc; display: inline-block; font-size: 12px; padding: 0 4px;  letter-spacing: 0px; border-radius: 2px; height: $tag-height;  }
  }
  .pagination { padding: 20px 70px 20px 0; text-align: center; }
  .pos-button { position: absolute; right: 20px; bottom: 20px;}
</style>
