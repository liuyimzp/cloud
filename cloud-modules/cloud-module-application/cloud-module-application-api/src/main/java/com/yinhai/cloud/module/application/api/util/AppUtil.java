package com.yinhai.cloud.module.application.api.util;

import com.yinhai.cloud.core.api.constant.ConfigPropKey;
import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.ssh.SlashPath;
import com.yinhai.cloud.core.api.ssh.command.AbstractCommand;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.UploadContentCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.application.api.bpo.*;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.kubernetes.base.BaseStatefulSet;
import com.yinhai.cloud.module.application.api.kubernetes.base.AppKubernetesYamlObject;
import com.yinhai.cloud.module.application.api.kubernetes.helper.AppYamlObjectFactory;
import com.yinhai.cloud.module.application.api.kubernetes.helper.PvcName;
import com.yinhai.cloud.module.application.api.util.sm4.SM4Utils;
import com.yinhai.cloud.module.application.api.vo.*;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.pv.api.bpo.IPersistentVolumeBpo;
import com.yinhai.cloud.module.resource.pv.api.constants.PvConstants;
import com.yinhai.cloud.module.resource.pv.api.vo.PvVo;
import com.yinhai.cloud.module.resource.pv.api.vo.StorageVo;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.sysframework.util.IConstants;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhaokai
 * @create: 2018-09-29 15:55:10
 */
public class AppUtil {
    private final static INamespaceBpo namespaceBpo = ServiceLocator.getService(INamespaceBpo.class);
    private final static IAppBpo appBpo = ServiceLocator.getService(IAppBpo.class);
    private final static IAppVersionBpo appVersionBpo = ServiceLocator.getService(IAppVersionBpo.class);
    private final static INodeBpo nodeBpo= ServiceLocator.getService(INodeBpo.class);
    private final static IPersistentVolumeBpo persistentVolumeBpo = ServiceLocator.getService(IPersistentVolumeBpo.class);
    private final static IConfigBackUpBpo configBackUpBpo = ServiceLocator.getService(IConfigBackUpBpo.class);
    private final static IConfigMapBpo configMapBpo = ServiceLocator.getService(IConfigMapBpo.class);
    private static final String FILE_SEPARATOR = "/";
    private static final String backupPath = SystemConfigUtil.getSystemConfigValue("backup.file.path");

    public static List<String> getDeletePvcCommandList(AppVo appVo) throws Exception {
        List<String> cmdList = new ArrayList<>();

        // 中间件
        AppKubernetesYamlObject yamlObject = AppYamlObjectFactory.createByApp(appVo);
        if (yamlObject instanceof BaseStatefulSet) {
            // 是StatefulSet 中间件
            BaseStatefulSet statefulSet = (BaseStatefulSet) yamlObject;
            List<PvcName> pvcNameList = statefulSet.getPvcNameList();
            for (PvcName n : pvcNameList) {
                for (int i = 0; i < n.getReplicas(); i++) {
                    cmdList.add("source ~/.bash_profile && kubectl delete pvc " + n.getPvcTemplateName() + "-" + n.getPodName() + "-" + i + " -n" + " " + appVo.getNamespaceIdentify());
                }
            }
        }
        return cmdList;
    }

    public static void applyApp(Long appId) throws Exception {
        AppVo appVo = appBpo.getApp(appId);
        ConnVo connVo = NodeUtil.getConnection(appVo.getClusterId());
        String serverYamlFilePath;
        String Yaml;
        if (IAppConstants.APP_TYPE_CUSTOM.equals(appVo.getAppType())){
            Yaml = appVo.getAppDiyFile();
        }else{
            AppKubernetesYamlObject yamlObject = AppYamlObjectFactory.createByApp(appVo);
            Yaml = yamlObject.buildYaml();
            if (IAppConstants.APP_TYPE_HPA.equals(appVo.getAppType())){
                Yaml += AppYamlObjectFactory.createHpa(appVo);
            }
        }
        List<AppVersionVo> versionVos = appVersionBpo.getUseVersion(appId);
        if (versionVos.isEmpty()){
            serverYamlFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT), FILE_SEPARATOR + IAppConstants.YAML_DIR_NAME + FILE_SEPARATOR + appVo.getNamespaceIdentify() + FILE_SEPARATOR + appVo.getAppIdentify() + ".yaml").toString();
        }else {
            AppVersionVo versionVo = versionVos.get(0);
            serverYamlFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT), FILE_SEPARATOR + IAppConstants.YAML_DIR_NAME + FILE_SEPARATOR + appVo.getNamespaceIdentify() + FILE_SEPARATOR + appVo.getAppIdentify() + versionVo.getVersionNameStr() + ".yaml").toString();
        }
        AbstractCommand uploadYaml = new UploadContentCommand(serverYamlFilePath, Yaml);
        ConsoleCommand updateKubeletRes = new ConsoleCommand();
        updateKubeletRes.appendCommand("source ~/.bash_profile && kubectl apply -f " + serverYamlFilePath);
        CommonSshExecUtil.exec(connVo, uploadYaml, updateKubeletRes);
        if (!versionVos.isEmpty()){
            AppVersionUtil.backVersion(versionVos.get(0));
        }
    }

    public static void stopApp(Long appId) throws Exception {
        AppVo appVo = appBpo.getApp(appId);
        ConnVo connVo = NodeUtil.getConnection(appVo.getClusterId());
        List<AppVersionVo> versionVos = appVersionBpo.getUseVersion(appId);
        String serverYamlFilePath;
        ConsoleCommand stopCommand = new ConsoleCommand();

        NamespaceVo namespaceVo = namespaceBpo.getNamespace(appVo.getNamespaceId());
        if (versionVos.isEmpty()){
            serverYamlFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT), FILE_SEPARATOR + IAppConstants.YAML_DIR_NAME + FILE_SEPARATOR + appVo.getNamespaceIdentify() + FILE_SEPARATOR + appVo.getAppIdentify() + ".yaml").toString();
        }else {
            AppVersionVo versionVo = versionVos.get(0);
            serverYamlFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT), FILE_SEPARATOR + IAppConstants.YAML_DIR_NAME + FILE_SEPARATOR + appVo.getNamespaceIdentify() + FILE_SEPARATOR + appVo.getAppIdentify() + versionVo.getVersionNameStr() + ".yaml").toString();
        }
        String Yaml;
        boolean isStatefulSet = false;
        if (IAppConstants.APP_TYPE_CUSTOM.equals(appVo.getAppType())){
            Yaml = appVo.getAppDiyFile();
        }else{
            AppKubernetesYamlObject appYaml = AppYamlObjectFactory.createByApp(appVo);
            Yaml = appYaml.buildYaml();
            isStatefulSet = appYaml instanceof BaseStatefulSet;
        }
        AbstractCommand uploadYaml = new UploadContentCommand(serverYamlFilePath, Yaml);
        String resourceType = "deployment";
        String notJob = " -n " + namespaceVo.getNamespaceIdentify();
        if (isStatefulSet) {
            resourceType = "StatefulSet";
        }
        if (ValidateUtil.isNotEmpty(appVo.getAppRestartPolicy())){
            resourceType = "Job";
            notJob = "";
        }
        if (versionVos.isEmpty()){
            stopCommand.appendCommand("source ~/.bash_profile && kubectl scale " + resourceType + " " + appVo.getAppIdentify() + " --replicas 0" + notJob);
        }else {
            AppVersionVo versionVo = versionVos.get(0);
            stopCommand.appendCommand("source ~/.bash_profile && kubectl scale " + resourceType + " " + appVo.getAppIdentify() + versionVo.getVersionNameStr() + " --replicas 0" + notJob);
        }
        CommonSshExecUtil.exec(connVo, uploadYaml, stopCommand);
    }

    public static void removeApp(Long appId) throws Exception {
        AppVo appVo = appBpo.getApp(appId);
        ConnVo connVo = NodeUtil.getConnection(appVo.getClusterId());
        String serverYamlFilePath;
        String destinationYamlFilePath;
        List<AppVersionVo> versionVos = appVersionBpo.getUseVersion(appId);
        if (versionVos.isEmpty()){
            serverYamlFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT), FILE_SEPARATOR + IAppConstants.YAML_DIR_NAME + FILE_SEPARATOR + appVo.getNamespaceIdentify() + FILE_SEPARATOR + appVo.getAppIdentify() + ".yaml").toString();
        }else {
            AppVersionVo versionVo = versionVos.get(0);
            serverYamlFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT), FILE_SEPARATOR + IAppConstants.YAML_DIR_NAME + FILE_SEPARATOR + appVo.getNamespaceIdentify() + FILE_SEPARATOR + appVo.getAppIdentify() + versionVo.getVersionNameStr() + ".yaml").toString();
        }
        AbstractCommand uploadYaml;
        ConsoleCommand removeCommand = new ConsoleCommand();
        if (!"3".equals(appVo.getAppType())){
            AppKubernetesYamlObject appYaml = AppYamlObjectFactory.createByApp(appVo);
            if (IAppConstants.APP_TYPE_HPA.equals(appVo.getAppType())){
                uploadYaml = new UploadContentCommand(serverYamlFilePath, appYaml.buildYaml() + AppYamlObjectFactory.createHpa(appVo));
            }else {
                uploadYaml = new UploadContentCommand(serverYamlFilePath, appYaml.buildYaml());
            }
            boolean isStatefulSet = appYaml instanceof BaseStatefulSet;
            if (isStatefulSet) {
                // 如果应用正在运行，则需要删除pvc
                List<String> deletePvcCommandList = AppUtil.getDeletePvcCommandList(appVo);
                for (String c : deletePvcCommandList) {
                    removeCommand.appendCommand(c);
                }
            }
        }else{
            uploadYaml = new UploadContentCommand(serverYamlFilePath,appVo.getAppDiyFile());
        }
        removeCommand.appendCommand("source ~/.bash_profile && kubectl delete -f " + serverYamlFilePath);
        if (!versionVos.isEmpty()){
            destinationYamlFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT), FILE_SEPARATOR + IAppConstants.YAML_DIR_NAME + FILE_SEPARATOR + appVo.getNamespaceIdentify() + FILE_SEPARATOR + "DestinationRule" + FILE_SEPARATOR + appVo.getAppIdentify() + "DRVS.yaml").toString();
            removeCommand.appendCommand("source ~/.bash_profile && kubectl delete -f " + destinationYamlFilePath);
        }
        try {
            CommonSshExecUtil.exec(connVo, uploadYaml, removeCommand);
        } catch (SSHExecuteException | SSHConnectionException ignored) {


        }


    }


    public static void flexApp(Long appId, Integer newReplicas) throws Exception {
        AppVo appVo = appBpo.getApp(appId);
        if (!ValidateUtil.areEqual(IAppConstants.APP_STATUS_RUNNING, appVo.getAppStatus()) && !ValidateUtil.areEqual(IAppConstants.APP_STATUS_STOP, appVo.getAppStatus())) {
            // 应用未处于运行中，或者已停止状态，不需要删除pvc
            return;
        }
        ConnVo connVo = NodeUtil.getConnection(appVo.getClusterId());
        AppKubernetesYamlObject appYaml = AppYamlObjectFactory.createByApp(appVo);
        ConsoleCommand scaleCommand = new ConsoleCommand();
        NamespaceVo namespaceVo = namespaceBpo.getNamespace(appVo.getNamespaceId());
        boolean isStatefulSet = appYaml instanceof BaseStatefulSet;
        if (isStatefulSet) {
            BaseStatefulSet statefulSet = (BaseStatefulSet) appYaml;
            List<PvcName> pvcNameList = statefulSet.getPvcNameList();
            for (PvcName p : pvcNameList) {
                int oldNum = p.getReplicas();
                boolean replicasReduce = oldNum > newReplicas;// 实例数减少
                if (replicasReduce && p.isAllowFlexDelete()) {
                    // 删除多余PVC
                    // 6 (0~-5) -> 4(0~3)，移除 4,5
                    for (int i = oldNum - 1; i > newReplicas - 1; i--) {
                        scaleCommand.appendCommand("source ~/.bash_profile && kubectl delete pvc " + p.getPvcTemplateName() + "-" + p.getPodName() + "-" + i + " -n" + " " + namespaceVo.getNamespaceIdentify());

                    }

                }
            }

        }
        if(ValidateUtil.areEqual(IAppConstants.APP_STATUS_RUNNING, appVo.getAppStatus())){
            // 应用处于运行中，才进行scale操作
            scaleCommand.appendCommand("source ~/.bash_profile && kubectl scale " + (isStatefulSet ? "statefulset" : "deployment") + " " + appVo.getAppIdentify() + " --replicas " + newReplicas + " -n " + namespaceVo.getNamespaceIdentify());
        }

        CommonSshExecUtil.exec(connVo, scaleCommand);


    }


    /**
     * 将时间替换为常用如：h换为小时
     * @param time
     * @return
     */
    public static String getTime(String time){
        String num = time.substring(0,time.length()-1);
        String houzui = time.substring(time.length()-1);
        String nowTime = "";
        switch (houzui) {
            case "s":
                nowTime = num + "秒";
                break;
            case "m":
                nowTime = num + "分钟";
                break;
            case "h":
                nowTime = num + "小时";
                break;
            case "d":
                nowTime = num + "天";
                break;
            case "M":
                nowTime = num + "月";
                break;
            case "y":
                nowTime = num + "年";
                break;
        }
        return nowTime;
    }

    /**
     * 中间件容器数据备份
     * @param appVo
     */
    public static void backUpContainer(AppVo appVo) throws Exception{
        ConfigBackUpVo configVo = new ConfigBackUpVo();
        ConnVo connVo;
        Date now = new Date();
        configVo.setBackup_file_path(String.valueOf(now.getTime()));
        String identify = appVo.getAppIdentify();
        configVo.setAppId(appVo.getId());
        configVo.setCreateTime(now);
        configVo.setIsConfig("0");
        configVo.setBackup_ismount("0");
        configVo.setIsUse("0");
        configVo.setInstanceNum(appVo.getInstanceNum());
        //判断是否有配置文件有则备份
        if (ValidateUtil.isNotEmpty(appVo.getMiddleware_configfile())){
            //获得节点运行管理节点
            NodeBasicInfoVo node = nodeBpo.queryNodeInfoById(appVo.getNodeId());
            connVo= NodeUtils.createConnFromNode(node);
            configVo.setIsConfig("1");
            String configKey = "config." + getSysConfigKey(appVo.getMiddleWareType());
            if (ValidateUtil.isEmpty(getSysConfigKey(appVo.getMiddleWareType()))){
                throw new Exception("当前版本只支持中间件备份");
            }
            String configPath = SystemConfigUtil.getSystemConfigValue(configKey);
            ConsoleCommand command = new ConsoleCommand();
            command.appendCommand("mkdir -p " + backupPath + "/" + identify + "/" + now.getTime() + " && cp " + configPath + "/" + identify + ".conf " + backupPath + "/" + identify + "/" + now.getTime());
            CommonSshExecUtil.exec(connVo,command);
        }else {
            connVo = NodeUtil.getConnection(appVo.getClusterId());
        }
        //查找到应用绑定pv用于寻找文件挂载目录
        List<PvVo> pvVos = persistentVolumeBpo.getPvByAppId(appVo.getId());
        if (pvVos.isEmpty()){
            throw new Exception("该应用未挂载存储");
        }
        PvVo pvVo = pvVos.get(0);
        if (PvConstants.PV_TYPE_CEPH.equals(pvVo.getVolumeType())){
            throw new Exception("Ceph存储占时不可备份");
        }
        //获取备份文件所在机器以及文件夹并备份
        StorageVo storageVo = persistentVolumeBpo.getStorageById(pvVo.getVolumeCloudStorageId());
        if (storageVo == null){
            throw new Exception("没找到对应云目录");
        }
        ConnVo storageConn = NodeUtils.createConnFromStotageVo(storageVo);
        // 中间件
        AppKubernetesYamlObject yamlObject = AppYamlObjectFactory.createByApp(appVo);
        if (yamlObject instanceof BaseStatefulSet) {
            // 是StatefulSet 中间件
            configVo.setBackup_ismount("1");
            BaseStatefulSet statefulSet = (BaseStatefulSet) yamlObject;
            List<PvcName> pvcNameList = statefulSet.getPvcNameList();
            for (PvcName n : pvcNameList) {
                for (int i = 0; i < n.getReplicas(); i++) {
                    ConsoleCommand command1 = new ConsoleCommand();
                    command1.appendCommand("kubectl get pvc -n " + appVo.getNamespaceIdentify() + " |grep " + n.getPvcTemplateName() + "-" + n.getPodName() + "-" + i);
                    MsgVO msgVO = CommonSshExecUtil.exec(connVo, command1).get(command1);
                    if (msgVO.isSuccess()) {
                        String sout = msgVO.getSysoutMsg();
                        String[] nodeStatusStrs = sout.split("\\n");
                        String [] pvcStr = nodeStatusStrs[0].split("\\s+");
                        if (pvcStr.length != 7){
                            throw new Exception("pvc启动失败不能备份");
                        }
                        ConsoleCommand commandCopy = new ConsoleCommand();
                        commandCopy.appendCommand("mkdir -p " + backupPath + "/" + identify + "/" + now.getTime() + " && cp -a " + pvVo.getVolumeStoragePath() + "/" + appVo.getNamespaceIdentify() + "-" + pvcStr[0] + "-" + pvcStr[2] + " " +  backupPath + "/" + identify + "/" + now.getTime())
                            .appendCommand("touch " + backupPath + "/" + identify + "/" + now.getTime() + "/" + appVo.getNamespaceIdentify() + "-" + pvcStr[0] + "-" + pvcStr[2] + "/probe");
                        CommonSshExecUtil.exec(storageConn,commandCopy);
                    }
                }
            }
        }
        //将数据放入数据库
        configBackUpBpo.saveConfig(configVo);
    }

    /**
     * 删除备份文件
     * @param vo
     */
    public static void deleteBackUpFile(ConfigBackUpVo vo) throws Exception{
        AppVo appVo = appBpo.getApp(vo.getAppId());
        //判断是否有配置文件存在则删除
        if (IAppConstants.BACK_UP_CONFIG.equals(vo.getIsConfig())){
            NodeBasicInfoVo node = nodeBpo.queryNodeInfoById(appVo.getNodeId());
            ConnVo connVo= NodeUtils.createConnFromNode(node);
            if (ValidateUtil.isEmpty(getSysConfigKey(appVo.getMiddleWareType()))){
                throw new Exception("当前版本只支持中间件备份");
            }
            ConsoleCommand command = new ConsoleCommand();
            command.appendCommand("rm -rf " + backupPath + "/" + appVo.getAppIdentify() + "/" + vo.getBackup_file_path());
            CommonSshExecUtil.exec(connVo,command);
        }
        //是否挂载有数据文件存在则删除
        if ("1".equals(vo.getBackup_ismount())){
            List<PvVo> pvVos = persistentVolumeBpo.getPvByAppId(appVo.getId());
            PvVo pvVo = pvVos.get(0);
            if (PvConstants.PV_TYPE_CEPH.equals(pvVo.getVolumeType())){
                throw new Exception("Ceph存储占时不可备份");
            }
            //获取备份文件所在机器以及文件夹并备份
            StorageVo storageVo = persistentVolumeBpo.getStorageById(pvVo.getVolumeCloudStorageId());
            if (storageVo == null){
                throw new Exception("没找到对应云目录");
            }
            ConnVo storageConn = NodeUtils.createConnFromStotageVo(storageVo);
            ConsoleCommand command = new ConsoleCommand();
            command.appendCommand("rm -rf " + backupPath + "/" + appVo.getAppIdentify() + "/" + vo.getBackup_file_path());
            CommonSshExecUtil.exec(storageConn,command);
        }
    }

    /**
     * 将当前备份的文件替换掉以前的
     * @param vo
     */
    public static void reductionConfigBackUp(ConfigBackUpVo vo) throws Exception{
        AppVo appVo = appBpo.getApp(vo.getAppId());
        String identify = appVo.getAppIdentify();
        ConnVo connVo;
        //判断是否有配置文件有则备份
        if (ValidateUtil.isNotEmpty(appVo.getMiddleware_configfile())){
            //获得节点运行管理节点
            NodeBasicInfoVo node = nodeBpo.queryNodeInfoById(appVo.getNodeId());
            connVo= NodeUtils.createConnFromNode(node);
            String configKey = "config." + getSysConfigKey(appVo.getMiddleWareType());
            if (ValidateUtil.isEmpty(getSysConfigKey(appVo.getMiddleWareType()))){
                throw new Exception("当前版本只支持中间件备份");
            }
            String configPath = SystemConfigUtil.getSystemConfigValue(configKey);
            ConsoleCommand command = new ConsoleCommand();
            command.appendCommand("\\cp -af " + backupPath + "/" + identify + "/" + vo.getBackup_file_path() + "/" + identify + ".conf " +  configPath);
            ConsoleCommand ncommand = new ConsoleCommand();
            ncommand.appendCommand("cat " + backupPath + "/" + identify + "/" + vo.getBackup_file_path() + "/" + identify + ".conf");
            MsgVO msgVO = CommonSshExecUtil.exec(connVo,command,ncommand).get(ncommand);
            msgVO.getSysoutMsg();
            appVo.setMiddleware_configfile(msgVO.getSysoutMsg());
            appBpo.editApp(appVo);
        }else {
            connVo = NodeUtil.getConnection(appVo.getClusterId());
        }
        //查找到应用绑定pv用于寻找文件挂载目录
        List<PvVo> pvVos = persistentVolumeBpo.getPvByAppId(appVo.getId());
        if (pvVos.isEmpty()){
            throw new Exception("该应用未挂载存储");
        }
        PvVo pvVo = pvVos.get(0);
        if (PvConstants.PV_TYPE_CEPH.equals(pvVo.getVolumeType())){
            throw new Exception("Ceph存储占时不可备份");
        }
        //获取备份文件所在机器以及文件夹并备份
        StorageVo storageVo = persistentVolumeBpo.getStorageById(pvVo.getVolumeCloudStorageId());
        if (storageVo == null){
            throw new Exception("没找到对应云目录");
        }
        ConnVo storageConn = NodeUtils.createConnFromStotageVo(storageVo);
        // 中间件
        AppKubernetesYamlObject yamlObject = AppYamlObjectFactory.createByApp(appVo);
        if (yamlObject instanceof BaseStatefulSet) {
            // 是StatefulSet 中间件
            BaseStatefulSet statefulSet = (BaseStatefulSet) yamlObject;
            List<PvcName> pvcNameList = statefulSet.getPvcNameList();
            for (PvcName n : pvcNameList) {
                for (int i = 0; i < n.getReplicas(); i++) {
                    ConsoleCommand command1 = new ConsoleCommand();
                    command1.appendCommand("kubectl get pvc -n " + appVo.getNamespaceIdentify() + " |grep " + n.getPvcTemplateName() + "-" + n.getPodName() + "-" + i);
                    MsgVO msgVO = CommonSshExecUtil.exec(connVo, command1).get(command1);
                    if (msgVO.isSuccess()) {
                        String sout = msgVO.getSysoutMsg();
                        String[] nodeStatusStrs = sout.split("\\n");
                        String [] pvcStr = nodeStatusStrs[0].split("\\s+");
                        ConsoleCommand commandCopy = new ConsoleCommand();
                        commandCopy.appendCommand("rm -rf " + pvVo.getVolumeStoragePath() + "/" + appVo.getNamespaceIdentify() + "-" + pvcStr[0] + "-" + pvcStr[2] + "/* && \\cp -af " + backupPath + "/" + identify + "/" + vo.getBackup_file_path() + "/" + appVo.getNamespaceIdentify() + "-" + pvcStr[0] + "-" + pvcStr[2] + "/* " + pvVo.getVolumeStoragePath() + "/" + appVo.getNamespaceIdentify() + "-" + pvcStr[0] + "-" + pvcStr[2] + "");
                        CommonSshExecUtil.exec(storageConn,commandCopy);
                    }
                }
            }
        }
        ConsoleCommand getNodeCommand = new ConsoleCommand();
        getNodeCommand.appendCommand("kubectl get pods -n " + appVo.getNamespaceIdentify() + " -o wide |grep " + appVo.getAppIdentify() + "|awk '{print $7}'");
        MsgVO msgVo1 = CommonSshExecUtil.exec(storageConn,getNodeCommand).get(getNodeCommand);
        if (msgVo1.isSuccess()){
            String[] nodeNames = msgVo1.getSysoutMsg().split("\\n");
            try {
                restartContainer(nodeNames,appVo);
            }catch (Exception e){
                throw new Exception(e.getMessage());
            }
        }
    }

    /**
     *将分配了对应应用的节点重启相对应用的容器
     * @param nodeNames appVo
     */
    private static void restartContainer(String[] nodeNames,AppVo appVo) throws Exception{
        List<NodeBasicInfoVo> nodeBasicInfoVos = nodeBpo.queryNodesByClusterId(appVo.getClusterId()).stream().filter(node -> ArrayUtils.contains(nodeNames,node.getNodeName())).collect(Collectors.toList());
        if (nodeBasicInfoVos.isEmpty()){
            throw new Exception("没查询到node");
        }
        for (NodeBasicInfoVo nodeBasicInfoVo : nodeBasicInfoVos){
            ConnVo connVo = NodeUtils.createConnFromNode(nodeBasicInfoVo);
            ConsoleCommand command = new ConsoleCommand();
            command.appendCommand("docker ps |grep " + appVo.getAppIdentify() + "|grep -v POD|awk '{print $1}'|xargs docker container restart");
            CommonSshExecUtil.exec(connVo,command);
        }
    }

    public static String getSysConfigKey(String appMiddlewareType){
        StringBuilder configPath = new StringBuilder();
        switch (appMiddlewareType){
            case "1":
                configPath.append("zookeeper");
                break;
            case "2":
                configPath.append("redis");
                break;
            case "3":
                configPath.append("mysql");
                break;
            case "4":
                configPath.append("activemq");
                break;
            case "5":
                configPath.append("oracle");
                break;
            case "6":
                configPath.append("nginx");
                break;
            case "7":
                configPath.append("kafka");
                break;
            case "8":
                configPath.append("elasticsearch");
                break;
            default:return null;
        }
        return configPath.toString();
    }

    public static String getStringToList(List<ConfigMapVo> list) {
        if (list.isEmpty()){
            return "该应用没有配置参数！";
        }
        StringBuilder mapStr = new StringBuilder("key : value : isEnvironment (0 no , 1 yes) : isHidden : (0 no , 1 yes)\n");
        for (ConfigMapVo cvo : list){
            AppVo appVo = appBpo.getApp(cvo.getAppId());
            mapStr.append(cvo.getPropertyName())
                    .append(" : ")
                    .append(IConstants.COMMON_YES.equals(cvo.getEncrypt())?SM4Utils.decrypt(cvo.getPropertyValue()):cvo.getPropertyValue())
                    .append(" : ")
                    .append(cvo.getIsEnv())
                    .append(" : ")
                    .append(cvo.getEncrypt()).append("\n");
        }
        return mapStr.toString();
    }

    /**
     * 将配置信息写入数据库
     * @param reqStr
     * @param id
     * @return
     * @throws Exception
     */
    public static String setAppConfig(String reqStr,Long id) throws Exception{
        if (ValidateUtil.isEmail(reqStr)){
            throw new Exception("文件中没有值");
        }
        String [] configStrs = reqStr.split("\\n");
        StringBuilder errStr = new StringBuilder();
        List<String> list = configMapBpo.getConfigMapByAppId(id).stream().map(cvo -> cvo.getPropertyName()).collect(Collectors.toList());
        for (int num = 1; num < configStrs.length; num++){
            String [] item = configStrs[num].split(":");
            if (item.length != 4){
                errStr.append("\n第" + (num + 1) + "行  " + configStrs[num] + "  数据格式可能有误\n");
                continue;
            }
            if (list.contains(item[0].trim())){
                errStr.append("\n第" + (num + 1) + "行  " + configStrs[num] + "  key已经存在\n");
                continue;
            }
            ConfigMapVo vo = new ConfigMapVo();
            vo.setAppId(id);
            vo.setEncrypt(item[3].trim());
            vo.setIsEnv(item[2].trim());
            vo.setPropertyName(item[0].trim());
            vo.setPropertyValue(item[1]);
            configMapBpo.saveConfigMap(vo);
        }
        return errStr.toString();
    }

    /**
     * 删除历史版本
     * @param appId
     */
    public static void deleteHostroy(Long appId) throws Exception{
        AppVo appVo = appBpo.getApp(appId);
        ConnVo connVo= NodeUtil.getConnection(appVo.getClusterId());
        List<AppVersionVo> list = appVersionBpo.getAllByAppId(appId).stream().filter(vo -> IAppConstants.APPVERSION_NO_USE.equals(vo.getIsuse())).collect(Collectors.toList());
        if (!list.isEmpty()){
            for (AppVersionVo version : list){
                if (IAppConstants.APPVERSION_ACTION.equals(version.getIsAction())){
                    ConsoleCommand consoleCommand = new ConsoleCommand();
                    consoleCommand.appendCommand("kubectl delete deployment " + appVo.getAppIdentify() + version.getVersionNameStr() + " -n " + appVo.getNamespaceIdentify());
                    CommonSshExecUtil.exec(connVo,consoleCommand);
                    version.setIsAction(IAppConstants.APPVERSION_NO_ACTION);
                    appVersionBpo.updateVersion(version);
                }
            }
        }
    }
    public static String getAccesPort(NodeBasicInfoVo vo) throws Exception{
        ConnVo connVo = NodeUtils.createConnFromNode(vo);
        ConsoleCommand consoleCommand = new ConsoleCommand();
        consoleCommand.appendCommand("kubectl get svc -n istio-system|grep istio-ingressgateway");
        MsgVO msgVO = null;
        try {
            msgVO = CommonSshExecUtil.exec(connVo, consoleCommand).get(consoleCommand);
            if (msgVO.isSuccess()){
                String sout = msgVO.getSysoutMsg();
                String[] soutArr = sout.split("\\s+");
                if (soutArr.length != 6){
                    throw new Exception("获取端口失败");
                }
                String [] nodePorts = soutArr[4].split(",");
                for (String nodePort : nodePorts){
                    if (nodePort.indexOf("80:") != -1){
                        return nodePort.substring(nodePort.indexOf("80:") + 3).split("/")[0];
                    }
                }
            }
        } catch (SSHExecuteException e) {
            throw new Exception("执行命令失败");
        } catch (SSHConnectionException e) {
            throw new Exception("连接服务器失败");
        }
        return null;
    }
}