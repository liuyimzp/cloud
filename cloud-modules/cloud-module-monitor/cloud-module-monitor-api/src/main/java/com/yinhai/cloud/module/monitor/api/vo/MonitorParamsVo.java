package com.yinhai.cloud.module.monitor.api.vo;

/**
 * @Author ：kangdw
 * @Date : 2019/8/8
 */
public class MonitorParamsVo {

    private String query;
    private String start;
    private String end;
    private Integer step;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }
}
