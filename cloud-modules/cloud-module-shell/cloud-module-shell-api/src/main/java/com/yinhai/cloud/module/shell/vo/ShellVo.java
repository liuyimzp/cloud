package com.yinhai.cloud.module.shell.vo;

import com.google.common.base.CaseFormat;
import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * @author jianglw
 */
public class ShellVo extends BaseVo {
    private static final long serialVersionUID = -6777514440892654495L;
    private Long shellId;
    private String shellName;
    private String shellUid;
    private String shellUidDesc;
    private String shellType;
    private String shellTypeDesc;
    private String shellContent;
    private String shellUidConstant;

    public String getShellUidConstant() {
        if (shellUidDesc == null) {
            shellUidDesc = "";
        }
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, shellUidDesc);
    }

    public String getShellUidDesc() {
        return shellUidDesc;
    }

    public void setShellUidDesc(final String shellUidDesc) {
        this.shellUidDesc = shellUidDesc;
    }

    public String getShellTypeDesc() {
        return shellTypeDesc;
    }

    public void setShellTypeDesc(final String shellTypeDesc) {
        this.shellTypeDesc = shellTypeDesc;
    }

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
