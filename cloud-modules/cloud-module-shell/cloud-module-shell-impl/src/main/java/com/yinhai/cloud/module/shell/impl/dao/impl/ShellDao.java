package com.yinhai.cloud.module.shell.impl.dao.impl;

import com.yinhai.cloud.module.shell.impl.dao.IShellDao;
import com.yinhai.cloud.module.shell.po.ShellPo;
import com.yinhai.cloud.module.shell.vo.ShellVo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * @author jianglw
 */
public class ShellDao extends HibernateDAO<ShellPo> implements IShellDao {
    @Override
    public List<ShellPo> queryAllShell() {
        String hql = "from ShellPo";
        return super.find(hql);
    }

    @Override
    public boolean saveShell(final ShellVo vo) {
        // 判断是否有重复类型的脚本
        if (queryAllShell().stream().anyMatch(it -> it.getShellUid().equals(vo.getShellUid()))) {
            return false;
        }
        super.save(vo.toPO(new ShellPo()));
        return true;
    }

    @Override
    public boolean updateShell(final ShellVo vo) {
        final ShellPo oneShell = getOneShell(vo.getShellId());
        if (oneShell == null) {
            return false;
        }
        oneShell.setShellContent(vo.getShellContent());
        oneShell.setShellUid(vo.getShellUid());
        oneShell.setShellType(vo.getShellType());
        oneShell.setShellName(vo.getShellName());
        super.update(oneShell);
        return true;
    }

    @Override
    public boolean delete(final Long shellId) {
        super.delete(getOneShell(shellId));
        return true;
    }
}
