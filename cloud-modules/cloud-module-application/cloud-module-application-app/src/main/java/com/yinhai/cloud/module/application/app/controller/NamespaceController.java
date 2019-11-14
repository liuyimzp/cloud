package com.yinhai.cloud.module.application.app.controller;

import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.application.api.bpo.IAppBpo;
import com.yinhai.cloud.module.application.api.bpo.INamespaceBpo;
import com.yinhai.cloud.module.application.api.util.AppVersionUtil;
import com.yinhai.cloud.module.application.api.util.NamespaceUtil;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.application.api.vo.NamespaceVo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.user.api.bpo.IUserAuthorityBpo;
import com.yinhai.core.app.ta3.web.controller.BaseController;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tianhy on 2018/7/6.
 */
@Controller
@RequestMapping({"/namespace"})
public class NamespaceController extends BaseController {

    @Resource
    private INamespaceBpo namespaceBpo;

    @Resource
    private IClusterBpo clusterBpo;
    @Resource
    private IAppBpo appBpo;

    @Resource
    private IUserAuthorityBpo userAuthorityBpo;

    @RequestMapping("/get.do")
    @ResponseBody
    public CommonResultVo getNamespaces(@RequestBody NamespaceVo vo) {
        CommonResultVo result = new CommonResultVo();
        Long userid = getTaDto().getUser().getUserid();
        if (ValidateUtil.areEqual(ServerCmdConstant.ADMIN_ID, userid)){
            result.setData(namespaceBpo.getNamespaces(vo.getClusterId()));
        }else {
            List<Long> nameSpaceIds = userAuthorityBpo.getUserNameSpaceIds(userid);
            result.setData(namespaceBpo.getNamespaces(vo.getClusterId()).stream().filter(nvo -> nameSpaceIds.contains(nvo.getId())).collect(Collectors.toList()));
        }
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public CommonResultVo addNamespace(@RequestBody NamespaceVo vo) {
        CommonResultVo result = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo.getClusterId())) {
            result.setSuccess(false);
            result.setErrorMsg("集群ID不能未空!");
            return result;
        }
        if (ValidateUtil.isEmpty(vo.getNamespaceIdentify())) {
            result.setSuccess(false);
            result.setErrorMsg("命名空间标识不能未空!");
            return result;
        }
        Map<String,CommonResultVo> map = ApplicationDuplicationCode.getBolNameSpace(vo);
        if (!map.get("result").isSuccess()){
            return (CommonResultVo)map.get("result");
        }
        if (namespaceBpo.checkIdentifyExist(vo.getClusterId(), vo.getNamespaceIdentify())) {
            result.setSuccess(false);
            result.setErrorMsg("当前集群下已存在该命名空间!");
            return result;
        }
        ClusterVo clusterVo = clusterBpo.getClusterById(vo.getClusterId());
        if(ValidateUtil.isEmpty(clusterVo)){
            result.setSuccess(false);
            result.setErrorMsg("集群不存在!");
            return result;
        }
        if(vo.getMaxCPULimit() > clusterVo.getTotalCpuAvailable()){
            result.setSuccess(false);
            result.setErrorMsg("命名空间配置CPU超过集群可用CPU数量!");
            return result;
        }
        if(vo.getMaxMemoryLimit() > clusterVo.getTotalMemAvailable()){
            result.setSuccess(false);
            result.setErrorMsg("命名空间配置内存超过集群可用内存大小!");
            return result;
        }
        try {
            NamespaceUtil.createNamespace(vo);
            AppVersionUtil.createGateWay(vo);
        } catch (SSHConnectionException e) {
            result.setSuccess(false);
            result.setErrorMsg("无法连接该master节点!");
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        vo.setAvailableCPU(vo.getMaxCPULimit());
        vo.setAvailableMemory(vo.getMaxMemoryLimit());
        NamespaceVo namespaceVo2 = namespaceBpo.saveNamespace(vo);
        clusterVo.setTotalCpuAvailable(clusterVo.getTotalCpuAvailable() - vo.getMaxCPULimit());
        clusterVo.setTotalMemAvailable(clusterVo.getTotalMemAvailable() - vo.getMaxMemoryLimit());
        clusterBpo.updateClusterById(clusterVo);
        result.setSuccess(true);
        result.setData(namespaceVo2);
        return result;
    }

    @RequestMapping("/edit.do")
    @ResponseBody
    public CommonResultVo editNamespace(@RequestBody NamespaceVo vo) {
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getId())){
            result.setSuccess(false);
            result.setErrorMsg("命名空间ID不能为空!");
            return result;
        }
        Map<String,CommonResultVo> map = ApplicationDuplicationCode.getBolNameSpace(vo);
        if (!map.get("result").isSuccess()){
            return (CommonResultVo)map.get("result");
        }
        NamespaceVo namespaceVo = namespaceBpo.getNamespace(vo.getId());
        if(ValidateUtil.areEqual(vo.getMaxCPULimit(), namespaceVo.getMaxCPULimit()) && ValidateUtil.areEqual(vo.getMaxMemoryLimit(), namespaceVo.getMaxMemoryLimit())){
            vo.setAvailableCPU(namespaceVo.getAvailableCPU());
            vo.setAvailableMemory(namespaceVo.getAvailableMemory());
            namespaceBpo.updateNamespace(vo);
            result.setSuccess(true);
            return result;
        }
        if(namespaceVo.getMaxCPULimit() - namespaceVo.getAvailableCPU() > vo.getMaxCPULimit()){
            result.setSuccess(false);
            result.setErrorMsg("命名空间使用CPU数量已超过修改后的CPU总量!");
            return result;
        }
        if(namespaceVo.getMaxMemoryLimit() - namespaceVo.getAvailableMemory() > vo.getMaxMemoryLimit()){
            result.setSuccess(false);
            result.setErrorMsg("命名空间使用内存数量已超过修改后的内存总量大小!");
            return result;
        }
        ClusterVo clusterVo = clusterBpo.getClusterById(namespaceVo.getClusterId());
        if(vo.getMaxCPULimit() - namespaceVo.getMaxCPULimit() > clusterVo.getTotalCpuAvailable()){
            result.setSuccess(false);
            result.setErrorMsg("增加的CPU数量超过集群可用CPU数量!");
            return result;
        }
        if(vo.getMaxMemoryLimit() - namespaceVo.getMaxMemoryLimit() > clusterVo.getTotalMemAvailable()){
            result.setSuccess(false);
            result.setErrorMsg("增加的内存大小超过集群可用内存大小!");
            return result;
        }
        try {
            vo.setNamespaceIdentify(namespaceVo.getNamespaceIdentify());
            vo.setClusterId(namespaceVo.getClusterId());
            NamespaceUtil.updateNamespaceResourceQuota(vo);
        } catch (SSHConnectionException e) {
            result.setSuccess(false);
            result.setErrorMsg("无法连接该master节点!");
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        vo.setAvailableCPU(vo.getMaxCPULimit() - (namespaceVo.getMaxCPULimit() - namespaceVo.getAvailableCPU()));
        vo.setAvailableMemory(vo.getMaxMemoryLimit() - (namespaceVo.getMaxMemoryLimit() - namespaceVo.getAvailableMemory()));
        namespaceBpo.updateNamespace(vo);
        clusterVo.setTotalCpuAvailable(clusterVo.getTotalCpuAvailable() - (vo.getMaxCPULimit() - namespaceVo.getMaxCPULimit()));
        clusterVo.setTotalMemAvailable(clusterVo.getTotalMemAvailable() - (vo.getMaxMemoryLimit() - namespaceVo.getMaxMemoryLimit()));
        clusterBpo.updateClusterById(clusterVo);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping("/delete.do")
    @ResponseBody
    public CommonResultVo deleteNamespace(@RequestBody NamespaceVo vo) {
        CommonResultVo result = new CommonResultVo();
        if(ValidateUtil.isEmpty(vo.getId())){
            result.setSuccess(false);
            result.setErrorMsg("命名空间ID不能为空!");
            return result;
        }

        //命名空间删除前先判断命名空间下的应用是否被删除
        List<AppVo> appVoList = appBpo.getAppVoByNamespaceId(vo.getId());
        if(!appVoList.isEmpty()){
            result.setSuccess(false);
            result.setErrorMsg("请先删除当前命名空间下的应用!");
            return result;
        }
        NamespaceVo namespaceVo = namespaceBpo.getNamespace(vo.getId());

        try {
            NamespaceUtil.deleteNamespace(namespaceVo);
        } catch (SSHConnectionException e) {
            result.setSuccess(false);
            result.setErrorMsg("无法连接该master节点!");
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }
        ClusterVo clusterVo = clusterBpo.getClusterById(namespaceVo.getClusterId());
        clusterVo.setTotalCpuAvailable(clusterVo.getTotalCpuAvailable() + namespaceVo.getMaxCPULimit());
        clusterVo.setTotalMemAvailable(clusterVo.getTotalMemAvailable() + namespaceVo.getMaxMemoryLimit());
        namespaceBpo.deleteNamespace(vo.getId());
        clusterBpo.updateClusterById(clusterVo);
        result.setSuccess(true);
        return result;
    }
}
