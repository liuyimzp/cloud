package com.yinhai.cloud.module.resource.nodes.api.vo;

import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;

import java.io.File;

/**
 * @author: zhaokai
 * @create: 2018-08-23 16:29:44
 */
public class EtcdCtlHttpsCommandFactory {

    private static String ctlRootDir = "/usr/bin";
    private static String sslRootDir = "/etc/etcd/ssl";

    public static ConsoleCommand memberList(String etcdServiceIp, int etcdServicePort) {
        return generateEtcdCtlCmd(etcdServiceIp, etcdServicePort, "member list");
    }

    public static ConsoleCommand removeMember(String etcdServiceIp, int etcdServicePort, String memberId) {
        return generateEtcdCtlCmd(etcdServiceIp, etcdServicePort, "member remove " + memberId);
    }

    public static ConsoleCommand addMember(String etcdServiceIp, int etcdServicePort, String etcdName, String addEtcdServiceIp, int addEtcdServicePort) {
        return generateEtcdCtlCmd(etcdServiceIp, etcdServicePort, "member add " + etcdName + " https://" + addEtcdServiceIp + ":" + addEtcdServicePort);
    }


    public static ConsoleCommand generateEtcdCtlCmd(String etcdServiceIp, int etcdServicePort, String etcdCmd) {

        String etcdctl = (ctlRootDir + "/" + "etcdctl").replaceAll("/+", "/");
        String etcdSSLRootPath = (sslRootDir + ServerCmdConstant.FILE_SEPARATOR).replaceAll("/+", "/");


        String prop = " --endpoints=https://" + etcdServiceIp + ":" + etcdServicePort + " --ca-file=" + etcdSSLRootPath + "ca.pem --cert-file=" + etcdSSLRootPath + "client.pem --key-file=" + etcdSSLRootPath + "client-key.pem";

        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                etcdctl + " " + prop + " " + etcdCmd
        );
        return cmd;


    }

}
