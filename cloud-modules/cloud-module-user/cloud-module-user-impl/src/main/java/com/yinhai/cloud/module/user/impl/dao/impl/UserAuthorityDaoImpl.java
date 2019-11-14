package com.yinhai.cloud.module.user.impl.dao.impl;

import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.repertory.api.vo.AppImageQueryVo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.cloud.module.repertory.impl.po.AppGroupPo;
import com.yinhai.cloud.module.repertory.impl.po.AppImagePo;
import com.yinhai.cloud.module.repertory.impl.po.AppRepertoryPo;
import com.yinhai.cloud.module.user.impl.dao.UserAuthorityDao;
import com.yinhai.cloud.module.user.impl.po.UserAuthorityId;
import com.yinhai.cloud.module.user.impl.po.UserAuthorityPo;
import com.yinhai.core.common.api.base.IPage;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * Created by tianhy on 2018/9/11.
 */
public class UserAuthorityDaoImpl extends HibernateDAO<UserAuthorityPo> implements UserAuthorityDao {

    @Override
    public UserAuthorityPo select(UserAuthorityId id) {
        return super.get(id);
    }

    @Override
    public UserAuthorityPo insert(UserAuthorityPo po) {
        super.save(po);
        return po;
    }

    @Override
    public void delete(UserAuthorityPo o) {
        super.delete(o);
    }

    @Override
    public void update(UserAuthorityPo o) {
        super.update(o);
    }

    @Override
    public List<UserAuthorityPo> getUserAppAuthoritys(Long userid) {
        return super.find("from " + super.getEntityClassName(UserAuthorityPo.class.getName()) + " where id.userId = ? and (resourceType = ? or resourceType = ?)", userid, IAppConstants.RESOURCE_TYPE_CLUSTER, IAppConstants.RESOURCE_TYPE_NAMESPACE);
    }

    @Override
    public List<UserAuthorityPo> getUserImageAuthoritys(Long userid) {
        return super.find("from " + super.getEntityClassName(UserAuthorityPo.class.getName()) + " where id.userId = ? and (resourceType = ? or resourceType = ?)", userid, IAppConstants.RESOURCE_TYPE_BUSINESSAREA, IAppConstants.RESOURCE_TYPE_APPGROUP);
    }

    @Override
    public void deleteByUserid(Long userid) {
        super.delete("delete from " + getEntityClassName(UserAuthorityPo.class.getName()) + " where id.userId = ?", userid);
    }

    @Override
    public List<UserAuthorityPo> getUserClusterAuthoritys(Long userid) {
        return super.find("from " + super.getEntityClassName(UserAuthorityPo.class.getName()) + " where id.userId = ? and resourceType = ?", userid, IAppConstants.RESOURCE_TYPE_CLUSTER);
    }

    @Override
    public List<UserAuthorityPo> getUserNameSpaceAuthoritys(Long userid) {
        return super.find("from " + super.getEntityClassName(UserAuthorityPo.class.getName()) + " where id.userId = ? and resourceType = ?", userid, IAppConstants.RESOURCE_TYPE_NAMESPACE);
    }

    @Override
    public IPage<AppImageVo> getUserAuthorityQueryAppImages(AppImageQueryVo vo, Long userid) {
        Integer limit = ValidateUtil.isEmpty(vo.getPageSize()) ? 10 : vo.getPageSize();
        Integer start = ValidateUtil.isEmpty(vo.getCurrentPage()) || vo.getCurrentPage() <= 0 ? 0 : (vo.getCurrentPage() - 1) * limit;
        StringBuffer sql = new StringBuffer("");
        sql.append("select new com.yinhai.cloud.module.repertory.api.vo.AppImageVo(a.id , a.repertoryId, b.groupId, c.groupIdentify, c.groupName, b.appName, b.imagePath, b.identify, a.createTime, a.version, a.effective, a.downloading,a.downloadNum) from ")
                .append(super.getEntityClassName(AppImagePo.class.getName()) + " a, ")
                .append(super.getEntityClassName(AppRepertoryPo.class.getName()) + " b, ")
                .append(super.getEntityClassName(AppGroupPo.class.getName()) + " c, ")
                .append(super.getEntityClassName(UserAuthorityPo.class.getName()) + " d ")
                .append(" where a.repertoryId = b.id and b.groupId = c.id and c.id = d.id.resourceId")
                .append(" and d.id.userId = " + userid);
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
                .append(", a.id");
        return super.selectFromMultiTableWithPage(sql.toString(), start, limit, "");
    }
}
