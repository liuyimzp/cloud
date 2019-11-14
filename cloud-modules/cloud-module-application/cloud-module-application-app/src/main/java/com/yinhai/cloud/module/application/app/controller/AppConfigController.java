package com.yinhai.cloud.module.application.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.bpo.IAppBpo;
import com.yinhai.cloud.module.application.api.bpo.IAppConfigBpo;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.util.NamespaceUtil;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppImageBpo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppRepertoryBpo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageQueryVo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.cloud.module.repertory.api.vo.AppRepertoryVo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.user.api.bpo.IUserAuthorityBpo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.modules.org.ta3.domain.po.IUser;
import com.yinhai.sysframework.util.IConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tianhy on 2018/6/13.
 */
@Controller
@RequestMapping({"/appConfig"})
public class AppConfigController extends BaseController {
    @Resource
    private IAppConfigBpo appConfigBpo;

    @Resource
    private IAppImageBpo appImageBpo;

    @Resource
    private IAppBpo appBpo;

    @Resource
    private IClusterBpo clusterBpo;

    @Resource
    private IAppRepertoryBpo appRepertoryBpo;

    @Resource
    private IUserAuthorityBpo userAuthorityBpo;

    private static final Logger logger = LoggerFactory.getLogger(AppConfigController.class);

    @RequestMapping("/get.do")
    @ResponseBody
    public CommonResultVo getAppConfig(@RequestBody AppConfigVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getAppId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        AppConfigVo appConfigVo = appConfigBpo.getAppConfig(vo.getAppId());
        try {
            Map map = appConfigVo.toMap();
            if (!ValidateUtil.isEmpty(appConfigVo.getAppImageId())) {
                AppImageVo appImageVo = appImageBpo.getAppImage(appConfigVo.getAppImageId());
                AppRepertoryVo appRepertoryVo = appRepertoryBpo.getAppRepertory(appImageVo.getRepertoryId());
                map.put("imagePath", appRepertoryVo.getImagePath());
                map.put("version", appImageVo.getVersion());
            }
            result.setSuccess(true);
            result.setData(map);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setData("获取配置信息失败!");
        }
        return result;
    }

    @RequestMapping("/getClusterInfo.do")
    @ResponseBody
    public CommonResultVo getClusterInfo(@RequestBody AppConfigVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getAppId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getAppId());
        ClusterVo clusterVo = clusterBpo.queryClusterStatistics(appVo.getClusterId());
        result.setSuccess(true);
        result.setData(clusterVo);
        return result;
    }

    @RequestMapping("/getAllAppImages.do")
    @ResponseBody
    public CommonResultVo getAllAppImages(@RequestBody AppImageQueryVo vo) {
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getAppId())){
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getAppId());
        if(ValidateUtil.isEmpty(appVo)){
            result.setSuccess(false);
            result.setErrorMsg("应用ID不正确!");
            return result;

        }
        if (vo.getIdentify() == null || vo.getIdentify() == ""){
            vo.setIdentify(appVo.getAppIdentify());
        }
        appImageBpo.syncDockerRepertory(vo);
        vo.setEffective(IConstants.EFFECTIVE_YES);
        IPage<AppImageVo> page = userAuthorityBpo.getQueryAppImages(vo, getTaDto().getUser().getUserid());
        List<Map> list2 = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (AppImageVo appImageVo : page.getList()) {
            try {
                Map map = appImageVo.toMap();
                map.put("createTime", sdf.format(appImageVo.getCreateTime()));
                list2.add(map);
            } catch (Exception e) {
                logger.error(logger.getName() + "context",e);
            }
        }
        result.setSuccess(true);
        Map map = new HashMap();
        map.put("list", list2);
        map.put("total", page.getTotal());
        result.setData(map);
        return result;
    }

    @RequestMapping("/saveJobConfig.do")
    @ResponseBody
    public CommonResultVo saveJobConfig(@RequestBody AppConfigVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getAppImageId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用镜像ID不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getAppId());
        AppConfigVo appConfigVo = appConfigBpo.getAppConfig(vo.getAppId());
        try{
            appConfigBpo.saveAppConfig(vo,appVo,appConfigVo);
        }catch (Exception e){
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/saveAppConfig.do")
    @ResponseBody
    public CommonResultVo saveAppConfig(@RequestBody AppConfigVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getAppImageId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用镜像ID不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getMinCPU())) {
            vo.setMinCPU(IAppConstants.CPU_MIN_DEFAULT);
        }
        if (ValidateUtil.isEmpty(vo.getMaxCPU())) {
            vo.setMaxCPU(IAppConstants.CPU_MAX_DEFAULT);
        }
        if (ValidateUtil.isEmpty(vo.getMinMemory())) {
            vo.setMinMemory(IAppConstants.MEMORY_MIN_DEFAULT);
        }
        if (ValidateUtil.isEmpty(vo.getMaxMemory())) {
            vo.setMaxMemory(IAppConstants.MEMORY_MAX_DEFAULT);
        }
        AppVo appVo = appBpo.getApp(vo.getAppId());

        AppConfigVo appConfigVo = appConfigBpo.getAppConfig(vo.getAppId());
        Double oldMaxCpu = ValidateUtil.isEmpty(appConfigVo) ? 0.0 : appConfigVo.getMaxCPU();
        Double oldMaxMemory = ValidateUtil.isEmpty(appConfigVo) ? 0.0 : appConfigVo.getMaxMemory();
        //若编辑后的资源大于当前则检查是否超出可用
        if(vo.getMaxCPU().compareTo(oldMaxCpu) > 0 || vo.getMaxMemory().compareTo(oldMaxMemory) > 0){
            result = NamespaceUtil.checkResourceEnoughWithAppResourceIncrease(appVo.getNamespaceId(), vo.getMaxCPU() - oldMaxCpu, vo.getMaxMemory() - oldMaxMemory, appVo.getInstanceNum());
            if(!result.isSuccess()){
                return result;
            }
        }
        try{
            appConfigBpo.saveAppConfig(vo,appVo,appConfigVo);
        }catch (Exception e){
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        if(vo.getMaxCPU().compareTo(oldMaxCpu) != 0 || vo.getMaxMemory().compareTo(oldMaxMemory) != 0){
            NamespaceUtil.changeResourceWithAppResourceChange(appVo.getNamespaceId(), vo.getMaxCPU() - oldMaxCpu, vo.getMaxMemory() - oldMaxMemory, appVo.getInstanceNum());
        }
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/getMiddleWareImages.do")
    @ResponseBody
    public CommonResultVo getMiddleWareImages(@RequestBody AppImageQueryVo vo) {
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getIdentify())){
            result.setSuccess(false);
            result.setErrorMsg("中间件标识为空!");
            return result;
        }
        IPage<AppImageVo> page = appImageBpo.getQueryAppImages(vo);
        if(page.getList().isEmpty()){
            result.setSuccess(false);
            result.setErrorMsg("无该中间件镜像!");
            return result;
        }
        List<Map> list = new ArrayList<>();
        for (AppImageVo appImageVo : page.getList()) {
            Map<String,Object> map = new HashMap<>();
            map.put("value",appImageVo.getId());
            map.put("label",vo.getIdentify()+":"+appImageVo.getVersion());
            list.add(map);
        }
        result.setData(list);
        result.setSuccess(true);
        return  result;
    }

    @RequestMapping("/getMiddleWareDefaultConfig.do")
    @ResponseBody
    public CommonResultVo getMiddleWareDefaultConfig(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getMiddleWareType())){
            result.setSuccess(false);
            result.setErrorMsg("中间件类型为空!");
            return result;
        }
        Map<String,Object> map = new HashMap<>();
        if(vo.getAppStatus().equals(IAppConstants.APP_STATUS_UNRELEASED)){
            switch(vo.getMiddleWareType()){
                case "1":
                    map.put("maxCPU",IAppConstants.ZOOKEEPER_DEFAULT_CPU_LIMIT);
                    map.put("maxMemory",IAppConstants.ZOOKEEPER_DEFAULT_MEMORY_LIMIT);
                    break;
                case "2":
                    map.put("maxCPU",IAppConstants.REDIS_DEFAULT_CPU_LIMIT);
                    map.put("maxMemory",IAppConstants.REDIS_DEFAULT_MEMORY_LIMIT);
                    break;
                case "3":
                    map.put("maxCPU",IAppConstants.MYSQL_DEFAULT_CPU_LIMIT);
                    map.put("maxMemory",IAppConstants.MYSQL_DEFAULT_MEMORY_LIMIT);
                    break;
                case "4":
                    map.put("maxCPU",IAppConstants.ACTIVEMQ_DEFAULT_CPU_LIMIT);
                    map.put("maxMemory",IAppConstants.ACTIVEMQ_DEFAULT_MEMORY_LIMIT);
                    break;
                case "5":
                    map.put("maxCPU",IAppConstants.ORACLE_DEFAULT_CPU_LIMIT);
                    map.put("maxMemory",IAppConstants.ORACLE_DEFAULT_MEMORY_LIMIT);
                    break;
                case "6":
                    map.put("maxCPU",IAppConstants.NGINX_DEFAULT_CPU_LIMIT);
                    map.put("maxMemory",IAppConstants.NGINX_DEFAULT_MEMORY_LIMIT);
                    break;
                case "7":
                    map.put("maxCPU",IAppConstants.KAFKA_DEFAULT_CPU_LIMIT);
                    map.put("maxMemory",IAppConstants.KAFKA_DEFAULT_MEMORY_LIMIT);
                    break;
                case "8":
                    map.put("maxCPU",IAppConstants.ELASTICSEARCH_DEFAULT_CPU_LIMIT);
                    map.put("maxMemory",IAppConstants.ELASTICSEARCH_DEFAULT_MEMORY_LIMIT);
                    break;
                default:break;
            }
        }else{
            AppConfigVo configVo = appConfigBpo.getAppConfig(vo.getId());
            map.put("maxCPU",configVo.getMaxCPU());
            map.put("maxMemory",configVo.getMaxMemory());
            map.put("appStatus",IAppConstants.APP_STATUS_UNSTART);
            map.put("appImageId",configVo.getAppImageId());
        }
        result.setSuccess(true);
        result.setData(map);
        return result;
    }
}
