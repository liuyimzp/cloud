package com.yinhai.cloud.module.application.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.bpo.IAppBpo;
import com.yinhai.cloud.module.application.api.bpo.IAppConfigBpo;
import com.yinhai.cloud.module.application.api.bpo.IAppVersionBpo;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.util.AppUtil;
import com.yinhai.cloud.module.application.api.util.AppVersionUtil;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.application.api.vo.AppVersionVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppImageBpo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppRepertoryBpo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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
@RequestMapping({"/appVersion"})
public class AppVersionController extends BaseController {
    @Resource
    IAppVersionBpo appVersionBpo;

    @Resource
    IAppConfigBpo appConfigBpo;

    @Resource
    IAppRepertoryBpo appRepertoryBpo;

    @Resource
    IAppBpo appBpo;

    @Resource
    IAppImageBpo appImageBpo;

    @RequestMapping("/getAll.do")
    @ResponseBody
    public CommonResultVo getAllApps(@RequestBody AppVo vo) {
        CommonResultVo result = new CommonResultVo();
        List<AppVersionVo> list = appVersionBpo.getAllByAppId(vo.getId());
        result.setData(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/backVersion.do")
    @ResponseBody
    public CommonResultVo backVersion(@RequestBody AppVersionVo vo) {
        CommonResultVo result = new CommonResultVo();
        vo.setIsAction(IAppConstants.APPVERSION_ACTION);
        vo.setIsuse(IAppConstants.APPVERSION_IS_USE);
        appVersionBpo.updateVersion(vo);
        appVersionBpo.setVersionIsUse(vo);
        try {
            //将老版本应用启动起来
            AppUtil.applyApp(vo.getAppId());
            //将流量指向指定应用
            AppVersionUtil.backVersion(vo);
            //删除新版本老版本
            AppUtil.deleteHostroy(vo.getAppId());
        }catch (Exception e){
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        result.setSuccess(true);
        result.setErrorMsg("成功回滚到" + vo.getVersionNameStr() + "版本");
        return result;
    }

    @RequestMapping("/getVersionImages.do")
    @ResponseBody
    public CommonResultVo getVersionImages(@RequestBody AppVersionVo vo) {
        CommonResultVo result = new CommonResultVo();
        AppConfigVo appConfig = appConfigBpo.getAppConfig(vo.getAppId());
        if (appConfig == null){
            result.setErrorMsg("该应用还未发布版本");
            result.setSuccess(false);
            return result;
        }
        AppImageVo appImageVo = appImageBpo.getAppImage(appConfig.getAppImageId());
        AppVo appVo = appBpo.getApp(vo.getAppId());
        List<AppImageVo> images = appImageBpo.getAppImageByReId(appImageVo.getRepertoryId());
        List<Map> list = new ArrayList<>();
        for (AppImageVo image : images) {
            Map<String,Object> map = new HashMap<>();
            map.put("value",image.getId());
            map.put("label",appVo.getAppIdentify()+":"+image.getVersion());
            list.add(map);
        }
        result.setData(list);
        result.setSuccess(true);
        return result;
    }
    @RequestMapping("/saveAppVersion.do")
    @ResponseBody
    public CommonResultVo saveAppVersion(@RequestBody AppVersionVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (vo.getImageId() == null){
            result.setErrorMsg("未选择镜像");
            result.setSuccess(false);
            return result;
        }
        AppConfigVo appConfig = appConfigBpo.getAppConfig(vo.getAppId());
        appConfig.setAppImageId(vo.getImageId());
        appConfigBpo.updateAppConfig(appConfig);
        if (appConfig == null){
            result.setErrorMsg("该应用还未发布版本");
            result.setSuccess(false);
            return result;
        }
        if(!appVersionBpo.getVersionByAppIdAndImage(vo.getAppId(),vo.getImageId()).isEmpty()){
            result.setErrorMsg("该应用已经存在该镜像的版本，可选择版本回退");
            result.setSuccess(false);
            return result;
        }
        AppVersionVo appVersionVo = new AppVersionVo();
        appVersionVo.setImageId(appConfig.getAppImageId());
        appVersionVo.setAppId(vo.getAppId());
        appVersionVo.setIsuse(IAppConstants.APPVERSION_IS_USE);
        appVersionVo.setIsAction(IAppConstants.APPVERSION_ACTION);
        appVersionVo = appVersionBpo.saveVersion(appVersionVo);
        appVersionBpo.setVersionIsUse(appVersionVo);
        try {
            AppUtil.applyApp(vo.getAppId());
            //删除老版本
            AppUtil.deleteHostroy(vo.getAppId());
        } catch (Exception e) {
            result.setErrorMsg(e.getMessage());
            result.setSuccess(false);
            return result;
        }
        result.setSuccess(true);
        return result;
    }
    @RequestMapping("/deleteVersion.do")
    @ResponseBody
    public CommonResultVo deleteVersion(@RequestBody AppVersionVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (vo.getId() == null){
            result.setErrorMsg("请选择版本信息");
            result.setSuccess(false);
            return result;
        }
        AppConfigVo appConfig = appConfigBpo.getAppConfig(vo.getAppId());
        if (appConfig == null){
            result.setErrorMsg("该应用还未发布版本");
            result.setSuccess(false);
            return result;
        }
        AppVersionVo version = appVersionBpo.getVersion(vo.getId());
        if (version == null){
            result.setErrorMsg("该版本已经被删除");
            result.setSuccess(false);
            return result;
        }
        if (IAppConstants.APPVERSION_IS_USE.equals(version.getIsuse())){
            result.setErrorMsg("该版本正在被使用，不可删除！");
            result.setSuccess(false);
            return result;
        }
        try {
            appVersionBpo.deleteVersion(vo);
        } catch (Exception e) {
            result.setErrorMsg(e.getMessage());
            result.setSuccess(false);
            return result;
        }
        result.setSuccess(true);
        return result;
    }
}
