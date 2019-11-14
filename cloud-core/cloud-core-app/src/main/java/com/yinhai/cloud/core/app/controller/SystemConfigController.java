package com.yinhai.cloud.core.app.controller;

import com.yinhai.cloud.core.api.bpo.ISystemConfigBpo;
import com.yinhai.cloud.core.api.util.UserUtils;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.core.api.vo.SystemConfigVo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.modules.codetable.api.domain.bpo.IAppCodeBpo;
import com.yinhai.modules.codetable.api.domain.vo.AppCodeVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.util.List;

/**
 * Created by tianhy on 2018/5/18.
 */
@Controller
@RequestMapping({"sysConfig"})
public class SystemConfigController extends BaseController {
    @Resource
    private ISystemConfigBpo systemConfigBpo;


    @RequestMapping({"list"})
    @ResponseBody
    public CommonResultVo getList(){
        CommonResultVo result = new CommonResultVo();
        result.setData(systemConfigBpo.getAllSystemConfig());
        result.setSuccess(true);
        return result;
    }

    /**
     * 分页查询
     * @return
     */
    @RequestMapping({"listPage"})
    @ResponseBody
    public CommonResultVo getListByPage(@RequestBody SystemConfigVo vo){
        CommonResultVo result = new CommonResultVo();
        result.setData(systemConfigBpo.getAllSystemConfigByPage(vo));
        result.setSuccess(true);
        return result;
    }


    @RequestMapping({"save"})
    @ResponseBody
    public CommonResultVo save(@RequestBody  SystemConfigVo vo,ServletRequest request) {
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getPropKey())){
            result.setSuccess(false);
            result.setErrorMsg("系统配置的键不能为空");
            return result;
        }

        if(ValidateUtil.isEmpty(vo.getPropKey())){
            result.setSuccess(false);
            result.setErrorMsg("系统配置的值不能为空");
            return result;
        }
        List<SystemConfigVo> systemConfigVos = systemConfigBpo.getAllSystemConfig();
        if (!systemConfigVos.isEmpty()){
            for (SystemConfigVo systemConfigVo : systemConfigVos){
                if (vo.getPropKey().equals(systemConfigVo.getPropKey())){
                    result.setSuccess(false);
                    result.setErrorMsg("已经存在键：" + vo.getPropKey());
                    return result;
                }
            }
        }
        vo.setPropCreateUser(UserUtils.getUserId(request));
        result.setData(systemConfigBpo.insertSystemConfig(vo));
        result.setSuccess(true);
        return result;
    }
    @RequestMapping({"update"})
    @ResponseBody
    public CommonResultVo update(@RequestBody  SystemConfigVo vo,ServletRequest request) {
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getPropKey())){
            result.setSuccess(false);
            result.setErrorMsg("系统配置的键不能为空");
            return result;
        }

        if(ValidateUtil.isEmpty(vo.getPropKey())){
            result.setSuccess(false);
            result.setErrorMsg("系统配置的值不能为空");
            return result;
        }
        vo.setPropCreateUser(UserUtils.getUserId(request));
        systemConfigBpo.updateSystemConfig(vo);
        result.setSuccess(true);
        return result;
    }


    @RequestMapping({"delete"})
    @ResponseBody
    public CommonResultVo delete(@RequestBody  SystemConfigVo vo) {
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getPropKey())){
            result.setSuccess(false);
            result.setErrorMsg("系统配置的键不能为空");
            return result;
        }
        systemConfigBpo.deleteSystemConfig(vo.getPropKey());
        result.setSuccess(true);
        return result;
    }

    @RequestMapping({"get"})
    @ResponseBody
    public CommonResultVo get(@RequestBody  SystemConfigVo vo) {
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getPropKey())){
            result.setSuccess(false);
            result.setErrorMsg("系统配置的键不能为空");
            return result;
        }
        result.setSuccess(true);
        result.setData( systemConfigBpo.getSystemConfig(vo.getPropKey()));
        return result;
    }
}
