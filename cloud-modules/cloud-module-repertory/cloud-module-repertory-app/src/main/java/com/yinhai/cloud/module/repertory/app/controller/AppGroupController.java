package com.yinhai.cloud.module.repertory.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppGroupBpo;
import com.yinhai.cloud.module.repertory.api.vo.AppGroupVo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by tianhy on 2018/7/30.
 * 应用分组
 */
@Controller
@RequestMapping({"/appGroup"})
public class AppGroupController extends BaseController {

    @Resource
    private IAppGroupBpo appGroupBpo;

    @RequestMapping("/getAll.do")
    @ResponseBody
    public CommonResultVo getAppGroups(@RequestBody AppGroupVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getBusinessArea())) {
            result.setSuccess(false);
            result.setErrorMsg("所属业务领域不能为空!");
            return result;
        }
        result.setSuccess(true);
        result.setData(appGroupBpo.getAppGroups(vo));
        return result;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public CommonResultVo saveAppGroup(@RequestBody AppGroupVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getBusinessArea())) {
            result.setSuccess(false);
            result.setErrorMsg("所属业务领域不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getGroupIdentify())) {
            result.setSuccess(false);
            result.setErrorMsg("应用分组标识不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getGroupName())) {
            result.setSuccess(false);
            result.setErrorMsg("应用分组名称不能为空!");
            return result;
        }
        if (appGroupBpo.checkIdentifyExist(vo.getGroupIdentify())) {
            result.setSuccess(false);
            result.setErrorMsg("应用分组标识已存在!");
            return result;
        }
        AppGroupVo appGroupVo = appGroupBpo.saveAppGroup(vo);
        result.setSuccess(true);
        result.setData(appGroupVo);
        return result;
    }
}
