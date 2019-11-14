package com.yinhai.cloud.module.repertory.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppImageBpo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppRepertoryBpo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageQueryVo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.cloud.module.repertory.api.vo.AppRepertoryQueryVo;
import com.yinhai.cloud.module.repertory.api.vo.AppRepertoryVo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.modules.codetable.api.domain.bpo.IAppCodeBpo;
import com.yinhai.modules.codetable.api.domain.vo.AppCodeVo;
import com.yinhai.modules.codetable.api.util.CodeTableUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tianhy on 2018/5/17.
 * 应用仓库管理
 */
@Controller
@RequestMapping({"/appRepertory"})
public class AppRepertoryController extends BaseController {

    @Resource
    private IAppRepertoryBpo appRepertoryBpo;

    @Resource
    private IAppImageBpo appImageBpo;

    @Resource
    private IAppCodeBpo appCodeBpo;

    @RequestMapping("/getAll.do")
    @ResponseBody
    public CommonResultVo getAllAppRepertories(@RequestBody AppRepertoryQueryVo vo) {
        CommonResultVo result = new CommonResultVo();
        IPage<AppRepertoryVo> list = appRepertoryBpo.getQueryAppRepertories(vo,getTaDto().getUser().getUserid());
        result.setSuccess(true);
        result.setData(list);
        return result;
    }

    @RequestMapping("/checkIdentify.do")
    @ResponseBody
    public CommonResultVo checkIdentify(@RequestBody AppRepertoryVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getGroupId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用分组不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getIdentify())) {
            result.setSuccess(false);
            result.setErrorMsg("英文标识不能为空!");
            return result;
        }
        if (appRepertoryBpo.checkIdentifyExist(vo.getGroupId(), vo.getIdentify())) {
            result.setSuccess(false);
            result.setErrorMsg("英文标识已存在!");
            return result;
        }
        ;
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public CommonResultVo saveAppRepertory(@RequestBody AppRepertoryVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getGroupId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用分组不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getBusinessArea())) {
            result.setSuccess(false);
            result.setErrorMsg("所属业务领域不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getAppName())) {
            result.setSuccess(false);
            result.setErrorMsg("应用名称不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getAppType())) {
            result.setSuccess(false);
            result.setErrorMsg("应用类型不能为空!");
            return result;
        }
        result = checkIdentify(vo);
        if (!result.isSuccess()) {
            return result;

        }
        appRepertoryBpo.saveAppRepertory(vo);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/edit.do")
    @ResponseBody
    public CommonResultVo editAppRepertory(@RequestBody AppRepertoryVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用仓库ID不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getBusinessArea())) {
            result.setSuccess(false);
            result.setErrorMsg("所属业务领域不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getGroupId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用分组不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getAppName())) {
            result.setSuccess(false);
            result.setErrorMsg("应用名称不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getAppType())) {
            result.setSuccess(false);
            result.setErrorMsg("应用类型不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getIdentify())) {
            result.setSuccess(false);
            result.setErrorMsg("英文标识不能为空!");
            return result;
        }
        if (!appRepertoryBpo.checkIdentifyChange(vo.getId(), vo.getGroupId(), vo.getIdentify())) {
            result.setSuccess(false);
            result.setErrorMsg("英文标识已存在!");
            return result;
        }
        appRepertoryBpo.editAppRepertory(vo);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/remove.do")
    @ResponseBody
    public CommonResultVo removeAppRepertory(@RequestBody AppRepertoryVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getId())) {
            result.setSuccess(false);
            result.setErrorMsg("应用仓库ID不能为空!");
            return result;
        }
        AppImageQueryVo appImageQueryVo = new AppImageQueryVo();
        appImageQueryVo.setRepertoryId(vo.getId());
        appImageBpo.syncDockerRepertory(appImageQueryVo);
        IPage<AppImageVo> page = appImageBpo.getQueryAppImages(appImageQueryVo);
        if (ValidateUtil.isNotEmpty(page.getList())) {
            result.setSuccess(false);
            result.setErrorMsg("请先删除该应用仓库下所有镜像，再删除该应用仓库!");
            return result;
        }
        appRepertoryBpo.removeAppRepertory(vo.getId());
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/addBusinessArea.do")
    @ResponseBody
    public CommonResultVo addBusinessArea(@RequestBody AppCodeVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getCodeDESC())) {
            result.setSuccess(false);
            result.setErrorMsg("所属业务领域为空!");
            return result;
        }
        List<AppCodeVo> list = CodeTableUtil.getCodeList("BUSINESSAREA", "9999");
        int max = 1;
        for (AppCodeVo appCodeVo : list) {
            if (max < Integer.valueOf(appCodeVo.getCodeValue())) {
                max = Integer.valueOf(appCodeVo.getCodeValue());
            }
        }
        vo.setCodeType("BUSINESSAREA");
        vo.setCodeTypeDESC("所属业务领域");
        vo.setCodeValue(max + 1 + "");
        vo.setYab003("9999");
        vo.setVer(0);
        vo.setValidFlag("0");
        appCodeBpo.insertAa10(vo);
        result.setSuccess(true);
        result.setData(vo);
        return result;
    }

    @RequestMapping("/getUserBusinessArea.do")
    @ResponseBody
    public CommonResultVo getUserBusinessArea(@RequestBody AppCodeVo vo){
        CommonResultVo result = new CommonResultVo();
        Long userId = getTaDto().getUser().getUserid();
        List<AppCodeVo> list = appRepertoryBpo.getUserBusinessArea(vo,userId);
        result.setSuccess(true);
        result.setData(list);
        return result;
    }
}
