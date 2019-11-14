package com.yinhai.cloud.module.application.api.util;

import com.yinhai.cloud.core.api.constant.ConfigPropKey;
import com.yinhai.cloud.core.api.ssh.SlashPath;
import com.yinhai.cloud.core.api.ssh.command.AbstractCommand;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.UploadContentCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.application.api.bpo.IAppConfigBpo;
import com.yinhai.cloud.module.application.api.bpo.INamespaceBpo;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.kubernetes.namespace.NamespaceResourceQuota;
import com.yinhai.cloud.module.application.api.kubernetes.namespace.NamespaceYamlObject;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.application.api.vo.NamespaceVo;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tianhy on 2018/11/19.
 */
public class NamespaceUtil {

    private static final String FILE_SEPARATOR = ServerCmdConstant.FILE_SEPARATOR;
    private static final Logger logger = LoggerFactory.getLogger(NamespaceUtil.class);
    public static void createNamespace(NamespaceVo vo) throws Exception {
        ConnVo connVo = NodeUtil.getConnection(vo.getClusterId());
        String namespaceFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT), FILE_SEPARATOR + IAppConstants.YAML_DIR_NAME + FILE_SEPARATOR + vo.getNamespaceIdentify() + ".yaml").toString();
        String resourceQuotaFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT), FILE_SEPARATOR + IAppConstants.YAML_DIR_NAME + FILE_SEPARATOR + vo.getNamespaceIdentify() + "ResourceQuota.yaml").toString();
        NamespaceYamlObject namespaceYamlObject = new NamespaceYamlObject(vo.getNamespaceIdentify());
        NamespaceResourceQuota namespaceResourceQuota = new NamespaceResourceQuota(vo.getNamespaceIdentify(), vo.getMaxCPULimit(), vo.getMaxMemoryLimit());
        AbstractCommand uploadYaml = new UploadContentCommand(namespaceFilePath, namespaceYamlObject.buildYaml());
        AbstractCommand uploadYaml2 = new UploadContentCommand(resourceQuotaFilePath, namespaceResourceQuota.buildYaml());
        ConsoleCommand createCommand = new ConsoleCommand();
        createCommand.appendCommand("source ~/.bash_profile && kubectl create -f " + namespaceFilePath);
        createCommand.appendCommand("source ~/.bash_profile && kubectl create -f " + resourceQuotaFilePath + " --namespace=" + vo.getNamespaceIdentify());
        CommonSshExecUtil.exec(connVo, uploadYaml, uploadYaml2, createCommand);
    }

    public static void updateNamespaceResourceQuota(NamespaceVo vo) throws Exception {
        ConnVo connVo = NodeUtil.getConnection(vo.getClusterId());
        String resourceQuotaFilePath = new SlashPath(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT), FILE_SEPARATOR + IAppConstants.YAML_DIR_NAME + FILE_SEPARATOR + vo.getNamespaceIdentify() + "ResourceQuota.yaml").toString();
        NamespaceResourceQuota namespaceResourceQuota = new NamespaceResourceQuota(vo.getNamespaceIdentify(), vo.getMaxCPULimit(), vo.getMaxMemoryLimit());
        AbstractCommand uploadYaml = new UploadContentCommand(resourceQuotaFilePath, namespaceResourceQuota.buildYaml());
        ConsoleCommand createCommand = new ConsoleCommand();
        createCommand.appendCommand("source ~/.bash_profile && kubectl apply -f " + resourceQuotaFilePath + " --namespace=" + vo.getNamespaceIdentify());
        CommonSshExecUtil.exec(connVo, uploadYaml, createCommand);
    }

    public static void deleteNamespace(NamespaceVo vo) throws Exception {
        ConnVo connVo = NodeUtil.getConnection(vo.getClusterId());
        ConsoleCommand createCommand = new ConsoleCommand();
        createCommand.appendCommand("source ~/.bash_profile")
                        .appendCommand("kubectl get destinationrule -n " + vo.getNamespaceIdentify() + "|grep " + vo.getNamespaceIdentify() + "|awk '{system(\"kubectl delete destinationrule \"$2\" -n \"$1\"\")}'")
                        .appendCommand("kubectl get virtualservice -n " + vo.getNamespaceIdentify() + "|grep " + vo.getNamespaceIdentify() + "|awk '{system(\"kubectl delete virtualservice \"$2\" -n \"$1\"\")}'")
                        .appendCommand("kubectl get gateway -n " + vo.getNamespaceIdentify() + "|grep " + vo.getNamespaceIdentify() + "|awk '{system(\"kubectl delete gateway \"$2\" -n \"$1\"\")}'")
                        .appendCommand("kubectl delete namespace " + vo.getNamespaceIdentify());
        CommonSshExecUtil.exec(connVo, createCommand);
    }

    /**
     * 检查常规应用资源增长是否超出命名空间限制
     * @param namespaceId
     * @param increaseCpu
     * @param increaseMemory
     * @param instanceNum
     * @return
     */
    public static CommonResultVo checkResourceEnoughWithAppResourceIncrease(Long namespaceId, Double increaseCpu, Double increaseMemory, int instanceNum){
        CommonResultVo result = new CommonResultVo();
        INamespaceBpo namespaceBpo = (INamespaceBpo) ServiceLocator.getService(INamespaceBpo.SERVICEKEY);
        NamespaceVo namespaceVo = namespaceBpo.getNamespace(namespaceId);
        if(namespaceVo.getAvailableCPU() < increaseCpu * instanceNum){
            result.setSuccess(false);
            result.setErrorMsg("CPU超出命名空间限制!");
            return result;
        }
        if(namespaceVo.getAvailableMemory() < increaseMemory * instanceNum){
            result.setSuccess(false);
            result.setErrorMsg("内存超出命名空间限制!");
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 检查创建中间件应用是否超过命名空间资源限制
     * @param namespaceId
     * @param middlewareType
     * @param instanceNum
     * @return
     */
    public static CommonResultVo checkResourceEnoughWithMiddlewareCreate(Long namespaceId, String middlewareType, int instanceNum){
        CommonResultVo result = new CommonResultVo();
        try {
            Double increaseCpu = getMiddlewareDefaultCpu(middlewareType);
            Double increaseMemory =getMiddlewareDefaultMemory(middlewareType);
            result = checkResourceEnoughWithAppResourceIncrease(namespaceId, increaseCpu, increaseMemory, instanceNum);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 检查应用实例增长是否超过命名空间资源限制
     * @param appVo
     * @param increaseNum
     * @return
     */
    public static CommonResultVo checkResourceEnoughWithInstanceIncrease(AppVo appVo, int increaseNum){
        CommonResultVo result = new CommonResultVo();
        Double increaseCpu = 0.0;
        Double increaseMemory = 0.0;
        switch (appVo.getAppType()){
            case IAppConstants.APP_TYPE_COMMON:
                IAppConfigBpo appConfigBpo = (IAppConfigBpo) ServiceLocator.getService(IAppConfigBpo.SERVICEKEY);
                AppConfigVo appConfigVo = appConfigBpo.getAppConfig(appVo.getId());
                increaseCpu = appConfigVo.getMaxCPU();
                increaseMemory = appConfigVo.getMaxMemory();
                break;
            case IAppConstants.APP_TYPE_MIDDLEWARE:
                try {
                    increaseCpu = getMiddlewareDefaultCpu(appVo.getMiddleWareType());
                    increaseMemory =getMiddlewareDefaultMemory(appVo.getMiddleWareType());
                }catch (Exception e){
                    result.setSuccess(false);
                    result.setErrorMsg(e.getMessage());
                    return result;
                }
                break;
            case IAppConstants.APP_TYPE_CUSTOM:

                break;
            default:
                result.setSuccess(false);
                result.setErrorMsg("不支持的应用类型!");
                return result;

        }
        result = checkResourceEnoughWithAppResourceIncrease(appVo.getNamespaceId(), increaseCpu, increaseMemory, increaseNum);
        return result;
    }

    /**
     * 常规应用资源增长时修改命名空间可用资源
     * @param namespaceId
     * @param increaseCpu
     * @param increaseMemory
     * @param instanceNum
     */
    public static void changeResourceWithAppResourceChange(Long namespaceId, Double increaseCpu, Double increaseMemory, int instanceNum){
        INamespaceBpo namespaceBpo = (INamespaceBpo) ServiceLocator.getService(INamespaceBpo.SERVICEKEY);
        NamespaceVo namespaceVo = namespaceBpo.getNamespace(namespaceId);
        namespaceVo.setAvailableCPU(namespaceVo.getAvailableCPU() - increaseCpu * instanceNum);
        namespaceVo.setAvailableMemory(namespaceVo.getAvailableMemory() - increaseMemory * instanceNum);
        namespaceBpo.updateNamespace(namespaceVo);
    }

    /**
     * 中间件创建时修改命名空间可用资源
     * @param namespaceId
     * @param middlewareType
     * @param instanceNum
     */
    public static void changeResourceWithMiddlewareCreate(Long namespaceId, String middlewareType, int instanceNum){
        try {
            Double increaseCpu = getMiddlewareDefaultCpu(middlewareType);
            Double increaseMemory =getMiddlewareDefaultMemory(middlewareType);
            changeResourceWithAppResourceChange(namespaceId, increaseCpu, increaseMemory, instanceNum);
        }catch (Exception e) {
           logger.error(logger.getName() + "context",e);
        }
    }

    /**
     * 应用实例增长时修改命名空间可用资源
     * @param appVo
     * @param increaseNum
     */
    public static void changeResourceWithInstanceNumChange(AppVo appVo, int increaseNum){
        Double increaseCpu;
        Double increaseMemory;
        IAppConfigBpo appConfigBpo = (IAppConfigBpo) ServiceLocator.getService(IAppConfigBpo.SERVICEKEY);
        AppConfigVo appConfigVo = appConfigBpo.getAppConfig(appVo.getId());
        switch (appVo.getAppType()){
            case IAppConstants.APP_TYPE_COMMON:
                // 普通应用未配置资源的情况
                if(ValidateUtil.isEmpty(appConfigVo)){
                    return;
                }
                increaseCpu = appConfigVo.getMaxCPU();
                increaseMemory = appConfigVo.getMaxMemory();
                break;
            case IAppConstants.APP_TYPE_MIDDLEWARE:
                try {
                    increaseCpu = appConfigVo.getMaxCPU();
                    increaseMemory =appConfigVo.getMaxMemory();
                }catch (Exception e){
                    logger.error(logger.getName() + "context",e);
                    return;
                }
                break;
            case IAppConstants.APP_TYPE_CUSTOM:
                // 自定义应用未配置资源的情况
                if(ValidateUtil.isEmpty(appConfigVo)){
                    return;
                }
                increaseCpu = appConfigVo.getMaxCPU();
                increaseMemory = appConfigVo.getMaxMemory();
                break;
            default:
                return;
        }
        changeResourceWithAppResourceChange(appVo.getNamespaceId(), increaseCpu, increaseMemory, increaseNum);
    }

    public static void changeResourceWithAppDelete(AppVo appVo){
        changeResourceWithInstanceNumChange(appVo, 0 - appVo.getInstanceNum());
    }

    private static Double getMiddlewareDefaultCpu(String middlewareType) throws Exception{
        switch (middlewareType) {
            case IAppConstants.MIDDLEWARE_ZOOKEEPER:
                return IAppConstants.ZOOKEEPER_DEFAULT_CPU_LIMIT.doubleValue();
            case IAppConstants.MIDDLEWARE_REDIS:
                return IAppConstants.REDIS_DEFAULT_CPU_LIMIT.doubleValue();
            case IAppConstants.MIDDLEWARE_MYSQL:
                return IAppConstants.MYSQL_DEFAULT_CPU_LIMIT.doubleValue();
            case IAppConstants.MIDDLEWARE_ACTIVEMQ:
                return IAppConstants.ACTIVEMQ_DEFAULT_CPU_LIMIT.doubleValue();
            case IAppConstants.MIDDLEWARE_ORACLE_DATABASE:
                return IAppConstants.ORACLE_DEFAULT_CPU_LIMIT.doubleValue();
            case IAppConstants.MIDDLEWARE_NGINX:
                return IAppConstants.NGINX_DEFAULT_CPU_LIMIT.doubleValue();
            case IAppConstants.MIDDLEWARE_KAFKA:
                return IAppConstants.KAFKA_DEFAULT_CPU_LIMIT.doubleValue();
            case IAppConstants.MIDDLEWARE_ELASTIC_SEARCH:
                return IAppConstants.ELASTICSEARCH_DEFAULT_CPU_LIMIT.doubleValue();
            default:
                throw new Exception("不支持的中间件类型!");
        }
    }

    private static Double getMiddlewareDefaultMemory(String middlewareType) throws Exception{
        switch (middlewareType) {
            case IAppConstants.MIDDLEWARE_ZOOKEEPER:
                return IAppConstants.ZOOKEEPER_DEFAULT_MEMORY_LIMIT.doubleValue();
            case IAppConstants.MIDDLEWARE_REDIS:
                return IAppConstants.REDIS_DEFAULT_MEMORY_LIMIT.doubleValue();
            case IAppConstants.MIDDLEWARE_MYSQL:
                return IAppConstants.MYSQL_DEFAULT_MEMORY_LIMIT.doubleValue();
            case IAppConstants.MIDDLEWARE_ACTIVEMQ:
                return IAppConstants.ACTIVEMQ_DEFAULT_MEMORY_LIMIT.doubleValue();
            case IAppConstants.MIDDLEWARE_ORACLE_DATABASE:
                return IAppConstants.ORACLE_DEFAULT_MEMORY_LIMIT.doubleValue();
            case IAppConstants.MIDDLEWARE_NGINX:
                return IAppConstants.NGINX_DEFAULT_MEMORY_LIMIT.doubleValue();
            case IAppConstants.MIDDLEWARE_KAFKA:
                return IAppConstants.KAFKA_DEFAULT_MEMORY_LIMIT.doubleValue();
            case IAppConstants.MIDDLEWARE_ELASTIC_SEARCH:
                return IAppConstants.ELASTICSEARCH_DEFAULT_MEMORY_LIMIT.doubleValue();
            default:
                throw new Exception("不支持的中间件类型!");
        }
    }
}
