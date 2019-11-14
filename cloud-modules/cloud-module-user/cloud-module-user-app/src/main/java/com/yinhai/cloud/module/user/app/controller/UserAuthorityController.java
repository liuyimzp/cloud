package com.yinhai.cloud.module.user.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.user.api.bpo.IUserAuthorityBpo;
import com.yinhai.cloud.module.user.api.vo.UserAuthorityListVo;
import com.yinhai.cloud.module.user.api.vo.UserAuthorityVo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by tianhy on 2018/9/11.
 */
@Controller
@RequestMapping({"/userAuthority"})
public class UserAuthorityController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(UserAuthorityController.class);

    @Resource
    private IUserAuthorityBpo userAuthorityBpo;

    @RequestMapping("/getAllAppAuthoritys.do")
    @ResponseBody
    public CommonResultVo getAllAppAuthoritys(@RequestBody UserAuthorityVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getUserId())) {
            result.setSuccess(false);
            result.setErrorMsg("人员ID不能为空!");
            return result;
        }
        List<UserAuthorityVo> list = userAuthorityBpo.getAllAppAuthorityNodes();
        List<UserAuthorityVo> list2 = userAuthorityBpo.getUserAppAuthoritys(vo.getUserId());
        result.setSuccess(true);
        result.setData(buildTree(list, list2));
        return result;
    }

    private List<Map> buildTree(List<UserAuthorityVo> authorityList, List<UserAuthorityVo> userAuthorityList) {
        Set<Long> userAuthoritys = new HashSet<>();
        for (UserAuthorityVo userAuthorityVo : userAuthorityList) {
            if (!userAuthoritys.contains(userAuthorityVo.getResourceId())) {
                userAuthoritys.add(userAuthorityVo.getResourceId());
            }
        }
        Map<Long, Map> resultMap = new HashMap<>();
        for (UserAuthorityVo userAuthorityVo : authorityList) {
            if (ValidateUtil.areEqual(IAppConstants.RESOURCE_TYPE_CLUSTER, userAuthorityVo.getResourceType())
                    || ValidateUtil.areEqual(IAppConstants.RESOURCE_TYPE_BUSINESSAREA, userAuthorityVo.getResourceType())) {
                try {
                    Map nodeMap = userAuthorityVo.toMap();
                    if (userAuthoritys.contains(userAuthorityVo.getResourceId())) {
                        nodeMap.put("checked", true);
                    }
                    resultMap.put(userAuthorityVo.getResourceId(), nodeMap);
                } catch (Exception e) {
                    logger.error(logger.getName() + "context",e);
                }
            }
        }
        for (UserAuthorityVo userAuthorityVo : authorityList) {
            if (!ValidateUtil.isEmpty(userAuthorityVo.getpId())) {
                Map pNodeMap = resultMap.get(userAuthorityVo.getpId());
                if (!ValidateUtil.isEmpty(pNodeMap)) {
                    try {
                        Map nodeMap = userAuthorityVo.toMap();
                        if (userAuthoritys.contains(userAuthorityVo.getResourceId())) {
                            nodeMap.put("checked", true);
                        }
                        List<Map> children = (List<Map>) pNodeMap.get("children");
                        if (ValidateUtil.isEmpty(children)) {
                            children = new ArrayList<>();
                        }
                        children.add(nodeMap);
                        pNodeMap.put("children", children);
                        resultMap.put(userAuthorityVo.getpId(), pNodeMap);
                    } catch (Exception e) {
                        logger.error(logger.getName() + "context",e);
                    }
                }
            }
        }
        return new ArrayList<>(resultMap.values());
    }

    @RequestMapping("/getUserAppAuthoritys.do")
    @ResponseBody
    public CommonResultVo getUserAppAuthoritys(@RequestBody UserAuthorityVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getUserId())) {
            result.setSuccess(false);
            result.setErrorMsg("人员ID不能为空!");
            return result;
        }
        List<UserAuthorityVo> list = userAuthorityBpo.getUserAppAuthoritys(vo.getUserId());
        result.setSuccess(true);
        result.setData(list);
        return result;
    }

    @RequestMapping("/getAllImageAuthoritys.do")
    @ResponseBody
    public CommonResultVo getAllImageAuthoritys(@RequestBody UserAuthorityVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getUserId())) {
            result.setSuccess(false);
            result.setErrorMsg("人员ID不能为空!");
            return result;
        }
        List<UserAuthorityVo> list = userAuthorityBpo.getAllImageAuthorityNodes();
        List<UserAuthorityVo> list2 = userAuthorityBpo.getUserImageAuthoritys(vo.getUserId());
        result.setSuccess(true);
        result.setData(buildTree(list, list2));
        return result;
    }

    @RequestMapping("/getUserImageAuthoritys.do")
    @ResponseBody
    public CommonResultVo getUserImageAuthoritys(@RequestBody UserAuthorityVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getUserId())) {
            result.setSuccess(false);
            result.setErrorMsg("人员ID不能为空!");
            return result;
        }
        List<UserAuthorityVo> list = userAuthorityBpo.getUserImageAuthoritys(vo.getUserId());
        result.setSuccess(true);
        result.setData(list);
        return result;
    }

    @RequestMapping("/saveAuthoritys.do")
    @ResponseBody
    public CommonResultVo saveAuthoritys(@RequestBody UserAuthorityListVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getUserId())) {
            result.setSuccess(false);
            result.setErrorMsg("人员ID不能为空!");
            return result;
        }
        if (ValidateUtil.isNotEmpty(vo.getAddList())) {
            userAuthorityBpo.saveAuthoritys(vo.getUserId(), vo.getAddList());
        }
        if (ValidateUtil.isNotEmpty(vo.getRemoveList())) {
            userAuthorityBpo.removeAuthoritys(vo.getUserId(), vo.getRemoveList());
        }
        result.setSuccess(true);
        return result;
    }
}
