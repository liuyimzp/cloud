package com.yinhai.cloud.module.user.app.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.user.api.bpo.IUserAuthorityBpo;
import com.yinhai.cloud.module.user.api.vo.UserAuthorityVo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.exception.AppException;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.modules.org.api.bpo.IUserBpo;
import com.yinhai.modules.org.api.vo.user.AddUserVo;
import com.yinhai.modules.org.api.vo.user.QueryUserVo;
import com.yinhai.modules.org.api.vo.user.UserVo;
import com.yinhai.modules.org.ta3.domain.po.IOrg;
import com.yinhai.modules.org.ta3.domain.po.IUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianhy on 2018/9/11.
 */
@Controller
@RequestMapping({"/user"})
public class UserController extends BaseController {

    @Resource
    private IUserBpo userBpo;

    @Resource
    private IUserAuthorityBpo userAuthorityBpo;

    @RequestMapping("/getUsers.do")
    @ResponseBody
    public CommonResultVo getQueryUsers(@RequestBody QueryUserVo vo) {
        CommonResultVo result = new CommonResultVo();
        vo.setStart(ValidateUtil.isEmpty(vo.getStart()) ? 0 : vo.getStart());
        vo.setLimit(ValidateUtil.isEmpty(vo.getLimit()) ? 10 : vo.getLimit());
        vo.setIsShow(true);
        vo.setOrgid(IOrg.ORG_ROOT_ID);
        vo.setNowPositionId(IUser.ROOT_USERID);
        vo.setUserid(getTaDto().getUser().getUserid());
        result.setData(userBpo.queryUsersInfo(vo));
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public CommonResultVo saveUser(@RequestBody UserVo userVo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(userVo.getLoginid())) {
            result.setSuccess(false);
            result.setErrorMsg("登录账号不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(userVo.getPassword())) {
            result.setSuccess(false);
            result.setErrorMsg("密码不能为空!");
            return result;
        }
        if (ValidateUtil.isEmpty(userVo.getName())) {
            result.setSuccess(false);
            result.setErrorMsg("姓名不能为空!");
            return result;
        }
        AddUserVo addUserVo = new AddUserVo();
        addUserVo.setW_loginid(userVo.getLoginid());
        addUserVo.setW_password(userVo.getPassword());
        addUserVo.setW_name(userVo.getName());
        addUserVo.setCurPositionId(this.getTaDto().getUser().getNowPosition().getPositionid());
        addUserVo.setCurUserId(this.getTaDto().getUser().getUserid());
        addUserVo.setW_orgid(IOrg.ORG_ROOT_ID);
        try {
            UserVo user = userBpo.addUser(addUserVo);
            result.setSuccess(true);
            result.setData(user);
        } catch (AppException e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
        }
        return result;
    }

    @RequestMapping("/remove.do")
    @ResponseBody
    public CommonResultVo removeUser(@RequestBody UserAuthorityVo userVo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(userVo.getUserId())) {
            result.setSuccess(false);
            result.setErrorMsg("人员ID不能为空!");
            return result;
        }
        userAuthorityBpo.deleteUserAuthpritysByUserid(userVo.getUserId());
        List<Long> userids = new ArrayList<>();
        userids.add(userVo.getUserId());
        userBpo.deleteUsers(userids, this.getTaDto().getUser().getNowPosition().getPositionid(), this.getTaDto().getUser().getUserid());
        result.setSuccess(true);
        return result;
    }
}
