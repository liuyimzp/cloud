package com.yinhai.cloud.module.shell.vo;

/**
 * @author: zhaokai
 * @create: 2018-08-17 15:50:53
 */
public class ExecShell {
    private String shellUid;
    private String params = "";
    private boolean withSudo;

    public ExecShell(String shellUid, boolean withSudo) {
        this.shellUid = shellUid;
        this.withSudo = withSudo;
    }

    public ExecShell(String shellUid, boolean withSudo, String params) {
        this.shellUid = shellUid;
        this.params = params;
    }

    public boolean isWithSudo() {
        return withSudo;
    }

    public void setWithSudo(boolean withSudo) {
        this.withSudo = withSudo;
    }

    public String getShellUid() {
        return shellUid;
    }

    public void setShellUid(String shellUid) {
        this.shellUid = shellUid;
    }

    public String getParams() {
        if (params == null) {
            return "";
        }
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public ExecShell appendParam(String... params) {
        StringBuilder sb = new StringBuilder();
        for (String s : params) {
            sb.append(" ").append(s);
        }
        this.params += sb.toString();
        return this;
    }
}
