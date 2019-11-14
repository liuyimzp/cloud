package com.yinhai.cloud.module.shell.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

/**
 * @author jianglw
 */
public class ShellPo extends BasePo {
    private static final long serialVersionUID = -3009992721734555869L;
    private Long shellId;
    private String shellName;
    private String shellUid;
    private String shellType;
    private String shellContent;

    public Long getShellId() {
        return shellId;
    }

    public void setShellId(final Long shellId) {
        this.shellId = shellId;
    }

    public String getShellName() {
        return shellName;
    }

    public void setShellName(final String shellName) {
        this.shellName = shellName;
    }

    public String getShellUid() {
        return shellUid;
    }

    public void setShellUid(final String shellUid) {
        this.shellUid = shellUid;
    }

    public String getShellType() {
        return shellType;
    }

    public void setShellType(final String shellType) {
        this.shellType = shellType;
    }

    public String getShellContent() {
        return shellContent;
    }

    public void setShellContent(final String shellContent) {
        this.shellContent = shellContent;
    }
}
