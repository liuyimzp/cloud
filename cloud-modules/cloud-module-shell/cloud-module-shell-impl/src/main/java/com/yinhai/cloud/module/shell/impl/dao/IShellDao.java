package com.yinhai.cloud.module.shell.impl.dao;

import com.yinhai.cloud.module.shell.po.ShellPo;
import com.yinhai.cloud.module.shell.vo.ShellVo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jianglw
 */
public interface IShellDao {

    default ShellPo getOneShell(final Long shellId) {
        List<ShellPo> all = queryAllShell();
        final List<ShellPo> collect = all.stream().filter(it -> it != null && shellId.equals(it.getShellId())).limit(1).collect(Collectors.toList());
        if (collect.size() != 1) {
            return null;
        }
        return collect.get(0);
    }

    /**
     * 查询所有的脚本数据
     *
     * @return
     */
    List<ShellPo> queryAllShell();

    /**
     * 新增脚本数据
     *
     * @param vo
     * @return
     */
    boolean saveShell(final ShellVo vo);

    /**
     * 修改脚本内容
     *
     * @param vo
     * @return
     */
    boolean updateShell(final ShellVo vo);

    /**
     * 删除一个脚本
     *
     * @param shellId
     * @return
     */
    boolean delete(final Long shellId);
}
