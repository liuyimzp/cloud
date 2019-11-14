package com.yinhai.cloud.module.application.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.bpo.IAppBpo;
import com.yinhai.cloud.module.application.api.bpo.IAppServiceBpo;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.vo.AppServiceVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by tianhy on 2018/6/14.
 */
@Controller
@RequestMapping({"/appService"})
public class AppServiceController extends BaseController {
    @Resource
    private IAppServiceBpo appServiceBpo;

    @Resource
    private IAppBpo appBpo;

    @RequestMapping("/get.do")
    @ResponseBody
    public CommonResultVo getAppService(@RequestBody AppServiceVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getAppId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能未空!");
            return result;
        }
        List<AppServiceVo> list = appServiceBpo.getAppServicesByAppId(vo.getAppId());
        result.setSuccess(true);
        result.setData(list);
        return result;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public CommonResultVo saveAppService(@RequestBody AppServiceVo vo) {
        CommonResultVo result = new CommonResultVo();
        Map<String,CommonResultVo> map = ApplicationDuplicationCode.getBolServiceOne(vo);
        if (!map.get("result").isSuccess()){
            return map.get("result");
        }
        if (IAppConstants.SERVICE_TYPE_OUTER.equals(vo.getServiceType())) {
            map = ApplicationDuplicationCode.getBolServiceTwo(vo);
            if (!map.get("result").isSuccess()){
                return map.get("result");
            }
            AppVo appVo = appBpo.getApp(vo.getAppId());
            Boolean isExist = appServiceBpo.checkMappingPortExist(appVo.getClusterId(), vo.getMappingPort());
            if (isExist) {
                result.setSuccess(false);
                result.setErrorMsg("该外部端口已被占用!");
                return result;
            }
        }
        if (ValidateUtil.isEmpty(vo.getProtocolType())) {
            result.setSuccess(false);
            result.setErrorMsg("协议类型不能未空!");
            return result;
        }
        Boolean isExist2 = appServiceBpo.checkInnerPortExist(vo.getAppId(), vo.getInnerPort());
        if (isExist2) {
            result.setSuccess(false);
            result.setErrorMsg("该内部端口已被占用!");
            return result;
        }
        appServiceBpo.saveAppService(vo);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/edit.do")
    @ResponseBody
    public CommonResultVo editAppService(@RequestBody AppServiceVo vo) {
        CommonResultVo result = new CommonResultVo();
        Map<String,CommonResultVo> map = ApplicationDuplicationCode.getBolServiceOne(vo);
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用服务配置ID不能未空!");
            return result;
        }
        if (!map.get("result").isSuccess()){
            return map.get("result");
        }
        if (IAppConstants.SERVICE_TYPE_OUTER.equals(vo.getServiceType())) {
            map = ApplicationDuplicationCode.getBolServiceTwo(vo);
            if (!map.get("result").isSuccess()){
                return map.get("result");
            }
        }
        result = appServiceBpo.checkEditPort(vo);
        if (!result.isSuccess()) {
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getProtocolType())) {
            result.setSuccess(false);
            result.setErrorMsg("协议类型不能未空!");
            return result;
        }
        appServiceBpo.editAppService(vo);
        return result;
    }

    @RequestMapping("/remove.do")
    @ResponseBody
    public CommonResultVo removeAppService(@RequestBody AppServiceVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用服务配置ID不能未空!");
            return result;
        }
        appServiceBpo.removeAppService(vo.getId());
        result.setSuccess(true);
        return result;
    }
}
