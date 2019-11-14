package com.yinhai.cloud.module.shell.impl.bpo;

import com.yinhai.cloud.module.shell.api.IShellBpo;
import com.yinhai.cloud.module.shell.impl.dao.IShellDao;
import com.yinhai.cloud.module.shell.po.ShellPo;
import com.yinhai.cloud.module.shell.vo.ShellVo;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jianglw
 */
public class ShellBpo extends TaBaseBpo implements IShellBpo {

    @Autowired
    private IShellDao shellDao;

    @Override
    public List<ShellVo> queryAllShell() {
        final List<ShellPo> pos = shellDao.queryAllShell();
        final List<ShellVo> collect = pos.stream().map(it -> {
            final ShellVo vo = it.toVo(new ShellVo());
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public ShellVo getOneShell(final Long shellId) {
        final ShellPo oneShellPo = shellDao.getOneShell(shellId);
        return oneShellPo.toVo(new ShellVo());
    }

    @Override
    public boolean save(final ShellVo vo) {
        return shellDao.saveShell(vo);
    }

    @Override
    public boolean update(final ShellVo vo) {
        return shellDao.updateShell(vo);
    }

    @Override
    public boolean delete(final ShellVo vo) {
        return shellDao.delete(vo.getShellId());
    }
}
