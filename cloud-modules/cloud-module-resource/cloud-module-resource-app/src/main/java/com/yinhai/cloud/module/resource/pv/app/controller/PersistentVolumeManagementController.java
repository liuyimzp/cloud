package com.yinhai.cloud.module.resource.pv.app.controller;

import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.util.UserUtils;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.bpo.IAppPVBpo;
import com.yinhai.cloud.module.repertory.api.constant.IPersistentVolumeConstant;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.pv.api.bpo.IPersistentVolumeBpo;
import com.yinhai.cloud.module.resource.pv.api.vo.DiskVo;
import com.yinhai.cloud.module.resource.pv.api.vo.PvVo;
import com.yinhai.cloud.module.resource.pv.api.vo.StorageVo;
import com.yinhai.cloud.module.resource.util.NfsInstall;
import com.yinhai.modules.security.api.vo.UserAccountInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

;

/**
 * 存储空间管理
 *
 * @author jianglw
 */
@RequestMapping("/pv")
@Controller
public class PersistentVolumeManagementController {

    @Resource
    private IPersistentVolumeBpo pvBpo;

    @Resource
    private IAppPVBpo appPVBpo;

    @RequestMapping("/queryAll.do")
    @ResponseBody
    public CommonResultVo queryAllPv(@RequestBody PvVo vo) {
        final CommonResultVo resultVo = new CommonResultVo();
        final List<PvVo> voList = pvBpo.queryAll(vo.getClusterId());
        resultVo.setData(voList);
        resultVo.setSuccess(true);
        return resultVo;
    }

    @RequestMapping("/detail.do")
    @ResponseBody
    public CommonResultVo getStroageInfo(@RequestBody PvVo vo) {
        final CommonResultVo resultVo = new CommonResultVo();
        final PvVo voList = pvBpo.queryByPvId(vo.getVolumeId());
        resultVo.setData(voList);
        resultVo.setSuccess(true);
        return resultVo;
    }

    @RequestMapping("/addPv.do")
    @ResponseBody
    public CommonResultVo addPv(@RequestBody PvVo vo, HttpServletRequest request) {
        final CommonResultVo resultVo = new CommonResultVo();
        final UserAccountInfo userAccountInfo = (UserAccountInfo) request.getSession(false).getAttribute(ResourceConstant.USER_INFO_KEY);
        final String userId = userAccountInfo.getUserId();
        vo.setVolumeCreateUser(userId);
        vo.setVolumeCreateDate(new Date());
        try {
            pvBpo.addPv(vo);
            resultVo.setSuccess(true);
        } catch (Exception e) {
            resultVo.setSuccess(false);
            resultVo.setErrorMsg(e.getMessage());
        }

        return resultVo;
    }

    @RequestMapping("/deletePv.do")
    @ResponseBody
    public CommonResultVo deletePv(@RequestBody PvVo vo) {
        final CommonResultVo resultVo = new CommonResultVo();
        try {
            pvBpo.deletePv(vo);
            resultVo.setSuccess(true);
        } catch (Exception e) {
            resultVo.setSuccess(false);
            resultVo.setErrorMsg(e.getMessage());
        }

        return resultVo;
    }

    @RequestMapping("/updatePv.do")
    @ResponseBody
    public CommonResultVo updatePv(@RequestBody PvVo vo) {
        final CommonResultVo resultVo = new CommonResultVo();
        try {
            pvBpo.updatePv(vo);
            resultVo.setSuccess(true);
        } catch (Exception e) {
            resultVo.setSuccess(false);
            resultVo.setErrorMsg(e.getMessage());
        }
        return resultVo;
    }

    @RequestMapping("/getDiskMounts.do")
    @ResponseBody
    public CommonResultVo getDiskMounts(@RequestBody StorageVo vo) {
        final CommonResultVo resultVo = new CommonResultVo();
        try {
            List<DiskVo> diskMounts = pvBpo.getDiskMountList(vo);
            //检查
            boolean existNfs = NfsInstall.existNfs(vo);
            if (existNfs) {
                resultVo.setSuccess(true);
                resultVo.setData(diskMounts);
            } else {
                resultVo.setSuccess(false);
                resultVo.setErrorMsg("NFS不可用");
            }
        } catch (Exception e) {
            resultVo.setSuccess(false);
            resultVo.setErrorMsg(e.getMessage());
        }

        return resultVo;
    }

    @RequestMapping("/addStorage.do")
    @ResponseBody
    public CommonResultVo addStorage(@RequestBody StorageVo vo, ServletRequest request) {
        CommonResultVo resultVo = new CommonResultVo();
        vo.setStorageCreateUser(UserUtils.getUserId(request));
        vo.setStorageCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));

        try {
            Long storageId = pvBpo.addStorage(vo);
            resultVo.setData(storageId);
            resultVo.setSuccess(true);
        } catch (Exception e) {
            resultVo.setSuccess(false);
            resultVo.setErrorMsg(e.getMessage());
        }
        return resultVo;
    }

    @RequestMapping("/queryAllStorage.do")
    @ResponseBody
    public CommonResultVo queryAllStorage() {
        final CommonResultVo resultVo = new CommonResultVo();
;        List<StorageVo> storageVos = null;
        try {
            storageVos = pvBpo.queryAllStorages();
        } catch (Exception e) {
            resultVo.setSuccess(false);
            resultVo.setErrorMsg(e.getMessage());
        }
        resultVo.setData(storageVos);
        resultVo.setSuccess(true);
        return resultVo;
    }

    @RequestMapping("/checkStorage.do")
    @ResponseBody
    public CommonResultVo checkStorage(@RequestBody StorageVo vo) {
        final CommonResultVo resultVo = new CommonResultVo();
        try {
            List<DiskVo> diskMount = pvBpo.checkStorages(vo);
            boolean existNfs = NfsInstall.existNfs(vo);
            if (!NfsInstall.activeNfs(vo)){
                existNfs = false;
            }
            if (existNfs) {
                resultVo.setSuccess(true);
                resultVo.setData(diskMount);

            } else {
                resultVo.setSuccess(false);
                resultVo.setErrorMsg("NFS不可用");
            }
        } catch (Exception e) {
            resultVo.setSuccess(false);
            resultVo.setErrorMsg(e.getMessage());
        }
        return resultVo;
    }

    @RequestMapping("/deleteStorage.do")
    @ResponseBody
    public CommonResultVo deleteStorage(@RequestBody StorageVo vo) {
        CommonResultVo resultVo = new CommonResultVo();
        PvVo pvVo = pvBpo.queryByStorageId(vo.getStorageId());
        if (pvVo != null) {
            if (appPVBpo.checkPVUsed(pvVo.getVolumeId())) {
                resultVo.setSuccess(false);
                resultVo.setErrorMsg("该目录 下的存储 有应用在使用，请先删除应用");
                return resultVo;
            }
        }
        try {
            if (pvVo != null) {
                pvBpo.deletePv(pvVo);
            }
            pvBpo.deleteStorage(vo);
            resultVo.setSuccess(true);
        } catch (SSHConnectionException e) {
            resultVo.setSuccess(false);
            resultVo.setData(IPersistentVolumeConstant.SSH_CONNECT_DEFEATED);
            resultVo.setErrorMsg("无法连接云存储");
        } catch (Exception e) {
            resultVo.setSuccess(false);
            resultVo.setErrorMsg(e.getMessage());
        }
        return resultVo;
    }

    @RequestMapping("/deleteStorageForce.do")
    @ResponseBody
    /**
     * 无法连接云存储时强制删除
     */
    public CommonResultVo deleteStorageForce(@RequestBody StorageVo vo) {
        CommonResultVo resultVo = new CommonResultVo();
        pvBpo.deleteStorageForce(vo);
        resultVo.setSuccess(true);
        return resultVo;
    }

    @RequestMapping("/stopNfs.do")
    @ResponseBody
    /**
     * 停止节点nfs服务
     */
    public CommonResultVo stopNfs(@RequestBody StorageVo vo) {
        CommonResultVo resultVo = new CommonResultVo();
        try {
            pvBpo.stopNfs(vo);
        } catch (Exception e) {
            resultVo.setErrorMsg(e.getMessage());
            resultVo.setSuccess(false);
            return resultVo;
        }
        resultVo.setSuccess(true);
        return resultVo;
    }

    @RequestMapping("/startNfs.do")
    @ResponseBody
    /**
     * 停止节点nfs服务
     */
    public CommonResultVo startNfs(@RequestBody StorageVo vo) {
        CommonResultVo resultVo = new CommonResultVo();
        try {
            pvBpo.startNfs(vo);
        } catch (Exception e) {
            resultVo.setErrorMsg(e.getMessage());
            resultVo.setSuccess(false);
            return resultVo;
        }
        resultVo.setSuccess(true);
        return resultVo;
    }

}
