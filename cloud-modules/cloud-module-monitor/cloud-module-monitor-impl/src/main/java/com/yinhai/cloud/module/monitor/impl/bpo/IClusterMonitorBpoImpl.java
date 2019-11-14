package com.yinhai.cloud.module.monitor.impl.bpo;

import com.yinhai.cloud.module.application.impl.dao.AppDao;
import com.yinhai.cloud.module.application.impl.po.AppPo;
import com.yinhai.cloud.module.monitor.api.bpo.IClusterMonitorBpo;
import com.yinhai.cloud.module.monitor.api.vo.ClusterMonitorVo;
import com.yinhai.cloud.module.monitor.impl.dao.IClusterMonitorDao;
import com.yinhai.cloud.module.monitor.impl.po.ClusterMonitorPo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pengwei on 2018/8/29.
 */
public class IClusterMonitorBpoImpl extends TaBaseBpo implements IClusterMonitorBpo {

    @Autowired
    private IClusterBpo clusterBpo;

    @Autowired
    private IClusterMonitorDao clusterMonitorDao;

    @Autowired
    private AppDao appDao;

    @Override
    public List<ClusterMonitorVo> getAllClustersMonitorInfo(String userId) throws Exception {
        List<ClusterMonitorVo> clusterMonitorVos = new ArrayList<ClusterMonitorVo>();
        List<ClusterVo> clusterVos = clusterBpo.queryAllClusterBasicInfo(1L);
        if (!clusterVos.isEmpty()) {
            ClusterMonitorVo clusterMonitorVo = null;
            for (ClusterVo clusterVo : clusterVos) {
                clusterMonitorVo = new ClusterMonitorVo();
                clusterMonitorVo.setClusterId(clusterVo.getId());
                clusterMonitorVo.setClusterName(clusterVo.getName());
                clusterMonitorVo.setTotalCpu(clusterVo.getTotalCpu());
                clusterMonitorVo.setTotalNodes(clusterVo.getNumOfNodes());
                clusterMonitorVo.setTotalMemory(clusterVo.getTotalMemory());
                clusterMonitorVo.setTotalMemAvailable(clusterVo.getTotalMemAvailable());

                List<AppPo> appPos = appDao.getAppByClusterId(clusterVo.getId());
                clusterMonitorVo.setTotalApps(appPos.size());

                //解析cpu使用率和内存使用率，取最近的10条数据组成
                List<ClusterMonitorPo> clusterMonitorPoList = clusterMonitorDao.queryClusterMonitorInfoByClusterId(clusterVo.getId());
                if (!clusterMonitorPoList.isEmpty()) {
                    clusterMonitorVo.setClusterStatus(clusterMonitorPoList.get(0).getClusterStatus());//取最新的一条监控数据的集群状态为当前集群状态
                    clusterMonitorVo.setCurrCpuUsedAvail(clusterMonitorPoList.get(0).getCpuAvailability());
                    Double memUsedPercent = clusterMonitorPoList.get(0).getMemoryAvailability() / clusterVo.getTotalMemory() * 100;
                    clusterMonitorVo.setCurrMemUsedAvail(Double.valueOf(new DecimalFormat("#.00").format(memUsedPercent)));
                }
                List<Map> cpuAvails = new ArrayList<Map>();
                List<Map> memAvails = new ArrayList<Map>();
                Map cpuAvail = null;
                Map memAvail = null;
                for (ClusterMonitorPo clusterMonitorPo : clusterMonitorPoList) {
                    cpuAvail = new HashMap();
                    memAvail = new HashMap();

                    cpuAvail.put("cpuAvail", clusterMonitorPo.getCpuAvailability());
                    cpuAvail.put("biztime", new SimpleDateFormat("HH:mm:ss").format(clusterMonitorPo.getBiztime()));

                    memAvail.put("memAvail", clusterMonitorPo.getMemoryAvailability());
                    memAvail.put("biztime", new SimpleDateFormat("HH:mm:ss").format(clusterMonitorPo.getBiztime()));

                    cpuAvails.add(cpuAvail);
                    memAvails.add(memAvail);
                }
                clusterMonitorVo.setCpuAvails(cpuAvails);
                clusterMonitorVo.setMemAvails(memAvails);

                clusterMonitorVos.add(clusterMonitorVo);
            }
        }

        return clusterMonitorVos;
    }

    @Override
    public void insertNewMonitorData(ClusterMonitorVo clusterMonitorVo) {
        ClusterMonitorPo clusterMonitorPo = new ClusterMonitorPo();
        clusterMonitorPo.setClusterId(clusterMonitorVo.getClusterId());
        clusterMonitorPo.setClusterName(clusterMonitorVo.getClusterName());
        clusterMonitorPo.setClusterStatus(clusterMonitorVo.getClusterStatus());
        clusterMonitorPo.setCpuAvailability(clusterMonitorVo.getCurrCpuUsedAvail());
        clusterMonitorPo.setMemoryAvailability(clusterMonitorVo.getCurrMemUsedAvail());
        clusterMonitorPo.setBiztime(clusterMonitorVo.getBiztime());

        clusterMonitorDao.insertNewMonitorData(clusterMonitorPo);
    }

    @Override
    public void deleteMonitorDataByClusterId(Long clusterId) {
        clusterMonitorDao.deleteMonitorDataByClusterId(clusterId);
    }


    public void deleteExpireMonitorData(Date date) {
        return;
    }
}
