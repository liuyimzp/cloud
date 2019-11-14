import { fetchPost,fetchGet } from "./fetch";
import api from './url';

// 用户相关
export function login(param) {
  return fetchPost(api.login,param)
}
export function logout(param) {
  return fetchPost(api.logout,param)
}
export function getUser(param) {
  return fetchPost(api.getUser,param)
}
export function getUsers(param) {
  return fetchPost(api.getUsers,param)
}
export function userSave(param) {
  return fetchPost(api.userSave,param)
}
export function userRemove(param) {
  return fetchPost(api.userRemove,param)
}
export function showPlatformInfo(param) {
  return fetchPost(api.showPlatformInfo,param)
}
export function getPodsSum(param) {
  return fetchPost(api.getPodsSum,param)
}

// 容器集群管理
export function clusterGetAll(param) {
  return fetchPost(api.clusterGetAll,param)
}
export function clusterAdd(param) {
  return fetchPost(api.clusterAdd,param)
}
export function clusterUpdate(param) {
  return fetchPost(api.clusterUpdate,param)
}
export function clusterCheckIdentifyRepeat(param) {
  return fetchPost(api.clusterCheckIdentifyRepeat,param)
}
export function clusterDelete(param) {
  return fetchPost(api.clusterDelete,param)
}
export function checkHAIp(param) {
  return fetchPost(api.checkHAIp,param)
}

// 节点管理
export function findClusterNodes(param) {
  return fetchPost(api.findClusterNodes,param)
}
export function nodeTest(param) {
  return fetchPost(api.nodeTest,param)
}
export function nodeSave(param) {
  return fetchPost(api.nodeSave,param)
}
export function nodeDelete(param) {
  return fetchPost(api.nodeDelete,param)
}
export function nodeUpdate(param) {
  return fetchPost(api.nodeUpdate,param)
}
export function refreshInfo(param) {
  return fetchPost(api.refreshInfo,param)
}
export function setNodeState(param) {
  return fetchPost(api.setNodeState,param)
}
export function reInitNode(param) {
  return fetchPost(api.reInitNode,param)
}
export function setNodeAsChild(param) {
  return fetchPost(api.setNodeAsChild,param)
}
export function initStep(param) {
  return fetchPost(api.initStep,param)
}
export function fromFailed(param) {
  return fetchPost(api.fromFailed,param)
}export function testA(param) {
  return fetchPost(api.testA,param)
}
export function findResource(param) {
  return fetchPost(api.findResource,param)
}
export function operateStack(param) {
  return fetchPost(api.operateStack,param)
}

//ceph集群
export function cephClusterGetAll(param) {
  return fetchPost(api.cephClusterGetAll,param)
}
export function cephClusterGetAllDev(param) {
  return fetchPost(api.cephClusterGetAllByClusterId,param)
}
export function cephClusterCheck(param) {
  return fetchPost(api.cephClusterCheck,param)
}
export function cephClusterAdd(param) {
  return fetchPost(api.cephClusterAdd,param)
}
export function cephClusterUpdate(param) {
  return fetchPost(api.cephClusterUpdate,param)
}
export function cephClusterDelete(param) {
  return fetchPost(api.cephClusterDelete,param)
}
export function refreshCephInfo(param) {
  return fetchPost(api.refreshCephInfo,param)
}
export function createFsName(param) {
  return fetchPost(api.createFsName,param)
}

//ceph节点
export function cephNodeTest(param) {
  return fetchPost(api.cephNodeTest,param)
}
export function SaveCephNode(param) {
  return fetchPost(api.SaveCephNode,param)
}
export function getAllCephNode(param) {
  return fetchPost(api.getAllCephNode,param)
}
export function cephNodeDelete(param) {
  return fetchPost(api.cephNodeDelete,param)
}
export function stopOsdNode(param) {
  return fetchPost(api.stopOsdNode,param)
}
export function nodeToMds(param) {
  return fetchPost(api.nodeToMds,param)
}
export function getAllCephPool(param) {
  return fetchPost(api.getAllCephPool,param)
}
export function SaveCephPool(param) {
  return fetchPost(api.SaveCephPool,param)
}
export function poolUpdate(param) {
  return fetchPost(api.poolUpdate,param)
}
export function cephPoolDelete(param) {
  return fetchPost(api.cephPoolDelete,param)
}

// 脚本管理
export function shellGetAll(param) {
  return fetchGet(api.shellGetAll,param)
}
export function shellSave(param) {
  return fetchPost(api.shellSave,param)
}
export function shellEdit(param) {
  return fetchPost(api.shellEdit,param)
}
export function shellDelete(param) {
  return fetchPost(api.shellDelete,param)
}
export function shellUid(param) {
  return fetchGet(api.shellUid,param)
}
export function shellType(param) {
  return fetchGet(api.shellType,param)
}


// 应用仓库管理
export function appRepertoryGetAll(param) {
  return fetchPost(api.appRepertoryGetAll,param)
}
export function appRepertoryCheckIdentify(param) {
  return fetchPost(api.appRepertoryCheckIdentify,param)
}
export function appRepertorySave(param) {
  return fetchPost(api.appRepertorySave,param)
}
export function appRepertoryEdit(param) {
  return fetchPost(api.appRepertoryEdit,param)
}
export function appRepertoryRemove(param) {
  return fetchPost(api.appRepertoryRemove,param)
}
export function appCode(param) {
  return fetchPost(api.appCode,param)
}
export function appBusinessCode(param) {
  return fetchPost(api.appBusinessCode,param)
}
export function appGroupGetAll(param) {
  return fetchPost(api.appGroupGetAll,param)
}
export function appGroupSave(param) {
  return fetchPost(api.appGroupSave,param)
}
export function addBusinessArea(param) {
  return fetchPost(api.addBusinessArea,param)
}

//全局变量管理
export function getSysPropertyAll(param) {
  return fetchPost(api.getSysPropertyAll,param)
}
export function sysSave(param) {
  return fetchPost(api.sysSave,param)
}
export function sysRemove(param) {
  return fetchPost(api.sysRemove,param)
}
export function sysEdit(param) {
  return fetchPost(api.sysEdit,param)
}

// 应用镜像管理
export function appImageGetAll(param) {
  return fetchPost(api.appImageGetAll,param)
}
export function appImageUpload(param) {
  return fetchPost(api.appImageUpload,param, {timeout: 3600000})
}
export function appImageRemove(param) {
  return fetchPost(api.appImageRemove,param)
}
export function sshDownloadImage(param) {
  return fetchPost(api.sshDownloadImage,param)
}
export function checkFile(param) {
  return fetchPost(api.checkFile,param)
}
export function imageDownload(param) {
  return fetchPost(api.downloadImage,param)
}
export function createDiyImage(param) {
  return fetchPost(api.createDiyImage,param)
}
export function uploadDiyImage(param) {
  return fetchPost(api.uploadDiyImage,param)
}
export function saveDiyImage(param) {
  return fetchPost(api.saveDiyImage,param)
}

//jenkins管理
export function getJenkinsUrl(param) {
  return fetchPost(api.getJenkinsUrl,param)
}

// 应用管理
export function appGetAll(param) {
  return fetchPost(api.appGetAll,param)
}
export function getAllClusters(param) {
  return fetchPost(api.getAllClusters,param)
}
export function addAppGroup(param) {
  return fetchPost(api.addAppGroup,param)
}
export function checkAppIdentify(param) {
  return fetchPost(api.checkAppIdentify,param)
}
export function appSave(param) {
  return fetchPost(api.appSave,param)
}
export function appDiySave(param) {
  return fetchPost(api.appDiySave,param)
}
export function appDiyUpload(param) {
  return fetchPost(api.appDiyUpload,param)
}
export function filePreview(param) {
  return fetchPost(api.filePreview,param)
}
export function appConfigUpload(param) {
  return fetchPost(api.appConfigUpload,param)
}
export function appEdit(param) {
  return fetchPost(api.appEdit,param)
}
export function appRemove(param) {
  return fetchPost(api.appRemove,param)
}
export function saveMiddleWare(param) {
  return fetchPost(api.saveMiddleWare,param)
}
export function appStart(param) {
  return fetchPost(api.appStart,param)
}
export function fnAppDownloadConfig(param) {
  return fetchPost(api.downloadConfig,param)
}
export function appStop(param) {
  return fetchPost(api.appStop,param)
}
export function fnBackUp(param) {
  return fetchPost(api.backUp,param)
}
export function getAllBackUp(param) {
  return fetchPost(api.getAllBackUp,param)
}
export function deleBackUp(param) {
  return fetchPost(api.deleBackUp,param)
}
export function reductionBackUp(param) {
  return fetchPost(api.reductionBackUp,param)
}
export function appFlex(param) {
  return fetchPost(api.appFlex,param)
}
export function createYaml(param) {
  return fetchPost(api.createYaml,param)
}
export function getYaml(param) {
  return fetchPost(api.getYaml,param)
}
export function getNameSpace(param) {
  return fetchPost(api.getNameSpace,param)
}
export function saveNameSpace(param) {
  return fetchPost(api.saveNameSpace,param)
}
export function getAllNameSpace(param) {
  return fetchPost(api.getAllNameSpace,param)
}
export function getNameSpaceByUser(param) {
  return fetchPost(api.getNameSpaceByUser,param)
}
export function getPodInfo(param) {
  return fetchPost(api.getPodInfo,param)
}
export function getRunInfo(param) {
  return fetchPost(api.getRunInfo,param)
}
export function getPodLog(param) {
  return fetchPost(api.getPodLog,param)
}
export function getAllVersion(param) {
  return fetchPost(api.getAllVersion,param)
}

export function backTheVersion(param) {
  return fetchPost(api.backTheVersion,param)
}
export function getVersionImages(param) {
  return fetchPost(api.getVersionImages,param)
}
export function saveAppVersion(param) {
  return fetchPost(api.saveAppVersion,param)
}
export function deleteVersion(param) {
  return fetchPost(api.deleteVersion,param)
}

// 资源配置
export function getAppConfig(param) {
  return fetchPost(api.getAppConfig,param)
}
export function getClusterInfo(param) {
  return fetchPost(api.getClusterInfo,param)
}
export function getAllAppImages(param) {
  return fetchPost(api.getAllAppImages,param)
}
export function getMiddleWareImages(param) {
  return fetchPost(api.getMiddleWareImages,param)
}
export function getMiddleWareDefaultConfig(param) {
  return fetchPost(api.getMiddleWareDefaultConfig,param)
}
export function saveAppConfig(param) {
  return fetchPost(api.saveAppConfig,param)
}
export function saveJobConfig(param) {
  return fetchPost(api.saveJobConfig,param)
}
// 存储管理
export function pvQueryAll(param) {
  return fetchPost(api.pvQueryAll,param)
}
export function pvDetail(param) {
  return fetchPost(api.pvDetail,param)
}
export function addPv(param) {
  return fetchPost(api.addPv,param)
}
export function deletePv(param) {
  return fetchPost(api.deletePv,param)
}
export function updatePv(param) {
  return fetchPost(api.updatePv,param)
}
export function getDiskMounts(param) {
  return fetchPost(api.getDiskMounts,param)
}
export function checkStorage(param) {
  return fetchPost(api.checkStorage,param)
}
export function stopNfs(param) {
  return fetchPost(api.stopNfs,param)
}
export function startNfs(param) {
  return fetchPost(api.startNfs,param)
}
export function addStorage(param) {
  return fetchPost(api.addStorage,param)
}
export function queryAllStorage(param) {
  return fetchPost(api.queryAllStorage,param)
}
export function queryAllceph(param) {
  return fetchPost(api.cephClusterGetAll,param)
}
export function deleteStorage(param) {
  return fetchPost(api.deleteStorage,param)
}
export function deleteStorageForce(param) {
  return fetchPost(api.deleteStorageForce,param)
}

// 环境配置 存储
export function getPVs(param) {
  return fetchPost(api.getPVs,param)
}
export function savePV(param) {
  return fetchPost(api.savePV,param)
}
export function removePV(param) {
  return fetchPost(api.removePV,param)
}
export function checkPath(param) {
  return fetchPost(api.checkPath,param)
}

// 服务暴露配置
export function appServiceGet(param) {
  return fetchPost(api.appServiceGet,param)
}
export function appServiceSave(param) {
  return fetchPost(api.appServiceSave,param)
}
export function appServiceEdit(param) {
  return fetchPost(api.appServiceEdit,param)
}
export function appServiceRemove(param) {
  return fetchPost(api.appServiceRemove,param)
}

//configMap 配置
export function getConfigMap(param) {
  return fetchPost(api.getConfigMap,param)
}
export function saveConfigMap(param) {
  return fetchPost(api.saveConfigMap,param)
}
export function editConfigMap(param) {
  return fetchPost(api.editConfigMap,param)
}
export function removeConfigMap(param) {
  return fetchPost(api.removeConfigMap,param)
}
export function showConfigMap(param) {
  return fetchPost(api.showConfigMap,param)
}
export function createConfigMap(param) {
  return fetchPost(api.createConfigMap,param)
}

// 环境参数配置
export function appEnvGet(param) {
  return fetchPost(api.appEnvGet,param)
}
export function appEnvSave(param) {
  return fetchPost(api.appEnvSave,param)
}
export function appEnvEdit(param) {
  return fetchPost(api.appEnvEdit,param)
}
export function appEnvRemove(param) {
  return fetchPost(api.appEnvRemove,param)
}

// 人员权限管理
export function getAllAppAuthoritys(param) {
  return fetchPost(api.getAllAppAuthoritys,param)
}
export function getAllImageAuthoritys(param) {
  return fetchPost(api.getAllImageAuthoritys,param)
}
export function saveAuthoritys(param) {
  return fetchPost(api.saveAuthoritys,param)
}

// 定时任务配置
export function addMonitorJob(param) {
  return fetchPost(api.addMonitorJob,param)
}
export function getAllJob(param) {
  return fetchPost(api.getAllJob,param)
}
export function pauseJob(param) {
  return fetchPost(api.pauseJob,param)
}
export function resumeJob(param) {
  return fetchPost(api.resumeJob,param)
}
export function removeJob(param) {
  return fetchPost(api.removeJob,param)
}
export function getAllClusterMonitorInfo(param) {
  return fetchPost(api.getAllClusterMonitorInfo,param)
}
export function getNodeMonitor(param) {
  return fetchPost(api.getNodeMonitor,param)
}

export function getMaster(param) {
  return fetchPost(api.getMaster, param)
}
export function monitorProxy(param) {
  return fetchPost(api.monitorProxy, param)
}
export function namespaceGet(param) {
  return fetchPost(api.namespaceGet,param)
}
export function namespaceSave(param) {
  return fetchPost(api.namespaceSave,param)
}
export function namespaceEdit(param) {
  return fetchPost(api.namespaceEdit,param)
}
export function namespaceDelete(param) {
  return fetchPost(api.namespaceDelete,param)
}

//jenkins
export function getAllJenkinsJob(param) {
  return fetchPost(api.getAllJenkinsJob,param)
}
