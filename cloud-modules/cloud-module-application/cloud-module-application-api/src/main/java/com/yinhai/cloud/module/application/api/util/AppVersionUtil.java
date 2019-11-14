package com.yinhai.cloud.module.application.api.util;

import com.yinhai.cloud.core.api.constant.ConfigPropKey;
import com.yinhai.cloud.core.api.ssh.SlashPath;
import com.yinhai.cloud.core.api.ssh.command.AbstractCommand;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.UploadContentCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.application.api.bpo.*;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.kubernetes.gateway.GatewayYamlObject;
import com.yinhai.cloud.module.application.api.kubernetes.resource.DestinationRuleAndVirtualService;
import com.yinhai.cloud.module.application.api.vo.*;
import com.yinhai.core.app.api.util.ServiceLocator;

/**
 * @author: zhaokai
 * @create: 2018-09-29 15:55:10
 */
public class AppVersionUtil {
    private final static INamespaceBpo namespaceBpo = ServiceLocator.getService(INamespaceBpo.class);
    private final static IAppBpo appBpo = ServiceLocator.getService(IAppBpo.class);
    private final static IAppVersionBpo appVersionBpo = ServiceLocator.getService(IAppVersionBpo.class);
    private static final String FILE_SEPARATOR = "/";

    /**
     * 将app的访问流量跳转到指定版本指向的镜像
     * @param vo
     * @throws Exception
     */
    public static void backVersion(AppVersionVo vo) throws Exception{
        AppVo appVo = appBpo.getApp(vo.getAppId());
        ConnVo connVo = NodeUtil.getConnection(appVo.getClusterId());
        //修改数据库数据将当前版本修改为已使用其他版本修改为未使用
        appVersionBpo.setVersionIsUse(vo);
        //重写DestinationRule和VirtualService
        DestinationRuleAndVirtualService drvs = new DestinationRuleAndVirtualService(vo.getAppId());
        String serverYamlFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT), FILE_SEPARATOR + IAppConstants.YAML_DIR_NAME + FILE_SEPARATOR + appVo.getNamespaceIdentify() + FILE_SEPARATOR + "DestinationRule" + FILE_SEPARATOR + appVo.getAppIdentify() + "DRVS.yaml").toString();
        AbstractCommand uploadYaml = new UploadContentCommand(serverYamlFilePath, drvs.toString());
        ConsoleCommand updateKubeletRes = new ConsoleCommand();
        updateKubeletRes.appendCommand("source ~/.bash_profile && kubectl apply -f " + serverYamlFilePath);
        CommonSshExecUtil.exec(connVo, uploadYaml, updateKubeletRes);
    }

    /**
     * 创建网关（每个命名空间都会创建一个）
     * @param vo
     */
    public static void createGateWay(NamespaceVo vo) throws Exception {
        ConnVo connVo = NodeUtil.getConnection(vo.getClusterId());
        String gatewayFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT), FILE_SEPARATOR + IAppConstants.YAML_DIR_NAME + FILE_SEPARATOR + "Gateway" + FILE_SEPARATOR + vo.getNamespaceIdentify() + "gateway.yaml").toString();
        GatewayYamlObject gateway = new GatewayYamlObject(vo.getNamespaceIdentify());
        AbstractCommand uploadYaml = new UploadContentCommand(gatewayFilePath, gateway.getYaml());
        ConsoleCommand updateKubeletRes = new ConsoleCommand();
        updateKubeletRes.appendCommand("source ~/.bash_profile && kubectl apply -f " + gatewayFilePath);
        CommonSshExecUtil.exec(connVo, uploadYaml, updateKubeletRes);
    }
}