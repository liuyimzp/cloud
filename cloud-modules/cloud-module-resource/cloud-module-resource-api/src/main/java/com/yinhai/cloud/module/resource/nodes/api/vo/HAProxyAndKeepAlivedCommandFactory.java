package com.yinhai.cloud.module.resource.nodes.api.vo;

import com.yinhai.cloud.core.api.constant.ConfigPropKey;
import com.yinhai.cloud.core.api.ssh.command.AbstractCommand;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.UploadContentCommand;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;

import java.util.List;

/**
 * @author: zhaokai
 * @create: 2018-11-15 15:08:22
 */
public class HAProxyAndKeepAlivedCommandFactory {
    public static final Integer HAPROXY_LISTEN_PORT = 7443;
    public static  final Integer NOHACLUSTER_ETCD_PORT = 6443;

    public static ConsoleCommand installHaproxyAndKeepalive() {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "if [ \"0\" == \"`rpm -qa |grep haproxy |wc -l`\" ] ;then yum install -y haproxy  ;fi",
                "if [ \"0\" == \"`rpm -qa |grep keepalived |wc -l`\" ] ;then yum install -y  keepalived ;fi",
                "chkconfig haproxy on && chkconfig keepalived on"
        );
        cmd.setWithSudo(true);
        return cmd;
    }

    public static UploadContentCommand uploadHaproxyConfig(List<String> masterIpList) {
        StringBuilder configContent = new StringBuilder(
                "global\n" +
                        "    log         127.0.0.1 local2\n" +
                        "    chroot      /var/lib/haproxy\n" +
                        "    pidfile     /var/run/haproxy.pid\n" +
                        "    maxconn     4000\n" +
                        "    daemon\n" +
                        "    stats socket /var/lib/haproxy/stats\n" +
                        "\n" +
                        "defaults\n" +
                        "    mode                    tcp\n" +
                        "    log                     global\n" +
                        "    option                  tcplog\n" +
                        "    option                  dontlognull\n" +
                        "    option                  redispatch\n" +
                        "    retries                 3\n" +
                        "    timeout queue           1m\n" +
                        "    timeout connect         10s\n" +
                        "    timeout client          1m\n" +
                        "    timeout server          1m\n" +
                        "    timeout check           10s\n" +
                        "    maxconn                 3000\n" +
                        "\n" +
                        "frontend  k8s_https\n" +
                        "    bind *:"+HAPROXY_LISTEN_PORT+"\n" +
                        "    mode      tcp\n" +
                        "    maxconn      2000\n" +
                        "    default_backend     https_sri\n" +
                        "\n" +
                        "backend https_sri\n" +
                        "    balance      roundrobin\n");

        for (int i = 0; i < masterIpList.size(); i++) {
            String ip = masterIpList.get(i);
            configContent.append("    server s" + (i + 1) + " ").append(ip).append(":6443 check\n");
        }
        return new UploadContentCommand(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.HAPROXY_CONFIG_FILE_PATH), configContent.toString());
    }

    public static UploadContentCommand uploadCheckHaproxyShellScript() {
        StringBuilder configContent = new StringBuilder(
                        "#!/bin/bash\n" +
                        "flag=$(systemctl status haproxy &> /dev/null;echo $?)\n" +
                        "if [[ $flag != 0 ]];then\n" +
                        "        echo \"haproxy is down,close the keepalived\"\n" +
                        "        systemctl stop keepalived\n" +
                        "fi\n");
        return new UploadContentCommand(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.CHECK_HAPROXY_FILE_PATH), configContent.toString());

    }


    public static UploadContentCommand uploadKeepaliedConfig(String virtualIP, Integer subnetMaskBitSize,boolean isBackup,String networkInterfaceName) {
        StringBuilder configContent = new StringBuilder(
                "! Configuration File for keepalived\n" +
                        "global_defs {\n" +
                        "    notification_email {\n" +
                        "        test@sina.com\n" +
                        "    }\n" +
                        "    notification_email_from admin@test.com\n" +
                        "    smtp_server 127.0.0.1\n" +
                        "    smtp_connect_timeout 30\n" +
                        "    router_id LVS_MASTER\n" +
                        "}\n" +
                        "\n" +
                        "vrrp_script check_haproxy {\n" +
                        "    script \"/etc/keepalived/check_haproxy.sh\"\n" +
                        "    interval 3\n" +
                        "}\n" +
                        "\n" +
                        "vrrp_instance VI_1 {\n" +
                        "    state "+(isBackup?"BACKUP":"MASTER")+"\n" +
                        "    interface "+networkInterfaceName+"\n" +
                        "    virtual_router_id 60\n" +
                        "    priority "+(isBackup?"100":"150")+"\n" +
                        "    advert_int 1\n" +
                        "    authentication {\n" +
                        "        auth_type PASS\n" +
                        "        auth_pass 1111\n" +
                        "    }\n" +
                        "    virtual_ipaddress {\n" +
                        "        " + virtualIP + "/" + subnetMaskBitSize + "\n" +
                        "    }\n" +
                        "\n" +
                        "    track_script {\n" +
                        "        check_haproxy\n" +
                        "    }\n" +
                        "}");
        return new UploadContentCommand(SystemConfigUtil.getSystemConfigValue(ConfigPropKey.KEEPALIVED_CONFIG_FILE_PATH), configContent.toString());
    }

    public static ConsoleCommand addExecuteModeToCheckHaproxyShellScript() {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "chmod +x " + SystemConfigUtil.getSystemConfigValue(ConfigPropKey.CHECK_HAPROXY_FILE_PATH)
        );
        return cmd;
    }

    public static AbstractCommand startHaproxyAndKeepalived() {

        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                " setsebool -P haproxy_connect_any=1",
                "service haproxy restart && service keepalived  restart ",
                "chkconfig haproxy on && chkconfig keepalived on"
        );
        return cmd;
    }
}
