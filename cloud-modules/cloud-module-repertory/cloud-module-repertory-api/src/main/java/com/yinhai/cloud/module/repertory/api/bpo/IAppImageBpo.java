package com.yinhai.cloud.module.repertory.api.bpo;

import com.yinhai.cloud.module.repertory.api.vo.AppImageQueryVo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.core.common.api.base.IPage;

import java.util.List;


/**
 * Created by tianhy on 2018/5/17.
 */
public interface IAppImageBpo {
    String SERVICEKEY = "appImageBpo";

    /**
     * 根据应用镜像ID查询应用镜像信息
     *
     * @param id
     * @return
     */
    AppImageVo getAppImage(Long id);

    /**
     * 根据查询条件获取应用镜像列表
     *
     * @param vo
     * @return
     */
    IPage<AppImageVo> getQueryAppImages(AppImageQueryVo vo);


    /**
     * 保存应用镜像信息
     *
     * @param repertoryId
     * @param version
     */
    void saveAppImage(Long repertoryId, String version);

    /**
     * 删除应用镜像信息
     *
     * @param id
     */
    void removeAppImage(Long id);

    /**
     * 检查版本号是否已存在
     *
     * @param repertoryId
     * @param version
     * @return
     */
    boolean checkVersionExist(Long repertoryId, String version);

    /**
     * 保存自定义镜像信息
     *
     * @param vo
     */
    void saveDiyImage(AppImageVo vo);

    /**
     * 更新镜像信息
     *
     * @param vo
     */
    void updateImage(AppImageVo vo);

    /**
     * 同步远程仓库中的镜像列表
     *
     * @param vo
     */
    public void syncDockerRepertory(AppImageQueryVo vo);

    /**
     * 将镜像的下载状态修改
     * @param id
     * @param nodownload
     */
    void updateImageDownload(Long id, Integer nodownload);

    /**
     * 修改镜像下载人数
     * @param id
     * @param i
     */
    void updateImageDownNum(Long id, int i);

    /**
     * 得到某一个厂库下所有可用镜像
     * @param repertoryId
     * @return
     */
    List<AppImageVo> getAppImageByReId(Long repertoryId);
}
