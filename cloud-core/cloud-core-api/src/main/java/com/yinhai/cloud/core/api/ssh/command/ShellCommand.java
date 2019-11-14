package com.yinhai.cloud.core.api.ssh.command;


import com.yinhai.cloud.core.api.ssh.SlashPath;

import java.util.Objects;

/**
 * @author: zhaokai
 * @create: 2018-09-05 18:22:24
 */
public class ShellCommand extends AbstractCommand {

    private String shellContent;
    private String shellParams = "";
    private String shellName;
    private String shellServerWorkDir;


    public String getShellContent() {
        return shellContent;
    }

    public void setShellContent(String shellContent) {
        this.shellContent = shellContent;
    }

    public String getShellName() {
        return shellName;
    }

    public void setShellName(String shellName) {
        this.shellName = shellName;
    }

    public String getShellServerWorkDir() {
        return shellServerWorkDir;
    }

    public void setShellServerWorkDir(String shellServerWorkDir) {
        this.shellServerWorkDir = shellServerWorkDir;
    }

    public ShellCommand appendShellParam(String... param) {
        for (String p : param) {
            shellParams += " " + p;

        }
        return this;
    }

    @Override
    public String buildRunCmd() {
        return  new SlashPath(shellServerWorkDir,shellName).toString()+" "+ shellParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShellCommand that = (ShellCommand) o;
        return Objects.equals(shellContent, that.shellContent) &&
                Objects.equals(shellParams, that.shellParams) &&
                Objects.equals(shellName, that.shellName) &&
                Objects.equals(shellServerWorkDir, that.shellServerWorkDir);
    }

    @Override
    public int hashCode() {

        return Objects.hash(shellContent, shellParams, shellName, shellServerWorkDir);
    }
}
