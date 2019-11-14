package com.yinhai.cloud.core.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.modules.codetable.api.domain.bpo.IAppCodeBpo;
import com.yinhai.modules.codetable.api.domain.vo.AppCodeVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tianhy on 2018/5/18.
 */
@Controller
@RequestMapping({"appCode"})
public class AppCodeController extends BaseController {
    @Resource
    private IAppCodeBpo appCodeBpo;

    @RequestMapping({"getList.do"})
    @ResponseBody
    public CommonResultVo getList(@RequestBody AppCodeVo vo) throws Exception {
        CommonResultVo result = new CommonResultVo();
        List<AppCodeVo> list = this.appCodeBpo.getAppCodeInAa10(vo).getList();
        result.setSuccess(true);
        result.setData(list);
        return result;
    }
}
