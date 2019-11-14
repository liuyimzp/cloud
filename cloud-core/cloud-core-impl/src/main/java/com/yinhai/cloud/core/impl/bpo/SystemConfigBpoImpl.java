package com.yinhai.cloud.core.impl.bpo;

import com.yinhai.cloud.core.api.bpo.ISystemConfigBpo;
import com.yinhai.cloud.core.api.util.CommonUtil;
import com.yinhai.cloud.core.api.vo.SystemConfigVo;
import com.yinhai.cloud.core.impl.dao.SystemConfigDao;
import com.yinhai.cloud.core.impl.po.SystemConfigPo;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.common.ta3.base.Page;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhaokai
 * @create: 2018-08-15 09:55:19
 */
public class SystemConfigBpoImpl extends TaBaseBpo implements ISystemConfigBpo{
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    @Autowired
    private SystemConfigDao systemConfigDao;

    @Resource
    private SessionFactory sessionFactory;

    @Override
    public List<SystemConfigVo> getAllSystemConfig() {
        return systemConfigDao.getAllSystemConfig().stream().map(n->n.toVo(new SystemConfigVo())).collect(Collectors.toList());
    }

    @Override
    public SystemConfigVo getSystemConfig(String propKey) {
        SystemConfigPo po = systemConfigDao.getSystemConfig(propKey);
        if(po == null){
            return null;
        }
        return po.toVo(new SystemConfigVo());
    }

    @Override
    public void updateSystemConfig(SystemConfigVo vo) {
        String propKey = vo.getPropKey();
        SystemConfigPo po = systemConfigDao.getSystemConfig(propKey);
        if(po == null){
            insertSystemConfig(vo);
            return;
        }
        if(vo.getPropValue()!=null){
            po.setPropValue(vo.getPropValue());
        }
        if(vo.getPropCreateUser()!=null){
            po.setPropCreateUser(vo.getPropCreateUser());
        }
        po.setPropComment(vo.getPropComment());
        po.setPropUpdateTime(CommonUtil.getNow());
        systemConfigDao.updateSystemConfig(po);
    }

    @Override
    public void deleteSystemConfig(String propKey) {
        systemConfigDao.deleteSystemConfig(propKey);
    }

    @Override
    public IPage getAllSystemConfigByPage(SystemConfigVo vo) {
        Integer limit = ValidateUtil.isEmpty(vo.getPageSize()) ? 10 : vo.getPageSize();
        Integer start = ValidateUtil.isEmpty(vo.getCurrentPage()) || vo.getCurrentPage() <= 0 ? 0 : (vo.getCurrentPage() - 1) * limit;
        StringBuilder getCountSql = new StringBuilder();
        getCountSql.append("select count(a.propKey) from " + super.getEntityClassName(SystemConfigPo.class.getName()) + " a where 1=1 ");
        StringBuilder sql = new StringBuilder();
        sql.append("select new com.yinhai.cloud.core.api.vo.SystemConfigVo(a.propKey,a.propValue) from " + super.getEntityClassName(SystemConfigPo.class.getName()) + " a where 1=1 ");
        if (ValidateUtil.isNotEmpty(vo.getPropKey())){
            getCountSql.append("and a.propKey like '%" + vo.getPropKey() + "%'");
            sql.append("and a.propKey like '%" + vo.getPropKey() + "%'");
        }
        if (ValidateUtil.isNotEmpty(vo.getPropValue())){
            getCountSql.append("and a.propValue like '%" + vo.getPropValue() + "%'");
            sql.append("and a.propValue like '%" + vo.getPropValue() + "%'");
        }
        IPage<SystemConfigVo> p = new Page();
        p.setStart(start);
        p.setLimit(limit);
        p.setGridId("");
        Query query1 = sessionFactory.getCurrentSession().createQuery(getCountSql.toString());
        Query query2 = sessionFactory.getCurrentSession().createQuery(sql.toString());
        p.setTotal(((Long) query1.uniqueResult()).intValue());
        List<SystemConfigVo> list = query2.setFirstResult(start).setMaxResults(limit).list();
        p.setList(list);
        return p;
    }

    @Override
    public SystemConfigVo insertSystemConfig(SystemConfigVo vo) {
        SystemConfigPo savePo = vo.toPO(new SystemConfigPo());
        savePo.setPropUpdateTime(CommonUtil.getNow());
        systemConfigDao.insertSystemConfig(savePo);
        return savePo.toVo(new SystemConfigVo());
    }
}



