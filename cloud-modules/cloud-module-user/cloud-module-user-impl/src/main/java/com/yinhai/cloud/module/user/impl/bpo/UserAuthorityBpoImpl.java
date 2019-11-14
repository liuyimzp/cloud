package com.yinhai.cloud.module.user.impl.bpo;

import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.impl.dao.NamespaceDao;
import com.yinhai.cloud.module.application.impl.po.NamespacePo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageQueryVo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.cloud.module.repertory.impl.dao.AppGroupDao;
import com.yinhai.cloud.module.repertory.impl.dao.AppImageDao;
import com.yinhai.cloud.module.repertory.impl.po.AppGroupPo;
import com.yinhai.cloud.module.resource.cluster.impl.dao.IClusterDao;
import com.yinhai.cloud.module.resource.cluster.impl.po.ClusterPo;
import com.yinhai.cloud.module.user.api.bpo.IUserAuthorityBpo;
import com.yinhai.cloud.module.user.api.vo.UserAuthorityVo;
import com.yinhai.cloud.module.user.impl.dao.UserAuthorityDao;
import com.yinhai.cloud.module.user.impl.po.UserAuthorityId;
import com.yinhai.cloud.module.user.impl.po.UserAuthorityPo;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import com.yinhai.modules.codetable.api.domain.bpo.IAppCodeBpo;
import com.yinhai.modules.codetable.api.domain.vo.AppCodeListVo;
import com.yinhai.modules.codetable.api.domain.vo.AppCodeVo;
import com.yinhai.modules.codetable.api.util.CodeTableUtil;
import com.yinhai.modules.org.ta3.domain.po.IUser;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tianhy on 2018/9/11.
 */
public class UserAuthorityBpoImpl extends TaBaseBpo implements IUserAuthorityBpo {

    @Resource
    private UserAuthorityDao userAuthorityDao;

    @Resource
    private IClusterDao clusterDao;

    @Resource
    private NamespaceDao namespaceDao;

    @Resource
    private IAppCodeBpo appCodeBpo;

    @Resource
    private AppGroupDao appGroupDao;

    @Resource
    private AppImageDao appImageDao;

    @Override
    public List<UserAuthorityVo> getAllAppAuthorityNodes() {
        List<ClusterPo> clusterList = clusterDao.getClusterList();
        List<NamespacePo> namespaceList = namespaceDao.getAllNamespaces();
        List<UserAuthorityVo> list = new ArrayList<>();
        for (ClusterPo clusterPo : clusterList) {
            UserAuthorityVo vo = new UserAuthorityVo();
            vo.setResourceId(clusterPo.getId());
            vo.setResourceName(clusterPo.getName());
            vo.setResourceType(IAppConstants.RESOURCE_TYPE_CLUSTER);
            list.add(vo);
        }
        for (NamespacePo namespacePo : namespaceList) {
            UserAuthorityVo vo = new UserAuthorityVo();
            vo.setResourceId(namespacePo.getId());
            vo.setResourceName(namespacePo.getNamespaceName());
            vo.setResourceType(IAppConstants.RESOURCE_TYPE_NAMESPACE);
            vo.setpId(namespacePo.getClusterId());
            list.add(vo);
        }
        return list;
    }

    @Override
    public List<UserAuthorityVo> getAllImageAuthorityNodes() {
        AppCodeListVo buseinessAreaList = appCodeBpo.getAppCode("BUSINESSAREA", "9999", true);
        List<AppGroupPo> appGroupList = appGroupDao.getAllAppGroups();
        List<UserAuthorityVo> list = new ArrayList<>();
        for (AppCodeVo appCodeVo : buseinessAreaList.getList()) {
            UserAuthorityVo vo = new UserAuthorityVo();
            vo.setResourceId(Long.valueOf(appCodeVo.getCodeValue()));
            vo.setResourceName(appCodeVo.getCodeDESC());
            vo.setResourceType(IAppConstants.RESOURCE_TYPE_BUSINESSAREA);
            list.add(vo);
        }
        for (AppGroupPo appGroupPo : appGroupList) {
            UserAuthorityVo vo = new UserAuthorityVo();
            vo.setResourceId(appGroupPo.getId());
            vo.setResourceName(appGroupPo.getGroupName());
            vo.setResourceType(IAppConstants.RESOURCE_TYPE_APPGROUP);
            if (ValidateUtil.isNotEmpty(appGroupPo.getBusinessArea())) {
                vo.setpId(Long.valueOf(appGroupPo.getBusinessArea()));
            }
            list.add(vo);
        }
        return list;
    }

    @Override
    public List<UserAuthorityVo> getUserAppAuthoritys(Long userid) {
        return userAuthorityDao.getUserAppAuthoritys(userid).stream().map(po -> {
            UserAuthorityVo vo = new UserAuthorityVo();
            vo.setUserId(po.getId().getUserId());
            vo.setResourceId(po.getId().getResourceId());
            vo.setResourceType(po.getResourceType());
            if (ValidateUtil.areEqual(IAppConstants.RESOURCE_TYPE_CLUSTER, po.getResourceType())) {
                ClusterPo clusterPo = clusterDao.getClusterById(vo.getResourceId());
                if (!ValidateUtil.isEmpty(clusterPo)) {
                    vo.setResourceName(clusterPo.getName());
                }
            } else {
                NamespacePo namespacePo = namespaceDao.select(vo.getResourceId());
                if (!ValidateUtil.isEmpty(namespacePo)) {
                    vo.setResourceName(namespacePo.getNamespaceName());
                }
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserAuthorityVo> getUserImageAuthoritys(Long userid) {
        return userAuthorityDao.getUserImageAuthoritys(userid).stream().map(po -> {
            UserAuthorityVo vo = new UserAuthorityVo();
            vo.setUserId(po.getId().getUserId());
            vo.setResourceId(po.getId().getResourceId());
            vo.setResourceType(po.getResourceType());
            if (ValidateUtil.areEqual(IAppConstants.RESOURCE_TYPE_BUSINESSAREA, po.getResourceType())) {
                vo.setResourceName(CodeTableUtil.getDesc("BUSINESSAREA", vo.getResourceId().toString()));
            } else {
                AppGroupPo appGroupPo = appGroupDao.select(vo.getResourceId());
                if (!ValidateUtil.isEmpty(appGroupPo)) {
                    vo.setResourceName(appGroupPo.getGroupName());
                }
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void saveAuthoritys(Long userId, List<UserAuthorityVo> list) {
        for (UserAuthorityVo userAuthorityVo : list) {
            if (!ValidateUtil.isEmpty(userAuthorityVo.getResourceId()) && ValidateUtil.isNotEmpty(userAuthorityVo.getResourceType())) {
                UserAuthorityId userAuthorityId = new UserAuthorityId(userId, userAuthorityVo.getResourceId());
                UserAuthorityPo po = userAuthorityDao.select(userAuthorityId);
                if (ValidateUtil.isEmpty(po)) {
                    po = new UserAuthorityPo(userAuthorityId, userAuthorityVo.getResourceType());
                    userAuthorityDao.insert(po);
                }

            }
        }
    }

    @Override
    public void removeAuthoritys(Long userId, List<UserAuthorityVo> list) {
        for (UserAuthorityVo userAuthorityVo : list) {
            if (!ValidateUtil.isEmpty(userAuthorityVo.getResourceId())) {
                UserAuthorityPo po = userAuthorityDao.select(new UserAuthorityId(userId, userAuthorityVo.getResourceId()));
                if (!ValidateUtil.isEmpty(po)) {
                    userAuthorityDao.delete(po);
                }
            }
        }
    }

    @Override
    public void deleteUserAuthpritysByUserid(Long userid) {
        userAuthorityDao.deleteByUserid(userid);
    }

    @Override
    public List<UserAuthorityVo> getUserClusterAuthoritys(Long userid) {
        return userAuthorityDao.getUserClusterAuthoritys(userid).stream().map(po -> {
            UserAuthorityVo vo = new UserAuthorityVo();
            vo.setUserId(po.getId().getUserId());
            vo.setResourceId(po.getId().getResourceId());
            vo.setResourceType(po.getResourceType());
            return vo;
        }).collect(Collectors.toList());
    }

    public List<Long> getUserNameSpaceIds(Long userid){
        return userAuthorityDao.getUserNameSpaceAuthoritys(userid).stream().map(po -> po.getId().getResourceId()).collect(Collectors.toList());
    }

    @Override
    public IPage<AppImageVo> getQueryAppImages(AppImageQueryVo vo, Long userid) {
        if(ValidateUtil.areEqual(IUser.ROOT_USERID, userid)){
            return appImageDao.getQueryAppImages(vo);
        }else{
            return userAuthorityDao.getUserAuthorityQueryAppImages(vo, userid);
        }
    }
}
