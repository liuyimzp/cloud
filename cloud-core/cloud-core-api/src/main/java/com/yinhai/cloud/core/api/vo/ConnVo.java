package com.yinhai.cloud.core.api.vo;

import com.yinhai.cloud.core.api.util.DataEncrypt;
import com.yinhai.core.common.ta3.vo.BaseVo;

import java.util.Objects;

/**
 * @author jianglw
 */
public class ConnVo extends BaseVo {
    private static final long serialVersionUID = 13227983833156707L;
    private String ip;
    private Integer port = 22;
    private String user;
    private String pwd;


    public ConnVo() {

    }

    public ConnVo(String ip, Integer port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = DataEncrypt.decryptBasedDes(pwd);

    }


    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(final String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(final String pwd) {
        this.pwd = pwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConnVo connVo = (ConnVo) o;
        return Objects.equals(ip, connVo.ip) &&
                Objects.equals(port, connVo.port) &&
                Objects.equals(user, connVo.user) &&
                Objects.equals(pwd, connVo.pwd) ;
    }

    @Override
    public int hashCode() {

        return Objects.hash(ip, port, user, pwd);
    }
}
