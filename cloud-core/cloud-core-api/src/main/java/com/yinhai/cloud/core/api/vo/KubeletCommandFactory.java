package com.yinhai.cloud.core.api.vo;

import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;

/**
 * 获取Kubelet控制命令的抽象工厂类
 *
 * @author zhaokai
 */
public abstract class KubeletCommandFactory {


    public static ConsoleCommand stopKubeletCommand() {
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(" service kubelet stop ");
        cmd.setWithSudo(true);
        return cmd;
    }

    public static ConsoleCommand startKubeletCommand() {
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.setWithSudo(true);
        cmd.appendCommand("systemctl daemon-reload &&  service kubelet start ");

        return cmd;
    }

    public static ConsoleCommand resetNode() {
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("source ~/.bash_profile",
                "kubeadm reset",
                "if [ \"0\" != \"`ifconfig 2> /dev/null  |grep cni0|wc -l`\" ] ;then  ifconfig cni0 down ;fi ",
                "if [ \"0\" != \"`ip link 2> /dev/null |grep cni0|wc -l`\" ] ;then  ip link delete cni0 ;fi ",
                "if [ \"0\" != \"`ifconfig 2> /dev/null |grep flannel.1|wc -l`\" ] ;then  ifconfig flannel.1 down ;fi ",
                "if [ \"0\" != \"`ip link 2> /dev/null |grep flannel.1|wc -l`\" ] ;then  ip link delete flannel.1 ;fi ",
                "rm -rf /var/lib/cni/"
        );
        cmd.setWithSudo(true);
        return cmd;


    }

    public static ConsoleCommand configNormalUserAccessKubeletCluster() {
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("mkdir -p $HOME/.kube",
                "cp -i /etc/kubernetes/admin.conf $HOME/.kube/config",
                "chown $(id -u):$(id -g) $HOME/.kube/config"

        );

        return cmd;


    }


    public static ConsoleCommand joinNode(String token, String caHash, String masterIp, Integer apiServerPort) {
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("kubeadm join --token=" + token + " --discovery-token-ca-cert-hash sha256:" + caHash + " " + masterIp + ":" + apiServerPort);
        cmd.setWithSudo(true);
        return cmd;
    }


    public static ConsoleCommand forceRemoveEtcdData() {
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("service etcd stop && rm -rf /var/lib/etcd/etcd* ");
        cmd.setWithSudo(true);
        return cmd;
    }

    public static ConsoleCommand forceRemoveDockerAndK8s() {
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("yum remove -y kubeadm kubelet kubectl docker-engine");
        cmd.setWithSudo(true);
        return cmd;
    }

    public static ConsoleCommand forceStopHA() {
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("service haproxy  stop");
        cmd.setWithSudo(true);
        return cmd;
    }

    public static ConsoleCommand forceStopKeepalived() {
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("service keepalived stop");
        cmd.setWithSudo(true);
        return cmd;
    }

    public static ConsoleCommand changeDeployNodeCommunicateMaster(String apiServerIp, Integer apiServerPort) {
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                " sed -i 's#server:.*#server: https://" + apiServerIp + ":" + apiServerPort + "#g' /etc/kubernetes/kubelet.conf",
                " sed -i 's#server:.*#server: https://" + apiServerIp + ":" + apiServerPort + "#g' /etc/kubernetes/bootstrap-kubelet.conf",
                "service kubelet restart"
        );
        cmd.setWithSudo(true);
        return cmd;

    }

    public static ConsoleCommand getNodeJoinTokenAndCaHash() {
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "source ~/.bash_profile ",
                " echo token:`kubeadm token list | grep authentication,signing |grep forever | awk 'END{print $1}'`",
                "echo caHash:`openssl x509  -pubkey -in /etc/kubernetes/pki/ca.crt | openssl rsa -pubin -outform der 2>/dev/null  | openssl dgst -sha256 -hex | sed 's/^.* //'`"
        );

        return cmd;
    }


    public static ConsoleCommand deleteKubeletNode(String deleteHostName) {
        String hostname = deleteHostName.toLowerCase();
        final ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "source ~/.bash_profile ",
                "nb=`kubectl get nodes|grep " + hostname + "|awk 'END{print NR}'`",
                "if [ \"1\" == \"${nb}\" ] ;then  kubectl drain " + hostname + " --delete-local-data --force --ignore-daemonsets && kubectl delete node " + hostname + " ;else echo \"already delete current node " + hostname + "\" ;fi "
        );

        return cmd;
    }

    /**
     * 获取K8s集群可用资源信息
     *
     * @return
     */
    public static ConsoleCommand makeK8sClusterAllocatableCmd() {
        final ConsoleCommand cmd = new ConsoleCommand();
        //查询集群可用资源Cpu数量
        cmd.appendCommand("kubectl describe node |grep Allocatable: -A 1 |grep cpu|awk '{sum+=$2} END {print sum}'");
        //查询集群可用资源内存
        cmd.appendCommand("kubectl describe node |grep Allocatable: -A 6 |grep memory|awk '{sum+=$2/1024/1024} END {print sum}'");
        return cmd;
    }
    /**
     * 获取K8s节点可用资源信息
     *
     * @return
     */
    public static ConsoleCommand makeK8sNodeAllocatableCmd(String nodeName) {
        final ConsoleCommand cmd = new ConsoleCommand();
        //查询集群可用资源Cpu数量
        cmd.appendCommand("kubectl describe node "+nodeName+" |grep Allocatable: -A 1 |grep cpu|awk '{print $2}'");
        //查询集群可用资源内存
        cmd.appendCommand("kubectl describe node "+nodeName+" |grep Allocatable: -A 6 |grep memory|awk '{print $2/1024/1024} '");
        return cmd;
    }


}



