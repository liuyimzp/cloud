package com.yinhai.cloud.module.resource.nodes.impl.po;

import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.po.BasePo;

import java.util.Date;

/**
 * @author liuyi02
 */
public class CephNodeInfoPo extends BasePo {
    private static final long serialVersionUID = 6298514894347458881L;
    private Long nodeId;
    private String nodeName;
    private String nodeType;
    private String nodeIP;
    private String nodeUser;
    private String nodePassword;
    private Integer nodeSSHPort;
    private Date nodeCreateDate;
    private String nodeCreateUser;
    private String nodePath;
    private Long clusterId;
    private Double nodeDiskToTalMb;
    private Double nodeDiskToTalMbIdealState;
    private Integer cephOperation;//是否正在被操作
    private String nodeCName;//节点编号
    private Integer isMds;

    // 运行状态
    private Integer nodeRunningState;
    // 操作状态 ：删除中、启动中、停止中

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeIP() {
        return nodeIP;
    }

    public void setNodeIP(String nodeIP) {
        this.nodeIP = nodeIP;
    }

    public String getNodeUser() {
        return nodeUser;
    }

    public void setNodeUser(String nodeUser) {
        this.nodeUser = nodeUser;
    }

    public String getNodePassword() {
        return nodePassword;
    }

    public void setNodePassword(String nodePassword) {
        this.nodePassword = nodePassword;
    }

    public Integer getNodeSSHPort() {
        return nodeSSHPort;
    }

    public void setNodeSSHPort(Integer nodeSSHPort) {
        this.nodeSSHPort = nodeSSHPort;
    }

    public Date getNodeCreateDate() {
        if (!ValidateUtil.isEmpty(nodeCreateDate)){
            return (Date) nodeCreateDate.clone();
        }
        return null;
    }

    public void setNodeCreateDate(Date nodeCreateDate) {
        if (!ValidateUtil.isEmpty(nodeCreateDate)){
            this.nodeCreateDate = (Date) nodeCreateDate.clone();
        }
    }

    public String getNodeCreateUser() {
        return nodeCreateUser;
    }

    public void setNodeCreateUser(String nodeCreateUser) {
        this.nodeCreateUser = nodeCreateUser;
    }

    public Integer getNodeRunningState() {
        return nodeRunningState;
    }

    public void setNodeRunningState(Integer nodeRunningState) {
        this.nodeRunningState = nodeRunningState;
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public Double getNodeDiskToTalMb() {
        return nodeDiskToTalMb;
    }

    public void setNodeDiskToTalMb(Double nodeDiskToTalMb) {
        this.nodeDiskToTalMb = nodeDiskToTalMb;
    }

    public Double getNodeDiskToTalMbIdealState() {
        return nodeDiskToTalMbIdealState;
    }

    public void setNodeDiskToTalMbIdealState(Double nodeDiskToTalMbIdealState) {
        this.nodeDiskToTalMbIdealState = nodeDiskToTalMbIdealState;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public Integer getCephOperation() {
        return cephOperation;
    }

    public void setCephOperation(Integer cephOperation) {
        this.cephOperation = cephOperation;
    }

    public String getNodeCName() {
        return nodeCName;
    }

    public void setNodeCName(String nodeCName) {
        this.nodeCName = nodeCName;
    }

    public Integer getIsMds() {
        return isMds;
    }

    public void setIsMds(Integer isMds) {
        this.isMds = isMds;
    }
}
