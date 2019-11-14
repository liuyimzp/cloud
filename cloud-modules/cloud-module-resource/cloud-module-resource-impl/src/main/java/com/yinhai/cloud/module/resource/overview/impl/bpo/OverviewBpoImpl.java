package com.yinhai.cloud.module.resource.overview.impl.bpo;

import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.application.api.bpo.INamespaceBpo;
import com.yinhai.cloud.module.application.api.util.DuplicateCodeUtil;
import com.yinhai.cloud.module.application.api.vo.NamespaceVo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.cluster.impl.dao.AppArrDao;
import com.yinhai.cloud.module.resource.cluster.impl.dao.ImageArrDao;
import com.yinhai.cloud.module.resource.cluster.impl.po.AppArrVo;
import com.yinhai.cloud.module.resource.cluster.impl.po.ImageArrVo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.overview.bpo.IOverviewBpo;
import com.yinhai.cloud.module.resource.overview.impl.dao.IOverviewDao;
import com.yinhai.cloud.module.resource.overview.impl.po.SpaceInfoPo;
import com.yinhai.cloud.module.resource.overview.vo.OverviewInfoVo;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengwei on 2018/9/28.
 */
public class OverviewBpoImpl extends TaBaseBpo implements IOverviewBpo {

    @Autowired
    private IOverviewDao overviewDao;

    @Resource
    private IClusterBpo clusterBpo;

    @Resource
    private INamespaceBpo namespaceBpo;

    @Resource
    private INodeBpo nodeBpo;
    @Resource
    private ImageArrDao imageArrDao;

    @Resource
    private AppArrDao appArrDao;

    private static final Logger logger = LoggerFactory.getLogger(OverviewBpoImpl.class);

    @Override
    public OverviewInfoVo getPlatformInfo(Long userId) throws Exception {
        OverviewInfoVo overviewInfoVo = new OverviewInfoVo();

        List<ImageArrVo> listImage = imageArrDao.getAllAppImages();//获得所有镜像
        Map<String, List<String>> mapImage = getNewImage(listImage);
        overviewInfoVo.setImageNum(mapImage.get("num"));
        overviewInfoVo.setImageTime(mapImage.get("time"));

        List<AppArrVo> appArrVoList = appArrDao.getAllApp();//获得所有应用
        Map<String, List<String>> mapApp = getNewApp(appArrVoList);
        overviewInfoVo.setAppNum(mapApp.get("num"));
        overviewInfoVo.setAppTime(mapApp.get("time"));

        Integer clusterCount = overviewDao.countClusters(userId);
        //Integer runningClusters = overviewDao.countRunningClusters(userId);

        Integer nodeCount = overviewDao.countNodes(userId);
        Integer runningNodes = overviewDao.countRunningNodes(userId);

        Integer appCount = overviewDao.countApps(userId);
        Integer runningApps = overviewDao.countRunningApps(userId);
        Integer stopApps = overviewDao.countStopApps(userId);
        Integer noRunningApps = overviewDao.countNotRunningApps(userId);
        Integer noReleaseApps = overviewDao.countNoReleases(userId);

        Integer userImages = overviewDao.countDockerImages(userId);
        Integer middlewareImages = overviewDao.countDockerImagesByMiddleware(userId);

//        List<ImagePo> aadvList = arrAndDateDao.getArrAndDateVo(userId);
        overviewInfoVo.setTotalClusters(clusterCount);
        overviewInfoVo.setRunningClusters(clusterCount);
        overviewInfoVo.setAbnormalClusters(0);

        overviewInfoVo.setNoReleases(noReleaseApps);
        overviewInfoVo.setNotRunnings(noRunningApps);
        overviewInfoVo.setTotalNodes(nodeCount);
        overviewInfoVo.setRunningNodes(runningNodes);
        overviewInfoVo.setAbnormalNodes(nodeCount.intValue() - runningNodes.intValue());

        overviewInfoVo.setTotalApps(appCount);
        overviewInfoVo.setRunningApps(runningApps);
        overviewInfoVo.setStopApps(stopApps);
        overviewInfoVo.setNoDeployApps(appCount.intValue() - runningApps.intValue() - stopApps.intValue());

        overviewInfoVo.setUserImages(userImages);
        overviewInfoVo.setMiddlewareImages(middlewareImages);
        overviewInfoVo.setRegistryStatus(1);

//        overviewInfoVo.setTotalPods(getAllPods());

        List<SpaceInfoPo> spaceInfo = overviewDao.countStorage(userId);
        //liuyi 2019/3/1 修改
        try {
            overviewInfoVo.setTotalStorage(spaceInfo.get(0).getTotalSpace().intValue());
            overviewInfoVo.setFreeStorage(spaceInfo.get(0).getFreeSpace().intValue());
        } catch (NullPointerException e) {
            overviewInfoVo.setTotalStorage(0);
            overviewInfoVo.setFreeStorage(0);
        }

        return overviewInfoVo;
    }


    @Override
    public Integer getPodsSum() {
        int pods = 0;
        //获得所有集群
        List<ClusterVo> clusterVoList = clusterBpo.queryAllClusterBasicInfo(1L);
        //获得每个集群下所有命名空间
        if (!clusterVoList.isEmpty()) {
            for (ClusterVo clusterVo : clusterVoList) {
                List<NodeBasicInfoVo> masterNodes = nodeBpo.queryRunningMasterNodesByClusterId(clusterVo.getId());//获得节点用来运行命令
                if (ValidateUtil.isEmpty(masterNodes)) {
                    continue;//如果集群下没有运行的节点则跳出循环进入下一循环
                }
                ConnVo connVo = NodeUtils.createConnFromNode(masterNodes.get(0));//获得集群下的一个节点用于执行命令
                List<NamespaceVo> namespacesList = namespaceBpo.getNamespaces(clusterVo.getId());//获得所有命名空间
                if (!namespacesList.isEmpty()) {
                    for (NamespaceVo namespaceVo : namespacesList) {
                        //连接每个节点查询pods
                        ConsoleCommand commandVo = new ConsoleCommand();
                        String commond = "kubectl get po -n " + namespaceVo.getNamespaceIdentify();//查询每个命名空间下的所有pods
                        commandVo.appendCommand(commond);
                        try {
                            Map resultMap = CommonSshExecUtil.exec(connVo, commandVo);
                            if (!ValidateUtil.isEmpty(resultMap) && resultMap.size() > 0) {
                                MsgVO msgVo = (MsgVO) resultMap.values().iterator().next();
                                String msg = msgVo.getSysoutMsg();
                                if (ValidateUtil.isNotEmpty(msg)) {
                                    List<Map> list = DuplicateCodeUtil.parseSysoutMsg(msg);
                                    if (!ValidateUtil.isEmpty(list)) {
                                        pods += list.size();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.error("集群名称:" + clusterVo.getName() + "；命名空间：" + namespaceVo.getNamespaceName() + ";" + e.getMessage());
                        }
                    }
                }
            }
        }
        return pods;
    }

    //将镜像列表转化为集合
    private Map<String, List<String>> getNewImage(List<ImageArrVo> imageList) {
        Map<String, List<String>> resultMap = new HashMap<>();
        int num = 0;
        if (!imageList.isEmpty()) {
            List listTime = new ArrayList();
            List listNum = new ArrayList();
            SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
            for (int i = 0; i < imageList.size(); i++) {
                if (i == 0) {//第一条数据没有比较直接加一
                    num++;
                    if (i == imageList.size() - 1) {//如果第一条数据也是最后一条数据
                        listTime.add(sdf.format(imageList.get(0).getCreateTime()));
                        listNum.add(String.valueOf(num));
                    }
                    continue;
                } else {
                    String beforeTime = sdf.format(imageList.get(i - 1).getCreateTime());
                    String nowTime = sdf.format(imageList.get(i).getCreateTime());
                    if (beforeTime.equals(nowTime)) {
                        num++;
                    } else {
                        listTime.add(beforeTime);
                        listNum.add(String.valueOf(num));
                        if (listTime.size() >= 5) {
                            break;//如果统计数据超过五条退出循环
                        }
                        num = 1;
                    }
                    if (i == imageList.size() - 1) {//最后一条数据不会有比较
                        listTime.add(beforeTime);
                        listNum.add(String.valueOf(num));
                    }
                }
            }
            resultMap.put("time", listTime);
            resultMap.put("num", listNum);
            return resultMap;
        }
        resultMap.put("time", new ArrayList<>());
        resultMap.put("num", new ArrayList<>());
        return resultMap;
    }

    //将镜像列表转化为集合
    private Map<String, List<String>> getNewApp(List<AppArrVo> appList) {
        Map<String, List<String>> resultMap = new HashMap<>();
        int num = 0;
        if (!appList.isEmpty()) {
            List listTime = new ArrayList();
            List listNum = new ArrayList();
            SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
            for (int i = 0; i < appList.size(); i++) {
                if (i == 0) {//第一条数据没有比较直接加一
                    num++;
                    if (i == appList.size() - 1) {//如果第一条数据也是最后一条数据
                        listTime.add(sdf.format(appList.get(0).getCreateTime()));
                        listNum.add(String.valueOf(num));
                    }
                    continue;
                } else {
                    String beforeTime = sdf.format(appList.get(i - 1).getCreateTime());
                    String nowTime = sdf.format(appList.get(i).getCreateTime());
                    if (beforeTime.equals(nowTime)) {
                        num++;
                    } else {
                        listTime.add(beforeTime);
                        listNum.add(String.valueOf(num));
                        if (listTime.size() >= 5) {
                            break;//如果统计数据超过五条退出循环
                        }
                        num = 1;
                    }
                    if (i == appList.size() - 1) {//最后一条数据不会有比较
                        listTime.add(beforeTime);
                        listNum.add(String.valueOf(num));
                    }
                }
            }
            resultMap.put("time", listTime);
            resultMap.put("num", listNum);
            return resultMap;
        }
        resultMap.put("time", new ArrayList<>());
        resultMap.put("num", new ArrayList<>());
        return resultMap;
    }
}
