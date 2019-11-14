package com.yinhai.project.listener;

import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeInitStepBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeOperateRecordBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeInitializeStepVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeOperateVo;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeInitializeStepDao;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeOperateDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeInitializeStepPo;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeOperatePo;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.core.common.api.base.IConstants;
import com.yinhai.core.common.api.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 框架启动监听器
 * @maxp 2017年9月25日15:16:17 修改日志使用slf4j
 */
public class StartupListener extends ContextLoaderListener{

	private final static Logger LOGGER = LoggerFactory.getLogger(StartupListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		LOGGER.info("开始启动系统");
		super.contextInitialized(event);
		IConstants.APP_IS_START_FINISH = true;
		LOGGER.info("启动系统完成");
		cleanBadNodes();
	}

	private void cleanBadNodes() {
		INodeBpo nodeBpo = ServiceLocator.getService(INodeBpo.class);
		INodeOperateDao nodeOperateDao = ServiceLocator.getService(INodeOperateDao.class);
		INodeOperateRecordBpo nodeOperateRecordBpo = ServiceLocator.getService(INodeOperateRecordBpo.class);
		INodeInitStepBpo nodeInitStepBpo = ServiceLocator.getService(INodeInitStepBpo.class);
		IClusterBpo clusterBpo = ServiceLocator.getService(IClusterBpo.class);
		INodeInitializeStepDao nodeInitializeStepDao = ServiceLocator.getService(INodeInitializeStepDao.class);

		List<NodeBasicInfoVo> nodes = nodeBpo.queryAllNodes();
		for (NodeBasicInfoVo node : nodes){
			//节点正在初始化时将其改为失败状态
			if (NodeState.INIT_RUNNING.equals(node.getNodeInitState())){
                LOGGER.info("清理初始化失败节点：" + node.getNodeName());
				List<NodeOperateVo> operateVos = nodeOperateRecordBpo.getOperateListByNodeId(node.getNodeId()).stream().filter(npo -> ValidateUtil.isEmpty(npo.getOperateFinishTime())).collect(Collectors.toList());
				if (!operateVos.isEmpty()){
					NodeOperateVo vo = operateVos.get(0);
					nodeInitStepBpo.nodeInitFailed(node.getNodeId(),vo.getOperateId(),"节点清理");
					NodeInitializeStepVo stepVo = nodeInitStepBpo.getNodeInitializeStep(node.getNodeId()).stream().filter(spo -> spo.getStepState() == ResourceConstant.NODE_INIT_STEP_RUNNING).collect(Collectors.toList()).get(0);
					stepVo.setStepState(ResourceConstant.NODE_INIT_STEP_FAILED);
					nodeInitStepBpo.setFalid(stepVo);
				}
			}
			else if (NodeState.OPERATE_DELETING.equals(node.getNodeOperateState())){
                LOGGER.info("清理删除失败节点：" + node.getNodeName());
                nodeBpo.deleteNode(node);
				//属性集群资源
				clusterBpo.refreshResource(node.getClusterId());
            }
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		LOGGER.info("系统开始关闭");
		super.contextDestroyed(event);
		LOGGER.info("系统成功关闭");
	}
}