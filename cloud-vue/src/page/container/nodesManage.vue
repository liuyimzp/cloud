<template>
  <div class="nodesManage">
    <div class="left-part">
      <div class="title">
        <span>节点管理</span>
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
      <div class="description">节点是指加入到集群的计算资源，包括虚拟机、物理机等；节点区分管理节点和负载节点，可通过添加负载加点来实现动态扩容。</div>
      <div class="flex-content">
        <div class="fit">
          <el-table v-loading="loading" element-loading-text="努力获取节点信息" :data="tableData" :stripe="true" :highlight-current-row="true" style="width: 100%;" height="100%" @expand-change="expandChange" :row-key="getRowKeys" :expand-row-keys="expands">
            <el-table-column type="expand">
              <template slot-scope="scope">
                <el-form label-position="left" inline class="demo-table-expand" label-width="110px">
                  <el-form-item label="可用cpu数">{{scope.row.nodeAllocatableCpu}}个</el-form-item>
                  <el-form-item label="创建人"><span>{{ scope.row.nodeCreateUser }}</span></el-form-item>
                  <el-form-item label="可用内存">{{scope.row.nodeAllocatableMemory | twoNumFilter}}Gb</el-form-item>
                  <el-form-item label="创建时间"><span>{{ scope.row.nodeCreateDateStr }}</span></el-form-item>
                  <el-form-item label="可用磁盘/总量">{{scope.row.nodeDiskUsableMb | twoNumFilter}}/{{scope.row.nodeDiskTotalMb | twoNumFilter}}Gb </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column prop="nodeName" label="节点名称" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="nodeType" label="节点类型" width="100px" :filters="[{text: '管理节点', value: '0'}, {text: '负载节点', value: '1'}]" :filter-method="filterHandler">
              <template slot-scope="scope">{{scope.row.nodeType === '0'? '管理节点':'负载节点'}}</template>
            </el-table-column>
            <el-table-column  prop="nodeStatus" label="节点状态" min-width="130px" align="center" :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <el-button v-if="scope.row.nodeDisplayStateClickType === 1" @click="fnGetNodeState(scope.row)" type="text" size="medium" :class="'color-'+scope.row.nodeDisplayStateStyle"><i class="icon-right" :class="'el-icon-'+scope.row.nodeDisplayStateStyle"></i>{{scope.row.nodeDisplayStateText}}</el-button>
                <span v-else :class="'color-'+scope.row.nodeDisplayStateStyle"><i class="icon-right" :class="'el-icon-'+scope.row.nodeDisplayStateStyle"></i>{{scope.row.nodeDisplayStateText}}</span>
                <span class="ops-stack" title="操作记录" @click="fnOperateStack(scope.row.nodeId)">
                  <i class="ops-stack-triangle"></i>
                  <span class="ops-stack-word">ops</span>
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="nodeIP" label="IP" :show-overflow-tooltip="true" align="center"></el-table-column>
            <el-table-column label="资源管理" width="100px" align="center">
              <template slot-scope="scope">
                <el-button @click="fnGetResouce(scope.row)" type="text" size="small" :disabled="scope.row.nodeAsChild === '0' && scope.row.nodeType == '0'">查看详情</el-button>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200px" align="center">
              <template slot-scope="scope">
                <!--<el-button @click="fnShowDialog(scope.row)" type="text" size="small" style="padding-left: 20px;">控制</el-button>-->
                <el-button @click="fnReInitNode(scope.row,scope.$index)" type="text" size="small" :disabled="scope.row.nodeDisplayStateStyle == 'loading'||iconLoading===scope.$index">{{scope.row.nodeInitState == 10? '强制初始化':'初始化'}}</el-button>
                <el-button @click="fnSetNodeAsChild(scope.row,scope.$index)" type="text" size="small" :disabled="scope.row.nodeDisplayStateStyle == 'loading'||iconLoading===scope.$index" v-if="scope.row.nodeType === '0'">{{scope.row.nodeAsChild === '1'? '不可部署':'可部署'}}</el-button>
                <el-button v-if="scope.row.nodeRunningState === 1" @click="fnNodeState(scope.row,scope.$index)"  type="text" size="small" :disabled="scope.row.nodeDisplayStateStyle == 'loading'||iconLoading===scope.$index">停用<i class="icon-width" :class="{'el-icon-loading': (scope.$index===iconLoading)}"></i></el-button>
                <el-button v-else @click="fnNodeState(scope.row,scope.$index)" type="text" size="small" :disabled="scope.row.nodeDisplayStateStyle == 'loading'||iconLoading===scope.$index">启用<i class="icon-width" :class="{'el-icon-loading': (scope.$index===iconLoading)}"></i></el-button>
              </template>
            </el-table-column>
            <el-table-column label="编辑" width="100px" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="editRow(scope.row)" size="small" :disabled="scope.row.nodeDisplayStateStyle == 'loading'||iconLoading===scope.$index">修改</el-button>
                <el-button type="text" @click="deleteRow(scope.row,scope.$index)" size="small" :disabled="scope.row.nodeDisplayStateStyle == 'loading'||iconLoading===scope.$index">删除</el-button>
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
        <el-form-item prop="clusterId" class="hidden"></el-form-item>
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
        <el-form-item prop="nodeAsChild" v-show="formInfo.nodeType == '0'" style="margin: -16px 0 0px;" label="同时作为子节点" label-width="110px">
          <el-switch v-model="formInfo.nodeAsChild" inactive-value="0" active-value="1" :disabled="formEdit"></el-switch>
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
        <!--<el-form-item prop="nodeCpuInfo" class="hidden"></el-form-item>-->
        <!--<el-form-item prop="nodeMemoryInfo" class="hidden"></el-form-item>-->
        <!--<el-form-item prop="nodeDiskInfoTotal" class="hidden"></el-form-item>-->
        <div v-show="checkout">
          <div class="system-title">系统：<span v-html="formInfo.nodeSystem"></span></div>
          <el-row>
            <el-col :span="8" style="text-align: center">
              <div class="title1">CPU</div>
              <div class="circle color1">{{NodeInfo.nodeCpuInfo}}</div>
            </el-col>
            <el-col :span="8" style="text-align: center">
              <div class="title2">内存</div>
              <div class="circle color2">{{NodeInfo.nodeMemoryInfo}}</div>
            </el-col>
            <el-col :span="8" style="text-align: center">
              <div class="title3">磁盘</div>
              <div class="circle color3">{{NodeInfo.nodeDiskInfoTotal}}</div>
            </el-col>
          </el-row>
          <div class="system-title1">var目录剩余{{formInfo.nodeVarDisInfoUsable}}G/{{formInfo.nodeVarDisInfoAlwas}}G </div>
        </div>
        <el-form-item v-if="checkout" label-width="120px" label="k8s使用cpu" prop="nodeCpuInfo">
          <el-input v-model="formInfo.nodeAllocatableCpu" class="full-width"></el-input>
        </el-form-item>
        <el-form-item v-if="checkout" label-width="120px" label="k8s使用内存" prop="nodeMemoryInfo">
          <el-input v-model="formInfo.nodeAllocatableMemory" class="full-width"></el-input>
        </el-form-item>
        <div class="system-title1"  v-if="checkout">更据系统需求预留资源供系统正常运行 </div>
        <!--<el-form-item v-if="checkout" label="磁盘" prop="nodeDiskInfoTotal">-->
          <!--<el-input v-model="formInfo.nodeDiskInfoTotal" class="full-width"></el-input>-->
        <!--</el-form-item>-->
        <div style="margin: 20px auto 0; text-align: center">
          <el-button size="small" type="primary" @click="fnCheckout('formInfo')" :disabled="checkLoading">测试<span v-if="checkLoading">中 <i class="el-icon-loading"></i></span></el-button>
          <el-button size="small" type="primary" v-if="checkout" :disabled="submitLoading" @click="submitForm('formInfo')">{{formEdit ? '修改':'添加'}}<i v-if="submitLoading" class="el-icon-loading el-icon--right"></i></el-button>
        </div>
      </el-form>
    </el-card>
    <el-dialog :fullscreen="true" title="创建详情" :visible.sync="dialogFormVisible" class="step-box" @close="fnClose()" :close-on-click-modal='false'>
      <el-scrollbar style="height: 100%; overflow-x: hidden; border-right: solid 1px #fff;" id="stepScroll">
        <el-steps direction="vertical" :active="baseStepList.length">
          <el-step v-for="(base,index) in baseStepList" :title="base.stepName" :id="(stepList[index].stepState == 1 || stepList[index].stepState == -1) ? 'stepInit': ''" :key="index" :status="stepTemp[stepList[index].stepState]" >
            <div slot="description" class="description-box" v-if="base.stepOrder == stepList[index].stepOrder">
              <div class="slot-state color-error" v-if="stepList[index].stepState == -1">执行失败</div>
              <div class="slot-state color-disabled" v-else-if="stepList[index].stepState == 0">未执行</div>
              <div class="slot-state color-loading" v-else-if="stepList[index].stepState == 1">正在执行</div>
              <div class="slot-state color-success" v-else="stepList[index].stepState == 2">执行成功</div>
              <div class="slot-time" v-if="stepList[index].stepRunTime">{{stepList[index].stepRunTime}}</div>
              <el-progress v-if="stepList[index].stepState == -1 || stepList[index].stepState == 2" :percentage="100" :status="stepList[index].stepState == 2 ? 'success' : 'exception'" ></el-progress>
              <el-progress v-else-if="stepList[index].stepState == 1" :percentage="stepList[index].stepFinishedPercentage"></el-progress>
              <div class="slot-log" v-if="stepList[index].stepLog" :class="{'show-detail': (detailClickTemp === index)}">
                <div class="slot-log-content" v-html="stepList[index].stepLog"></div>
              </div>
              <div class="log-detail" v-if="stepList[index].stepLog" @mouseenter="detailTemp = index" @mouseleave="detailTemp = ''" @click="showDetail(index)">
                <i class="el-icon-caret-bottom" :class="{'color-info': (detailTemp === index), 'el-icon-caret-top': (detailClickTemp === index)}"></i>
                <span v-show="detailTemp === index" :class="{'color-info': (detailTemp === index)}">{{detailClickTemp === index ? '收起更多':'点击查看更多'}}</span>
              </div>
            </div>
          </el-step>
        </el-steps>
      </el-scrollbar>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="fnReStep()" v-show="reStep" type="primary">重试<i :class="{'el-icon-loading': reStepLoading,'icon-left': reStepLoading}"></i></el-button>
        <el-button size="small" @click="fnClose()">确定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="控制台" width="1000px" :visible.sync="dialogConsoleVisible" class="consoleBox" @close="fnCloseDialog()">
      <div class="console-body" id="scroll" @contextmenu.prevent="showRightMenu($event)" @click="showRight = false">
        <div ref="console" class="console-show"></div>
        <textarea class="console-edit" v-model="consoleInput" id="DialogInput" :autofocus="true" @keyup.enter="enterEvent"></textarea>
      </div>
      <el-menu default-active="1" class="el-menu-vertical-demo right-menu" id="rightMenu" v-show="showRight">
        <el-menu-item index="1" @click="fnClearConsole">
          <i class="el-icon-delete"></i>
          <span slot="title">清空控制台</span>
        </el-menu-item>
      </el-menu>
    </el-dialog>
    <el-dialog title="资源分配详情" width="600px" :visible.sync="dialogResource" class="consoleBox" @close="fnCloseDialogRes()">
      <el-row :span="24" class="pd-t2">
        <el-col :span="12">
          <draw-pie id="1" :data-pie="dateMem" top-title="内存分配"></draw-pie>
        </el-col>
        <el-col :span="12">
          <draw-pie id="2" :data-pie="dateCpu" top-title="cpu分配"></draw-pie>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
         <el-button size="small" @click="fnCloseDialogRes()">退出</el-button>
      </div>
    </el-dialog>
    <el-dialog title="提示" :visible.sync="forceDelete" @close="forceInfo.forceInput = ''; $refs['forceInfo'].resetFields();">
      <span>节点已经完成初始化，强制初始化会<span style="color:#F56C6C">删除节点数据，可能丢失数据</span>，确定操作请输入"确定"</span>
      <el-form :model="forceInfo" :rules="forceInfoRules" ref="forceInfo">
        <el-form-item prop="forceInput">
          <el-input v-model="forceInfo.forceInput" class="full-width" style="margin-top: 20px" placeholder="请输入'确定'"></el-input>
          <el-input v-show="false"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="forceDelete = false;">取消</el-button>
        <el-button size="small" type="primary" @click="fnForceInfo('forceInfo')" :disabled="forceLoading">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="操作记录" width="70%" :visible.sync="stackVisible" class="stack-box" @close="fnCloseStack()">
      <el-table v-loading="stackLoading" :data="stackData" style="width: 100%" height="280px" @expand-change="expandChangeStack" :row-key="getRowKeysStack" :expand-row-keys="expandsStack">
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="日志信息" style="width: 100%"><span class="stack-log">{{scope.row.operateLog || "暂无"}}</span></el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="operateDesc" label="操作"></el-table-column>
        <el-table-column prop="operateStateDesc" label="状态" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.operateState == 1" class="color-success">{{scope.row.operateStateDesc}}</span>
            <span v-else-if="scope.row.operateState == -1" class="color-error">{{scope.row.operateStateDesc}}</span>
            <span v-else class="color-loading">{{scope.row.operateStateDesc}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="takeTimeSeconds" label="耗时" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.operateState == 0">--</span>
            <span v-else>{{scope.row.takeTimeSeconds}}s</span>
          </template>
        </el-table-column>
        <el-table-column prop="operateFinishedPercentage" label="进度" width="260px" align="left" header-align="center">
          <template slot-scope="scope">
            <el-progress v-if="scope.row.operateState == 1 || scope.row.operateState == -1" :percentage="100" :status="scope.row.operateState == 1?'success':'exception'"></el-progress>
            <el-progress v-else :percentage="scope.row.operateFinishedPercentage"></el-progress>
          </template>
        </el-table-column>
        <el-table-column prop="operateStartTime" label="开始时间" width="200px" align="center" :show-overflow-tooltip="true"></el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination @current-change="fnCurrentChange" background :current-page.sync="start" :page-size="limit" layout="prev, pager, next, jumper, ->, total" :total="total"></el-pagination>
      </div>
      <div class="pos-button">
        <el-button size="small" @click="stackVisible = false">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {clusterGetAll,findClusterNodes,nodeTest,nodeSave,nodeDelete,setNodeAsChild,nodeUpdate,setNodeState,reInitNode,initStep,fromFailed,findResource,operateStack} from '../../axios/api'
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
    name: 'nodesManage',
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
      var validForce = (rule, value, callback) => {
        if (!value){
          callback(new Error('请输入\'确定\''))
        }else if (value != "确定"){
          callback(new Error('请输入正确信息'))
        }else {
          callback()
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
      var validCPU = (rule, value, callback) => {
        if (!value){
          callback(new Error('请输入主机需要分配到集群的CPU数量'))
        }else if(!isNumber(value)) {
          callback(new Error('只允许输入数字'))
        }else if(value > this.NodeInfo.nodeCpuInfo){
          callback(new Error('CPU数量要小于节点总CPU数量'))
        }else {
          callback()
        }
      };
      var validMemory = (rule, value, callback) => {
        if (!value){
          callback(new Error('请输入主机需要分配到集群的内存数量'))
        }else if(value > this.NodeInfo.nodeMemoryInfo){
          callback(new Error('内存数量要小于节点总内存数量'))
        }else {
          callback()
        }
      };
      var validDisk = (rule, value, callback) => {
        if (!value){
          callback(new Error('请输入主机需要分配到集群的磁盘大小'))
        }else if(value > this.NodeInfo.nodeDiskInfoTotal){
          callback(new Error('磁盘数量要小于节点总磁盘数量'))
        }else {
      callback()
    }
  };
      return {
        dateCpu:[],
        dateMem:[],
        rightMenu:false,
        dialogFormVisible:false,
        dialogConsoleVisible:false,
        dialogResource:false,
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
          clusterId:'',
          clusterName:'',
          nodeName:'',
          nodeType:'',
          nodeAsChild:0,
          nodeIP:'',
          nodeSSHPort:'',
          nodeUser:'',
          pwd:'',
          nodePassword:'',
          nodeSystem:'',
          nodeCpuInfo:'',
          nodeMemoryInfo:'',
          nodeDiskInfoTotal:'',
          nodeVarDisInfoUsable:'',
          nodeVarDisInfoAlwas:'',
          nodeAllocatableCpu:'',
          nodeAllocatableMemory:'',
        },
        NodeInfo:{
          nodeCpuInfo:'',
          nodeMemoryInfo:'',
          nodeDiskInfoTotal:'',
        },
        NodeOptions: [{ value: '0', label: '管理节点' }, { value: '1', label: '负载节点' }],
        rules: {
          pwd: [ {required: true, validator: validatePass, trigger: 'blur' } ],
          nodePassword: [ {required: true, validator: validatePass2, trigger: 'blur' } ],
          nodeIP: [ {required: true, validator: validNodeIP, trigger: 'blur' } ],
          nodeName: [ {required: true, validator: validNodeName, trigger: 'blur' } ],
          nodeUser: [ {required: true, message:'请输入账号', trigger: 'blur' } ],
          nodeSSHPort: [ {required: true, validator: validSSH, trigger: 'blur' } ],
          nodeCpuInfo: [ {required: true, validator: validCPU, trigger: 'blur' } ],
          nodeMemoryInfo: [ {required: true, validator: validMemory, trigger: 'blur' } ],
          nodeDiskInfoTotal: [ {required: true, validator: validDisk, trigger: 'blur' } ],
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
        forceInfoRules: {
          forceInput: [ { required: true, trigger: 'blur', validator: validForce } ]
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
      if(this.$route.meta.keepAlive === false && this.$route.name == 'nodesManage') {
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
          this.formInfo = Object.assign({},this.formInfo,{
            "clusterName":this.selectClusterName.value,
            "clusterId": this.selectClusterName.id,
            "nodeSSHPort":22
          });
          this.rightMenu = true;
          this.formEdit = false;
          this.checkout = false;
          this.checkLoading = false;
        }
      },
      /*下拉框选择*/
      fnSelectChange(data){
        this.formInfo.clusterName = data.value;
        this.formInfo.clusterId = data.id;
        this.fnIntervalTable(data.id);
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
            nodeTest(this.formInfo).then((res)=>{
              if(res.success) {
                this.formInfo.nodeSystem = res.data.nodeSystem;
                this.formInfo.nodeCpuInfo = res.data.nodeCpuInfo;
                this.formInfo.nodeAllocatableCpu = res.data.nodeCpuInfo;
                this.formInfo.nodeMemoryInfo = res.data.nodeMemoryInfo;
                this.formInfo.nodeAllocatableMemory = res.data.nodeMemoryInfo;
                this.formInfo.nodeDiskInfoTotal = res.data.nodeDiskInfoTotal;
                this.formInfo.nodeVarDisInfoUsable = res.data.nodeVarDisInfoUsable;
                this.formInfo.nodeVarDisInfoAlwas = res.data.nodeVarDisInfoAlwas;
                this.checkout = true;
                this.NodeInfo.nodeCpuInfo = res.data.nodeCpuInfo;
                this.NodeInfo.nodeMemoryInfo = res.data.nodeMemoryInfo;
                this.NodeInfo.nodeDiskInfoTotal = res.data.nodeDiskInfoTotal;
              }else{
                this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
              }
              this.checkLoading = false;
            })
          }
        });
      },
      /*表格周期查询*/
      fnIntervalTable(clusterId){
        this.fnQueryAll(clusterId);
        this.fnClearTable();
        interval = setInterval(()=>{
          this.fnQueryAll(clusterId);
        }, 10000);
      },
      /*表格查询周期停止*/
      fnClearTable(){
        clearInterval(interval);
      },
      /*表格查询*/
      fnQueryAll(clusterId){
        findClusterNodes({clusterId:clusterId}).then((res)=>{
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
              nodeSave(this.formInfo).then((res) => {
                if(res.success){
                  this.fnIntervalTable(this.formInfo.clusterId);
                  this.rightMenu = false;
                  this.$message({ showClose: true, message: '新增成功', type: 'success' });
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
          nodeDelete(rows).then((res)=>{
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
      fnReInitNode(row,index){
        this.iconLoading = index;
        if(row.nodeInitState == 10){
          this.forceDelete = true;
          this.forceInfo.nodeId = row.nodeId;
          this.forceInfo.clusterId = row.clusterId;
          this.iconLoading = "";
        }else {
          reInitNode({"nodeId":row.nodeId}).then((res)=>{
            if(res.success){
              this.fnIntervalTable(row.clusterId);
            }
            this.iconLoading = "";
          });
        }
      },
      /*设置节点是否可部署应用*/
      fnSetNodeAsChild(row,index){
        this.iconLoading = index;
        setNodeAsChild(row).then((res)=>{
          if(res.success){
            this.fnIntervalTable(row.clusterId);
          }else {
            if(res.errorMsg){
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
          }
          this.iconLoading = "";
        });
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
        setNodeState({nodeId:row.nodeId}).then((res)=>{
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
      /*关闭资源详情弹窗*/
      fnCloseDialogRes(){
        this.dialogResource = false;
        this.dateCpu = [];
        this.dateMem = [];
      },
      fnGetResouce(rows){
        findResource(rows).then((res)=>{
          if(res.success){
            this.dialogResource = true;
            setTimeout(()=>{
              this.dateCpu.push({value:res.data.kubeResCpu,name:'k8s预留'});
              this.dateCpu.push({value:res.data.kubeUseCpu,name:'k8s可用'});
              this.dateCpu.push({value:res.data.systemCpu,name:'系统预留'});
              this.dateMem.push({value:res.data.kubeResMem,name:'k8s预留'});
              this.dateMem.push({value:res.data.kubeUseMem,name:'k8s可用'});
              this.dateMem.push({value:res.data.evictionHard,name:'k8s最低限制'});
              this.dateMem.push({value:res.data.systemMem,name:'系统预留'});
            });
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
        });
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
