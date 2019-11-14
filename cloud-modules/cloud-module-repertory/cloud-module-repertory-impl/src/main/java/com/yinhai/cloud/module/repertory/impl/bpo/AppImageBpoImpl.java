package com.yinhai.cloud.module.repertory.impl.bpo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.ssh.command.AbstractCommand;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppImageBpo;
import com.yinhai.cloud.module.repertory.api.constant.RepositoryConstant;
import com.yinhai.cloud.module.repertory.api.vo.AppImageQueryVo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.cloud.module.repertory.impl.dao.AppGroupDao;
import com.yinhai.cloud.module.repertory.impl.dao.AppImageDao;
import com.yinhai.cloud.module.repertory.impl.dao.AppRepertoryDao;
import com.yinhai.cloud.module.repertory.impl.po.AppGroupPo;
import com.yinhai.cloud.module.repertory.impl.po.AppImagePo;
import com.yinhai.cloud.module.repertory.impl.po.AppRepertoryPo;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.core.app.api.util.JSonFactory;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import com.yinhai.modules.org.ta3.domain.po.IUser;
import com.yinhai.sysframework.util.IConstants;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by tianhy on 2018/5/17.
 */
public class AppImageBpoImpl extends TaBaseBpo implements IAppImageBpo {

    @Resource
    private AppImageDao appImageDao;

    @Resource
    private AppRepertoryDao appRepertoryDao;

    @Resource
    private AppGroupDao appGroupDao;

    @Override
    public AppImageVo getAppImage(Long id) {
        AppImageVo image = appImageDao.select(id).toVo(new AppImageVo());
        AppRepertoryPo repo = appRepertoryDao.select(image.getRepertoryId());
        image.setImageBuildDirName(RepositoryConstant.IMAGE_BUILD_DIR_PREFIX + "-" + repo.getIdentify() + "-" + image.getVersion());
        image.setLocalFileName(repo.getIdentify() + "-" + image.getVersion() + "." + image.getDiyType());
        return image;
    }

    @Override
    public IPage<AppImageVo> getQueryAppImages(AppImageQueryVo vo) {
            return appImageDao.getQueryAppImages(vo);
    }

    @Override
    public void saveAppImage(Long repertoryId, String version) {
        AppImagePo po = new AppImagePo();
        po.setType(RepositoryConstant.DOCKER_IMAGE_TYPE_ORIGINAL);
        po.setRepertoryId(repertoryId);
        po.setVersion(version);
        po.setCreateTime(getSysTimestamp());
        po.setEffective(IConstants.EFFECTIVE_YES);
        appImageDao.insert(po);
    }

    @Override
    public void saveDiyImage(AppImageVo vo) {
        AppImagePo po = vo.toPO(new AppImagePo());
        po.setType(RepositoryConstant.DOCKER_IMAGE_TYPE_DIY);
        po.setCreateTime(getSysTimestamp());
        po.setEffective(IConstants.EFFECTIVE_YES);
        appImageDao.insert(po);
    }

    @Override
    public void updateImage(AppImageVo vo) {
        appImageDao.update(vo.toPO(new AppImagePo()));
    }

    @Override
    public void removeAppImage(Long id) {
        appImageDao.deleteByInvalid(appImageDao.select(id));
    }

    @Override
    public boolean checkVersionExist(Long repertoryId, String version) {
        return ValidateUtil.isNotEmpty(appImageDao.getAppImagesByRepertoryIdAndVersion(repertoryId, version));
    }

    @Override
    public void syncDockerRepertory(AppImageQueryVo vo) {
        if (ValidateUtil.isEmpty(vo.getRepertoryId())) {
            List<AppRepertoryPo> appRepertoryList = appRepertoryDao.getRepertorysByIdentify(vo.getIdentify(), vo.getBusinessArea(), vo.getGroupId());
            for (AppRepertoryPo appRepertoryPo : appRepertoryList) {
                syncDockerRepertory(appRepertoryPo);
            }
        } else {
            AppRepertoryPo appRepertoryPo = appRepertoryDao.select(vo.getRepertoryId());
            syncDockerRepertory(appRepertoryPo);
        }
    }

    @Override
    public void updateImageDownload(Long id, Integer nodownload) {
        AppImagePo po = appImageDao.select(id);
        po.setDownloading(nodownload);
        appImageDao.update(po);
    }

    @Override
    public void updateImageDownNum(Long id, int i) {
        AppImagePo po = appImageDao.select(id);
        po.setDownloadNum(i);
        appImageDao.update(po);
    }

    @Override
    public List<AppImageVo> getAppImageByReId(Long repertoryId) {
        List<AppImagePo> list = appImageDao.getAppImagesByRepertoryId(repertoryId);
        return list == null?new ArrayList<>():list.stream()
                .filter(po -> IConstants.EFFECTIVE_YES.equals(po.getEffective()))
                .map(po -> po.toVo(new AppImageVo())).collect(Collectors.toList());
    }

    private void syncDockerRepertory(AppRepertoryPo appRepertoryPo) {
        ConsoleCommand commandVo = new ConsoleCommand();

        AppGroupPo appGroupPo = appGroupDao.select(appRepertoryPo.getGroupId());
        commandVo.appendCommand(new String[]{"curl -XGET http://" + SystemConfigUtil.getSystemConfigValue("docker.private.repo.ip") + ":" + SystemConfigUtil.getSystemConfigValue("docker.private.repo.port") + "/v2/" + appGroupPo.getGroupIdentify() + "/" + appRepertoryPo.getIdentify() + "/tags/list"});
        ConnVo connVo = NodeUtils.createDockerRepoServerConn();
        List<AppImagePo> appImageList = appImageDao.getAppImagesByRepertoryId(appRepertoryPo.getId());
        try {
            Map<AbstractCommand, MsgVO> result = CommonSshExecUtil.exec(connVo, commandVo);
            String msg = result.values().iterator().next().getSysoutMsg();
            Map map = JSonFactory.json2bean(msg, java.util.HashMap.class);
            JSONArray tags = (JSONArray) map.get("tags");
            if (ValidateUtil.isEmpty(tags)) {
                for (AppImagePo po : appImageList) {
                    po.setEffective(IConstants.EFFECTIVE_NO);
                    appImageDao.update(po);
                }
                return;
            }
            Iterator<Object> it = tags.iterator();
            Set<String> versions = new HashSet<String>();
            Set<String> imageVersions = new HashSet<>();
            for(AppImagePo po : appImageList){
                imageVersions.add(po.getVersion());
            }
            while (it.hasNext()) {
                String version = it.next().toString();
                if(!imageVersions.contains(version)){
                    AppImagePo appImagePo = new AppImagePo();
                    appImagePo.setRepertoryId(appRepertoryPo.getId());
                    appImagePo.setVersion(version);
                    appImagePo.setType(RepositoryConstant.DOCKER_IMAGE_TYPE_ORIGINAL);
                    appImagePo.setCreateTime(getSysTimestamp());
                    appImagePo.setEffective(IConstants.EFFECTIVE_YES);
                    appImageDao.insert(appImagePo);
                }
                versions.add(version);
            }
            for (AppImagePo po : appImageList) {
                if (versions.contains(po.getVersion())) {
                    if(ValidateUtil.areEqual(IConstants.EFFECTIVE_NO, po.getEffective())){
                        po.setEffective(IConstants.EFFECTIVE_YES);
                        appImageDao.update(po);
                    }
                }else{
                    po.setEffective(IConstants.EFFECTIVE_NO);
                    appImageDao.update(po);
                }
            }
        } catch (JSONException e) {
            for (AppImagePo po : appImageList) {
                po.setEffective(IConstants.EFFECTIVE_NO);
                appImageDao.update(po);
            }
        } catch (SSHConnectionException e) {
            e.printStackTrace();
        } catch (SSHExecuteException e) {
            for (AppImagePo po : appImageList) {
                po.setEffective(IConstants.EFFECTIVE_NO);
                appImageDao.update(po);
            }
        }
    }
}
