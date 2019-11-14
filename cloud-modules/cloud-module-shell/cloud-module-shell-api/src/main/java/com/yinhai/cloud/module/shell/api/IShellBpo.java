package com.yinhai.cloud.module.shell.api;

import com.yinhai.cloud.module.shell.vo.ShellVo;

import java.util.List;

/**
 * @author jianglw
 */
public interface IShellBpo {

    /**
     * 查询所有的脚本数据
     *
     * @return
     */
    List<ShellVo> queryAllShell();

    /**
     * 获取一条shell数据
     *
     * @param shellId
     * @return
     */
    ShellVo getOneShell(final Long shellId);

    /**
     * 新增脚本数据
     *
     * @param vo
     * @return
     */
    boolean save(final ShellVo vo);

    /**
     * 修改脚本内容
     *
     * @param vo
     * @return
     */
    boolean update(final ShellVo vo);

    /**
     * 删除一个脚本
     *
     * @param vo
     * @return
     */
    boolean delete(final ShellVo vo);
}
