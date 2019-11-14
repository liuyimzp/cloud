package com.yinhai.cloud.module.resource.nodes.api.vo;

import com.yinhai.cloud.core.api.constant.ConfigPropKey;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;

/**
 * @author: zhaokai
 * @create: 2018-08-23 17:41:49
 */
public class ConfigEtcdCommandFactory {
    private static final String serviceFile = "/etc/systemd/system/etcd.service";
    private static final String confFile = "/etc/etcd/etcd.conf";

    public static ConsoleCommand generateConfigJsonTemplate(String sslRootDir, String ip, String hostname) {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "cfssl print-defaults csr > " + sslRootDir + "/config.json",
                "sed -i '0,/CN/{s/example\\.net/'\"" + hostname + "\"'/}'" + sslRootDir + "/config.json",
                "sed -i 's/www\\.example\\.net/'\"" + ip + "\"'/' " + sslRootDir + "/config.json",
                "sed -i 's/example\\.net/'\"" + hostname + "\"'/' " + sslRootDir + "/config.json"
        );
        return cmd;
    }


    public static ConsoleCommand gencertAllSSLFile(String sslRootDir) {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "cd " + sslRootDir,
                "cfssl gencert -ca=ca.pem -ca-key=ca-key.pem -config=ca-config.json -profile=server config.json | cfssljson -bare server",
                "cfssl gencert -ca=ca.pem -ca-key=ca-key.pem -config=ca-config.json -profile=peer config.json | cfssljson -bare peer"
        );
        return cmd;
    }

    public static ConsoleCommand configEtcdServiceAndStart(EtcdServiceConfig config) {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "if [ -e " + confFile + " ] ;then bash -c \" echo '' > " + confFile + "\" ;else  touch " + confFile + " ;fi",
                " bash -c \"echo '' >" + serviceFile + "\"",
                " bash -c \"echo '[Unit]' >> " + serviceFile + "\"",
                " bash -c \"echo 'Description=etcd' >>" + serviceFile + "\"",
                " bash -c \"echo 'Documentation=https://github.com/coreos/etcd' >>" + serviceFile + "\"",
                " bash -c \"echo 'Conflicts=etcd.service' >> " + serviceFile + "\"",
                " bash -c \"echo 'Conflicts=etcd2.service' >> " + serviceFile + "\"",
                " bash -c \"echo ''>> " + serviceFile + "\"",
                " bash -c \"echo '[Service]'>>" + serviceFile + "\"",
                " bash -c \"echo 'EnvironmentFile=" + serviceFile + "'>>" + serviceFile + "\"",
                " bash -c \"echo 'Type=notify'>>" + serviceFile + "\"",
                " bash -c \"echo 'Restart=always'>>" + serviceFile + "\"",
                " bash -c \"echo 'RestartSec=5s'>>" + serviceFile + "\"",
                " bash -c \"echo 'LimitNOFILE=40000'>>" + serviceFile + "\"",
                " bash -c \"echo 'TimeoutStartSec=0'>>" + serviceFile + "\"",
                " bash -c \"echo ''>> " + serviceFile + "\"",
                " bash -c \"echo 'ExecStart=/usr/bin/etcd" +
                        "   --name " + config.getEtcdName() + "" +
                        "   --data-dir /var/lib/etcd/" + config.getEtcdName() + ".data" +
                        "   --listen-client-urls https://" + config.getCurrentIp() + ":" + config.getClientPort() + "" +
                        "   --advertise-client-urls https://" + config.getCurrentIp() + ":" + config.getClientPort() + "" +
                        "   --listen-peer-urls https://" + config.getCurrentIp() + ":" + config.getServerPort() + "" +
                        "   --initial-advertise-peer-urls https://" + config.getCurrentIp() + ":" + config.getServerPort() + "" +
                        "   --cert-file=" + config.getSslRoot() + "/server.pem" +
                        "   --key-file=" + config.getSslRoot() + "/server-key.pem" +
                        "   --client-cert-auth" +
                        "   --trusted-ca-file=" + config.getSslRoot() + "/ca.pem" +
                        "   --peer-cert-file=" + config.getSslRoot() + "/peer.pem" +
                        "   --peer-key-file=" + config.getSslRoot() + "/peer-key.pem" +
                        "   --peer-client-cert-auth" +
                        "   --peer-trusted-ca-file=" + config.getSslRoot() + "/ca.pem" +
                        "   --initial-cluster " + config.getClusterUrl() + "" +
                        "   --initial-cluster-token my-etcd-token" +
                        "   --initial-cluster-state " + config.getClusterState() + "' >> " + serviceFile + "\"",
                "bash -c \"echo '[Install]' >> " + serviceFile + "\"",
                "bash -c \"echo 'WantedBy=multi-user.target'>> " + serviceFile + "\"",
                "cat " + serviceFile,
                "service etcd stop",
                "rm -rf /var/lib/etcd/*",
                "bash -c \"echo '' > " + confFile + "\"",
                "systemctl daemon-reload && service etcd restart && chkconfig etcd on"
        );
        cmd.setWithSudo(true);
        return cmd;
    }

    public static ConsoleCommand cleanSSLRootDir(String sslRootDir) {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "rm -f " + sslRootDir + "/*",
                "mkdir -p " + sslRootDir + " "
        );
        cmd.setWithSudo(true);
        return cmd;
    }

    public static ConsoleCommand copyEtcdFile() {
        String mainDir = SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT);
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "if [ ! -e \"/usr/bin/etcd\" ] ;then cp " + mainDir + "/dependency/conf/etcd/bin/etcd /usr/bin/ ;else echo \"/usr/bin/etcd already existed,skip copy!\" ; fi",
                "if [ ! -e \"/usr/bin/etcdctl\" ] ;then cp " + mainDir + "/dependency/conf/etcd/bin/etcdctl /usr/bin/ ;else echo \"/usr/bin/etcdctl already existed,skip copy!\" ; fi", "chmod +x /usr/bin/etcd*"

        );
        cmd.setWithSudo(true);
        return cmd;
    }

    public static ConsoleCommand copyLocalEtcdCert(String sslRoot) {
        String mainDir = SystemConfigUtil.getSystemConfigValue(ConfigPropKey.K8S_INSTALL_SERVER_ROOT);
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "mkdir -p " + sslRoot,
                "scp " + mainDir + "/dependency/conf/etcd/ssl/* " + sslRoot
        );
        cmd.setWithSudo(true);
        return cmd;
    }

    public static ConsoleCommand createLocalEtcdCert(String sslRoot) {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "cd " + sslRoot,
                "cfssl gencert -initca ca-csr.json | cfssljson -bare ca -",
                "cfssl gencert -ca=ca.pem -ca-key=ca-key.pem -config=ca-config.json -profile=client client.json | cfssljson -bare client"

        );
        return cmd;
    }


    public static class EtcdServiceConfig {
        private String etcdName;
        private String sslRoot;
        private String currentIp;
        private int clientPort;
        private int serverPort;
        private String clusterUrl;
        private String clusterState;

        public String getEtcdName() {
            return etcdName;
        }

        public void setEtcdName(String etcdName) {
            this.etcdName = etcdName;
        }

        public String getSslRoot() {
            return sslRoot;
        }

        public void setSslRoot(String sslRoot) {
            this.sslRoot = sslRoot;
        }

        public String getCurrentIp() {
            return currentIp;
        }

        public void setCurrentIp(String currentIp) {
            this.currentIp = currentIp;
        }

        public int getClientPort() {
            return clientPort;
        }

        public void setClientPort(int clientPort) {
            this.clientPort = clientPort;
        }

        public int getServerPort() {
            return serverPort;
        }

        public void setServerPort(int serverPort) {
            this.serverPort = serverPort;
        }

        public String getClusterUrl() {
            return clusterUrl;
        }

        public void setClusterUrl(String clusterUrl) {
            this.clusterUrl = clusterUrl;
        }

        public String getClusterState() {
            return clusterState;
        }

        public void setClusterState(String clusterState) {
            this.clusterState = clusterState;
        }
    }


}
