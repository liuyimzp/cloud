package com.yinhai.cloud.module.resource.nodes.impl.bpo;

import com.yinhai.cloud.core.api.util.CommonUtil;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeInitStepBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeOperateRecordBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeOperateVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.OperateRunningServer;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeDao;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeInitBaseStepDao;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeInitializeStepDao;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeOperateDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeOperatePo;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.common.ta3.base.Page;
import com.yinhai.core.service.ta3.domain.service.TaBaseService;
import com.yinhai.modules.codetable.api.util.CodeTableUtil;
import com.yinhai.modules.org.ta3.domain.dao.IUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhaokai
 * @create: 2018-08-28 15:13:40
 */
public class NodeOperateRecordBpoImpl extends TaBaseService implements INodeOperateRecordBpo {
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private static final String CODE_KEY_NODE_OPERATE_CODE = "NODE_OPERATE_CODE";
    private static final String CODE_KEY_NODE_OP_STACK_STATE = "NODE_OP_STACK_STATE";
    @Autowired
    private IClusterBpo clusterBpo;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private INodeDao nodeDao;
    @Autowired
    private INodeInitBaseStepDao nodeInitBaseStepDao;
    @Autowired
    private INodeInitializeStepDao nodeInitializeStepDao;
    @Autowired
    private INodeOperateDao nodeOperateDao;
    @Autowired
    private INodeInitStepBpo nodeInitStepBpo;

    @Override
    public Long addNewNodeOperateRecord(Long nodeId, String operateCode, OperateRunningServer operateRunningServer) {
        NodeOperateVo vo = new NodeOperateVo();
        vo.setNodeId(nodeId);
        vo.setOperateFinishedPercentage(0);
        vo.setOperateCode(operateCode);
        vo.setOperateState(ResourceConstant.OPERATE_STATE_RUNNING);
        vo.setOperateStartTime(CommonUtil.getTime(ResourceConstant.TIME_FORMAT_WITH_NANO));
        vo.setOperateLog("");
        vo.setOperateRunningServer(operateRunningServer.toString());
        return nodeOperateDao.saveNewOperate(vo.toPO(new NodeOperatePo()));
    }


    @Override
    public IPage<NodeOperateVo> getNodeOperateStackWithPage(Long nodeId, Integer currentPage, Integer pageSize) {
        IPage<NodeOperatePo> poPage = nodeOperateDao.getNodeOperateStackWithPage(nodeId, currentPage, pageSize);
        List<NodeOperateVo> voList = poPage.getList().stream().map(n -> {
            NodeOperateVo vo = n.toVo(new NodeOperateVo());
            String operateDesc = CodeTableUtil.getDesc(CODE_KEY_NODE_OPERATE_CODE, String.valueOf(n.getOperateCode()));
            String stateDesc = CodeTableUtil.getDesc(CODE_KEY_NODE_OP_STACK_STATE, String.valueOf(n.getOperateState()));
            vo.setOperateDesc(operateDesc);
            vo.setOperateStateDesc(stateDesc);
            if (vo.getOperateStartTime() != null && vo.getOperateFinishTime() != null) {
                LocalDateTime start = LocalDateTime.parse(vo.getOperateStartTime(), DateTimeFormatter.ofPattern(ResourceConstant.TIME_FORMAT_WITH_NANO));
                LocalDateTime end = LocalDateTime.parse(vo.getOperateFinishTime(), DateTimeFormatter.ofPattern(ResourceConstant.TIME_FORMAT_WITH_NANO));
                long startNano = start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long endNano = end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                vo.setTakeTimeSeconds(decimalFormat.format((endNano - startNano) / 1000.0));
            }
            return vo;


        }).collect(Collectors.toList());
        Page<NodeOperateVo> voPage = new Page<>();
        voPage.setStart(poPage.getStart());
        voPage.setTotal(poPage.getTotal());
        voPage.setLimit(poPage.getLimit());
        voPage.setList(voList);
        return voPage;
    }

    @Override
    public List<NodeOperateVo> getOperateListByNodeId(Long nodeId) {
        return nodeOperateDao.getOperateListByNodeId(nodeId).stream().map(po -> {
            NodeOperateVo vo = po.toVo(new NodeOperateVo());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateNodeOperateAsSuccess(Long operateId) {
        NodeOperatePo po = new NodeOperatePo();
        po.setOperateId(operateId);
        po.setOperateState(ResourceConstant.OPERATE_STATE_SUCCESSFUL);
        po.setOperateFinishedPercentage(100);
        po.setOperateFinishTime(CommonUtil.getTime(ResourceConstant.TIME_FORMAT_WITH_NANO));
        nodeOperateDao.updateNodeOperate(po);
    }


    @Override
    public void updateNodeOperateAsFailed(Long operateId, String message) {

        NodeOperatePo po = new NodeOperatePo();
        po.setOperateId(operateId);
        po.setOperateState(ResourceConstant.OPERATE_STATE_FAILED);
        po.setOperateFinishTime(CommonUtil.getTime(ResourceConstant.TIME_FORMAT_WITH_NANO));
        if (!ValidateUtil.isEmpty(message)) {
            NodeOperatePo old = nodeOperateDao.getOperateRecordById(operateId);
            po.setOperateLog((old.getOperateLog() + "\n" + message).replaceAll("\n+", "\n"));
        }
        nodeOperateDao.updateNodeOperate(po);


    }


    @Override
    public void appendOperateLog(Long operateId, String log) {
        if (log == null || "".equals(log)) {
            return;
        }
        NodeOperatePo po = new NodeOperatePo();
        po.setOperateId(operateId);
        NodeOperatePo old = nodeOperateDao.getOperateRecordById(operateId);
        String newLog = ((old.getOperateLog() == null ? "" : old.getOperateLog()) + "\n" + log).replaceAll("\n+", "\n");
        po.setOperateLog(newLog);
        nodeOperateDao.updateNodeOperate(po);


    }

    @Override
    public void updateNodeOperatePercentage(Long operateId, int basePer) {

        NodeOperatePo op = new NodeOperatePo();
        op.setOperateId(operateId);
        op.setOperateFinishedPercentage(basePer);

        nodeOperateDao.updateNodeOperate(op);
    }


}
