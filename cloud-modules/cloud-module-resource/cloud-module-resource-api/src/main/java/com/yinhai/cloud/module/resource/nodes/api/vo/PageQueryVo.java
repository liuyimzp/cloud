package com.yinhai.cloud.module.resource.nodes.api.vo;

/**
 * @author: zhaokai
 * @create: 2018-08-29 09:46:38
 */
public class PageQueryVo {
    private Long nodeId;
    private Integer limit;
    private Integer start;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }
}
