package com.yinhai.cloud.module.repertory.impl.bpo;

import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.module.repertory.api.bpo.IAppRepertoryBpo;
import com.yinhai.cloud.module.repertory.api.vo.AppRepertoryQueryVo;
import com.yinhai.cloud.module.repertory.api.vo.AppRepertoryVo;
import com.yinhai.cloud.module.repertory.impl.dao.AppGroupDao;
import com.yinhai.cloud.module.repertory.impl.dao.AppImageDao;
import com.yinhai.cloud.module.repertory.impl.dao.AppRepertoryDao;
import com.yinhai.cloud.module.repertory.impl.po.AppGroupPo;
import com.yinhai.cloud.module.repertory.impl.po.AppImagePo;
import com.yinhai.cloud.module.repertory.impl.po.AppRepertoryPo;
import com.yinhai.cloud.module.user.api.bpo.IUserAuthorityBpo;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.common.ta3.base.Page;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import com.yinhai.modules.codetable.api.domain.bpo.IAppCodeBpo;
import com.yinhai.modules.codetable.api.domain.vo.AppCodeVo;
import com.yinhai.modules.codetable.api.util.CodeTableUtil;
import com.yinhai.sysframework.persistence.PageBean;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tianhy on 2018/5/17.
 */
public class AppRepertoryBpoImpl extends TaBaseBpo implements IAppRepertoryBpo {

    @Resource
    private AppRepertoryDao appRepertoryDao;

    @Resource
    private AppImageDao appImageDao;

    @Resource
    private AppGroupDao appGroupDao;

    @Resource
    private SessionFactory sessionFactory;

    @Resource
    private IUserAuthorityBpo userAuthorityBpo;

    @Resource
    private IAppCodeBpo appCodeBpo;


    @Override
    public void saveAppRepertory(AppRepertoryVo vo) {
        AppRepertoryPo po = vo.toPO(new AppRepertoryPo());
        AppGroupPo appGroupPo = appGroupDao.select(vo.getGroupId());
        po.setImagePath(SystemConfigUtil.getSystemConfigValue("docker.private.repo.ip") + ":" + SystemConfigUtil.getSystemConfigValue("docker.private.repo.port") + "/" + appGroupPo.getGroupIdentify() + "/" + po.getIdentify());
        appRepertoryDao.insert(po);
    }

    @Override
    public boolean checkIdentifyExist(Long groupId, String identify) {
        return ValidateUtil.isNotEmpty(appRepertoryDao.selectByIdentify(groupId, identify));
    }

    @Override
    public boolean checkIdentifyChange(Long id, Long groupId, String identify) {
        AppRepertoryPo po = appRepertoryDao.select(id);
        if (ValidateUtil.areEqual(po.getIdentify(), identify)) {
            return true;
        }
        return ValidateUtil.isEmpty(appRepertoryDao.selectByIdentify(groupId, identify));
    }

    @Override
    public void editAppRepertory(AppRepertoryVo vo) {
        AppRepertoryPo po = appRepertoryDao.select(vo.getId());
        String imagePath = po.getImagePath();
        String indefity = po.getIdentify();
        po = vo.toPO(po);
        po.setImagePath(imagePath);
        po.setIdentify(indefity);
        appRepertoryDao.update(po);
    }

    @Override
    public void removeAppRepertory(Long id) {
        appImageDao.deleteByRepertoryId(id);
        appRepertoryDao.delete(appRepertoryDao.select(id));
    }

    @Override
    public AppRepertoryVo getAppRepertory(Long id) {
        AppRepertoryPo po = appRepertoryDao.select(id);

        if (ValidateUtil.isEmpty(po)) {
            return null;
        }
        AppRepertoryVo vo = po.toVo(new AppRepertoryVo());
        AppGroupPo group = appGroupDao.select(po.getGroupId());
        vo.setGroupIdentify(group.getGroupIdentify());
        vo.setGroupName(group.getGroupName());
        return vo;
    }

    @Override
    public IPage<AppRepertoryVo> getQueryAppRepertories(AppRepertoryQueryVo vo,Long userId) {
        Integer limit = ValidateUtil.isEmpty(vo.getPageSize()) ? 10 : vo.getPageSize();
        Integer start = ValidateUtil.isEmpty(vo.getCurrentPage()) || vo.getCurrentPage() <= 0 ? 0 : (vo.getCurrentPage() - 1) * limit;
        StringBuffer getCountSql = new StringBuffer("");
        getCountSql.append("select count(a.id) from ")
                .append(super.getEntityClassName(AppRepertoryPo.class.getName()) + " a, ")
                .append(super.getEntityClassName(AppGroupPo.class.getName()) + " c where a.groupId = c.id");
        StringBuffer sql = new StringBuffer("");
        sql.append("select new com.yinhai.cloud.module.repertory.api.vo.AppRepertoryVo(a.id , a.groupId, c.groupIdentify, c.groupName, a.appName, a.identify, a.appType, a.imagePath, a.appDesc, a.businessArea, (select count(1) from " + super.getEntityClassName(AppImagePo.class.getName()) + " b where b.repertoryId = a.id)) from ")
                .append(super.getEntityClassName(AppRepertoryPo.class.getName()))
                .append(" a,")
                .append(super.getEntityClassName(AppGroupPo.class.getName()))
                .append(" c where a.groupId = c.id");
        if (ValidateUtil.isNotEmpty(vo.getBusinessArea())) {
            getCountSql.append(" and a.businessArea = '" + vo.getBusinessArea() + "'");
            sql.append(" and a.businessArea = '" + vo.getBusinessArea() + "'");
        }
        if (!ValidateUtil.isEmpty(vo.getGroupId())) {
            getCountSql.append(" and a.groupId = " + vo.getGroupId());
            sql.append(" and a.groupId = " + vo.getGroupId());
        }
        if (!ValidateUtil.isEmpty(vo.getAppName())) {
            getCountSql.append(" and a.appName like '%" + vo.getAppName() + "%'");
            sql.append(" and a.appName like '%" + vo.getAppName() + "%'");
        }
        if (!ValidateUtil.isEmpty(vo.getIdentifyt())) {
            getCountSql.append(" and a.identify like '" + vo.getIdentifyt() + "%'");
            sql.append(" and a.identify like '" + vo.getIdentifyt() + "%'");
        }
        List<Long> list1 =null;
        if(userId!=null && userId != 1){  //除超级管理员外，查询该人员对应的镜像组权限
            list1 = userAuthorityBpo.getUserImageAuthoritys(userId).stream().map(aaVo->{
                return aaVo.getResourceId();
            }).collect(Collectors.toList());
            getCountSql.append(" and a.groupId in(:groupList) " );
            sql.append(" and a.groupId in(:groupList) " );
            if(list1.isEmpty()){
                return new PageBean(new ArrayList());
            }
        }
        sql.append(" order by a.businessArea,a.groupId,a.id");

        IPage<AppRepertoryVo> p = new Page();
        p.setStart(start);
        p.setLimit(limit);
        p.setGridId("");
        Query query1 = sessionFactory.getCurrentSession().createQuery(getCountSql.toString());
        Query query2 = sessionFactory.getCurrentSession().createQuery(sql.toString());
        if(list1 !=null){  //带权限查询
            query1.setParameterList("groupList",list1);
            query2.setParameterList("groupList",list1);
        }
//        if (ValidateUtil.isNotEmpty(vo.getBusinessArea())){
//            query1.setParameter(0,vo.getBusinessArea());
//            query1.setParameter(0,vo.getBusinessArea());
//        }
        p.setTotal(((Long) query1.uniqueResult()).intValue());
        List<AppRepertoryVo> list = query2.setFirstResult(start).setMaxResults(limit).list();
        p.setList(list.stream().map(appRepertoryVo -> {
            appRepertoryVo.setBusinessAreaName(CodeTableUtil.getDesc("BUSINESSAREA", appRepertoryVo.getBusinessArea()));
            return appRepertoryVo;
        }).collect(Collectors.toList()));
        return p;
    }

    @Override
    public List<AppRepertoryVo> getAllAppRepertoryVo(Long userId) {
        StringBuffer sql = new StringBuffer("");
        sql.append("select new com.yinhai.cloud.module.repertory.api.vo.AppRepertoryVo(a.id , a.groupId, c.groupIdentify, c.groupName, a.appName, a.identify, a.appType, a.imagePath, a.appDesc, a.businessArea, (select count(1) from " + super.getEntityClassName(AppImagePo.class.getName()) + " b where b.repertoryId = a.id)) from ")
                .append(super.getEntityClassName(AppRepertoryPo.class.getName()))
                .append(" a,")
                .append(super.getEntityClassName(AppGroupPo.class.getName()))
                .append(" c where a.groupId = c.id");
        List<Long> list1 =null;
        if(userId!=null && userId != 1){  //除超级管理员外，查询该人员对应的镜像组权限
            list1 = userAuthorityBpo.getUserImageAuthoritys(userId).stream().map(aaVo->{
                return aaVo.getResourceId();
            }).collect(Collectors.toList());
            sql.append(" and a.groupId in(:groupList) " );
        }
        sql.append(" order by a.businessArea,a.groupId,a.id");
        Query query = sessionFactory.getCurrentSession().createQuery(sql.toString());
        if(list1 !=null){  //带权限查询
            query.setParameterList("groupList",list1);
        }
        List<AppRepertoryVo> list = query.list();
        return list.stream().map(appRepertoryVo -> {
            appRepertoryVo.setBusinessAreaName(CodeTableUtil.getDesc("BUSINESSAREA", appRepertoryVo.getBusinessArea()));
            return appRepertoryVo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AppCodeVo> getUserBusinessArea(AppCodeVo vo, Long userId) {
        List<AppCodeVo> list = this.appCodeBpo.getAppCodeInAa10(vo).getList();
        if ("BUSINESSAREA".equals(vo.getCodeType())){
            if (userId != null && userId != 1){
                List<String> businessAreaS = getAllAppRepertoryVo(userId).stream().map(apVo -> {
                    return apVo.getBusinessArea();
                }).collect(Collectors.toList());
                return list.stream().filter(gvo -> businessAreaS.contains(gvo.getCodeValue())).collect(Collectors.toList());
            }
        }
        return list;
    }


}
