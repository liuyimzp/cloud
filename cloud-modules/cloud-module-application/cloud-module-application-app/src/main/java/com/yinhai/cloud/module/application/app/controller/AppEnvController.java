package com.yinhai.cloud.module.application.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.bpo.IAppEnvBpo;
import com.yinhai.cloud.module.application.api.vo.AppEnvVo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tianhy on 2018/9/6.
 */
@Controller
@RequestMapping({"/appEnv"})
public class AppEnvController extends BaseController {

    @Resource
    private IAppEnvBpo appEnvBpo;

    @RequestMapping("/getAppEnvs.do")
    @ResponseBody
    public CommonResultVo getAppEnvs(@RequestBody AppEnvVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getAppId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        List<AppEnvVo> list = appEnvBpo.getAppEnvs(vo.getAppId());
        result.setSuccess(true);
        result.setData(list);
        return result;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public CommonResultVo saveAppEnv(@RequestBody AppEnvVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getAppId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用ID不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getEnvKey())) {
            result.setSuccess(false);
            result.setErrorMsg("参数名称不能为空!");
            return result;
        }
        if (appEnvBpo.checkAppIdAndEnvKeyExist(vo.getAppId(), vo.getEnvKey())) {
            result.setSuccess(false);
            result.setErrorMsg("该参数已存在!");
            return result;
        }

        if (ValidateUtil.isEmpty(vo.getEnvValue())) {
            result.setSuccess(false);
            result.setErrorMsg("参数值不能为空!");
            return result;
        }
        AppEnvVo appEnvVo = appEnvBpo.saveAppEnv(vo);
        result.setSuccess(true);
        result.setData(appEnvVo);
        return result;
    }

    @RequestMapping("/edit.do")
    @ResponseBody
    public CommonResultVo editAppEnv(@RequestBody AppEnvVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用参数ID不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getEnvValue())) {
            result.setSuccess(false);
            result.setErrorMsg("参数值不能为空!");
            return result;
        }
        appEnvBpo.editAppEnv(vo);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/remove.do")
    @ResponseBody
    public CommonResultVo removeAppEnv(@RequestBody AppEnvVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用参数ID不能为空!");
            return result;
        }
        appEnvBpo.removeAppEnv(vo.getId());
        result.setSuccess(true);
        return result;
    }
}
