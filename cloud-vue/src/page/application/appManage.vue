<template>
  <div class="appManage">
    <div class="left-part">
      <div class="title">
        <span style="line-height:32px;">应用管理</span>
        <div style="float: right;">
          <el-dropdown @command="addApp" trigger="click">
            <el-button type="primary" size="small">新增应用服务<i class="el-icon-arrow-down el-icon--right"></i></el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="1">常规应用服务</el-dropdown-item>
              <el-dropdown-item command="2">自定义应用服务</el-dropdown-item>
              <el-dropdown-item command="3">一次性应用服务</el-dropdown-item>
              <el-dropdown-item command="4">HPA应用服务</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-dropdown @command="addMiddle" trigger="click">
            <el-button type="primary" size="small">新增中间件服务<i class="el-icon-arrow-down el-icon--right"></i></el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="item in middleOptions" :key="item.codeValue" :command="item.codeValue">{{item.codeDESC}}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
      <div class="description">应用管理为用户提供了便捷的向导式应用发布流程。支持应用实例的动态伸缩、常用中间件的快速部署和应用状态监控。</div>
      <el-form label-position="right" :model="searchInfo" ref="searchInfo" label-width="108px" class="search-box">
        <el-form-item label="应用名称/标识" prop="field">
          <el-input v-model="searchInfo.field" class="full-width" clearable @change="fnSearchInfo"></el-input>
        </el-form-item>
        <el-form-item label="所在集群" prop="clusterId">
          <el-select v-model="searchInfo.clusterId" class="full-width" filterable @change="fnChangeSearch" clearable>
            <el-option v-for="item in allClusters" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <!--<el-button type="primary" size="small" style="margin: 4px 0 0 14px;" icon="el-icon-search" @click="fnSearchInfo">搜索</el-button>-->
        <el-button type="primary" size="small" style="margin: 4px 0 0 10px;"  @click="fnSearchReset">重置</el-button>
      </el-form>
      <div class="flex-content">
          <el-table :data="tableData" v-loading="loading" element-loading-text="拼命加载中" :stripe="true" :highlight-current-row="true" style="width: 100%" height="100%">
            <el-table-column align="center" :show-overflow-tooltip="true" prop="clusterId" min-width="100px" label="集群名称" :filters="filterCluster" :filter-method="filterHandler">
              <template slot-scope="scope">{{scopeFilters(filterCluster,scope.row.clusterId)}}</template>
            </el-table-column>
            <el-table-column align="center" prop="namespaceName" min-width="100px" label="命名空间" :filters="filterNameSpace" :filter-method="filterHandler">
              <template slot-scope="scope">{{scopeFilters(filterNameSpace,scope.row.namespaceName)}}</template>
            </el-table-column>
            <el-table-column prop="appIdentify" :show-overflow-tooltip="true" min-width="130px" label="应用标识" align="center"></el-table-column>
            <el-table-column prop="appName" :show-overflow-tooltip="true" min-width="120px" label="应用名称" align="center"></el-table-column>
            <el-table-column prop="appStatus" label="状态" align="center">
              <template slot-scope="scope">
                <span :class="scope.row.appStatus === '3' ? 'color-success' : 'color-warning'">{{scope.row.appStatusDesc}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="mappingPort" label="外部服务端口" align="center"></el-table-column>
            <el-table-column prop="instanceNum" label="实例个数" align="center">
              <template slot-scope="scope"  v-if="scope.row.appType != '4'">
                <div class="btn-flex" @click="fnOpenFlex(scope.row)" title="扩容">
                  {{scope.row.instanceNum}}
                  <i class="el-icon-edit"></i>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="发布操作" width="160px" align="center">
              <template slot-scope="scope" >
                <el-button @click="toAppSelect(scope.row)" type="text" size="small" v-if="(scope.row.appType == '4' || scope.row.appType == '1') && (scope.row.appRestartPolicy === null || scope.row.appRestartPolicy === '')">{{scope.row.appStatus === '1' ? '新发布':'新发布'}}</el-button>
                <el-button @click="middleWareConfig(scope.row,2)" type="text" size="small" v-if="scope.row.appType == '3'">配置资源</el-button>
                <el-button @click="uploadFile(scope.row)" type="text" size="small" v-if="scope.row.appType == '3'" :class="{disable:scope.row.appStatus === '1'}">上传文件</el-button>
                <el-button @click="middleWareConfig(scope.row,3)" type="text" size="small" v-if="scope.row.appType == '2'">镜像资源配置</el-button>
                <el-button @click="configMiddlewareFile(scope.row,3)" type="text" size="small" v-if="scope.row.appType == '2'">配置文件</el-button>
                <el-button @click="jobConfig(scope.row)" type="text" size="small" v-if="scope.row.appType == '1' && (scope.row.appRestartPolicy !== null && scope.row.appRestartPolicy!=='')" :disabled="scope.row.appStatus !== '1'&&scope.row.appStatus !== '2'">镜像配置</el-button>
              </template>
            </el-table-column>
            <el-table-column label="管理操作" width="200px" align="center">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="fnAppStart(scope.row,scope.$index)" v-if="scope.row.appStatus === '2' || scope.row.appStatus === '4'">启动<i class="icon-width" :class="{'el-icon-loading':(scope.$index===btnLoading)}"></i></el-button>
                <el-button type="text" size="small" @click="fnAppStop(scope.row,scope.$index)" v-if="scope.row.appStatus === '3'" :class="{'mg-r2':(scope.row.appType == '2')}" :disabled="scope.row.appRestartPolicy !== ''?true:false">{{scope.row.appRestartPolicy !== ''?'已执行':'停止'}}<i class="icon-width" :class="{'el-icon-loading':(scope.$index===btnLoading)}"></i></el-button>
                <el-button type="text" size="small" @click="fnBackUp(scope.row,scope.$index)"  v-if="scope.row.appType == '2'" :disabled="scope.row.appStatus !== '3' || scope.$index===btnLoading">备份</el-button>
                <el-button type="text" size="small" @click="fnOpenReduction(scope.row,scope.$index)"  v-if="scope.row.appType == '2'" :disabled="scope.row.appStatus !== '3' || scope.$index===btnLoading">还原</el-button>
                <a target="_blank" :href="scope.row.ip" v-if="scope.row.appStatus === '3' && (scope.row.appRestartPolicy === '' && (scope.row.appType == '1' || scope.row.appType == '4') || scope.row.appType == '3')"><el-button :disabled="!scope.row.ip"  type="text" size="small" style="margin-left: 0">访问</el-button></a>
                <a target="_blank" @click="fnQueryAppVersion(scope.row.id)" v-if="scope.row.appStatus === '3' && (scope.row.appRestartPolicy === '' && scope.row.appType == '1' || scope.row.appType == '3')"><el-button :disabled="!scope.row.ip"  type="text" size="small" style="margin-left: 0">版本控制</el-button></a>
                <a target="_blank" @click="fnAddAppVersion(scope.row,scope.$index)" v-if="scope.row.appStatus === '3' && (scope.row.appRestartPolicy === '' && scope.row.appType == '1' || scope.row.appType == '3')" :disabled="scope.$index===btnLoading"><el-button :disabled="!scope.row.ip"  type="text" size="small" style="margin-left: 0">升级</el-button></a>
              </template>
            </el-table-column>
            <el-table-column label="节点操作" width="150px" align="center">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="toPodInfo(scope.row)" :disabled="scope.row.appStatus !== '3' || scope.row.appType == '2'">查看节点</el-button>
                <el-button type="text" size="small" v-if="scope.row.appSchedule == '1'" @click="toRunInfo(scope.row)" :disabled="scope.row.appStatus !== '3'">执行状态</el-button>
              </template>
            </el-table-column>
            <el-table-column label="配置操作" width="160px" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="configRow(scope.row)" size="small" v-if="(scope.row.appRestartPolicy===null || scope.row.appRestartPolicy==='')">参数配置</el-button>
                <el-button type="text" @click="editRow(scope.row,1)" size="small" v-if="scope.row.appType == '1' && (scope.row.appRestartPolicy===null || scope.row.appRestartPolicy==='')">编辑</el-button>
                <el-button type="text" @click="editRow(scope.row,4)" size="small" v-if="scope.row.appType == '1' && (scope.row.appRestartPolicy!==null && scope.row.appRestartPolicy!=='') && (scope.row.appStatus ==='1' || scope.row.appStatus ==='2')">编辑</el-button>
                <el-button type="text" @click="editRow(scope.row,2)" size="small" v-if="scope.row.appType == '3'">编辑</el-button>
                <el-button type="text" @click="deleteRow(scope.row)" size="small">删除</el-button>
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
        <span class="card-title">{{formEdit ? '修改':'新增'}}{{statusType == 3 ?'中间件服务':statusType == 2 ?'自定义应用服务':statusType == 4 ?'一次性应用服务':statusType == 5 ?'HPA应用服务':'常规应用服务'}}</span>
        <el-button style="float: right; padding: 3px 0;" type="text" icon="el-icon-close" @click="closeForm">关闭</el-button>
      </div>
      <el-form label-position="right" :model="formInfo" :rules="formInfoRules" ref="formInfo" label-width="112px">
        <el-form-item label="所在集群" prop="clusterId">
          <el-select v-model="formInfo.clusterId" class="full-width" filterable @change="fnClusterChange" :disabled="formEdit">
            <el-option v-for="item in allClusters" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="集群标识" v-show="showIdentify">
          <el-input v-model="identifyInfo" class="full-width" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="命名空间" prop="namespaceId">
          <el-select v-model="formInfo.namespaceId" class="full-width" filterable :disabled="nameSpaceDisabled" >
            <el-option v-for="item in nameSpaceOptions" :key="item.id" :label="item.namespaceName" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="应用名称" prop="appName" v-if="statusType != 3">
          <el-input v-model="formInfo.appName" class="full-width"></el-input>
        </el-form-item>
        <el-form-item label="应用标识" prop="appIdentify" v-if="statusType != 3">
          <el-input v-model="formInfo.appIdentify" class="full-width" v-if="statusType !== 2" placeholder="选择镜像后自动生成" :disabled="true"></el-input>
          <el-input v-model="formInfo.appIdentify" class="full-width" v-else placeholder="请输入应用标识" :disabled="false"></el-input>
        </el-form-item>
        <el-form-item label="中间件类型" prop="middleWareType" v-if="statusType == 3">
          <el-select v-model="formInfo.middleWareType" class="full-width" filterable disabled>
            <el-option v-for="item in middleOptions" :key="item.codeValue" :label="item.codeDESC" :value="item.codeValue"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择存储" prop="volumeId" v-if="statusType == 3">
          <el-select v-model="formInfo.volumeId" class="full-width" filterable @change="setSpace">
            <el-option v-for="item in PVOptions" :key="item.volumeId" :label="item.volumeDisplayName" :value="item.volumeId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="存储分配空间" prop="space" v-if="statusType == 3">
          <el-input v-model="formInfo.space" class="full-width" :placeholder="availableSpace ? ('可分配 '+ availableSpace + ' GB'):''">
            <template slot="append">Gb</template>
          </el-input>
        </el-form-item>
        <el-form-item label="外部服务端口" prop="mappingPort" v-if="statusType != 1 && statusType != 4 && statusType != 5">
          <el-input v-model="formInfo.mappingPort" class="full-width" placeholder="须在30000-32767范围内"></el-input>
        </el-form-item>
        <el-form-item label="实例个数" prop="instanceNum" v-if="statusType != 5">
          <el-input-number v-if="isOneInstaceNum" v-model="formInfo.instanceNum" class="full-width" controls-position="right" :min="1" :disabled="formEdit || isOneInstaceNum"></el-input-number>
          <el-input-number v-else v-model="formInfo.instanceNum" class="full-width" controls-position="right" :min="0" :disabled="formEdit"></el-input-number>
        </el-form-item>
        <el-form-item label="升级策略" prop="appStrategy" v-if="statusType != 3 && statusType != 4">
          <el-select v-model="formInfo.appStrategy" class="full-width" filterable>
            <el-option v-for="item in updateOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否外挂存储" prop="recordLog" v-if="statusType != 3 && statusType != 4">
          <el-select v-model="formInfo.recordLog" class="full-width" filterable :disabled="formEdit">
            <el-option v-for="item in ynOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="应用重启条件" prop="appRestartPolicy" v-if="statusType === 4">
          <el-select v-model="formInfo.appRestartPolicy" class="full-width" filterable :disabled="formEdit">
            <el-option v-for="item in restartPolicy" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否设置周期" prop="appSchedule" v-if="statusType === 4">
          <el-select v-model="formInfo.appSchedule" class="full-width" filterable :disabled="formEdit">
            <el-option v-for="item in appSchedules" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="循环单位" prop="appSchedule" v-if="statusType === 4 && formInfo.appSchedule ==='1'">
          <el-select v-model="addAppSchedule" class="full-width" filterable :disabled="formEdit" @change="getMaxAndMinNum">
            <el-option v-for="item in addAppSchedules" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间间隔" prop="numAppSchedule" v-if="statusType === 4 && formInfo.appSchedule ==='1'">
          <el-input-number v-model="numAppSchedule" class="full-width" controls-position="right" :min="minAppSchedule" :max="maxAppSchedule" :disabled="formEdit" ></el-input-number>
        </el-form-item>
        <el-form-item label="开始日期" prop="beginTime" v-if="statusType === 4 && formInfo.appSchedule ==='1'">
          <el-date-picker v-model="formInfo.beginTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期" :picker-options="pickerOptions1" :default-value="new Date()" :disabled="formEdit"></el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="appDesc">
          <el-input v-model="formInfo.appDesc" type="textarea" :autosize="{ minRows: 3, maxRows: 5}"></el-input>
        </el-form-item>
        <div style="margin: 0 auto; text-align: center">
          <el-button type="primary" size="small" @click="fnSave('formInfo')" :disabled="submitLoading">保存<i v-if="submitLoading" class="el-icon-loading el-icon--right"></i></el-button>
        </div>
      </el-form>
    </el-card>
    <el-dialog title="命名空间" :visible.sync="dialogFormVisible" @close="dialogLoading = false">
      <el-form :model="formNameSpace" :rules="formNameSpaceRules" ref="formNameSpace" label-width="110px">
        <el-form-item label="命名空间名称" prop="namespaceName" style="margin-bottom: 0">
          <el-input v-model="formNameSpace.namespaceName" placeholder="小写英文、数字、减号，如abc-123-edf" class="full-width" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false; $refs['formNameSpace'].resetFields()">取消</el-button>
        <el-button size="small" type="primary" @click="fnSaveNameSpace('formNameSpace')" :disabled="dialogLoading">确定<i v-if="dialogLoading" class="el-icon-loading el-icon--right"></i></el-button>
      </div>
    </el-dialog>
    <el-dialog title="扩容" width="500px" :visible.sync="flexVisible" @close="closeFlex()">
      <div>
        <span class="flex-item">实例个数</span>
        <el-input-number v-model="flexInstanceNum" class="flex-width" controls-position="right" :min="0"></el-input-number>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="flexVisible = false;">取消</el-button>
        <el-button size="small" type="primary" @click="fnSaveFlex()" :disabled="dialogLoading">确定<i v-if="dialogLoading" class="el-icon-loading el-icon--right"></i></el-button>
      </div>
    </el-dialog>
    <el-dialog :title="statusType == '3'?'中间件镜像资源配置':'自定义应用资源配置'" :visible.sync="middleWareDialog" @close="dialogLoading = false">
      <el-form :model="formMiddleWare" :rules="formMiddleWareRules" ref="formMiddleWare" label-width="110px">
        <el-form-item v-if="this.statusType == 3" label="镜像" prop="appImageId" >
          <el-select v-model="formMiddleWare.appImageId" class="full-width" filterable >
            <el-option v-for="item in middleWareImages" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="CPU配额">
          <el-slider v-model="cpuQuota" style="width: 90%" :step="0.25" :max="totalCpu" @change="cpuChange"></el-slider>
        </el-form-item>
        <div class="slider-detail">
          <span>限制</span>
          <span class="slider-unit">{{cpuQuota}} Core</span>
          <div class="describe">CPU限制值，描述需要预备给容器的CPU值，依据此值选择有足够资源的节</div>
        </div>
        <el-form-item label="内存配额">
          <el-slider v-model="memoryQuota" style="width: 90%" :step="0.5" :max="totalMemory" @change="memoryChange"></el-slider>
        </el-form-item>
        <div class="slider-detail">
          <span>限制</span>
          <span class="slider-unit">{{memoryQuota}} Gib</span>
          <div class="describe">Gib申请值，描述需要预备给容器的CPU值，依据此值选择有足够资源的节</div>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="middleWareDialog = false; $refs['formMiddleWare'].resetFields()">取消</el-button>
        <el-button size="small" type="primary" @click="saveMiddleWareConfig" :disabled="dialogLoading">确定<i v-if="dialogLoading" class="el-icon-loading el-icon--right"></i></el-button>
      </div>
    </el-dialog>


    <el-dialog title="版本升级镜像选择" :visible.sync="versionAddDialog" @close="dialogLoading = false;btnLoading = ''">
      <el-form :model="versionFrom" :rules="versionFromRule" ref="versionFrom" label-width="110px">
        <el-form-item label="镜像" prop="imageId" >
          <el-select v-model="versionFrom.imageId" class="full-width" filterable >
            <el-option v-for="item in appVersionImages" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="versionAddDialog = false; $refs['versionFrom'].resetFields();btnLoading = ''">取消</el-button>
        <el-button size="small" type="primary" @click="saveVersionConfig" :disabled="dialogLoading">确定<i v-if="dialogLoading" class="el-icon-loading el-icon--right"></i></el-button>
      </div>
    </el-dialog>
    <el-dialog :title="editAppName + '自定义配置文件'" :visible.sync="middleWareConfigDialog" @close="dialogLoading = false">
      <ace-editor id="editorMiddleware" lang="batchfile" theme="terminal" height="300px"></ace-editor>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="middleWareConfigDialog = false; ">取消</el-button>
        <el-button size="small" type="primary" @click="saveMiddleWareConfigFile" :disabled="dialogLoading">保存<i v-if="dialogLoading" class="el-icon-loading el-icon--right"></i></el-button>
      </div>
    </el-dialog>
    <el-dialog title="应用备份管理" width="800px" :visible.sync="backDialog" @close="dialogLoading = false">
      <div class="flex-content" style="height: 300px">
        <el-table :data="backData" v-loading="loading" element-loading-text="拼命加载中" :stripe="true" :highlight-current-row="true" style="width: 100%" height="100%">
          <el-table-column prop="appName" :show-overflow-tooltip="true" min-width="100px" label="备份文件" align="center"></el-table-column>
          <el-table-column prop="createTimeFom" :show-overflow-tooltip="true" min-width="150px" label="备份时间" align="center"></el-table-column>
          <el-table-column prop="instanceNum" :show-overflow-tooltip="true" min-width="80px" label="实例个数" align="center"></el-table-column>
          <el-table-column prop="isConfig" :show-overflow-tooltip="true" min-width="80px" label="配置文件" align="center">
            <template slot-scope="scope">
              <span>{{scope.row.isConfig === '1'?'备份':'固定'}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="isUse" :show-overflow-tooltip="true" min-width="80px" label="备份" align="center">
            <template slot-scope="scope">
              <span>{{scope.row.isUse === '1'?'最近还原':'未使用'}}</span>
            </template>
          </el-table-column>
          <el-table-column label="删除" width="80px" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="fnDeleteBackUp(scope.row,scope.$index)" :disabled="dialogLoad===scope.$index">删除</el-button>
            </template>
          </el-table-column>
          <el-table-column label="还原" width="80px" align="center">
            <template slot-scope="scope">
              <el-button type="text" @click="fnReduction(scope.row,scope.$index)" size="small" :disabled="dialogLoad===scope.$index">还原</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="backDialog = false;">取消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="应用版本信息管理" width="1000px" :visible.sync="versionDialog" @close="dialogLoading = false">
      <div class="flex-content" style="height: 300px">
        <el-table :data="versionData" v-loading="loading" element-loading-text="拼命加载中" :stripe="true" :highlight-current-row="true" style="width: 100%" height="100%">
          <el-table-column prop="appName" :show-overflow-tooltip="true" min-width="100px" label="应用" align="center"></el-table-column>
          <el-table-column prop="createTimeFom" :show-overflow-tooltip="true" min-width="150px" label="创建时间" align="center"></el-table-column>
          <el-table-column prop="versionNameStr" :show-overflow-tooltip="true" min-width="150px" label="版本映射" align="center"></el-table-column>
          <el-table-column prop="imageName" :show-overflow-tooltip="true" min-width="150px" label="版本镜像" align="center"></el-table-column>
          <el-table-column prop="isuse" :show-overflow-tooltip="true" min-width="100px" label="当前使用" align="center">
            <template slot-scope="scope">
              <span>{{scope.row.isuse === '1'?'当前访问':'未使用'}}</span>
            </template>
          </el-table-column>
          <el-table-column label="删除" width="130px" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="fnDeleteVersion(scope.row,scope.$index)" :disabled="versionload===scope.$index || scope.row.isuse === '1'">删除</el-button>
            </template>
          </el-table-column>
          <el-table-column label="回滚到当前版本" width="150px" align="center">
            <template slot-scope="scope">
              <el-button type="text" @click="fnBackVersion(scope.row,scope.$index)" size="small" :disabled="versionload===scope.$index || scope.row.isuse === '1'">回滚到该版本</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="versionDialog = false;">取消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="上传文件" :visible.sync="uploadFileDialog" @close="dialogLoading = false">
      <el-form ref="formuploadFile"  label-width="110px">
        <el-form-item label="上传文件：" class="is-required" label-width="114px">
          <el-upload class="uploadLeft-box" ref="upload1"  :action="uploadUrl" :auto-upload="false" :limit="1" :on-change="uploadChange" :before-upload="beforeUpload" :on-remove="fnRemove" :on-exceed="fnMax">
            <el-button slot="trigger" size="small" type="primary" :disabled="submitLoading1">选取文件</el-button>
          </el-upload>
        </el-form-item>
        <el-alert title="注：应用程序只能是 'yml' 或 'yaml' 文件" type="info" show-icon :closable="false" style="margin: -20px 0 16px 0"></el-alert>
      </el-form>
      <div :class="showFile ? 'showFile':'onShowFile'" >
        <ace-editor id="editor" lang="batchfile" theme="terminal" height="300px"></ace-editor>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="uploadFileDialog = false; $refs['formuploadFile'].resetFields()">取消</el-button>
        <el-button size="small" type="primary" @click="fnUploadFile" :disabled="dialogLoading">上传<i v-if="dialogLoading" class="el-icon-loading el-icon--right"></i></el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {appGetAll,getAllClusters,appDiyUpload,filePreview,saveJobConfig,addAppGroup,checkAppIdentify,appSave,appDiySave,appEdit,appRemove,appStart,fnAppDownloadConfig,appStop,reductionBackUp,deleBackUp,getAllBackUp,fnBackUp,appFlex,getNameSpace,saveNameSpace,getAllNameSpace,getNameSpaceByUser,appCode,saveMiddleWare,pvQueryAll,getMiddleWareImages,getVersionImages,getMiddleWareDefaultConfig,saveAppConfig,getAllVersion,saveAppVersion,deleteVersion,backTheVersion,createYaml} from '../../axios/api'
  import { isLowercaseMinus,isNumber } from '../../assets/js/utils'
  import AceEditor from 'ace-vue2'
  var validIdentify = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入应用标识'))
    }else  if (!isLowercaseMinus(value)){
      callback(new Error('请输入小写英文、数字、减号(减号不能在首、尾)'))
    }else {
      callback()
    }
  };
  var validNamesSpace = (rule, value, callback) => {
    if (!value){
      callback(new Error('请输入命名空间名称'))
    }else  if (!isLowercaseMinus(value)){
      callback(new Error('请输入小写英文、数字、减号(减号不能在首、尾)'))
    }else {
      callback()
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
    name: 'application',
    data() {
      var validSpace = (rule, value, callback) => {
        if (!value){
          callback(new Error('请输入存储分配空间'))
        }else if (!isNumber(value)){
          callback(new Error('只允许输入数字'))
        }else if (this.availableSpace == 0){
          callback(new Error('请先选择存储'))
        }else if (this.availableSpace < Number(value)){
          callback(new Error('存储分配空间应小于 '+this.availableSpace+' Gb'))
        }else {
          callback()
        }
      };
      return {
        pickerOptions1: {
          disabledDate(time) {
            return time.getTime() < Date.now();
          }
        },
        startTime:'',
        rightMenu: false,
        showFile:false,
        dialogLoad:'',
        versionload:'',
        uploadUrl:"/api/app/filePreview.do",
        tableData: [],
        backData:[],
        numAppSchedule:'',
        addAppSchedule:'',
        maxAppSchedule:59,
        minAppSchedule:1,
        formInfo:{
          id:'',
          clusterId:'',
          namespaceId:'',
          msg:'',
          appName:'',
          appIdentify:'',
          middleWareType:'',
          volumeId:'',
          mappingPort:'',
          instanceNum:'',
          appStrategy:'',
          appSchedule:'',
          beginTime:'',
          sAppSchedule:'',
          appDesc:'',
          space:'',
          recordLog:'',
          statusType:'',
          appRestartPolicy:'',
          appStatus:'',
          middleware_configfile:'' //中间件应用对应配置文件详情
        },
        formInfoRules:{
          clusterId: [{ required: true, message: '请选择所在集群', trigger: 'change' }],
          namespaceId: [{ required: true, message: '请选择命名空间', trigger: 'change' }],
          appName: [{ required: true, message: '请输入应用名称', trigger: 'blur' }],
//          appIdentify: [{ required: true, trigger: 'blur', validator: validIdentify}],
          instanceNum: [{ required: true, message: '请输入实例个数', trigger: 'blur' }],
          appStrategy: [{ required: true, message: '请选择升级策略', trigger: 'change' }],
          recordLog: [{ required: true, message: '请选择是否记录日志', trigger: 'change' }],
          appRestartPolicy: [{ required: true, message: '请选择应用重启条件', trigger: 'change' }],
          appSchedule: [{ required: true, message: '请设置运行周期', trigger: 'change' }],
          beginTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
          appSchedule: [{ required: true, message: '请选择时间循环单位', trigger: 'change' }],
          middleWareType: [{ required: true, message: '请选择中间件类型', trigger: 'change' }],
          volumeId: [{ required: true, message: '请选择存储', trigger: 'change' }],
          space: [{ required: true, trigger: 'blur',validator: validSpace }],
          mappingPort: [{ required: true, trigger: 'blur',validator: validMappingPort }],
        },
        allClusters:[],
        identifyInfo: "",
        showIdentify:false,
        nameSpaceDisabled: true,
        formNameSpace:{
          namespaceName:''
        },
        formNameSpaceRules:{
          namespaceName: [ { required: true, validator: validNamesSpace, trigger: 'blur' } ,
            { min: 0, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur' } ]
        },
        nameSpaceOptions:[],
        formMiddleWare:{
          appId:'',
          appImageId:'',
          minCPU: '',
          maxCPU: '',
          minMemory: '',
          maxMemory: '',
        },
        versionFrom:{
          appId:'',
          appImageId:''
        },
        jobWare:{
          appId:'',
          appImageId:''
        },
        formuploadFile:new FormData(),
        formMiddleWareRules:{
          appImageId:{required: true,message: '请选择镜像'}
        },
        versionFromRule:{
          imageId:{required: true,message: '请选择镜像'}
        },
        cpuQuota:1,
        memoryQuota:2,
        totalCpu:4,
        totalMemory:10,
        middleWareDialog:false,
        middleWareConfigDialog:false,
        editAppName:'',
        uploadFileDialog:false,
        backDialog:false,
        versionDialog:false,
        versionAddDialog:false,
        middleWareImages:[],
        appVersionImages:[],
        versionData:[],
        statusType: '', // 1.应用服务 2.自定义应用服务 3.中间件 4.一次性应用 5.hpa  appType：1.应用服务 2.中间件 3.自定义应用 4.hpa
        schedule:false,//是否显示循环周期
        formEdit:false,
        loading:false,
        updateOptions: [{ value: '1', label: '覆盖' }, { value: '2', label: '滚动' }],
        ynOptions: [{ value: '0', label: '否' }, { value: '1', label: '是' }],
        restartPolicy: [{ value: '1', label: 'Never' }, { value: '2', label: 'OnFailure' }],
        appSchedules: [{ value: '0', label: '否' }, { value: '1', label: '是' }],
        addAppSchedules: [{ value: 1, label: '分' }, { value: 2, label: '时' }, { value: 3, label: '天' }, { value: 4, label: '月' }, { value: 5, label: '周' }],
        middleOptions: [],
        PVOptions: [],
        dialogFormVisible: false,
        filterNameSpace:[],
        filterCluster:[],
        btnLoading: '',
        flexVisible : false,
        flexId : '',
        flexInstanceNum : '',
        dialogLoading : false,
        submitLoading : false,
        submitLoading1 : false,
        currentPage:1,
        pageSize:5,
        total:0,
        isOneInstaceNum : false,
        middleWareS : ["6"],//只能允许一个实例的中间件
        searchInfo:{clusterId:'',field:''},
        availableSpace:0,
      }
    },
    components:{
      AceEditor
    },
    mounted(){
      if(this.$route.meta.keepAlive === false && this.$route.name == 'appManage') {
        /*选择镜像后*/
        if(this.$route.params.id != undefined){
          this.jobWare = Object.assign({}, this.jobWare, {
            'appImageId':this.$route.params.id,
            'appId':this.$route.query.id
          });
          this.saveJobConfig();
        }
        this.currentPage = 1;
        this.fnAppGetAll();
        this.fnGetAllClusters();
        this.fnGetAllNameSpace();
        this.fnGetMiddle();
      }
    },
    methods: {
      /*一次性应用镜像配置*/
      jobConfig(row){
        let checkImage = {};
        if(this.$route.params.id){
          checkImage = this.$route.params;
        }else {
          checkImage.id= this.formInfo.appImageId;
        }
        this.$router.push({name: 'changeImage', query: {'id':row.id,'job':row.appRestartPolicy}, params:{'checkImage': checkImage}})
      },
      saveJobConfig(){
        saveJobConfig(this.jobWare).then((res)=> {
          if(res.success){
            this.fnAppGetAll();
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
        });
      },
      fnRemove(){
        this.showFile = false;
        this.formuploadFile.delete("file");
      },
      /*上传前执行函数*/
      uploadChange(file){
        let type = file.name.substr(file.name.lastIndexOf(".")+1);
        if(type != 'yml' && type != 'yaml'){
          this.$message({ showClose: true, message: '上传镜像只能是 yml 格式，请重新选择', type: 'error' });
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
      fnUploadFile(){
        appDiyUpload(this.formuploadFile).then((res)=> {
          if (res.success){
            this.uploadFileDialog = false;
            this.$message({showClose: true, message:"上传成功！", type: 'success'});
          }else {
            if (res.errorMsg) {
              this.$message({showClose: true, message: res.errorMsg, type: 'error'});
            }
          }
        });
      },
      beforeUpload(file){
        this.formuploadFile.append('file', file);
      },
      fnMax(files){
        this.$message({ showClose: true, message: '当前限制一次只能选择 1 个文件', type: 'warning' });
      },
      /*查询所有数据*/
      fnAppGetAll(){
        this.loading = true;
        appGetAll({"currentPage":this.currentPage,"pageSize":this.pageSize,"field":this.searchInfo.field,"clusterId":this.searchInfo.clusterId}).then((res)=>{
          if(res.success){
            this.tableData = res.data.list;
            this.total = res.data.total;
          }else {
            if(res.errorMsg){
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            }
          }
          this.loading = false;
        });
      },
      /*分页*/
      fnCurrentChange(val) {
        this.currentPage = val;
        this.fnAppGetAll();
      },
      /*change搜索*/
      fnChangeSearch(clusterId){
        this.searchInfo.clusterId = clusterId;
        this.fnSearchInfo();
      },
      /*点击搜索*/
      fnSearchInfo(){
        this.currentPage = 1;
        this.fnAppGetAll();
      },
      fnSearchReset(){
        this.$refs['searchInfo'].resetFields();
        this.currentPage = 1;
        this.fnAppGetAll();
      },
      /*查询所有集群*/
      fnGetAllClusters(){
        getAllClusters().then((res)=>{
          if(res.success){
            this.allClusters = res.data;
            this.filterCluster = [];
            this.allClusters.forEach((it)=>{
              let temp = {};
              temp.value = it.id;
              temp.text = it.name;
              this.filterCluster.push(temp)
            })
          }
        })
      },
      /*获取所有中间件类型*/
      fnGetMiddle(){
        appCode({codeType:"MIDDLEWARETYPE",yab003:"9999"}).then((res)=>{
          if(res.success){
            this.middleOptions = res.data;
          }
        });
      },
      /*获取所有命名空间*/
      fnGetAllNameSpace(){
//        getAllNameSpace().then((res)=>{
        getNameSpaceByUser({}).then((res)=>{
          if(res.success){
            this.filterNameSpace = [];
            res.data.forEach((it)=>{
              let temp = {};
              temp.value = it.namespaceName;
              temp.text = it.namespaceName;
              this.filterNameSpace.push(temp)
            });
          }
        })
      },
      /*选择集群时*/
      fnClusterChange(clusterId){
        this.allClusters.forEach((item)=>{
          if(item.id == clusterId){
            this.identifyInfo = item.identify;
          }
        });
        this.showIdentify = true;
        this.fnGetNameSpace(clusterId);
        this.fnGetPV(clusterId);
        this.nameSpaceDisabled = false;
        if(this.formInfo.namespaceId != ""){
          this.$set(this.formInfo,"namespaceId", null);
        }
        if(this.formInfo.volumeId != ""){
          this.$set(this.formInfo,"volumeId", null);
        }
      },
      /*新增应用服务*/
      addApp(command) {
        if(command == '1'){
          /*新增常规应用服务*/
          this.statusType = 1;
          this.formInfo.statusType = "1"
          this.fnOpenMenu();
        }else if(command == '2'){
          /*新增自定义应用服务*/
          /*this.$message("暂未开放自定义应用服务");*/
          this.formInfo.statusType = "2";
           this.statusType = 2;
           this.fnOpenMenu();
        }else if(command == '3'){
          this.statusType = 4;
          this.formInfo.statusType = "4";
          this.fnOpenMenu();
        }else if(command == '4'){
            this.statusType = 5;
            this.formInfo.statusType = "5";
            this.fnOpenMenu();
        }
      },
      /*新增中间件服务*/
      addMiddle(command) {
        /*新增中间件服务*/
        this.formInfo.middleWareType = command;
        this.statusType = 3;
        this.formInfo.statusType = "3";
        this.isOneInstaceNum = this.middleWareS.includes(command);
        if(this.middleWareS.includes(command)){
          this.formInfo.instanceNum = "1";
        }
        this.fnOpenMenu();
      },
      fnOpenMenu(){
        this.rightMenu = true;
        this.formEdit = false;
        this.showIdentify = false;
        this.nameSpaceDisabled = true;
      },
      /*打开命名空间新增弹窗*/
      fnPosAdd(){
        if(this.formInfo.clusterId.length == 0){
          this.$message({ showClose: true, message: '请先选择所在集群', type: 'warning' });
        }else {
          this.dialogFormVisible = true;
          if(this.$refs['formNameSpace']){
            this.$refs['formNameSpace'].resetFields();
          }
        }
      },
      /*新增命名空间*/
      fnSaveNameSpace(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.dialogLoading = true;
            saveNameSpace({namespaceName: this.formNameSpace.namespaceName, 'clusterId': this.formInfo.clusterId}).then((res) => {
              if (res.success) {
                this.dialogFormVisible = false;
                this.fnGetNameSpace(this.formInfo.clusterId);
                this.fnGetAllNameSpace();
                this.$set(this.formInfo,"namespaceId",res.data.id);
                this.$message({ showClose: true, message: '新增命名空间成功', type: 'success' });
              } else {
                this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
              }
              this.dialogLoading = false;
            })
          }
        })
      },
      /*根据集群id获取命名空间*/
      fnGetNameSpace(clusterId){
        getNameSpace({clusterId:clusterId}).then((res)=>{
          if(res.success){
            this.nameSpaceOptions = res.data;
          }
        });
      },
      /*根据集群id获取存储*/
      fnGetPV(clusterId){
        pvQueryAll({clusterId:clusterId}).then((res)=>{
          if(res.success){
            this.PVOptions = res.data;
          }
        });
      },
      setSpace(volumeId){
        this.formInfo.space = "";
        this.$refs['formInfo'].clearValidate(['space']);
        for(let i=0; i<this.PVOptions.length; i++){
          if(volumeId == this.PVOptions[i].volumeId){
            this.availableSpace = this.PVOptions[i].volumeAvailableSpace;
            return false;
          }
        }
      },
      fnMiddleWareType(num){
      },
      /*关闭表单*/
      closeForm(){
        this.rightMenu = false;
        this.formEdit = false;
        this.isOneInstaceNum = false;
        this.showIdentify = false;
        this.submitLoading = false;
        this.$refs['formInfo'].resetFields();
        this.formInfo.id = '';
        this.PVOptions = [];
        this.availableSpace = "";
        if(this.statusType == 1){
          this.formInfo.appStrategy = '';
        }else if(this.statusType == 3){
          this.formInfo.mappingPort = '';
        }
      },
      /*保存*/
      fnSave(formName) {
        if(this.statusType != 4){
          //如果不是一次性应用将重启条件改为空
          this.formInfo.appRestartPolicy = '';
        }else{
          //如果是一次性应用检查是否设置重启周期
          if(this.formInfo.appSchedule == '1'){
            if(this.numAppSchedule == ''){
              this.$message({ message: '请设置应用运行周期', type: 'error' });
              return;
            }else{
              if (this.formInfo.beginTime === '' && !this.formEdit){
                this.$message({ message: '请选择任务开始执行时间', type: 'error' });
                return;
              }else{
                //将选中时间和选中周期改为表达式
                let timeArr1 = this.formInfo.beginTime.toString().split(" ")
                let timeArry = timeArr1[0].split("-").concat(timeArr1[1].split(":")).reverse();
                let time = ['分','时','天','月','周']
                let time1 = ['分','点','日','月']
                let numTime = [];
                let Schedule = "";
                let msg = "执行时间为：每" + (this.numAppSchedule == 1? "":this.numAppSchedule) + time[this.addAppSchedule - 1] + "的";
                if(this.addAppSchedule == 5){
                  Schedule = timeArry[1] + " " + timeArry[2] + " * * " + this.numAppSchedule;
                  msg = "执行时间为：每周" + this.numAppSchedule + " " + timeArry[2] + time1[1] + timeArry[1] + time1[0]
                }else{
                  for(let i = 1;i <= 5;i++){
                    if (i < this.addAppSchedule){
                      Schedule += timeArry[i] + " ";
                      numTime.push(timeArry[i] + time1[i - 1]);
                    }else if(i == this.addAppSchedule){
                      Schedule += "*/" + this.numAppSchedule;
                    }else{
                      Schedule += " *";
                    }
                  }
                  msg += numTime.reverse();
                }
                this.formInfo.sAppSchedule = Schedule;
                this.formInfo.msg = msg;
              }
            }
          }
        }
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.submitLoading = true;
            if(this.statusType == 3){
              if (!this.formEdit) {
                saveMiddleWare(this.formInfo,this.formMiddleWare).then((res) => {
                  if (res.success) {
                    this.currentPage = 1;
                    this.fnAppGetAll();
                    this.rightMenu = false;
                    this.isOneInstaceNum = false;
                    this.$message({ showClose: true, message: '新增成功', type: 'success'});
                    this.$refs['formInfo'].resetFields();
                  } else {
                    this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                  }
                  this.submitLoading = false;
                })
              }
            }else if(this.statusType == 1 || this.statusType == 4 || this.statusType == 5){
              if (this.formEdit) {
                appEdit(this.formInfo).then((res) => {
                  if (res.success) {
                    this.currentPage = 1;
                    this.fnAppGetAll();
                    this.rightMenu = false;
                    this.isOneInstaceNum = false;
                    this.formEdit = false;
                    this.$message({ showClose: true, message: '修改成功', type: 'success'});
                    this.$refs['formInfo'].resetFields();
                    this.formInfo.id = '';
                    this.formInfo.appRestartPolicy = '';
                  } else {
                    this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                  }
                  this.submitLoading = false;
                })
              } else {
                appSave(this.formInfo).then((res) => {
                  if (res.success) {
                    this.currentPage = 1;
                    this.fnAppGetAll();
                    this.rightMenu = false;
                    this.isOneInstaceNum = false;
                    this.$message({ showClose: true, message: '新增成功' + (res.errorMsg == undefined?"":"," + res.errorMsg), type: 'success'});
                    this.$refs['formInfo'].resetFields();
                    this.formInfo.id = '';
                    this.formInfo.appRestartPolicy = '';
                  } else {
                    this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                  }
                  this.submitLoading = false;
                })
              }
            }
            else {
              if (this.formEdit) {
                appEdit(this.formInfo).then((res) => {
                  if (res.success) {
                    this.currentPage = 1;
                    this.fnAppGetAll();
                    this.rightMenu = false;
                    this.isOneInstaceNum = false;
                    this.formEdit = false;
                    this.$message({ showClose: true, message: '修改成功', type: 'success'});
                    this.$refs['formInfo'].resetFields();
                    this.formInfo.id = '';
                  } else {
                    this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                  }
                  this.submitLoading = false;
                })
              } else {
                appDiySave(this.formInfo).then((res) => {
                  if (res.success) {
                    this.currentPage = 1;
                    this.fnAppGetAll();
                    this.rightMenu = false;
                    this.isOneInstaceNum = false;
                    this.$message({ showClose: true, message: '新增成功', type: 'success'});
                    this.$refs['formInfo'].resetFields();
                    this.formInfo.id = '';
                  } else {
                    this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                  }
                  this.submitLoading = false;
                })
              }
            }
          }
        });
      },
      /*表格节点类型筛选*/
      filterHandler(value, row, column) {
        const property = column['property'];
        return row[property] === value;
      },
      /*命名空间、集群显示*/
      scopeFilters: function(filters,name){
        let tempName = '';
        filters.forEach((it)=>{
          if(it.value == name){
            tempName = it.text
          }
        })
        return tempName;
      },
      /*配置参数*/
      configRow(row){
        this.$router.push({name: 'configMapManage', query: {'id':row.id,'n':row.appName}});
      },
      /*将后台返回的循环周期解析成界面数据*/
      getAddNum(time){
        let timeArr = time.split("/");//去掉字符串内所有空格在将字符串以/分开
        let result = [];
        if(timeArr.length == 1){
          result[0] = time.substring(time.length - 1);
          result[1] = 5;
        }else{
          result[0] = timeArr[1].split("*")[0];
          result[1] = timeArr[0].split(" ").length;
        }
        return result;
      },
      /*根据选择单位设置最大值*/
      getMaxAndMinNum(time){
        let maxNumArr = [59,23,31,12,6];
        let minNumArr = [1,1,1,1,0];
        this.maxAppSchedule = maxNumArr[time - 1];
        this.minAppSchedule = minNumArr[time - 1];
      },
      /*编辑*/
      editRow(row,num){
        this.formInfo =  Object.assign({},this.formInfo,{
          id:row.id,
          clusterId:row.clusterId,
          namespaceId:row.namespaceId,
          appName:row.appName,
          appIdentify:row.appIdentify,
          instanceNum:row.instanceNum,
          appStrategy:row.appStrategy,
          appDesc:row.appDesc,
          recordLog:row.recordLog,
          mappingPort:row.mappingPort,
          appRestartPolicy:row.appRestartPolicy,
          appSchedule:row.appSchedule,
          beginTime:row.beginTime,
          statusType:num,
          appStatus:row.appStatus
        });
        this.formEdit = true;
        this.rightMenu = true;
        this.nameSpaceDisabled = true,
        this.statusType = num;
        if (this.formInfo.appSchedule == '1') {
          let arr = this.getAddNum(row.sAppSchedule)
          this.numAppSchedule = arr[0];
          this.addAppSchedule = arr[1];
        }
        this.fnGetNameSpace(this.formInfo.clusterId);
      },
      /*删除*/
      deleteRow(row){
        this.$confirm('此操作将永久删除该应用, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.loading = true;
          appRemove({'id':row.id}).then((res)=>{
            if(res.success){
              this.$message({ showClose: true, message: '删除应用成功', type: 'success' });
              this.fnAppGetAll();
            }else {
              this.$message({ showClose: true, message: res.errorMsg || '删除应用失败', type: 'error' });
              this.loading = false
            }
          });
        }).catch(()=>{});
      },
      /*启动*/
      fnAppStart(row,index){
        this.btnLoading = index;
        appStart({'id':row.id}).then((res)=>{
          if(res.success){
            this.$message({ showClose: true, message: '启动成功', type: 'success' });
            this.fnAppGetAll();
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
          this.btnLoading = '';
        });
      },
      /*停止*/
      fnAppStop(row,index){
        this.btnLoading = index;
        appStop({'id': row.id}).then((res) => {
          if(res.success){
            this.$message({ showClose: true, message: '停止成功', type: 'success' });
            this.fnAppGetAll();
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
          this.btnLoading = '';
        });
      },
      /*备份*/
      fnBackUp(row,index){
        this.btnLoading = index;
        fnBackUp({'id': row.id}).then((res) => {
          if(res.success){
            this.$message({ showClose: true, message: '备份成功', type: 'success' });
            this.fnAppGetAll();
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
          this.btnLoading = '';
        });
      },
      /*备份*/
      fnDeleteBackUp(row,index){
        this.dialogLoad = index;
        deleBackUp(row).then((res) => {
          if(res.success){
            this.$message({ showClose: true, message: '删除成功', type: 'success' });
            this.getAllBackUp(row.appId);
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
          this.dialogLoad = '';
        });
      },
      /*还原备份*/
      fnReduction(row,index){
        this.dialogLoad = index;
        reductionBackUp(row).then((res) => {
          if(res.success){
            this.$message({ showClose: true, message: '还原成功', type: 'success' });
            this.getAllBackUp(row.appId);
            this.fnAppGetAll()
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
          this.dialogLoad = '';
          // this.backDialog = false;
        });
      },
      /*还原列表显示*/
      fnOpenReduction(row){
        this.backDialog = true;
        this.getAllBackUp(row.id);
      },
      getAllBackUp(id){
        getAllBackUp({'id': id}).then((res) => {
          if(res.success){
            this.backDialog = true;
            this.backData = res.data;
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
        });
      },
      /*打开扩容面板*/
      fnOpenFlex(row){
        this.flexVisible = true;
        this.flexId = row.id;
        this.flexInstanceNum = row.instanceNum;
      },
      /*关闭扩容面板*/
      closeFlex(){
        this.flexVisible = false;
        this.dialogLoading = false;
        this.flexId = '';
        this.flexInstanceNum = '';
      },
      /*保存扩容*/
      fnSaveFlex(){
        this.dialogLoading = true;
        appFlex({'id': this.flexId, 'instanceNum': this.flexInstanceNum}).then((res) => {
          if(res.success){
            this.$message({ showClose: true, message: '扩容成功', type: 'success' });
            this.flexVisible = false;
            this.fnAppGetAll();
          }else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
          this.dialogLoading = false;
        });
      },
      /*打开中间件镜像资源配置*/
      middleWareConfig(row,num){
        this.formMiddleWare.appId=row.id;
        this.middleWareDialog=true;
        this.statusType = num;

        if (num == 3){
          //查询镜像版本
          getMiddleWareImages({"identify":row.appName,"isFine":"1"}).then((res) => {
            if(res.success){
              this.middleWareImages = res.data;
            }else{
              this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
              this.middleWareDialog=false;
            }
          });
        }
        //查询 cpu 和 内存
        getMiddleWareDefaultConfig(row).then((res) => {
          if(res.success){
            this.cpuQuota = res.data.maxCPU;
            this.memoryQuota = res.data.maxMemory;
          }
        });
      },
      fnAddAppVersion(row,num){
        this.versionFrom.appId=row.id;
        this.versionAddDialog=true;
        this.btnLoading = num;
        //查询镜像版本
        getVersionImages({"appId":row.id}).then((res) => {
          if(res.success){
            this.appVersionImages = res.data;
          }else{
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
            this.versionAddDialog=false;
          }
          });
        },
        /*删除版本信息*/
        fnDeleteVersion(row,index){
            this.versionload = index;
            deleteVersion(row).then((res) => {
                if(res.success){
                    this.$message({ showClose: true, message: '删除成功', type: 'success' });
                    this.fnQueryAppVersion(row.appId);
                }else {
                    this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                }
                this.versionload = '';
            });
        },
      /*打开中间件配置文件*/
      configMiddlewareFile(row,num){
        this.middleWareConfigDialog=true;
        this.statusType = num;
        this.editAppName = row.appName;
        this.formInfo = row;
        setTimeout(()=>{
          let editor = ace.edit("editorMiddleware");
          editor.setValue(this.formInfo.middleware_configfile);
          editor.moveCursorTo(0, 0);
          editor.setReadOnly(false);
        });
      },

      /*查询应用版本信息并且显示*/
      fnQueryAppVersion(appId){
        this.versionDialog=true;
        getAllVersion({"id":appId}).then((res) => {
            if(res.success){
                this.versionData = res.data;
            }else{
                this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                this.versionDialog=false;
            }
        });
      },
      /*将应用回滚到当前版本*/
      fnBackVersion(row,num){
        this.versionload = num;
        backTheVersion(row).then((res) => {
            if(res.success){
                this.fnQueryAppVersion(row.appId);
                this.$message({ showClose: true, message: res.errorMsg, type: 'success' });
            }else{
                this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                this.versionDialog=false;
            }
            this.versionload = '';
        });
      },
        saveVersionConfig(){
            this.$refs['versionFrom'].validate((valid) => {
                if(valid){
                    saveAppVersion(this.versionFrom).then((res) => {
                        if(res.success){
                            this.$message({ showClose: true, message: "升级成功", type: 'success' });
                            this.fnAppGetAll();
                        }else {
                            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
                        }
                        this.versionAddDialog=false;
                        btnLoading = '';
                    });
                }
            });
        },
      uploadFile(row){
        this.formuploadFile.append('id', row.id);
        this.uploadFileDialog = true;
      },
      saveMiddleWareConfig(){
        this.$refs['formMiddleWare'].validate((valid) => {
          if(valid){
            this.middleWareDialog=false;
            this.submitLoading = true;
            if(this.statusType == 2) {
              this.formMiddleWare.appImageId = 0;//区分自定义应用与中间件应用
            }
            saveAppConfig(this.formMiddleWare).then((res) => {
              if(res.success){
                createYaml({"id":this.formMiddleWare.appId});
                this.fnAppGetAll();
              }else {
                this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
              }
              this.submitLoading = false;
            });
          }
        });
      },
      saveMiddleWareConfigFile(){
        let code = ace.edit("editorMiddleware").getValue();
        this.formInfo.middleware_configfile = code;
        appEdit(this.formInfo).then((res) => {
          if (res.success) {
            this.currentPage = 1;
            this.fnAppGetAll();
            this.$message({ showClose: true, message: '修改成功', type: 'success'});
            this.$refs['formInfo'].resetFields();
            this.formInfo.id = '';
            this.formInfo.appRestartPolicy = '';
            this.middleWareConfigDialog = false;
          } else {
            this.$message({ showClose: true, message: res.errorMsg, type: 'error' });
          }
          this.submitLoading = false;
        })
      },
      /*cpu滑块*/
      cpuChange(data){
        this.formMiddleWare.maxCPU = data;
      },
      /*memory滑块*/
      memoryChange(data){
        this.formMiddleWare.maxMemory = data;
      },
      /*跳转到资源配置页*/
      toAppSelect(row){ this.$router.push({name: 'resourceConfig', query: {'id':row.id,'recordLog':row.recordLog,'appStatus':row.appStatus}})},
      toPodInfo(row){
        this.$router.push({name: 'podInfo', query: {'id':row.id,'n':row.appName,'appIdentify':row.appIdentify,'recordLog':row.recordLog}})},
      toRunInfo(row){
        this.$router.push({name: 'runInfo', query: {'id':row.id,'n':row.appName,'appIdentify':row.appIdentify,'recordLog':row.recordLog}})},
    }
  }
</script>
<style lang="scss" type="text/scss">
  .el-textarea__inner { @include beautifyScrollbar($scrollbar-bgcolor: $textArea-scrollbar-bgcolor)}
  .el-table-filter__bottom { text-align: center!important; }
  .appManage {
    .el-table th { padding: 7px 0 }
    .el-input-number .el-input__inner { text-align: left; }
  }
  .search-box { .el-form-item { margin-right: 0; margin-bottom: 20px!important; width: 30%; float: left; } }
</style>
<style lang="scss" scoped type="text/scss">
  .el-table{
    color:#333
  }
  .appManage,.fit { height: 100%; }
  .title{height:32px;color:$main-ft-color;font-size:18px;}
  .description { margin: 10px 0 20px; padding: 20px; color: $main-ft-color; background-color: $description-color; }
  .left-part { padding: 20px 25px 0 25px; box-sizing: border-box; height: 100%; @include flexbox-display-flex(); }
  .box-card { @include rightMenu(); }
  .full-width { width: 100%; }
  .el-form-item { margin-bottom: 16px; }
  .flex-content { overflow: auto; @include flexbox-display-flex(); @include flexbox-flex(); @include beautifyScrollbar();}
  .pos-add { position: absolute; top: 0; right: -18px; }
  .icon-width { width: 20px; display: inline-block; }
  .btn-flex { cursor: pointer;
    &:hover { color: $color-info }
  }
  .flex-item { display: inline-block; width: 90px; line-height: 40px; text-align: right; margin-right: 10px; }
  .flex-width { width: 320px; box-sizing: border-box; }
  .pagination { padding: 20px 0; text-align: center; }
  .color-info { color: $color-info!important; }
  .color-loading { color: $color-info!important; }
  .color-success { color: $color-success!important; }
  .color-warning { color: $color-warning!important; }
  .color-error { color: $color-error!important; }
  .color-disabled { color: $color-disabled!important; }
  .mg-r2 { margin-right: -20px; }
  .slider-detail { margin-bottom: 10px; padding-left: 80px; }
  .slider-unit { margin: 0 15px; color: $color-info; font-weight: bold; }
  .describe { color: $text-l2-color; margin-top: 10px }
  .showFile{height: 300px; display: block}
  .onShowFile{display: none}
  .disable{pointer-events:none; color:$color-disabled}
</style>
