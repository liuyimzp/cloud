package com.yinhai.cloud.module.resource.nodes.api.vo;

import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.common.ta3.vo.BaseVo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuyi02
 */
public class CephNodeInfoVo extends BaseVo implements Serializable {
    private static final long serialVersionUID = 2370314786046563450L;
    private Long nodeId;
    private String nodeName;
    private String nodeType;
    private String nodeIP;
    private String nodeUser;
    private String nodePassword;
    private Integer nodeSSHPort;
    private Date nodeCreateDate;
    private String nodeCreateUser;
    private String nodeRunningText;
    private String nodeRunningStely;
    private String nodePath;
    private String creatUser;
    private Double nodeDiskToTalMb;
    private Double nodeDiskToTalMbIdealState;
    private Long clusterId;
    private Integer cephOperation;//是否正在被操作
    private String nodeCName;
    private Integer isMds;

    // 运行状态
    private Integer nodeRunningState;//0：初始化 1：运行正常 2：删除中 3:停止

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

    public String getNodeRunningText() {
        return nodeRunningText;
    }

    public void setNodeRunningText(String nodeRunningText) {
        this.nodeRunningText = nodeRunningText;
    }

    public String getNodeRunningStely() {
        return nodeRunningStely;
    }

    public void setNodeRunningStely(String nodeRunningStely) {
        this.nodeRunningStely = nodeRunningStely;
    }

    public String getCreatUser() {
        return creatUser;
    }

    public void setCreatUser(String creatUser) {
        this.creatUser = creatUser;
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
