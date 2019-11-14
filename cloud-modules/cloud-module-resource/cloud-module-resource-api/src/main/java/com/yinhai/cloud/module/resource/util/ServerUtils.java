package com.yinhai.cloud.module.resource.util;

import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exception.NodeInitRuntimeException;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.UploadContentCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.CommonUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.ICephClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.ICephPoolBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.CephClusterVo;
import com.yinhai.cloud.module.resource.cluster.api.vo.CephPoolVo;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.resource.kubernetes.pv.StorageClassCephObject;
import com.yinhai.cloud.module.resource.nodes.api.bpo.ICephNodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.CephNodeInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeInfoVo;
import com.yinhai.cloud.module.resource.pv.api.bpo.IPersistentVolumeBpo;
import com.yinhai.cloud.module.resource.pv.api.vo.PvVo;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.modules.cache.ehcache.TaCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jianglw
 */
@SuppressWarnings("WeakerAccess")
public class ServerUtils {
    public static final String NODE_INFO_CACHE_KEE = "serverInfoCache";
    public static final String CEPH_NODE_INFO_CACHE_KEE = "cephServerInfoCache";
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerUtils.class);
    private static final TaCacheManager cacheServiceManager = ServiceLocator.getService(TaCacheManager.class);
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    //liuyi 2019/3/1 修改.
    private static final Object UPDATE_NODE_REOURCE_LOCK = "1";
    private static final Object UPDATE_CEPHNODE_REOURCE_LOCK = "1";
    private static INodeBpo nodeBpo = ServiceLocator.getService(INodeBpo.class);
    private static ICephClusterBpo cephClusterBpo = ServiceLocator.getService(ICephClusterBpo.class);
    private static ICephNodeBpo cephNodeBpo = ServiceLocator.getService(ICephNodeBpo.class);
    private static ICephPoolBpo cephPoolBpo = ServiceLocator.getService(ICephPoolBpo.class);
    private static IPersistentVolumeBpo pvbpo = ServiceLocator.getService(IPersistentVolumeBpo.class);

    /**
     * 获取主机信息
     *
     * @param vo
     * @return
     */
    public static NodeInfoVo getNodeInfo(final NodeBasicInfoVo vo, final boolean refresh) {
        final String kee = vo.getClusterId() + ":" + vo.getNodeIP();
        // 检查是否有缓存
        Cache cache = cacheServiceManager.getCache(NODE_INFO_CACHE_KEE);
        if (!refresh && cache.get(kee) != null) {
            return (NodeInfoVo) cache.get(kee).get();
        }

        final NodeInfoVo nodePhysicalResources = ServerInfoUtils.getNodePhysicalResources(vo);
        ;
        // 缓存
        cache.put(kee, nodePhysicalResources);
        return nodePhysicalResources;
    }

    public static CommonResultVo getCephNodeInfo(CephNodeInfoVo vo, final boolean refresh,boolean refreshB){
        final String kee = "ceph:" + vo.getNodeIP();
        CommonResultVo result = new CommonResultVo();
        //检查是否有缓存
        Cache cache = cacheServiceManager.getCache(CEPH_NODE_INFO_CACHE_KEE);
        if (!refresh && cache.get(kee) != null) {
            CephNodeInfoVo cephNodeInfoVo = (CephNodeInfoVo) cache.get(kee).get();
            if (!ValidateUtil.isEmpty(cephNodeInfoVo)){
                result.setSuccess(true);
                result.setData(cephNodeInfoVo);
                return result;
            }
        }
        ConnVo conn = NodeUtils.createConnFromNode(vo);
        ConsoleCommand command = new ConsoleCommand();
        if (refreshB){
            command.appendCommand("df --total " + vo.getNodePath() + "|grep 'total' |awk '{print $4/1024/1024}'");
        }else {
            command.appendCommand("df --total|grep -w / |awk '{print $4/1024/1024}'");
        }
        try {
            MsgVO msgVO = CommonSshExecUtil.exec(conn,command).get(command);
            if (!msgVO.isSuccess()) {
                result.setSuccess(false);
                result.setErrorMsg("获取主机信息失败：" + msgVO.getErrorMsg());
                return result;
            }
            if (ValidateUtil.isEmpty(msgVO.getSysoutMsg())) {
                result.setSuccess(false);
                result.setErrorMsg("获取主机信息失败：执行获取命令返回信息为空");
                return result;
            }
            String[] resourceInfo = msgVO.getSysoutMsg().split("\\n");
            if (resourceInfo.length != 1) {
                result.setErrorMsg("获取主机信息失败：获取命令返回格式无法解析 " + msgVO.getSysoutMsg());
                result.setSuccess(false);
                return result;
            }
            vo.setNodeDiskToTalMb(Double.valueOf(decimalFormat.format(Double.valueOf(resourceInfo[0]))));
        } catch (SSHExecuteException e) {
            result.setSuccess(false);
            String exceptionMsgContent = CommonUtil.getExceptionMsgContent(e);
            result.setErrorMsg(exceptionMsgContent);
            return result;
        } catch (SSHConnectionException e) {
            result.setErrorMsg("连接主机失败，（请检查IP、用户名、密码、SSH端口）");
            result.setSuccess(false);
            return result;
        }
        // 缓存
        result.setData(vo);
        result.setSuccess(true);
        cache.put(kee, vo);
        return result;
    }

    /**
     * 获取K8s节点资源信息
     *
     * @param masterNode
     * @param childNodeName
     * @return
     */
    public static NodeInfoVo getK8sNodeInfo(NodeBasicInfoVo masterNode, String childNodeName) {
        NodeInfoVo nodeInfoVo = ServerInfoUtils.getK8sNodeDesc(masterNode, childNodeName);
        return nodeInfoVo;
    }

    /**
     * 同步更新节点资源信息，并存到数据库
     *
     * @param vo
     */
    public static void updateNodeResource(NodeBasicInfoVo vo) {
        synchronized (UPDATE_NODE_REOURCE_LOCK) {
            NodeInfoVo nodeResourceInfo = getNodeInfo(vo, true);
            // 未获取到新的资源信息，不进行数据库更新操作
            if (nodeResourceInfo == null || nodeResourceInfo.getNodeStatus() != NodeState.GET_RESOURCE_SUCCESSFULLY) {
                return;
            }
            //从管理节点获取负载节点资源信息后保存
            NodeBasicInfoVo masterNodeInfoVo = nodeBpo.queryMasterNodesByClusterId(vo.getClusterId()).stream().findAny().orElse(new NodeBasicInfoVo());
            NodeInfoVo k8sNodeInfo = ServerUtils.getK8sNodeInfo(masterNodeInfoVo, vo.getNodeName());
            nodeResourceInfo.setNodeAllocatableCpu(k8sNodeInfo.getNodeAllocatableCpu());
            nodeResourceInfo.setNodeAllocatableMemory(k8sNodeInfo.getNodeAllocatableMemory());
            nodeBpo.updateNodeResource(vo, nodeResourceInfo);
        }
    }

    /**
     * 实现免密登录
     * @param vo
     * @param vos
     * @return
     */
    public static CommonResultVo noPwdLogin(CephNodeInfoVo vo, List<CephNodeInfoVo> vos){
        CommonResultVo resultVo = new CommonResultVo();
        if (ValidateUtil.isEmpty(vo)){
            resultVo.setErrorMsg("请将必填属性填写完整！");
            resultVo.setSuccess(false);
            return resultVo;
        }
        ConnVo conn = NodeUtils.createConnFromNode(vo);
        ConsoleCommand command = new ConsoleCommand();
        command.appendCommand("rm -f ~/.ssh/*");
        command.appendCommand("ssh-keygen -f ~/.ssh/id_rsa -P \"\"");
        command.appendCommand("touch ~/.ssh/known_hosts && touch ~/.ssh/authorized_keys && touch ~/.ssh/config");
        command.appendCommand("echo StrictHostKeyChecking no>>~/.ssh/config");
        command.appendCommand("echo UserKnownHostsFile /dev/null>>~/.ssh/config");
        resultVo = sshExec(conn,command);
        if (!resultVo.isSuccess()){
            return resultVo;
        }
        if (vos.isEmpty()){
            resultVo.setSuccess(false);
            resultVo.setErrorMsg("该集群还没有其他机器");
            return resultVo;
        }
        //如果已经有其他节点则将该节点的密钥添加到其他节点上
        try {
            ConsoleCommand ncommand = new ConsoleCommand();
            ncommand.appendCommand("cat ~/.ssh/id_rsa.pub");
            MsgVO msgVO = CommonSshExecUtil.exec(conn,ncommand).get(ncommand);
            String[] resourceInfo = msgVO.getSysoutMsg().split("\\n");
            for (CephNodeInfoVo nvo : vos){
                ConnVo connVo = NodeUtils.createConnFromNode(nvo);
                ConsoleCommand authCommand = new ConsoleCommand();
                authCommand.appendCommand("echo " + resourceInfo[0] + ">>~/.ssh/authorized_keys");
                CommonSshExecUtil.exec(connVo,authCommand);
                MsgVO msgVO1 = CommonSshExecUtil.exec(connVo,ncommand).get(ncommand);
                String[] resourceInfo1 = msgVO1.getSysoutMsg().split("\\n");
                ConsoleCommand authCommand1 = new ConsoleCommand();
                authCommand1.appendCommand("echo " + resourceInfo1[0] + ">>~/.ssh/authorized_keys");
                CommonSshExecUtil.exec(conn,authCommand1);
            }
        }catch (SSHExecuteException e) {
            String exceptionMsgContent = CommonUtil.getExceptionMsgContent(e);
            LOGGER.error(LOGGER.getName() + exceptionMsgContent);
            resultVo.setSuccess(false);
            resultVo.setErrorMsg("执行ssh失败");
            return resultVo;
        } catch (SSHConnectionException e) {
            LOGGER.error("连接主机失败，请检查ip，用户名，密码，端口"+e);
            resultVo.setSuccess(false);
            resultVo.setErrorMsg("连接主机失败，请检查ip，用户名，密码，端口");
            return resultVo;
        }
        resultVo.setSuccess(true);
        return resultVo;
    }

    public static void unNopwdLogin(CephNodeInfoVo vo, List<CephNodeInfoVo> vos) throws Exception{
        ConnVo conn = NodeUtils.createConnFromNode(vo);//获得连接
        ConsoleCommand command = new ConsoleCommand();
        ConsoleCommand command1 = new ConsoleCommand();
        command.appendCommand("sed -i -e '/" + vo.getNodeName() + "/d' ~/.ssh/authorized_keys");
        vos.stream().forEach(nvo ->{
            ConnVo connVo = NodeUtils.createConnFromNode(nvo);//获得连接
            try {
                CommonSshExecUtil.exec(connVo, command);
            } catch (Exception e) {
                throw new NodeInitRuntimeException(e);
            }
        });
        command1.appendCommand("rm -f ~/.ssh/*");
        try {
            CommonSshExecUtil.exec(conn, command1);
        } catch (Exception e) {
            throw new NodeInitRuntimeException(e);
        }
    }

    /**
     * 设置
     * @param vo
     * @param vos
     */
    public static void setHosts(CephNodeInfoVo vo, List<CephNodeInfoVo> vos,boolean isDel) throws Exception{
        ConnVo conn = NodeUtils.createConnFromNode(vo);
        ConsoleCommand command = new ConsoleCommand();
        ConsoleCommand commandDel = new ConsoleCommand();
        commandDel.appendCommand("sed -i -e '/" + vo.getNodeIP() + "/d' /etc/hosts");
        command.appendCommand(" > /etc/hosts");
        command.appendCommand("echo 127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4>>/etc/hosts");
        command.appendCommand("echo ::1         localhost localhost.localdomain localhost6 localhost6.localdomain6>>/etc/hosts");
        command.appendCommand("echo " + vo.getNodeIP() + " " + vo.getNodeName() + ">>/etc/hosts");//记录自己的节点
        sshExec(conn,command);
        if (!isDel){
            //设置hostname
            ConsoleCommand setHostname = new ConsoleCommand();
            setHostname.appendCommand("echo " + vo.getNodeName() + "> /etc/hostname");
            sshExec(conn,setHostname);
            List<ConsoleCommand> list = vos.stream().filter(fvo -> !fvo.getNodeIP().equals(vo.getNodeIP())).map(nvo -> {
                ConnVo connv = NodeUtils.createConnFromNode(nvo);
                ConsoleCommand command1 = new ConsoleCommand();
                command1.appendCommand("echo " + nvo.getNodeIP() + " " + nvo.getNodeName() + ">>/etc/hosts");
                ConsoleCommand ncommand = new ConsoleCommand();
                ncommand.appendCommand("echo " + vo.getNodeIP() + " " + vo.getNodeName() + ">>/etc/hosts");
                sshExec(connv,ncommand);
                return command1;
            }).collect(Collectors.toList());
            try {
                CommonSshExecUtil.exec(conn, list.toArray(new ConsoleCommand[list.size()]));
            } catch (Exception e) {
                throw new NodeInitRuntimeException(e);
            }
        }else {
            vos.stream().filter(fvo -> !fvo.getNodeIP().equals(vo.getNodeIP())).forEach(nvo -> {
                ConnVo connv = NodeUtils.createConnFromNode(nvo);
                try {
                    CommonSshExecUtil.exec(connv,commandDel);
                } catch (Exception e) {
                    throw new NodeInitRuntimeException(e);
                }
            });
        }
    }

    /**
     * 在机器上安装ceph和ceph-deploy
     * @param vo
     */
    public static CommonResultVo installCeph(CephNodeInfoVo vo) {
        CommonResultVo resultVo;
        //安装ceph和ceph-deploy
        String cephBaseUrl = SystemConfigUtil.getSystemConfigValue("ceph.yum.baseurl");
        String cephDeployBaseUrl = SystemConfigUtil.getSystemConfigValue("ceph.deploy.yum.baseurl");
        String cephFileName = SystemConfigUtil.getSystemConfigValue("ceph.filename");
        String cephDeployFileName = SystemConfigUtil.getSystemConfigValue("ceph.deploy.filename");
        ConnVo conn = NodeUtils.createConnFromNode(vo);//获得连接
        ConsoleCommand command = new ConsoleCommand();
        command.appendCommand("rm -f /etc/yum.repos.d/ceph.repo");
        command.appendCommand("echo [" + cephFileName + "]>>/etc/yum.repos.d/ceph.repo");
        command.appendCommand("echo name=local>>/etc/yum.repos.d/ceph.repo");
        command.appendCommand("echo baseurl=" + cephBaseUrl + ">>/etc/yum.repos.d/ceph.repo");
        command.appendCommand("echo enabled=1>>/etc/yum.repos.d/ceph.repo");
        command.appendCommand("echo gpgcheck=0>>/etc/yum.repos.d/ceph.repo");
        command.appendCommand("echo [" + cephDeployFileName + "]>>/etc/yum.repos.d/ceph.repo");
        command.appendCommand("echo name=ceph>>/etc/yum.repos.d/ceph.repo");
        command.appendCommand("echo baseurl=" + cephDeployBaseUrl + ">>/etc/yum.repos.d/ceph.repo");
        command.appendCommand("echo gpgcheck=0>>/etc/yum.repos.d/ceph.repo");
        resultVo = sshExec(conn,command);
        if (!resultVo.isSuccess()){
            return resultVo;
        }
        ConsoleCommand inCommand = new ConsoleCommand();
        inCommand.appendCommand("mkdir -p " + vo.getNodePath() + " && chmod 777 " + vo.getNodePath());//创建文件夹
        inCommand.appendCommand("yum clean all");//清空yum缓存
        inCommand.appendCommand("yum install ceph-deploy -y");//安装ceph-deploy
        inCommand.appendCommand("yum install ceph -y");//安装ceph
        return sshExec(conn,inCommand);
    }

    private static CommonResultVo sshExec(ConnVo conn,ConsoleCommand command){
        CommonResultVo resultVo = new CommonResultVo();
        try {
            //构建ceph.reph文件
            CommonSshExecUtil.exec(conn,command);
        }catch (SSHExecuteException e) {
            String exceptionMsgContent = CommonUtil.getExceptionMsgContent(e);
            LOGGER.error(LOGGER.getName() + exceptionMsgContent);
            resultVo.setSuccess(false);
            resultVo.setErrorMsg("执行ssh失败");
            return resultVo;
        } catch (SSHConnectionException e) {
            LOGGER.error("连接主机失败，请检查ip，用户名，密码，端口"+e);
            resultVo.setSuccess(false);
            resultVo.setErrorMsg("连接主机失败，请检查ip，用户名，密码，端口");
            return resultVo;
        }
        resultVo.setSuccess(true);
        return resultVo;
    }

    /**
     * 集群扩容
     * @param vo
     * @param monNodes
     */
    public static void deployCluster(CephNodeInfoVo vo, List<CephNodeInfoVo> monNodes, List<CephNodeInfoVo> allNodes, CephClusterVo clusterVo){
        ConsoleCommand command = new ConsoleCommand();
        ConnVo conn = NodeUtils.createConnFromNode(vo);//获得连接
        if ("0".equals(vo.getNodeType())){
            //安装mon节点
            if(monNodes.isEmpty()){
                command.appendCommand("cd " + vo.getNodePath());//进入目录下
                command.appendCommand("ceph-deploy --overwrite-conf new " + vo.getNodeName());
                command.appendCommand("echo 'osd pool default size = " + clusterVo.getPoolSize() + "'>>ceph.conf");
                command.appendCommand("echo 'osd_pool_default_min_size = " + clusterVo.getMinPoolSize() + "'>>ceph.conf");
                command.appendCommand("ceph-deploy --overwrite-conf mon create-initial");
                command.appendCommand("ceph-deploy admin " + vo.getNodeName());
                command.appendCommand("sudo chmod +r /etc/ceph/ceph.client.admin.keyring");
                sshExec(conn,command);
            }else {
                //获得集群所有节点
                CephNodeInfoVo nvo = monNodes.get(0);
                ConnVo conn1 = NodeUtils.createConnFromNode(nvo);
                //修改mon节点的配置文件
                command.appendCommand("cd " + nvo.getNodePath());//进入目录下
                //修改ceph.conf文件
                command.appendCommand("sed -i -e 's/" + nvo.getNodeName() + "/" + nvo.getNodeName() + "," + vo.getNodeName() + "/g' ceph.conf");
                command.appendCommand("sed -i -e 's/" + nvo.getNodeIP() + "/" + nvo.getNodeIP() + "," + vo.getNodeIP() + "/g' ceph.conf");
                StringBuilder strNodes = new StringBuilder("ceph-deploy --overwrite-conf config push ");
                allNodes.stream().forEach(fvo -> {
                    strNodes.append(fvo.getNodeName() + " ");
                });
                command.appendCommand(strNodes.toString());
                command.appendCommand("ceph-deploy admin " + vo.getNodeName());
                command.appendCommand("ceph-deploy mon create " + vo.getNodeName());
                command.appendCommand("scp ./* " + vo.getNodeUser() + "@" + vo.getNodeName() + ":" + vo.getNodePath());
                sshExec(conn1,command);
                command = new ConsoleCommand();
                command.appendCommand("sudo chmod +r /etc/ceph/ceph.client.admin.keyring");
                sshExec(conn,command);
            }
        }else if ("1".equals(vo.getNodeType())){
            //安装osd节点
            CephNodeInfoVo nvo = monNodes.get(0);
            ConnVo conn1 = NodeUtils.createConnFromNode(nvo);
            command.appendCommand("cd " + nvo.getNodePath());
            command.appendCommand("ceph-deploy osd prepare " + vo.getNodeName() + ":" + vo.getNodePath());
            command.appendCommand("ceph-deploy osd activate " + vo.getNodeName() + ":" + vo.getNodePath());
            command.appendCommand("ceph-deploy admin " + vo.getNodeName());
            sshExec(conn1,command);
            ConsoleCommand ncommand = new ConsoleCommand();
            ncommand.appendCommand("sudo chmod +r /etc/ceph/ceph.client.admin.keyring");//确保新增节点可以操作这个文件
            sshExec(conn,ncommand);
        }
    }

    /**
     * 获得osd节点的编号用于删除等操作
     * @return
     */
    public static String getOsdName(CephNodeInfoVo vo){
        if (ServerCmdConstant.CEPH_OSD_NOD_NUM.equals(vo.getNodeType())){
            ConnVo conn = NodeUtils.createConnFromNode(vo);
            ConsoleCommand command = new ConsoleCommand();
            command.appendCommand("cat " + vo.getNodePath() + "/whoami");
            MsgVO msgVO = null;
            try {
                msgVO = CommonSshExecUtil.exec(conn,command).get(command);
                String[] resourceInfo = msgVO.getSysoutMsg().split("\\n");
                return "osd." + resourceInfo[0];
            } catch (SSHExecuteException e) {
                String exceptionMsgContent = CommonUtil.getExceptionMsgContent(e);
                LOGGER.error(LOGGER.getName() + exceptionMsgContent);
            } catch (SSHConnectionException e) {
                String exceptionMsgContent = CommonUtil.getExceptionMsgContent(e);
                LOGGER.error(LOGGER.getName() + exceptionMsgContent);
            }
        }
        return null;
    }

    /**
     * 将节点从集群移除
     * @param vo
     * @param monNode
     * @throws Exception
     */
    public static void deleteCephNode(CephNodeInfoVo vo,CephNodeInfoVo monNode,List<CephNodeInfoVo> nodes) throws Exception{
        ConnVo conn = NodeUtils.createConnFromNode(monNode);
        ConnVo connVo = NodeUtils.createConnFromNode(vo);
        ConsoleCommand command = new ConsoleCommand();
        if (ServerCmdConstant.CEPH_OSD_NOD_NUM.equals(vo.getNodeType())){
            stopService(vo);//关闭节点服务，但是开发过程中发现并没有对应执行文件s
            String osdName = vo.getNodeCName();
            if (ValidateUtil.isEmpty(vo.getNodeCName())){
                throw new Exception("未获得到osdName");
            }
            command.appendCommand("cd " + monNode.getNodePath());
            command.appendCommand("ceph osd out " + osdName);
            command.appendCommand("ceph osd crush remove " + osdName);
            command.appendCommand("ceph auth del " + osdName);
            sshExec(conn,command);
            rmCephOsdNode(monNode,vo);
        }else {
            int mons = nodes.stream().filter(fvo -> ServerCmdConstant.CEPH_MON_NOD_NUM.equals(fvo.getNodeType())).collect(Collectors.toList()).size();
            if (mons > 1){
                command.appendCommand("cd " + monNode.getNodePath());
                //修改ceph.conf文件
                command.appendCommand("sed -i -e 's/," + vo.getNodeCName() + "//g' ceph.conf");
                command.appendCommand("sed -i -e 's/" + vo.getNodeCName() + "//g' ceph.conf");
                command.appendCommand("sed -i -e 's/," + vo.getNodeIP() + "//g' ceph.conf");
                command.appendCommand("sed -i -e 's/" + vo.getNodeIP() + "//g' ceph.conf");
                //remove对应mon节点
                command.appendCommand("ceph mon remove " + vo.getNodeCName());
                //将配置push
                StringBuilder strNodes = new StringBuilder("ceph-deploy --overwrite-conf config push ");
                nodes.stream().forEach(fvo -> {
                    strNodes.append(fvo.getNodeName() + " ");
                });
                command.appendCommand(strNodes.toString());
                CommonSshExecUtil.exec(conn,command);
            }
        }
        command = new ConsoleCommand();
        command.appendCommand("ceph-deploy purge " + vo.getNodeName(),
                "ceph-deploy purgedata " + vo.getNodeName());
        CommonSshExecUtil.exec(conn,command);
        ConsoleCommand commandRm = new ConsoleCommand();
        commandRm.appendCommand("rm -rf " + vo.getNodePath());
        CommonSshExecUtil.exec(connVo,commandRm);
        eraseCephDeploy(vo);
        //将免密登录解除
        unNopwdLogin(vo,nodes);
        //设置节点hosts文件
        setHosts(vo,nodes,true);
    }

    public static void rmCephOsdNode(CephNodeInfoVo monNode, CephNodeInfoVo vo) throws Exception{
        ConnVo conn = NodeUtils.createConnFromNode(monNode);
//        ConnVo connOsd = NodeUtils.createConnFromNode(vo);
        ConsoleCommand command = new ConsoleCommand();
        ConsoleCommand command1 = new ConsoleCommand();
        String osdName = vo.getNodeCName();
        command.appendCommand("ceph osd down " + osdName);
        command1.appendCommand("ceph osd tree |grep " + osdName + " |awk '{print $4}'");
        try {
            CommonSshExecUtil.exec(conn,command);
            MsgVO msgVO = CommonSshExecUtil.exec(conn,command1).get(command1);
            String[] resourceInfo = msgVO.getSysoutMsg().split("\\n");
            if ("down".equals(resourceInfo[0])){
                ConsoleCommand commandRm = new ConsoleCommand();
                commandRm.appendCommand("ceph osd rm " + osdName,
                        "sed -i -e '/" + vo.getNodeCName() + "/d;/" + vo.getNodeName() + "/d' " + monNode.getNodePath() + "/ceph.conf");
                CommonSshExecUtil.exec(conn,commandRm);
//                commandRm = new ConsoleCommand();
//                commandRm.appendCommand("rm -rf " + vo.getNodePath());
//                CommonSshExecUtil.exec(connOsd,commandRm);
            }else {
                rmCephOsdNode(monNode,vo);
            }
        } catch (SSHExecuteException e) {
            throw new Exception("ServerUtils:执行ssh命令失败");
        } catch (SSHConnectionException e) {
            throw new Exception("ServerUtils:连接远程服务器失败");
        }
    }

    /**
     * 卸载ceph-deploy
     */
    public static void eraseCephDeploy(CephNodeInfoVo vo) throws Exception{
        ConnVo conn = NodeUtils.createConnFromNode(vo);
        ConsoleCommand command = new ConsoleCommand();
        command.appendCommand("yum erase ceph-deploy -y");
        try {
            CommonSshExecUtil.exec(conn,command);
        }catch (Exception e){
            throw new Exception("卸载ceph-deploy失败");
        }
    }

    /**
     * 停止对应节点服务
     * @param vo
     */
    public static void stopService(CephNodeInfoVo vo) throws Exception{
        ConnVo conn = NodeUtils.createConnFromNode(vo);
        ConsoleCommand command = new ConsoleCommand();
        if (ServerCmdConstant.CEPH_OSD_NOD_NUM.equals(vo.getNodeType())){
            command.appendCommand("systemctl stop ceph-osd.target");
        }else {
            command.appendCommand("systemctl stop ceph-mon.target");
        }
        try {
            CommonSshExecUtil.exec(conn,command);
        }catch (Exception e){
            throw new Exception("停止节点失败");
        }
    }

    /**
     * 启动对应节点服务
     * @param vo
     */
    public static void startService(CephNodeInfoVo vo) throws Exception{
        ConnVo conn = NodeUtils.createConnFromNode(vo);
        ConsoleCommand command = new ConsoleCommand();
        if (ServerCmdConstant.CEPH_OSD_NOD_NUM.equals(vo.getNodeType())){
            command.appendCommand("systemctl start ceph-osd.target");
        }else {
            command.appendCommand("systemctl start ceph-mon.target");
        }
        try {
            CommonSshExecUtil.exec(conn,command);
        }catch (Exception e){
            throw new Exception("启动节点失败");
        }
    }

    /**
     * 刷新ceph集群资源用于节点增删改后操作
     * @param clusterId
     */
    public static void refreshResources(Long clusterId){
        CephClusterVo clusterVo = cephClusterBpo.getClusterById(clusterId);
        List<CephNodeInfoVo> vos = cephNodeBpo.getAllCephNode(clusterId);
        List<CephNodeInfoVo> newVos = vos.stream().map(fvo -> {
            updateCephNode(fvo);
            return fvo;
        }).collect(Collectors.toList());
        clusterVo.setNumOfNodes(vos.size());
        Double memoryTotal = newVos.stream().filter(fvo -> ServerCmdConstant.CEPH_OSD_NOD_NUM.equals(fvo.getNodeType()) && fvo.getNodeRunningState() == NodeState.RUNNING_SUCCESS).mapToDouble(CephNodeInfoVo::getNodeDiskToTalMb).sum();
        Double memoryAvaiLabel = newVos.stream().filter(fvo -> ServerCmdConstant.CEPH_OSD_NOD_NUM.equals(fvo.getNodeType()) && fvo.getNodeRunningState() == NodeState.RUNNING_SUCCESS).mapToDouble(CephNodeInfoVo::getNodeDiskToTalMbIdealState).sum();
        clusterVo.setMemoryAvaiLabel(memoryAvaiLabel);
        clusterVo.setMemoryTotal(memoryTotal);
        cephClusterBpo.updateCluster(clusterVo);
    }

    /**
     * 更新节点资源
     * @param vo
     * @return
     */
    public static CephNodeInfoVo updateCephNode(CephNodeInfoVo vo){
        //更新资源锁
        synchronized (UPDATE_CEPHNODE_REOURCE_LOCK){
            CephNodeInfoVo oldVo = new CephNodeInfoVo();
            oldVo.setNodeDiskToTalMb(vo.getNodeDiskToTalMb());
            oldVo.setNodeDiskToTalMbIdealState(vo.getNodeDiskToTalMbIdealState());
            CephNodeInfoVo newVo = (CephNodeInfoVo) getCephNodeInfo(vo,true,true).getData();
            if (newVo == null || vo.getNodeRunningState() != 1){
                return new CephNodeInfoVo();
            }
            newVo.setNodeDiskToTalMbIdealState(newVo.getNodeDiskToTalMb() - (oldVo.getNodeDiskToTalMb() - oldVo.getNodeDiskToTalMbIdealState()));
            cephNodeBpo.update(newVo);
            return newVo;
        }
    }

    /**
     * 创建存储池
     * @param vo
     */
    public static void createPool(CephPoolVo vo)  throws Exception{
        //获得集群下任意一个mon节点
        List<CephNodeInfoVo> nodes = cephNodeBpo.getMonNodeInfo(vo.getClusterId());
        if (nodes.isEmpty()){
            throw new Exception("集群下面没有mon节点");
        }
        //创建连接
        ConnVo conn = NodeUtils.createConnFromNode(nodes.get(0));
        //执行命令，创建pool
        ConsoleCommand command = new ConsoleCommand();
        command.appendCommand("ceph osd pool create " + vo.getPoolName() + " " + vo.getPgNum());
        try {
            CommonSshExecUtil.exec(conn,command);
        } catch (SSHExecuteException e) {
            throw new Exception("ServerUtils:执行ssh命令失败");
        } catch (SSHConnectionException e) {
            throw new Exception("ServerUtils:连接远程服务器失败");
        }
    }

    /**
     * 修改pool的pgNum
     * @param vo
     * @throws Exception
     */
    public static void setPgNum(CephPoolVo vo) throws Exception{
        //获得集群下任意一个mon节点
        List<CephNodeInfoVo> nodes = cephNodeBpo.getMonNodeInfo(vo.getClusterId());
        if (nodes.isEmpty()){
            throw new Exception("集群下面没有mon节点");
        }
        //创建连接
        ConnVo conn = NodeUtils.createConnFromNode(nodes.get(0));
        //执行命令，修改pool的pgNum
        ConsoleCommand command = new ConsoleCommand();
        command.appendCommand("ceph osd pool set " + vo.getPoolName() + " pg_num " + vo.getPgNum());
        try {
            CommonSshExecUtil.exec(conn,command);
        } catch (SSHExecuteException e) {
            throw new Exception("ServerUtils:执行ssh命令失败");
        } catch (SSHConnectionException e) {
            throw new Exception("ServerUtils:连接远程服务器失败");
        }
    }

    /**
     * 修改pool的名称
     * @param oldVo
     * @param vo
     * @throws Exception
     */
    public static void setPoolName(CephPoolVo oldVo,CephPoolVo vo) throws Exception{
        //获得集群下任意一个mon节点
        List<CephNodeInfoVo> nodes = cephNodeBpo.getMonNodeInfo(vo.getClusterId());
        if (nodes.isEmpty()){
            throw new Exception("集群下面没有mon节点");
        }
        //创建连接
        ConnVo conn = NodeUtils.createConnFromNode(nodes.get(0));
        //执行命令，修改pool名称
        ConsoleCommand command = new ConsoleCommand();
        command.appendCommand("ceph osd pool rename " + oldVo.getPoolName() + " " + vo.getPoolName());
        try {
            CommonSshExecUtil.exec(conn,command);
        } catch (SSHExecuteException e) {
            throw new Exception("ServerUtils:执行ssh命令失败");
        } catch (SSHConnectionException e) {
            throw new Exception("ServerUtils:连接远程服务器失败");
        }
    }

    /**
     * 删除存储池
     * @param vo
     * @throws Exception
     */
    public static void deletePool(CephPoolVo vo) throws Exception{
        //判断该存储池是否被其他pv挂载
        boolean poolIsPv = pvbpo.queryByPoolId(vo.getPoolId());
        if (poolIsPv){
            throw new Exception("存储池已被挂载，请先删除挂载！");
        }
        //获得集群下任意一个mon节点
        List<CephNodeInfoVo> nodes = cephNodeBpo.getMonNodeInfo(vo.getClusterId());
        if (nodes.isEmpty()){
            throw new Exception("集群下面没有mon节点");
        }
        //创建连接
        ConnVo conn = NodeUtils.createConnFromNode(nodes.get(0));
        //执行命令，删除pool
        ConsoleCommand command = new ConsoleCommand();
        command.appendCommand("ceph osd pool delete " + vo.getPoolName() + " " + vo.getPoolName() + " --yes-i-really-really-mean-it  ");
        try {
            CommonSshExecUtil.exec(conn,command);
        } catch (SSHExecuteException e) {
            throw new Exception("ServerUtils:执行ssh命令失败");
        } catch (SSHConnectionException e) {
            throw new Exception("ServerUtils:连接远程服务器失败");
        }
    }

    /**
     * 创建mds
     * @param vo
     * @throws Exception
     */
    public static void setMonToMds(CephNodeInfoVo vo) throws Exception{
        ConnVo conn = NodeUtils.createConnFromNode(vo);
        ConsoleCommand command = new ConsoleCommand();
        command.appendCommand("cd " + vo.getNodePath(),"ceph-deploy  --overwrite-conf mds create " + vo.getNodeName());
        try {
            CommonSshExecUtil.exec(conn,command);
        } catch (SSHExecuteException e) {
            throw new Exception("ServerUtils:执行ssh命令失败");
        } catch (SSHConnectionException e) {
            throw new Exception("ServerUtils:连接远程服务器失败");
        }
    }

    /**
     * 创建文件系统
     * @param vo
     * @param pools
     * @param fsName
     * @throws Exception
     */
    public static void createCephFs(CephNodeInfoVo vo, List<CephPoolVo> pools,String fsName) throws Exception{
        ConnVo conn = NodeUtils.createConnFromNode(vo);
        ConsoleCommand command = new ConsoleCommand();
        command.appendCommand("cd " + vo.getNodePath(),"ceph fs new " + fsName + " " + pools.get(0).getPoolName() + " " + pools.get(1).getPoolName());
        try {
            CommonSshExecUtil.exec(conn,command);
            CephPoolVo pvo = pools.get(0);
            pvo.setPoolIsUse(1);
            cephPoolBpo.updatePool(pvo);
            CephPoolVo pvo1 = pools.get(1);
            pvo1.setPoolIsUse(1);
            cephPoolBpo.updatePool(pvo1);
        } catch (SSHExecuteException e) {
            throw new Exception("执行ssh命令失败");
        } catch (SSHConnectionException e) {
            throw new Exception("连接远程服务器失败");
        }
    }

    /**
     * 创建pv
     * @param masterNode
     * @param pv
     * @throws Exception
     */
    public static CephPoolVo createCephPv(NodeBasicInfoVo masterNode, PvVo pv) throws Exception {
        ConnVo masterConn = NodeUtils.createConnFromNode(masterNode);
        CephClusterVo cephClusterVo = cephClusterBpo.getClusterById(pv.getVolumeCloudStorageId());
        List<CephNodeInfoVo> monNodes = cephNodeBpo.getMonNodeInfo(pv.getVolumeCloudStorageId());
        int osdNum = cephNodeBpo.getOsdNodeNum(pv.getVolumeCloudStorageId());
        if (monNodes.isEmpty()){
            throw new Exception("ceph集群没有mon节点");
        }
        if (osdNum == 0){
            throw new Exception("ceph集群没有osd节点");
        }
        if (osdNum < cephClusterVo.getMinPoolSize()){
            throw new Exception("osd节点数量需要大于集群最小副本数(" + cephClusterVo.getMinPoolSize() + ")");
        }
        String serverYamlFilePath = SystemConfigUtil.getSystemConfigValue("ceph.file.path")+ ServerCmdConstant.FILE_SEPARATOR + pv.getVolumeUuid() + ServerCmdConstant.FILE_SEPARATOR + ServerCmdConstant.CEPH_TEMPLATE_STORAGECLASS;
        //设置rbd权限
        setRbdRole(masterConn);
        //设置密钥
        CephPoolVo pool = setSecret(monNodes,masterConn,cephClusterVo);
        StorageClassCephObject cephObject = new StorageClassCephObject(pv.getVolumeUuid(),monNodes,pool,cephClusterVo);
        UploadContentCommand uploadSaYaml = new UploadContentCommand(serverYamlFilePath,cephObject.buildYaml());
        CommonSshExecUtil.exec(masterConn,uploadSaYaml);
        ConsoleCommand updateKubeletRes = new ConsoleCommand();
        updateKubeletRes.appendCommand("source ~/.bash_profile && kubectl apply -f " + serverYamlFilePath);
        CommonSshExecUtil.exec(masterConn, uploadSaYaml, updateKubeletRes);
        //创建完成后各个节点资源刷新
        double dick;
        dick = 1.0 * pv.getVolumeMaxSpace() * cephClusterVo.getPoolSize()/osdNum;
        DecimalFormat df = new DecimalFormat("######0.00");
        cephNodeBpo.setResourcesChange(Double.valueOf(df.format(dick)),pv.getVolumeCloudStorageId(),false);
        return pool;
    }

    /**
     * 设置rbd权限
     * @param masterConn
     */
    private static void setRbdRole(ConnVo masterConn) throws Exception{
        ConsoleCommand command = new ConsoleCommand();
        command.appendCommand("kubectl apply -f " + SystemConfigUtil.getSystemConfigValue("ceph.rbdrole.path"));
        try {
            CommonSshExecUtil.exec(masterConn,command);
        } catch (SSHExecuteException e) {
            throw new Exception("ServerUtils:执行ssh命令失败");
        } catch (SSHConnectionException e) {
            throw new Exception("ServerUtils:连接远程服务器失败");
        }
    }


    /**
     * 设置ceph必要的密钥
     * @param nodes
     * @param masterConn
     * @param cephClusterVo
     * @throws Exception
     */
    public static CephPoolVo setSecret(List<CephNodeInfoVo> nodes,ConnVo masterConn,CephClusterVo cephClusterVo) throws Exception{
        if (!nodes.isEmpty()){
            CephNodeInfoVo monNode = nodes.get(0);
            ConnVo conn = NodeUtils.createConnFromNode(monNode);
            ConsoleCommand admincommand = new ConsoleCommand();
            ConsoleCommand poolcommand = new ConsoleCommand();
            ConsoleCommand command1 = new ConsoleCommand();
            ConsoleCommand master = new ConsoleCommand();
            admincommand.appendCommand("ceph auth get-key client.admin");
            List<CephPoolVo> pools = cephPoolBpo.getAllUmountPools(monNode.getClusterId());
            if (pools.isEmpty()){
                throw new Exception("没有可用存储池pool");
            }
            SecureRandom random = new SecureRandom();
            CephPoolVo pool = pools.get(random.nextInt(pools.size()));
            command1.appendCommand("ceph auth add client." + pool.getPoolName() + " mon 'allow r' osd 'allow rwx pool=" + pool.getPoolName() + "'");
            poolcommand.appendCommand("ceph auth get-key client." + pool.getPoolName());
            MsgVO msgVO = null;
            try {
                msgVO = CommonSshExecUtil.exec(conn,command1,admincommand).get(admincommand);
                String[] resourceInfo = msgVO.getSysoutMsg().split("\\n");
                String adminkey = resourceInfo[0];
                String poolkey = CommonSshExecUtil.exec(conn,poolcommand).get(poolcommand).getSysoutMsg().split("\\n")[0];
                ConsoleCommand getSecret = new ConsoleCommand();
                getSecret.appendCommand("kubectl get secret |grep -E 'ceph-admin-secret-"+ cephClusterVo.getIdentify() + "|ceph-secret-" + cephClusterVo.getIdentify() + "-" + pool.getPoolName() + "' | awk '{print $1}'");
                String secret = CommonSshExecUtil.exec(masterConn,getSecret).get(getSecret).getSysoutMsg().split("\\n")[0];
                if (ValidateUtil.isNotEmpty(secret)){
                    master.appendCommand("kubectl get secret |grep -E 'ceph-admin-secret-"+ cephClusterVo.getIdentify() + "|ceph-secret-" + cephClusterVo.getIdentify() + "-" + pool.getPoolName() + "'| awk '{print $1}' | xargs kubectl delete secret");
                }
                master.appendCommand("mkdir /secret -p","echo '" + adminkey + "' > /secret/adminkey-" + cephClusterVo.getIdentify(),
                        "echo '" + poolkey + "' > /secret/key-" + cephClusterVo.getIdentify() + "-" + pool.getPoolName(),
                        "kubectl create secret generic ceph-admin-secret-" + cephClusterVo.getIdentify() + " --from-file=/secret/adminkey-" + cephClusterVo.getIdentify() + " --namespace=default  --type=kubernetes.io/rbd",
                        "kubectl create secret generic ceph-secret-" + cephClusterVo.getIdentify() + "-" + pool.getPoolName() + " --from-file=/secret/key-" + cephClusterVo.getIdentify() + "-" + pool.getPoolName() + " --namespace=default   --type=kubernetes.io/rbd");
                CommonSshExecUtil.exec(masterConn,master);

            } catch (SSHExecuteException e) {
                throw new Exception("ServerUtils:执行ssh命令失败");
            } catch (SSHConnectionException e) {
                throw new Exception("ServerUtils:连接远程服务器失败");
            }
            return pool;
        }
        else {
            throw new Exception("ceph没有节点");
        }
    }

    /**
     * 删除ceph的挂载存储sc
     * @param pv
     */
    public static void deleteCephPv(NodeBasicInfoVo masterNode,PvVo pv) throws Exception{
        //删除sc和sc文件
        ConnVo conn = NodeUtils.createConnFromNode(masterNode);
        ConsoleCommand command = new ConsoleCommand();
        int osdNum = cephNodeBpo.getOsdNodeNum(pv.getVolumeCloudStorageId());
        CephPoolVo pool = cephPoolBpo.getPoolById(pv.getCephPoolId());//获得pv绑定的存储池
        CephClusterVo clusterVo = cephClusterBpo.getClusterById(pv.getVolumeCloudStorageId());
        command.appendCommand("source ~/.bash_profile && kubectl delete -f " + SystemConfigUtil.getSystemConfigValue("ceph.file.path") + "/" + pv.getVolumeUuid() + "/" + ServerCmdConstant.CEPH_TEMPLATE_STORAGECLASS);
        ConsoleCommand getSecret = new ConsoleCommand();
        getSecret.appendCommand("kubectl get secret |grep -E 'ceph-admin-secret-"+ clusterVo.getIdentify() + "|ceph-secret-" + clusterVo.getIdentify() + "-" + pool.getPoolName() + "' | awk '{print $1}'");
        String secret;
        if (osdNum == 0){
            throw new Exception("ceph集群没有osd节点");
        }
        try {
            secret = CommonSshExecUtil.exec(conn,getSecret).get(getSecret).getSysoutMsg().split("\\n")[0];
            //验证该集群下是否还有其他pv挂载
            List<PvVo> pvVos = pvbpo.queryAll(pv.getClusterId());
            long pvNum = pvVos.stream().filter(it -> {
                if (!it.getVolumeUuid().equals(pv.getVolumeUuid())  && it.getVolumeCloudStorageId().equals(pv.getVolumeCloudStorageId())){
                    return true;
                }else {
                    return false;
                }
            }).count();
            long pvNumAndPool = pvVos.stream().filter(it -> {
                if (!it.getVolumeUuid().equals(pv.getVolumeUuid())  && pv.getCephPoolId().equals(it.getCephPoolId())){
                    return true;
                }else {
                    return false;
                }
            }).count();
            if (ValidateUtil.isNotEmpty(secret)){
                if (pvNum == 0){
                    //删除掉secret
                    command.appendCommand("kubectl get secret |grep -E 'ceph-admin-secret-"+ clusterVo.getIdentify() + "|ceph-secret-" + clusterVo.getIdentify() + "-" + pool.getPoolName() + "'| awk '{print $1}' | xargs kubectl delete secret");
                    pool.setPoolIsUse(0);
                    cephPoolBpo.updatePool(pool);
                }else if (pvNumAndPool == 0){
                    command.appendCommand("kubectl delete secret ceph-secret-" + clusterVo.getIdentify() + "-" + pool.getPoolName());
                    pool.setPoolIsUse(0);
                    cephPoolBpo.updatePool(pool);
                }
            }
            CommonSshExecUtil.exec(conn,command);
        } catch (SSHExecuteException e) {
            throw new Exception("执行命令失败！");
        } catch (SSHConnectionException e) {
            throw new Exception("未连接到服务器");
        }
        //修改ceph节点理想空间大小
        setCephNodeResouresChange(pv.getVolumeMaxSpace(),pv.getVolumeCloudStorageId());
    }

    /**
     * 将集群中sc占用资源归还ceph集群
     * @param clusterId
     */
    public static void deletePvByClusterId(Long clusterId){
        List<PvVo> pvs = pvbpo.queryAll(clusterId.toString());
        if (pvs != null){
            for (PvVo pv : pvs){
                if (pv.getCephPoolId() != null){
                    setCephNodeResouresChange(pv.getVolumeMaxSpace(),pv.getVolumeCloudStorageId());
                }
            }
        }
    }

    /**
     * 修改ceph理想空间大小
     * @param volumeMaxSpace
     * @param volumeCloudStorageId
     */
    private static void setCephNodeResouresChange(Integer volumeMaxSpace, Long volumeCloudStorageId) {
        int osdNum = cephNodeBpo.getOsdNodeNum(volumeCloudStorageId);
        CephClusterVo clusterVo = cephClusterBpo.getClusterById(volumeCloudStorageId);
        //ceph集群osd节点可用磁盘恢复
        double dick;
        dick = 1.0 * volumeMaxSpace * clusterVo.getPoolSize()/osdNum;
        DecimalFormat df = new DecimalFormat("######0.00");
        cephNodeBpo.setResourcesChange(Double.valueOf(df.format(dick)),volumeCloudStorageId,true);
    }

    /**
     * 将节点分配占用的空间转移到其他节点
     * @param vo
     */
    public static void setNodeResource(CephNodeInfoVo vo) {
        List<CephNodeInfoVo> vos = cephNodeBpo.getOsdNodeInfo(vo.getClusterId()).stream().filter(fvo -> fvo.getNodeId()!=vo.getNodeId()).collect(Collectors.toList());
        if (ValidateUtil.isNotEmpty(vos)){
            int osdNum = vos.size();
            CephClusterVo clusterVo = cephClusterBpo.getClusterById(vo.getClusterId());
            //ceph集群osd节点可用磁盘恢复
            double dick;
            dick = (vo.getNodeDiskToTalMb() - vo.getNodeDiskToTalMbIdealState())/osdNum;
            DecimalFormat df = new DecimalFormat("######0.00");
            cephNodeBpo.setResourcesChange(Double.valueOf(df.format(dick)),vo.getClusterId(),false);
        }
    }

    /**
     * 从磁盘大小判断是否可以删除该节点
     * @param vo
     */
    public static CommonResultVo checkCephTatol(CephNodeInfoVo vo) {
        CommonResultVo result = new CommonResultVo();
        List<CephNodeInfoVo> vos = cephNodeBpo.getOsdNodeInfo(vo.getClusterId()).stream().filter(fvo -> fvo.getNodeId()!=vo.getNodeId()).collect(Collectors.toList());
        Double allTotal = 0.0;
        if (ValidateUtil.isNotEmpty(vos)){
            allTotal = vos.stream().mapToDouble(CephNodeInfoVo::getNodeDiskToTalMbIdealState).sum();
        }
        if (allTotal - (vo.getNodeDiskToTalMb() - vo.getNodeDiskToTalMbIdealState()) >= 0){
            result.setSuccess(true);
            return result;
        }
        result.setSuccess(false);
        result.setErrorMsg("删除该节点后集群内存小于已分配内存，不可删除");
        return result;
    }

    public static void deletePools(List<CephPoolVo> list) throws Exception{
        for (CephPoolVo poolVo : list){
            try {
                if (poolVo.getPoolIsUse() == 0){
                    deletePool(poolVo);
                }
            } catch (Exception e) {
                throw new Exception("删除pool失败");
            }
        }
    }

    /**
     * 删除ceph集群时将挂载了ceph集群的所有sc同时删除
     * @param clusterId
     */
    public static void deletePvByCephClusterId(Long clusterId) throws Exception{
        CephClusterVo vo = cephClusterBpo.getClusterById(clusterId);
        List<Long> poolIds = cephPoolBpo.getAllPools(clusterId).stream().map(pvo -> {
            return pvo.getPoolId();
        }).collect(Collectors.toList());
        if (!poolIds.isEmpty()){
            List<PvVo> pvs = pvbpo.queryAll(vo.getClusterId().toString()).stream().filter(pvvo -> poolIds.contains(pvvo.getCephPoolId())).collect(Collectors.toList());
            for (PvVo pvo : pvs){
                try {
                    pvbpo.deletePv(pvo);
                } catch (Exception e) {
                    throw new Exception(e.getMessage());
                }
            }
        }

    }

    /**
     * 设置管理节点是否可以部署应用
     * @param vo
     * @throws Exception
     */
    public static void setNodeAsChild(NodeBasicInfoVo vo) throws Exception{
        ConnVo conn = NodeUtils.createConnFromNode(vo);
        ConsoleCommand command = new ConsoleCommand();
        if (ResourceConstant.MASTER_ALSO_AS_CHILD.equals(vo.getNodeAsChild())){
            command.appendCommand("kubectl taint nodes " + vo.getNodeName() + " node-role.kubernetes.io/master=true:NoSchedule");
//            command.appendCommand("source ~/.bash_profile && kubectl taint nodes " + vo.getNodeName() + " node-role/" + vo.getNodeName() + "=:NoExecute");
        }else {
            command.appendCommand("kubectl taint nodes " + vo.getNodeName() + " node-role.kubernetes.io/master-");
//            command.appendCommand("source ~/.bash_profile && kubectl taint nodes " + vo.getNodeName() + " " + vo.getNodeName() + ":NoExecute- ");
        }
        CommonSshExecUtil.exec(conn,command);
    }
}
