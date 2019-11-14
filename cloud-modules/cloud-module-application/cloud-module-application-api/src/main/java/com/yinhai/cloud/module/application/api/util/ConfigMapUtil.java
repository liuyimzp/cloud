package com.yinhai.cloud.module.application.api.util;

import com.yinhai.cloud.core.api.constant.ConfigPropKey;
import com.yinhai.cloud.core.api.ssh.SlashPath;
import com.yinhai.cloud.core.api.ssh.command.AbstractCommand;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.UploadContentCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.kubernetes.configmap.ConfigMapYamlObject;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.application.api.vo.ConfigMapVo;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;

import java.io.File;
import java.util.List;

/**
 * Created by yanglg on 2019/3/4.
 */
public class ConfigMapUtil {

    private static final String FILE_SEPARATOR = ServerCmdConstant.FILE_SEPARATOR;

    public static void applyConfigMap(AppVo appVo,List<ConfigMapVo> configMapList)throws Exception{
        ConnVo connVo = NodeUtil.getConnection(appVo.getClusterId());
        String serverYamlFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT), FILE_SEPARATOR + IAppConstants.YAML_DIR_NAME + FILE_SEPARATOR + appVo.getNamespaceIdentify() + FILE_SEPARATOR + appVo.getAppIdentify() + "-configMap.yaml").toString();
        ConfigMapYamlObject configMapYamlObject = new ConfigMapYamlObject(appVo.getNamespaceIdentify(),appVo.getAppIdentify(),configMapList);
        AbstractCommand uploadYaml = new UploadContentCommand(serverYamlFilePath, configMapYamlObject.buildYaml());
        ConsoleCommand updateKubeletRes = new ConsoleCommand();
        updateKubeletRes.appendCommand("source ~/.bash_profile && kubectl apply -f " + serverYamlFilePath);
        CommonSshExecUtil.exec(connVo, uploadYaml, updateKubeletRes);
    }

    public static String showConfigMap(AppVo appVo,List<ConfigMapVo> configMapList){
        ConfigMapYamlObject configMapYamlObject = new ConfigMapYamlObject(appVo.getNamespaceIdentify(),appVo.getAppIdentify(),configMapList);
        return  configMapYamlObject.buildYaml();
    }

    public static void removeConfigMap(AppVo appVo)throws Exception{
        ConnVo connVo = NodeUtil.getConnection(appVo.getClusterId());
        String serverYamlFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT), FILE_SEPARATOR + IAppConstants.YAML_DIR_NAME + FILE_SEPARATOR + appVo.getNamespaceIdentify() + FILE_SEPARATOR + appVo.getAppIdentify() + "-configMap.yaml").toString();
        ConsoleCommand removeCommand = new ConsoleCommand();
        removeCommand.appendCommand("source ~/.bash_profile && kubectl delete -f " + serverYamlFilePath);
        CommonSshExecUtil.exec(connVo, removeCommand);
    }
}
