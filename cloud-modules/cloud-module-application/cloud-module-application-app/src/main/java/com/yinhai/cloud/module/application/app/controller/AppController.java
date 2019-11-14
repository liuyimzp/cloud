package com.yinhai.cloud.module.application.app.controller;

import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.application.api.bpo.*;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.k8sapi.PodApi;
import com.yinhai.cloud.module.application.api.kubernetes.base.AppKubernetesYamlObject;
import com.yinhai.cloud.module.application.api.kubernetes.helper.AppYamlObjectFactory;
import com.yinhai.cloud.module.application.api.util.*;
import com.yinhai.cloud.module.application.api.vo.*;
import com.yinhai.cloud.module.application.impl.po.AppDiyFileVo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.modules.codetable.api.util.CodeTableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tianhy on 2018/6/12.
 * <p>
 * 应用管理
 */
@Controller
@RequestMapping({"/app"})
public class AppController extends BaseController {
    @Resource
    private IAppBpo appBpo;

    private final static Logger logger = LoggerFactory.getLogger(AppController.class);
    @Resource
    private IAppConfigBpo appConfigBpo;

    @Resource
    private IAppServiceBpo appServiceBpo;

    @Resource
    private IConfigMapBpo configMapBpo;

    @Resource
    private IAppVersionBpo appVersionBpo;

    @Resource
    private INamespaceBpo namespaceBpo;

    @Resource
    private IAppPVBpo appPVBpo;

    @Resource
    private INodeBpo nodeBpo;

    @Resource
    IConfigTemplateBpo configTemplateBpo;

    @Resource
    IConfigBackUpBpo configBackUpBpo;


    @RequestMapping("/getAll.do")
    @ResponseBody
    public CommonResultVo getAllApps(@RequestBody AppQueryVo vo) {
        Long userId = getTaDto().getUser().getUserid();
        CommonResultVo result = new CommonResultVo();
        IPage<Map> page = appBpo.getQueryApps(vo, userId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Map map : page.getList()) {
            if (map.get("beginTime") != null && map.get("beginTime") != "") {
                map.put("beginTime", sdf.format(map.get("beginTime")));
            }
            if(map.get("appRestartPolicy") == null){
                map.put("appRestartPolicy","");
            }
        }
        result.setSuccess(true);
        result.setData(page);
        return result;
    }

    @RequestMapping("/getAllClusters.do")
    @ResponseBody
    public CommonResultVo getAllClusters() {
        CommonResultVo result = new CommonResultVo();
        List<ClusterVo> list = appBpo.getAllClusters();
        result.setSuccess(true);
        result.setData(list);
        return result;
    }

    @RequestMapping("/checkAppIdentify.do")
    @ResponseBody
    public CommonResultVo checkIdentify(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getClusterId())) {
            result.setSuccess(false);
            result.setErrorMsg("集群ID不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getNamespaceId())) {
            result.setSuccess(false);
            result.setErrorMsg("命名空间ID不能为空!");
            return result;
        }
/*   if (ValidateUtil.isEmpty(vo.getAppIdentify())) {
            result.setSuccess(false);
            result.setErrorMsg("应用标识不能为空!");
            return result;
        }
        vo.setAppIdentify(vo.getAppIdentify().toLowerCase());
        if (appBpo.checkAppIdentifyExist(vo)) {
            result.setSuccess(false);
            result.setErrorMsg("应用标识已存在!");
            return result;
        }*/
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public CommonResultVo saveApp(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if ('\u0000' == vo.getRecordLog()) {
            vo.setRecordLog('0');
        }
        if (ValidateUtil.isEmpty(vo.getAppRestartPolicy())) {
            vo.setAppRestartPolicy("");
        }
        if (ValidateUtil.isEmpty(vo.getAppName())) {
            result.setSuccess(false);
            result.setErrorMsg("应用名称不能为空!");
            return result;
        }
        result = checkIdentify(vo);
        if (!result.isSuccess()) {
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getInstanceNum())) {
            result.setSuccess(false);
            result.setErrorMsg("实例个数不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getAppStrategy()) && !"4".equals(vo.getStatusType())) {
            result.setSuccess(false);
            result.setErrorMsg("升级策略不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getAppRestartPolicy()) && "4".equals(vo.getStatusType())) {
            result.setSuccess(false);
            result.setErrorMsg("应用重启条件不能为空!");
            return result;
        }
        if ("4".equals(vo.getStatusType()) && ValidateUtil.isEmpty(vo.getAppSchedule())) {
            result.setSuccess(false);
            result.setErrorMsg("应用是否重启未选择!");
            return result;
        }
        if ("4".equals(vo.getStatusType()) && "1".equals(vo.getAppSchedule()) && ValidateUtil.isEmpty(vo.getsAppSchedule())) {
            result.setSuccess(false);
            result.setErrorMsg("未设置应用循环周期!");
            return result;
        }
        if ("4".equals(vo.getStatusType()) && "1".equals(vo.getAppSchedule()) && ValidateUtil.isEmpty(vo.getsAppSchedule())) {
            result.setSuccess(false);
            result.setErrorMsg("未设置应用循环周期!");
            return result;
        }
        vo.setAppStatus(IAppConstants.APP_STATUS_UNRELEASED);//状态：未启动
        vo.setAppIdentify(vo.getAppIdentify().toLowerCase());
        if ("5".equals(vo.getStatusType())){
            vo.setAppType(IAppConstants.APP_TYPE_HPA);
            vo.setInstanceNum(1);
        }else {
            vo.setAppType(IAppConstants.APP_TYPE_COMMON);
        }
        vo.setCreateUser(getTaDto().getUser().getUserid());
        appBpo.saveApp(vo);
        result.setSuccess(true);
        if (ValidateUtil.isNotEmpty(vo.getMsg())) {
            result.setErrorMsg(vo.getMsg());
        }
        return result;
    }

    @RequestMapping("/appDiySave.do")
    @ResponseBody
    public CommonResultVo appDiySave(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getAppName())) {
            result.setSuccess(false);
            result.setErrorMsg("应用名称不能为空!");
            return result;
        }
        result = checkIdentify(vo);
        if (!result.isSuccess()) {
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getInstanceNum())) {
            result.setSuccess(false);
            result.setErrorMsg("实例个数不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getAppStrategy())) {
            result.setSuccess(false);
            result.setErrorMsg("升级策略不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getMappingPort())) {
            result.setSuccess(false);
            result.setErrorMsg("映射端口不能为空!");
            return result;
        }
        if (vo.getMappingPort() <= 30000 || vo.getMappingPort() > 32767) {
            result.setSuccess(false);
            result.setErrorMsg("映射端口必须大于30000!小于32767");
            return result;
        }
        Boolean isExist = appServiceBpo.checkMappingPortExist(vo.getClusterId(), vo.getMappingPort());
        if (isExist) {
            result.setSuccess(false);
            result.setErrorMsg("该映射端口已被占用!");
            return result;
        }
        vo.setAppStatus(IAppConstants.APP_STATUS_UNRELEASED);//状态：未启动
        vo.setAppIdentify(vo.getAppIdentify().toLowerCase());
        vo.setAppType(IAppConstants.APP_TYPE_CUSTOM);
        vo.setCreateUser(getTaDto().getUser().getUserid());
        AppVo appVo = appBpo.saveApp(vo);
        AppServiceVo appServiceVo = new AppServiceVo();
        appServiceVo.setAppId(appVo.getId());
        appServiceVo.setServiceType(IAppConstants.SERVICE_TYPE_OUTER);
        appServiceVo.setMappingPort(vo.getMappingPort());
        appServiceBpo.saveAppService(appServiceVo);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/saveMiddleWare.do")
    @ResponseBody
    public CommonResultVo saveMiddleWare(@RequestBody AppMiddleWareVo vo) {
        System.out.println(vo.getRecordLog());
        if ('\u0000' == vo.getRecordLog()) {
            vo.setRecordLog('1');
        }
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getClusterId())) {
            result.setSuccess(false);
            result.setErrorMsg("集群ID不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getNamespaceId())) {
            result.setSuccess(false);
            result.setErrorMsg("命名空间ID不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getMiddleWareType())) {
            result.setSuccess(false);
            result.setErrorMsg("中间件类型不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getInstanceNum())) {
            result.setSuccess(false);
            result.setErrorMsg("实例个数不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getMappingPort())) {
            result.setSuccess(false);
            result.setErrorMsg("映射端口不能为空!");
            return result;
        }
        if (vo.getMappingPort() <= 30000) {
            result.setSuccess(false);
            result.setErrorMsg("映射端口必须大于30000!");
            return result;
        }
        //检查外部映射端口是否存在
        Boolean isExist = appServiceBpo.checkMappingPortExist(vo.getClusterId(), vo.getMappingPort());
        if (isExist) {
            result.setSuccess(false);
            result.setErrorMsg("该映射端口已被占用!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getVolumeId())) {
            result.setSuccess(false);
            result.setErrorMsg("存储不能为空!");
            return result;
        }

        //检查存储空间是否充足
        result = PersistentVolumeUtil.checkPVSpaceEnough(vo.getVolumeId(), vo.getSpace(), vo.getInstanceNum());
        if (!result.isSuccess()) {
            return result;
        }
        vo.setAppName(CodeTableUtil.getDesc(IAppConstants.MIDDLE_WARE_TYPE, vo.getMiddleWareType()));
        Long cnt = appBpo.queryMiddleWareCount(vo.getClusterId(), vo.getMiddleWareType());
        vo.setAppIdentify((vo.getAppName() + "-" + (cnt + 1)).toLowerCase());
        vo.setAppStatus(IAppConstants.APP_STATUS_UNRELEASED);
        vo.setAppStrategy(IAppConstants.APP_STRATEGY_COVER);
        vo.setAppType(IAppConstants.APP_TYPE_MIDDLEWARE);
        //uodate by liuyi02 中间件配置文件详情
        ConfigTemplateVo configTemplateVo = configTemplateBpo.queryConfigTemplateByAppType(vo.getMiddleWareType());
        if (!ValidateUtil.isEmpty(configTemplateVo)){
            vo.setMiddleware_configfile(configTemplateVo.getConfigTemplateContent());
        }
        AppVo appVo = appBpo.saveApp(vo);
        AppServiceVo appServiceVo = new AppServiceVo();
        appServiceVo.setAppId(appVo.getId());
        appServiceVo.setServiceType(IAppConstants.SERVICE_TYPE_OUTER);
        appServiceVo.setMappingPort(vo.getMappingPort());
        appServiceBpo.saveAppService(appServiceVo);
        // 修改命名空间可用资源 由appconfig 添加后修改
//        NamespaceUtil.changeResourceWithMiddlewareCreate(vo.getNamespaceId(), vo.getMiddleWareType(), vo.getInstanceNum());
        // 修改存储池可用资源
        PersistentVolumeUtil.changePVSpace(vo.getVolumeId(), vo.getSpace(), vo.getInstanceNum());
        AppPVVo appPVVo = new AppPVVo();
        appPVVo.setAppId(appVo.getId());
        appPVVo.setVolumeId(vo.getVolumeId());
        appPVVo.setSpace(vo.getSpace());
        appPVBpo.savePV(appPVVo);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/edit.do")
    @ResponseBody
    public CommonResultVo editApp(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getClusterId())) {
            result.setSuccess(false);
            result.setErrorMsg("集群ID不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getNamespaceId())) {
            result.setSuccess(false);
            result.setErrorMsg("命名空间ID不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getAppName())) {
            result.setSuccess(false);
            result.setErrorMsg("应用名称不能为空!");
            return result;
        }
        vo.setAppIdentify(vo.getAppIdentify().toLowerCase());
        if (!appBpo.checkIdentifyChange(vo)) {
            result.setSuccess(false);
            result.setErrorMsg("应用标识已存在!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getInstanceNum())) {
            result.setSuccess(false);
            result.setErrorMsg("实例个数不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getAppStrategy()) && !"4".equals(vo.getStatusType())) {
            result.setSuccess(false);
            result.setErrorMsg("升级策略不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getAppRestartPolicy())) {
            vo.setAppRestartPolicy("");
        }
//        vo.setAppStatus("1");//状态：未启动
        appBpo.editApp(vo);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/remove.do")
    @ResponseBody
    public CommonResultVo
    removeApp(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getId());

        if (ValidateUtil.areEqual(IAppConstants.APP_STATUS_RUNNING, appVo.getAppStatus()) || ValidateUtil.areEqual(IAppConstants.APP_STATUS_STOP, appVo.getAppStatus())) {
            try {
                AppUtil.removeApp(vo.getId());
            } catch (Exception e) {
                result.setSuccess(false);
                result.setErrorMsg(e.getMessage());
                return result;
            }
        }
        //一次性应用
        if (ValidateUtil.isEmpty(appVo.getAppRestartPolicy())) {
            NamespaceUtil.changeResourceWithAppDelete(appVo);
        }
        PersistentVolumeUtil.changePVSpaceWithAppDelete(appVo);
        try {
            configBackUpBpo.deleteByAppId(vo.getId());
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        appConfigBpo.removeAppConfig(vo.getId());
        appServiceBpo.removeAppServiceByAppId(vo.getId());
        configMapBpo.removeConfigMapByAppId(vo.getId());
        appPVBpo.removePVByAppId(vo.getId());
        appBpo.removeApp(vo.getId());
        try {
            ConfigMapUtil.removeConfigMap(appVo);
        } catch (Exception e) {

        }
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/start.do")
    @ResponseBody
    public CommonResultVo startApp(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getId());
        if (IAppConstants.APP_TYPE_CUSTOM.equals(appVo.getAppType()) && ValidateUtil.isEmpty(appVo.getAppDiyFile())) {
            result.setSuccess(false);
            result.setErrorMsg("请上传yml文件！");
            return result;
        }
        try {
            AppUtil.applyApp(vo.getId());
        } catch (SSHConnectionException e) {
            result.setSuccess(false);
            result.setErrorMsg("无法连接该master节点!");
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        String oldStatus = appVo.getAppStatus();
        appVo.setAppStatus(IAppConstants.APP_STATUS_RUNNING);
        appBpo.editApp(appVo);

        if (IAppConstants.APP_STATUS_STOP.equals(oldStatus)) {
            // 从停止到启动，不消费新的存储
            result.setSuccess(true);
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/stop.do")
    @ResponseBody
    public CommonResultVo stopApp(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getId());
        try {
            AppUtil.stopApp(vo.getId());
            appVo.setAppStatus(IAppConstants.APP_STATUS_STOP);
            appBpo.editApp(appVo);
        } catch (SSHConnectionException e) {
            result.setSuccess(false);
            result.setErrorMsg("无法连接该master节点!");
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }

        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/flex.do")
    @ResponseBody
    public CommonResultVo flexApp(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getInstanceNum())) {
            result.setSuccess(false);
            result.setErrorMsg("实力个数不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getId());
        Integer add = vo.getInstanceNum() - appVo.getInstanceNum();
        if ((!ValidateUtil.areEqual(IAppConstants.APP_STATUS_UNRELEASED, appVo.getAppStatus()) || "2".equals(appVo.getAppType())) && vo.getInstanceNum() > appVo.getInstanceNum()) {
            result = NamespaceUtil.checkResourceEnoughWithInstanceIncrease(appVo, add);
            if (!result.isSuccess()) {
                return result;
            }
            result = PersistentVolumeUtil.checkPVSpaceEnoughWithInstanceIncrease(appVo, add);
            if (!result.isSuccess()) {
                return result;
            }
        }

        try {
            AppUtil.flexApp(vo.getId(), vo.getInstanceNum());
        } catch (SSHConnectionException e) {
            result.setSuccess(false);
            result.setErrorMsg("无法连接该master节点!");
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        if ((!ValidateUtil.areEqual(IAppConstants.APP_STATUS_UNRELEASED, appVo.getAppStatus()) || "2".equals(appVo.getAppType())) && vo.getInstanceNum() != appVo.getInstanceNum()) {
            NamespaceUtil.changeResourceWithInstanceNumChange(appVo, add);
            PersistentVolumeUtil.changePVSpaceWithInstanceChange(appVo, add);
        }
        appVo.setInstanceNum(vo.getInstanceNum());
        appBpo.editApp(appVo);
//        PersistentVolumeUtil.changePVSpace(appVo.getVolumeId(), vo.getSpace(), vo.getInstanceNum());
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/createYaml.do")
    @ResponseBody
    public CommonResultVo createYaml(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getId());
        if (ValidateUtil.areEqual(IAppConstants.APP_STATUS_UNRELEASED, appVo.getAppStatus())) {
            appVo.setAppStatus(IAppConstants.APP_STATUS_UNSTART);
            appBpo.editApp(appVo);
        }
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/getYaml.do")
    @ResponseBody
    public CommonResultVo getYaml(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getId());
        try {
            AppKubernetesYamlObject yamlObject = AppYamlObjectFactory.createByApp(appVo);
            result.setSuccess(true);
            if (IAppConstants.APP_TYPE_HPA.equals(appVo.getAppType())){
                result.setData(yamlObject.buildYaml() + AppYamlObjectFactory.createHpa(appVo));
            }else {
                result.setData(yamlObject.buildYaml());
            }
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
    }

    @RequestMapping("/getRunInfo.do")
    @ResponseBody
    public CommonResultVo getRunInfo(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getId());
        List<NodeBasicInfoVo> masterNodes = nodeBpo.queryRunningMasterNodesByClusterId(appVo.getClusterId());
        if (ValidateUtil.isEmpty(masterNodes)) {
            result.setSuccess(false);
            result.setErrorMsg("该集群下无正在运行中的master节点!");
            return result;
        }
        ConnVo connVo = NodeUtils.createConnFromNode(masterNodes.get(0));
        ConsoleCommand commandVo = new ConsoleCommand();
        String command = "kubectl get cronjob " + appVo.getAppIdentify() + " |grep " + appVo.getAppIdentify();
        commandVo.appendCommand(command);
        try {
            Map resultMap = CommonSshExecUtil.exec(connVo, commandVo);
            if (!ValidateUtil.isEmpty(resultMap) && resultMap.size() > 0) {
                MsgVO msgVo = (MsgVO) resultMap.values().iterator().next();
                String msg = msgVo.getSysoutMsg();
                if (ValidateUtil.isNotEmpty(msg)) {
                    List<Map> list = DuplicateCodeUtil.parseSysoutMsg(msg);
                    if (ValidateUtil.isNotEmpty(list)) {
                        List<Map> data = new ArrayList<>();
                        for (Map map : list) {
                            Map<String, Object> map1 = new HashMap<>();
                            map1.put("appName", map.get(1));
                            map1.put("appCycle", map.get(3));
                            map1.put("appNum", map.get(4));
                            map1.put("appLastTime", AppUtil.getTime(map.get(5) + "") + "前");
                            map1.put("appAge", AppUtil.getTime(map.get(6) + ""));
                            data.add(map1);
                        }
                        result.setData(data);
                    }
                }
            }
        } catch (SSHExecuteException e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        } catch (SSHConnectionException e) {
            result.setSuccess(false);
            result.setErrorMsg("无法连接该master节点!");
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/getPodInfo.do")
    @ResponseBody
    public CommonResultVo getPodInfo(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getId());
        List<NodeBasicInfoVo> masterNodes = nodeBpo.queryRunningMasterNodesByClusterId(appVo.getClusterId());
        if (ValidateUtil.isEmpty(masterNodes)) {
            result.setSuccess(false);
            result.setErrorMsg("该集群下无正在运行中的master节点!");
            return result;
        }
        String filterStr = vo.getAppIdentify();
        //获得当前应用现在运行的版本,将其拼接近过滤条件
        List<AppVersionVo> appVersionVos = appVersionBpo.getUseVersion(vo.getId());
        if (!appVersionVos.isEmpty()){
            filterStr += appVersionVos.get(0).getVersionNameStr();
        }
        NamespaceVo namespaceVo = namespaceBpo.getNamespace(appVo.getNamespaceId());
        ConnVo connVo = NodeUtils.createConnFromNode(masterNodes.get(0));
        ConsoleCommand commandVo = new ConsoleCommand();
        String yml = "";
        if (ValidateUtil.isEmpty(appVo.getAppRestartPolicy())) {
            yml = "-n " + namespaceVo.getNamespaceIdentify();
        }
        String commond = "source ~/.bash_profile && kubectl get po " + yml + " -o wide |grep " + filterStr + "-";
        commandVo.appendCommand(commond);
        try {
            Map resultMap = CommonSshExecUtil.exec(connVo, commandVo);
            if (!ValidateUtil.isEmpty(resultMap) && resultMap.size() > 0) {
                MsgVO msgVo = (MsgVO) resultMap.values().iterator().next();
                String msg = msgVo.getSysoutMsg();
                if (ValidateUtil.isNotEmpty(msg)) {
                    List<Map> list = DuplicateCodeUtil.parseSysoutMsg(msg);
                    if (ValidateUtil.isNotEmpty(list)) {
                        List<Map> data = new ArrayList<>();
                        List<NodeBasicInfoVo> nodeList = nodeBpo.queryNodesByClusterId(appVo.getClusterId());
                        Map<String, NodeBasicInfoVo> nodeMap = new HashMap<>();
                        if (ValidateUtil.isNotEmpty(nodeList)) {
                            for (NodeBasicInfoVo nodeBasicInfoVo : nodeList) {
                                nodeMap.put(nodeBasicInfoVo.getNodeName(), nodeBasicInfoVo);
                            }
                        }
                        //过滤

                        for (Map map : list) {
                            String podDAppNum = ((String) map.get(1)).substring(filterStr.length());
                            int appNum = podDAppNum.split("-").length;
                            if ((appNum == 3 && (0 == vo.getRecordLog() || '0' == vo.getRecordLog())) || (appNum == 2 && (1 == vo.getRecordLog() || '1' == vo.getRecordLog())) || (appNum == 2 && ValidateUtil.isNotEmpty(appVo.getAppRestartPolicy())) || (appNum == 3 && "1".equals(appVo.getAppSchedule()))) {
                                Map<String, Object> map2 = new HashMap();
                                map2.put("appId", vo.getId());
                                map2.put("podName", map.get(1));
                                map2.put("status", map.get(3));
                                String nodeName = map.get(7).toString();
                                map2.put("nodeName", nodeName);
                                NodeBasicInfoVo nodeVo = nodeMap.get(nodeName);
                                if (nodeVo != null) {
                                    ConnVo ndoeConnVo = NodeUtils.createConnFromNode(nodeVo);
                                    ConsoleCommand nodeCommandVo = new ConsoleCommand();
                                    nodeCommandVo.appendCommand("docker ps |grep " + map.get(1));
                                    try {
                                        Map nodeResultMap = CommonSshExecUtil.exec(ndoeConnVo, nodeCommandVo);
                                        if (!ValidateUtil.isEmpty(nodeResultMap) && nodeResultMap.size() > 0) {
                                            MsgVO ndoeMsgVo = (MsgVO) nodeResultMap.values().iterator().next();
                                            String ndoeMsg = ndoeMsgVo.getSysoutMsg();
                                            if (ValidateUtil.isNotEmpty(ndoeMsg)) {
                                                List<Map> nodeMsgList = DuplicateCodeUtil.parseSysoutMsg(ndoeMsg);
                                                if (ValidateUtil.isNotEmpty(nodeMsgList)) {
                                                    for (Map nodeMsgMap : nodeMsgList) {
                                                        String name = nodeMsgMap.get(6).toString();
                                                        if (name.indexOf("_POD_") == -1) {
                                                            String port = ValidateUtil.isNotEmpty(SystemConfigUtil.getSystemConfigValue("k8s.get.pod.info.container.port")) ? SystemConfigUtil.getSystemConfigValue("k8s.get.pod.info.container.port") : "2378";
                                                            map2.put("containerIP", "http://" + nodeVo.getNodeIP() + ":" + port + "?id=" + name);
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    } catch (SSHExecuteException e) {
                                        e.printStackTrace();
                                    } catch (SSHConnectionException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (!ValidateUtil.isEmpty(nodeVo)) {
                                    map2.put("nodeId", nodeVo.getNodeId());
                                    map2.put("nodeIP", nodeVo.getNodeIP());
                                }
                                data.add(map2);
                            }
                        }
                        result.setData(data);
                    }
                }
            }
        } catch (SSHExecuteException e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        } catch (SSHConnectionException e) {
            result.setSuccess(false);
            result.setErrorMsg("无法连接该master节点!");
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/getPodLog.do")
    @ResponseBody
    public CommonResultVo getPodLog(@RequestBody PodLogVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getAppId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getPodName())) {
            result.setSuccess(false);
            result.setErrorMsg("POD名称不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getAppId());
        List<NodeBasicInfoVo> masterNodes = nodeBpo.queryRunningMasterNodesByClusterId(appVo.getClusterId());
        if (ValidateUtil.isEmpty(masterNodes)) {
            result.setSuccess(false);
            result.setErrorMsg("该集群下无正在运行中的master节点!");
            return result;
        }
        NamespaceVo namespaceVo = namespaceBpo.getNamespace(appVo.getNamespaceId());
        try {
            String podLog = PodApi.getPodLog(vo, namespaceVo, masterNodes.get(0));
            result.setData(podLog);
        } catch (SSHExecuteException e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
        } catch (SSHConnectionException e) {
            result.setSuccess(false);
            result.setErrorMsg("无法连接该master节点!");
        }
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/appDiyUpload.do")
    @ResponseBody
    public CommonResultVo uploadFile(HttpServletRequest req) {
        CommonResultVo result = new CommonResultVo();
        Map map = req.getParameterMap();
        if (ValidateUtil.isEmpty(map)) {
            result.setSuccess(false);
            result.setErrorMsg("参数为空!");
            return result;
        } else if (ValidateUtil.isEmpty(map.get("id"))) {
            result.setSuccess(false);
            result.setErrorMsg("应用id为空!");
            return result;
        }
        String contentType = req.getContentType();
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
            MultipartHttpServletRequest multipartRequest =
                    WebUtils.getNativeRequest(req, MultipartHttpServletRequest.class);
            multipartRequest.getMultiFileMap();
            MultipartFile file = multipartRequest.getFile("file");
            if (ValidateUtil.isEmpty(file)) {
                result.setSuccess(false);
                result.setErrorMsg("上传文件为空!");
                return result;
            }
            try {
                AppDiyFileVo appDiyFileVo = new AppDiyFileVo();
                appDiyFileVo.setId(Long.valueOf(((String[]) map.get("id"))[0]));
                appDiyFileVo.setAppDiyFile(new String(file.getBytes(), "UTF-8"));
                appDiyFileVo.setAppStatus("2");
                appBpo.saveFile(appDiyFileVo.getId(), appDiyFileVo.getAppDiyFile(), appDiyFileVo.getAppStatus());
                result.setSuccess(true);
                return result;
            } catch (IOException e) {
                result.setSuccess(false);
                result.setErrorMsg("上传文件失败!");
                return result;
            }
        }
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/filePreview.do")
    @ResponseBody
    public CommonResultVo filePreview(HttpServletRequest req) {
        CommonResultVo result = new CommonResultVo();
        Map map = req.getParameterMap();
        if (ValidateUtil.isEmpty(map)) {
            result.setSuccess(false);
            result.setErrorMsg("参数为空!");
            return result;
        }
        String contentType = req.getContentType();
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
            MultipartHttpServletRequest multipartRequest =
                    WebUtils.getNativeRequest(req, MultipartHttpServletRequest.class);
            multipartRequest.getMultiFileMap();
            MultipartFile file = multipartRequest.getFile("file");
            if (ValidateUtil.isEmpty(file)) {
                result.setSuccess(false);
                result.setErrorMsg("上传文件为空!");
                return result;
            }
            try {
                result.setData(new String(file.getBytes(), "UTF-8"));
                result.setSuccess(true);
                return result;
            } catch (Exception e) {
                logger.error(logger.getName() + "文件打开失败" + e);
                result.setSuccess(false);
                result.setErrorMsg("文件上传失败!");
                return result;
            }
        }
        result.setSuccess(false);
        result.setErrorMsg("文件上传失败!");
        return result;
    }

    /**
     * 中间件应用备份将当前数据文档备份一份
     * @return
     */
    @RequestMapping("/backUp.do")
    @ResponseBody
    public CommonResultVo backUp(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getId());
        if (!IAppConstants.APP_STATUS_RUNNING.equals(appVo.getAppStatus())){
            result.setSuccess(false);
            result.setErrorMsg("该应用并未启动不可备份");
            return result;
        }
        if (!IAppConstants.APP_TYPE_MIDDLEWARE.equals(appVo.getAppType())){
            result.setSuccess(false);
            result.setErrorMsg("不是中间件应用不可备份");
            return result;
        }
        try {
            AppUtil.backUpContainer(appVo);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 返回应用所有备份信息
     * @return
     */
    @RequestMapping("/getAllBackUp.do")
    @ResponseBody
    public CommonResultVo getAllBackUp(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getId());
        if (!IAppConstants.APP_STATUS_RUNNING.equals(appVo.getAppStatus())){
            result.setSuccess(false);
            result.setErrorMsg("该应用并未启动不可还原");
            return result;
        }
        if (!IAppConstants.APP_TYPE_MIDDLEWARE.equals(appVo.getAppType())){
            result.setSuccess(false);
            result.setErrorMsg("不是中间件应用不可还原");
            return result;
        }
        List<ConfigBackUpVo> configBackUpVos = configBackUpBpo.queryAllBackUpByAppId(appVo.getId());
        result.setSuccess(true);
        result.setData(configBackUpVos);
        return result;
    }

    /**
     * 中间件应用备份将当前数据文档备份一份
     * @return
     */
    @RequestMapping("/deleBackUp.do")
    @ResponseBody
    public CommonResultVo deleBackUp(@RequestBody ConfigBackUpVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getConfigId())) {
            result.setSuccess(false);
            result.setErrorMsg("备份ID不能为空!");
            return result;
        }
//        ConfigBackUpVo configBackUpVo = configBackUpBpo.queryAllBackUpById(vo.getConfigId());
        if (vo == null){
            result.setSuccess(false);
            result.setErrorMsg("该信息已被删除!");
            return result;
        }
        try {
            configBackUpBpo.deleteConfigBackUp(vo);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 还原当前备份
     * @return
     */
    @RequestMapping("/reductionBackUp.do")
    @ResponseBody
    public CommonResultVo reductionBackUp(@RequestBody ConfigBackUpVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getConfigId())) {
            result.setSuccess(false);
            result.setErrorMsg("备份ID不能为空!");
            return result;
        }
        if (vo == null){
            result.setSuccess(false);
            result.setErrorMsg("该条备份信息已被删除!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getAppId());
        Integer add = vo.getInstanceNum() - appVo.getInstanceNum();
        if ((!ValidateUtil.areEqual(IAppConstants.APP_STATUS_UNRELEASED, appVo.getAppStatus()) || "2".equals(appVo.getAppType())) && vo.getInstanceNum() > appVo.getInstanceNum()) {
            result = NamespaceUtil.checkResourceEnoughWithInstanceIncrease(appVo, add);
            if (!result.isSuccess()) {
                return result;
            }
            result = PersistentVolumeUtil.checkPVSpaceEnoughWithInstanceIncrease(appVo, add);
            if (!result.isSuccess()) {
                return result;
            }
        }
        try {
            //将实例个数修改为备份个数
            AppUtil.flexApp(appVo.getId(), vo.getInstanceNum());
            if ((!ValidateUtil.areEqual(IAppConstants.APP_STATUS_UNRELEASED, appVo.getAppStatus()) || "2".equals(appVo.getAppType())) && vo.getInstanceNum() != appVo.getInstanceNum()) {
                NamespaceUtil.changeResourceWithInstanceNumChange(appVo, add);
                PersistentVolumeUtil.changePVSpaceWithInstanceChange(appVo, add);
            }
            appVo.setInstanceNum(vo.getInstanceNum());
            appBpo.editApp(appVo);
            configBackUpBpo.reductionConfigBackUp(vo);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setSuccess(true);
        return result;
    }

}
