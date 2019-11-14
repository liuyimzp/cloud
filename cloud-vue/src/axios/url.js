export default {
  // 用户相关
  login: '/api/user/login.do',
  logout: '/api/user/logout.do',
  changePwd: '/api/user/changePwd.do',
  getUser: '/api/user/getUser.do',
  getUsers: '/api/user/getUsers.do',
  userSave: '/api/user/save.do',
  userRemove: '/api/user/remove.do',
  showPlatformInfo: '/api/overview/showPlatformInfo.do',
  getPodsSum: '/api/overview/getPodsSum.do',

  // 容器集群管理
  clusterGetAll: '/api/cluster/get/all.do',
  clusterAdd: '/api/cluster/add.do',
  clusterUpdate: '/api/cluster/update.do',
  clusterCheckIdentifyRepeat: '/api/cluster/checkIdentifyRepeat.do',
  clusterDelete: '/api/cluster/delete.do',
  checkHAIp: '/api/cluster/checkHAIp.do',

  // 节点配置
  findClusterNodes: '/api/node/findClusterNodes.do',
  nodeTest: '/api/node/preCheck.do',
  nodeSave: '/api/node/save.do',
  nodeDelete: '/api/node/delete.do',
  nodeUpdate: '/api/node/update.do',
  refreshInfo: '/api/cluster/refresh/resource.do',
  setNodeState: '/api/node/toggleNode.do',
  reInitNode: '/api/node/reInitNode/fully.do',
  setNodeAsChild: '/api/node/setNodeAsChild.do',
  initStep: '/api/node/initStep.do',
  fromFailed: '/api/node/reInitNode/fromFailed.do',
  findResource: '/api/node/findResourceDetails.do',
  operateStack: '/api/node/operateStack.do',

  //ceph集群
  cephClusterGetAll: '/api/cephCluster/get/all.do',
  cephClusterGetAllByClusterId: '/api/cephCluster/get/allDev.do',
  cephClusterCheck: '/api/cephCluster/check.do',
  cephClusterAdd: '/api/cephCluster/save.do',
  cephClusterUpdate: '/api/cephCluster/update.do',
  cephClusterDelete: '/api/cephCluster/delete.do',
  refreshCephInfo: '/api/cephCluster/refreshCephInfo.do',
  createFsName: '/api/cephCluster/createFsName.do',

  //ceph节点配置
  SaveCephNode: '/api/ceph/addCephNode.do',
  getAllCephNode: '/api/ceph/getAllCephNode.do',
  cephNodeTest: '/api/ceph/cephNodeTest.do',
  cephNodeDelete: '/api/ceph/deleteCephNode.do',
  stopOsdNode: '/api/ceph/stopOsdNode.do',
  nodeToMds: '/api/ceph/nodeToMds.do',
  getAllCephPool: '/api/cephPool/getAll.do',
  SaveCephPool: '/api/cephPool/saveCephPool.do',
  poolUpdate: '/api/cephPool/updatePool.do',
  cephPoolDelete: '/api/cephPool/cephPoolDelete.do',

  // 存储管理
  pvQueryAll: '/api/pv/queryAll.do',
  pvDetail: '/api/pv/detail.do',
  addPv: '/api/pv/addPv.do',
  deletePv: '/api/pv/deletePv.do',
  updatePv: '/api/pv/updatePv.do',
  getDiskMounts: '/api/pv/getDiskMounts.do',
  checkStorage: '/api/pv/checkStorage.do',
  stopNfs: '/api/pv/stopNfs.do',
  startNfs: '/api/pv/startNfs.do',
  addStorage: '/api/pv/addStorage.do',
  queryAllStorage: '/api/pv/queryAllStorage.do',
  deleteStorage: '/api/pv/deleteStorage.do',
  deleteStorageForce: '/api/pv/deleteStorageForce.do',

  // 脚本管理
  shellGetAll: '/api/shell/get/all.do',
  shellSave: '/api/shell/save.do',
  shellEdit: '/api/shell/edit.do',
  shellDelete: '/api/shell/delete.do',
  shellUid: '/api/shell/init/uid.do',
  shellType: '/api/shell/init/type.do',

  // 应用仓库管理
  appRepertoryGetAll: '/api/appRepertory/getAll.do',
  appRepertoryCheckIdentify: '/api/appRepertory/checkIdentify.do',
  appRepertorySave: '/api/appRepertory/save.do',
  appRepertoryEdit: '/api/appRepertory/edit.do',
  appRepertoryRemove: '/api/appRepertory/remove.do',
  appBusinessCode: '/api/appRepertory/getUserBusinessArea.do',
  appCode: '/api/appCode/getList.do',
  appGroupGetAll: '/api/appGroup/getAll.do',
  appGroupSave: '/api/appGroup/save.do',
  addBusinessArea: '/api/appRepertory/addBusinessArea.do',

  //全局变量管理
  getSysPropertyAll: '/api/sysConfig/listPage.do',
  sysSave: '/api/sysConfig/save.do',
  sysRemove: '/api/sysConfig/delete.do',
  sysEdit: '/api/sysConfig/update.do',

  // 应用镜像管理
  appImageGetAll: '/api/appImage/getAll.do',
  appImageUpload: '/api/appImage/upload.do',
  appImageRemove: '/api/appImage/remove.do',
  downloadImage: '/api/appImage/downloadImage.do',
  sshDownloadImage: '/api/appImage/sshDownloadImage.do',
  checkFile: '/api/appImage/checkFile.do',
  downloadExcete: '/api/appImage/downloadExcete.do',
  createDiyImage: '/api/diyImage/createDiyImage.do',
  uploadDiyImage: '/api/diyImage/upload.do',
  saveDiyImage: '/api/diyImage/save.do',

  //jenkins管理
  getJenkinsUrl: '/api/jenkins/getJenkinsUrl.do',

  // 应用管理
  appGetAll: '/api/app/getAll.do',
  getAllClusters: '/api/app/getAllClusters.do',
  addAppGroup: '/api/app/addAppGroup.do',
  checkAppIdentify: '/api/app/checkAppIdentify.do',
  appSave: '/api/app/save.do',
  appDiySave: '/api/app/appDiySave.do',
  filePreview: '/api/app/filePreview.do',
  appConfigUpload: '/api/configMap/appConfigUpload.do',
  appDiyUpload: '/api/app/appDiyUpload.do',
  appEdit: '/api/app/edit.do',
  appRemove: '/api/app/remove.do',
  saveMiddleWare: '/api/app/saveMiddleWare.do',
  appStart: '/api/app/start.do',
  downloadConfig: '/api/configMap/downloadConfigMap.do',
  appStop: '/api/app/stop.do',
  backUp: '/api/app/backUp.do',
  getAllBackUp: '/api/app/getAllBackUp.do',
  deleBackUp: '/api/app/deleBackUp.do',
  reductionBackUp: '/api/app/reductionBackUp.do',
  appFlex: '/api/app/flex.do',
  createYaml: '/api/app/createYaml.do',
  getYaml: '/api/app/getYaml.do',
  getNameSpace: '/api/namespace/get.do',
  saveNameSpace: '/api/namespace/save.do',
  getAllNameSpace: '/api/namespace/getAll.do',
  getNameSpaceByUser: '/api/namespace/get.do',
  getPodInfo: '/api/app/getPodInfo.do',
  getRunInfo: '/api/app/getRunInfo.do',
  getPodLog: '/api/app/getPodLog.do',
  getAllVersion: '/api/appVersion/getAll.do',
  backTheVersion: '/api/appVersion/backVersion.do',
  getVersionImages: '/api/appVersion/getVersionImages.do',
  saveAppVersion: '/api/appVersion/saveAppVersion.do',
  deleteVersion: '/api/appVersion/deleteVersion.do',

  // 资源配置
  getAppConfig: '/api/appConfig/get.do',
  getClusterInfo: '/api/appConfig/getClusterInfo.do',
  getAllAppImages: '/api/appConfig/getAllAppImages.do',
  getMiddleWareImages: '/api/appConfig/getMiddleWareImages.do',
  getMiddleWareDefaultConfig: '/api/appConfig/getMiddleWareDefaultConfig.do',
  saveAppConfig: '/api/appConfig/saveAppConfig.do',
  saveJobConfig: '/api/appConfig/saveJobConfig.do',

  //configMap 配置
  getConfigMap: '/api/configMap/get.do',
  saveConfigMap: '/api/configMap/save.do',
  editConfigMap: '/api/configMap/edit.do',
  removeConfigMap: '/api/configMap/remove.do',
  showConfigMap: '/api/configMap/show.do',
  createConfigMap: '/api/configMap/create.do',

  // 环境配置 存储
  getPVs: '/api/appPV/getPVs.do',
  savePV: '/api/appPV/savePV.do',
  removePV: '/api/appPV/removePV.do',
  checkPath: '/api/appPV/check/container/path.do',

  // 服务暴露配置
  appServiceGet: '/api/appService/get.do',
  appServiceSave: '/api/appService/save.do',
  appServiceEdit: '/api/appService/edit.do',
  appServiceRemove: '/api/appService/remove.do',

  // 环境参数配置
  appEnvGet: '/api/appEnv/getAppEnvs.do',
  appEnvSave: '/api/appEnv/save.do',
  appEnvEdit: '/api/appEnv/edit.do',
  appEnvRemove: '/api/appEnv/remove.do',

  // 人员权限管理
  getAllAppAuthoritys: '/api/userAuthority/getAllAppAuthoritys.do',
  getAllImageAuthoritys: '/api/userAuthority/getAllImageAuthoritys.do',
  saveAuthoritys: '/api/userAuthority/saveAuthoritys.do',

  // 定时任务配置
  getAllJob: '/api/monitorConfig/getAllJob.do',
  addMonitorJob: '/api/monitorConfig/addMonitorJob.do',
  pauseJob: '/api/monitorConfig/pauseJob.do',
  resumeJob: '/api/monitorConfig/resumeJob.do',
  removeJob: '/api/monitorConfig/removeJob.do',
  getAllClusterMonitorInfo: '/api/monitor/cluster/getAllClusterMonitorInfo.do',
  getNodeMonitor: '/api/monitor/node/getNodeMonitor.do',

  //命名空间
  namespaceGet: '/api/namespace/get.do',
  namespaceSave: '/api/namespace/save.do',
  namespaceEdit: '/api/namespace/edit.do',
  namespaceDelete: '/api/namespace/delete.do',
  //监控管理
  getMaster: '/api/monitor/cluster/getMaster.do',
  monitorProxy: '/api/monitor/cluster/monitorProxy.do',
  //jenkins管理
  getAllJenkinsJob: '/api/jenkins/getAllJenkinsJob.do',

}

