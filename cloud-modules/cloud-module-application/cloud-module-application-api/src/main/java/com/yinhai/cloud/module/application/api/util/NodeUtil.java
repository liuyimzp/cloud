package com.yinhai.cloud.module.application.api.util;

import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.core.common.api.exception.AppException;
import com.yinhai.core.common.api.util.ValidateUtil;

import java.util.List;

/**
 * Created by tianhy on 2018/11/19.
 */
public class NodeUtil {

    private final static INodeBpo nodeBpo = ServiceLocator.getService(INodeBpo.class);

    public static ConnVo getConnection(Long clusterId) throws Exception{
        List<NodeBasicInfoVo> masterNodes = nodeBpo.queryRunningMasterNodesByClusterId(clusterId);
        if(ValidateUtil.isEmpty(masterNodes)){
            throw new AppException("该集群下无正在运行中的master节点!");
        }
        return NodeUtils.createConnFromNode(masterNodes.get(0));
    }
}
