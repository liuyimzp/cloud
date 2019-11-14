package com.yinhai.cloud.module.repertory.impl.dao.impl;

import com.yinhai.cloud.module.repertory.api.vo.AppImageQueryVo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.cloud.module.repertory.impl.dao.AppImageDao;
import com.yinhai.cloud.module.repertory.impl.po.AppGroupPo;
import com.yinhai.cloud.module.repertory.impl.po.AppImagePo;
import com.yinhai.cloud.module.repertory.impl.po.AppRepertoryPo;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * Created by tianhy on 2018/5/17.
 */
public class AppImageDaoImpl extends HibernateDAO<AppImagePo> implements AppImageDao {
    @Override
    public AppImagePo select(Long id) {
        return super.get(id);
    }

    @Override
    public AppImagePo insert(AppImagePo po) {
        super.save(po);
        return po;
    }

    @Override
    public void delete(AppImagePo o) {
        super.delete(o);
    }
    @Override
    public void deleteByInvalid(AppImagePo o) {
        o.setEffective("0");
        super.update(o);
    }
    @Override
    public void deleteByRepertoryId(Long id) {
        super.delete("delete from " + super.getEntityClassName(AppImagePo.class.getName()) + " where repertoryId = ?", id);
    }

    @Override
    public void update(AppImagePo o) {
        super.update(o);
    }

    @Override
    public List<AppImagePo> getAppImagesByRepertoryIdAndVersion(Long repertoryId, String version) {
        return super.find("from " + super.getEntityClassName(AppImagePo.class.getName()) + " where repertoryId = ? and version = ?", repertoryId, version);
    }

    @Override
    public List<AppImagePo> getAppImagesByRepertoryId(Long repertoryId) {
        return super.find("from " + super.getEntityClassName(AppImagePo.class.getName()) + " where repertoryId = ?", repertoryId);
    }

    @Override
    public IPage<AppImageVo> getQueryAppImages(AppImageQueryVo vo) {
        Integer limit = ValidateUtil.isEmpty(vo.getPageSize()) ? 10 : vo.getPageSize();
        Integer start = ValidateUtil.isEmpty(vo.getCurrentPage()) || vo.getCurrentPage() <= 0 ? 0 : (vo.getCurrentPage() - 1) * limit;
        StringBuffer sql = new StringBuffer("");
        sql.append("select new com.yinhai.cloud.module.repertory.api.vo.AppImageVo(a.id , a.repertoryId, b.groupId, c.groupIdentify, c.groupName, b.appName, b.imagePath, b.identify, a.createTime, a.version, a.effective, a.downloading,a.downloadNum) from ")
                .append(super.getEntityClassName(AppImagePo.class.getName()) + " a, ")
                .append(super.getEntityClassName(AppRepertoryPo.class.getName()) + " b, ")
                .append(super.getEntityClassName(AppGroupPo.class.getName()) + " c where a.repertoryId = b.id and b.groupId = c.id ");
        if (ValidateUtil.isNotEmpty(vo.getEffective())) {
            sql.append(" and a.effective = '" + vo.getEffective() + "'");
        }
        if (!ValidateUtil.isEmpty(vo.getRepertoryId())) {
            sql.append(" and b.id = " + vo.getRepertoryId());
        }else{
            if (ValidateUtil.isNotEmpty(vo.getIdentify())) {
                sql.append(" and b.identify like '%" + vo.getIdentify() + "%'");
            }
            if(ValidateUtil.isNotEmpty(vo.getBusinessArea())){
                sql.append(" and b.businessArea = '" + vo.getBusinessArea() + "'");
            }
            if(!ValidateUtil.isEmpty(vo.getGroupId())){
                sql.append(" and b.groupId = " + vo.getGroupId());
            }
            if(!ValidateUtil.isEmpty(vo.getIsFine())){
                sql.append("and a.effective = '1'");
            }
        }
        sql.append(" order by b.groupId,a.repertoryId")
                .append(", a.createTime desc")
                /*.append(",to_number(substr(a.version, 0, instr(a.version, '.'))) desc")
                .append(",case instr(a.version, '.', 1, 2) when 0 then to_number(substr(a.version, instr(a.version, '.') + 1)) else to_number(substr(a.version, instr(a.version, '.') + 1,  instr(a.version, '.', 1, 2) - instr(a.version, '.') - 1)) end desc")*/
                .append(",a.id");
        return super.selectFromMultiTableWithPage(sql.toString(), start, limit, "");
    }
}
