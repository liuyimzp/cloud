package com.yinhai.cloud.module.application.app.controller;


import com.yinhai.cloud.core.api.constant.ConfigPropKey;
import com.yinhai.cloud.core.api.ssh.SlashPath;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.bpo.IAppBpo;
import com.yinhai.cloud.module.application.api.bpo.IAppConfigBpo;
import com.yinhai.cloud.module.application.api.bpo.IAppPVBpo;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.util.PersistentVolumeUtil;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.application.api.vo.AppPVVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppImageBpo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.resource.pv.api.bpo.IPersistentVolumeBpo;
import com.yinhai.cloud.module.resource.pv.api.vo.PvVo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by tianhy on 2018/7/9.
 * 应用存储(PV)配置
 */
@Controller
@RequestMapping({"/appPV"})
public class AppPVController extends BaseController {

    @Resource
    private IAppBpo appBpo;
    @Resource
    private IAppConfigBpo appConfigBpo;
    @Resource
    private IAppImageBpo appImageBpo;
    @Resource
    private IAppPVBpo appPVBpo;
    @Resource
    private IPersistentVolumeBpo persistentVolumeBpo;

    @RequestMapping("/getPVs.do")
    @ResponseBody
    public CommonResultVo getPVs(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能未空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getId());
        List<PvVo> list = persistentVolumeBpo.queryAll(appVo.getClusterId().toString());
        List<AppPVVo> list2 = appPVBpo.getPVsByAppId(vo.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("allData", list);
        map.put("existData", list2);
        result.setSuccess(true);
        result.setData(map);
        return result;
    }

    @RequestMapping("/savePV.do")
    @ResponseBody
    public CommonResultVo savePV(@RequestBody AppPVVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getAppId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能未空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getVolumeId())) {
            result.setSuccess(false);
            result.setErrorMsg("PVID不能未空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getPath())) {
            result.setSuccess(false);
            result.setErrorMsg("容器内路径不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getSpace())) {
            result.setSuccess(false);
            result.setErrorMsg("空间大小不能为空!");
            return result;
        }
        AppVo appVo = appBpo.getApp(vo.getAppId());
        result = PersistentVolumeUtil.checkPVSpaceEnough(vo.getVolumeId(), vo.getSpace(), appVo.getInstanceNum());
        if (!result.isSuccess()) {
            return result;
        }
        PersistentVolumeUtil.changePVSpace(vo.getVolumeId(), vo.getSpace(), appVo.getInstanceNum());
        AppPVVo appPVVo = appPVBpo.savePV(vo);
        result.setData(appPVVo.getId());
        return result;
    }

    @RequestMapping("/check/container/path.do")
    @ResponseBody
    public CommonResultVo checkContainerPath(@RequestBody AppPVVo checkInfo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(checkInfo.getAppId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能未空!");
            return result;
        }
        if (ValidateUtil.isEmpty(checkInfo.getPath())) {
            result.setSuccess(false);
            result.setErrorMsg("容器内路径不能未空!");
            return result;
        }
        AppConfigVo appConfig = appConfigBpo.getAppConfig(checkInfo.getAppId());
        AppImageVo appImage = appImageBpo.getAppImage(appConfig.getAppImageId());
        boolean needCheckPath = Objects.equals(appImage.getType(), IAppConstants.IMAGE_TYPE_DIY);
        if (!needCheckPath) {
            result.setSuccess(true);
            return result;
        }
        String path = new SlashPath(ServerCmdConstant.FILE_SEPARATOR + checkInfo.getPath()).getFullPath().trim();
        String prefix = SystemConfigUtil.getSystemConfigValue(ConfigPropKey.DIY_IMAGE_UNSUPPORTED_PATH_PREFIX);
        if (path.equals(prefix) || path.equals(prefix + "/")) {
            result.setSuccess(false);
            result.setErrorMsg("容器内路径 [ " + path + " ] 不能为Tomcat Home路径" + prefix);
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/removePV.do")
    @ResponseBody
    public CommonResultVo removePV(@RequestBody AppPVVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用存储配置ID不能未空!");
            return result;
        }
        AppPVVo appPVVo = appPVBpo.getPV(vo.getId());
        AppVo appVo = appBpo.getApp(appPVVo.getAppId());
        PersistentVolumeUtil.changePVSpace(appPVVo.getVolumeId(), appPVVo.getSpace(), 0 - appVo.getInstanceNum());
        appPVBpo.removePV(vo.getId());
        result.setSuccess(true);
        return result;
    }

}
