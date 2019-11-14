package com.yinhai.cloud.module.application.impl.bpo;

import com.yinhai.cloud.module.application.api.bpo.IAppBpo;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.vo.AppQueryVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.application.impl.dao.*;
import com.yinhai.cloud.module.application.impl.po.AppPo;
import com.yinhai.cloud.module.application.impl.po.AppServicePo;
import com.yinhai.cloud.module.application.impl.po.NamespacePo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.cluster.impl.dao.IClusterDao;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeBasicInfoPo;
import com.yinhai.cloud.module.user.api.bpo.IUserAuthorityBpo;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import com.yinhai.modules.codetable.api.util.CodeTableUtil;
import com.yinhai.sysframework.persistence.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tianhy on 2018/6/12.
 */
public class AppBpoImpl extends TaBaseBpo implements IAppBpo {

    @Resource
    private AppDao appDao;

    @Resource
    private IClusterDao clusterDao;

    @Resource
    private INodeDao nodeDao;

    @Resource
    private AppServiceDao appServiceDao;

    @Resource
    private NamespaceDao namespaceDao;

    @Resource
    private AppVersionDao appVersionDao;

    @Resource
    private AppConfigDao appConfigDao;
    @Resource
    private AppPVDao appPVDao;

    @Resource
    private AppDiyFileDao appDiyFileDao;


    @Resource
    private IUserAuthorityBpo userAuthorityBpo;

    private static final Logger logger = LoggerFactory.getLogger(AppBpoImpl.class);

    @Override
    public AppVo getApp(Long appId) {
        AppPo po = appDao.select(appId);
        if (po == null) {
            return null;
        }
        AppVo vo = po.toVo(new AppVo());
        NamespacePo npo = namespaceDao.select(po.getNamespaceId());
        vo.setNamespaceIdentify(npo == null ? null : npo.getNamespaceIdentify());
        return vo;
    }

    @Override
    public AppVo getAppByNI(Long namespaceId, Long imageId) {
        AppPo po = appDao.findAppByNI(namespaceId, imageId);
        if (po == null) {
            return null;
        }
        AppVo vo = po.toVo(new AppVo());
        return vo;
    }

    @Override
    public AppVo getAppByIdentify(String identify,long namespaceId) {
        AppPo po = appDao.findAppByIdenify(identify,namespaceId);
        if (po == null) {
            return null;
        }
        AppVo vo = po.toVo(new AppVo());
        return vo;
    }

    @Override
    public List<ClusterVo> getAllClusters() {
        return clusterDao.getClusterList().stream().map(po -> po.toVo(new ClusterVo())).collect(Collectors.toList());
    }

    @Override
    public boolean checkAppIdentifyExist(AppVo vo) {
        return ValidateUtil.isNotEmpty(appDao.selectByAppIdentify(vo.getClusterId(), vo.getNamespaceId(), vo.getAppIdentify()));
    }

    @Override
    public AppVo saveApp(AppVo vo) {
        vo.setCreateTime(getSysDate());
        return (appDao.insert(vo.toPO(new AppPo()))).toVo(new AppVo());
    }

    @Override
    public boolean saveFile(Long id,String appDiyFile,String appStatus) {
//        AppDiyFileVo appDiyFileVo = new AppDiyFileVo();
//        appDiyFileVo.setId(id);
//        appDiyFileVo.setAppDiyFile(appDiyFile);
//        appDiyFileVo.setAppStatus(appStatus);
        return appDiyFileDao.saveFile(id,appDiyFile,appStatus);
    }

    @Override
    public boolean checkIdentifyChange(AppVo vo) {
        AppPo po = appDao.select(vo.getId());
        if (ValidateUtil.areEqual(po.getAppIdentify(), vo.getAppIdentify())) {
            return true;
        }
        return ValidateUtil.isEmpty(appDao.selectByAppIdentify(vo.getClusterId(), vo.getNamespaceId(), vo.getAppIdentify()));
    }

    @Override
    public void editApp(AppVo vo) {
        AppPo po = appDao.select(vo.getId());
        if (!ValidateUtil.isEmpty(vo.getAppStatus())){
            po.setAppStatus(vo.getAppStatus());
        }
        if (!ValidateUtil.isEmpty(vo.getAppDesc())){
            po.setAppDesc(vo.getAppDesc());
        }
        if (!ValidateUtil.isEmpty(vo.getAppIdentify())){
            po.setAppIdentify(vo.getAppIdentify());
        }
        if (!ValidateUtil.isEmpty(vo.getAppType())){
            po.setAppType(vo.getAppType());
        }
        if (!ValidateUtil.isEmpty(vo.getCreateTime())){
            po.setCreateTime(vo.getCreateTime());
        }
        if (!ValidateUtil.isEmpty(vo.getAppDiyFile())){
            po.setAppDiyFile(vo.getAppDiyFile());
        }
        if (!ValidateUtil.isEmpty(vo.getAppName())){
            po.setAppName(vo.getAppName());
        }
        if (!ValidateUtil.isEmpty(vo.getAppRestartPolicy())){
            po.setAppRestartPolicy(vo.getAppRestartPolicy());
        }
        if (!ValidateUtil.isEmpty(vo.getAppSchedule())){
            po.setAppSchedule(vo.getAppSchedule());
        }
        if (!ValidateUtil.isEmpty(vo.getAppStrategy())){
            po.setAppStrategy(vo.getAppStrategy());
        }
        if (!ValidateUtil.isEmpty(vo.getBeginTime())){
            po.setBeginTime(vo.getBeginTime());
        }
        if (!ValidateUtil.isEmpty(vo.getInstanceNum())){
            po.setInstanceNum(vo.getInstanceNum());
        }
        if (!ValidateUtil.isEmpty(vo.getMiddleWareType())){
            po.setMiddleWareType(vo.getMiddleWareType());
        }
        if (!ValidateUtil.isEmpty(vo.getRecordLog())){
            po.setRecordLog(vo.getRecordLog());
        }
        if (!ValidateUtil.isEmpty(vo.getsAppSchedule())){
            po.setsAppSchedule(vo.getsAppSchedule());
        }
        if (!ValidateUtil.isEmpty(vo.getMiddleware_configfile())){
            po.setMiddleware_configfile(vo.getMiddleware_configfile());
        }
        if (!ValidateUtil.isEmpty(vo.getNodeId())){
            po.setNodeId(vo.getNodeId());
        }
        appDao.update(po);
    }

    @Override
    public void removeApp(Long id) {
        appDao.delete(appDao.select(id));
    }

    @Override
    public Long queryMiddleWareCount(Long clusterId, String middleWareType) {
        List<AppPo> list = appDao.queryMiddleWare(clusterId, middleWareType);
        if (ValidateUtil.isEmpty(list)) {
            return 0L;
        }
        String appIdentify = list.get(0).getAppIdentify();
        return Long.valueOf(appIdentify.substring(appIdentify.lastIndexOf('-') + 1));
    }

    @Override
    public void deleteAppByClusterId(Long clusterId) {
        List<AppPo> appList = appDao.getAppByClusterId(clusterId);
        for (AppPo app : appList) {
            appConfigDao.deleteByAppId(app.getId());
            appPVDao.deleteByAppId(app.getId());
            appServiceDao.deleteByAppId(app.getId());
            appDao.delete(app);
        }

    }

    @Override
    public IPage<Map> getQueryApps(AppQueryVo vo) {
        IPage<AppPo> page = appDao.getQueryApps(vo);
        return doPageResult(page);
    }

    @Override
    public IPage<Map> getQueryApps(AppQueryVo vo, Long userId) {
        IPage<Map> result = null;
        if (1 == userId) {
            result = doPageResult(appDao.getQueryApps(vo));
        } else if (userId != null) {
            List<Long> list1 = userAuthorityBpo.getUserAppAuthoritys(userId).stream().map(aaVo -> {
                return aaVo.getResourceId();
            }).collect(Collectors.toList());
            if (!list1.isEmpty()) {
                result = doPageResult(appDao.getQueryApps(vo, list1));
            } else {
                result = new PageBean();
                result.setList(new ArrayList<>());
            }
        }
        return result;
    }

    @Override
    public List<AppVo> getAppVoByNamespaceId(Long namespaceId) {
        List<AppVo> appVoList = new ArrayList<>();
        appDao.getAppVoByNamespaceId(namespaceId).stream().forEach(po -> appVoList.add(po.toVo(new AppVo())));
        return appVoList;
    }

    //处理分页查询后的应用数据
    private IPage<Map> doPageResult(IPage<AppPo> page) {
        IPage<Map> result = new PageBean();
        result.setGridId(page.getGridId());
        result.setStart(page.getStart());
        result.setLimit(page.getLimit());
        result.setTotal(page.getTotal());
        result.setList(page.getList().stream().map(po -> {
            Map map = null;
            int nodePort = clusterDao.getClusterById(po.getClusterId()).getIstioPort();
            try {
                map = po.toMap();
                NamespacePo namespacePo = namespaceDao.select(po.getNamespaceId());
                if (!ValidateUtil.isEmpty(namespacePo)) {
                    map.put("namespaceName", namespacePo.getNamespaceName());
                }
                if (ValidateUtil.areEqual(IAppConstants.APP_STATUS_RUNNING, po.getAppStatus())) {
                    List<NodeBasicInfoPo> nodeList = nodeDao.queryRunningMasterNodesByClusterId(po.getClusterId());
                    if (ValidateUtil.isNotEmpty(nodeList)) {
                        try {
                            String ip = nodeList.get(0).getNodeIP();
                            List<AppServicePo> appServiceList = appServiceDao.getAppServicesByAppId(po.getId());
                            if (ValidateUtil.isNotEmpty(appServiceList)) {
                                Integer port = appServiceList.get(0).getMappingPort();
                                if (!appVersionDao.getAllByAppId(po.getId()).isEmpty()){
                                    port = nodePort;
                                }
                                map.put("ip", "http://" + ip + ":" + port + "/" + po.getAppIdentify() + "/");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                List<AppServicePo> appServiceList = appServiceDao.getAppServicesByAppId(po.getId());
                if (ValidateUtil.isNotEmpty(appServiceList)) {
                    map.put("mappingPort", appServiceList.get(0).getMappingPort());
                }
                map.put("appStatusDesc", CodeTableUtil.getDesc("APPSTATUS", po.getAppStatus()));
            } catch (IllegalAccessException e) {
                logger.error(logger.getName() + "context", e);
            } catch (InvocationTargetException e) {
                logger.error(logger.getName() + "context", e);
            } catch (NoSuchMethodException e) {
                logger.error(logger.getName() + "context", e);
            }
            return map;
        }).collect(Collectors.toList()));
        return result;
    }
}
