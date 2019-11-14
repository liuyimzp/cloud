package com.yinhai.cloud.module.resource.util;

import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.core.api.vo.LinuxCommandFactory;
import com.yinhai.cloud.module.resource.pv.api.vo.DiskVo;
import com.yinhai.cloud.module.resource.pv.api.vo.StorageVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengwei on 2018/6/21.
 */
public class DiskUtil {

    /**
     * 获取磁盘目录挂载列表
     *
     * @param storage
     * @return
     * @throws Exception
     */
    public static List<DiskVo> getServerDiskMountList(StorageVo storage) throws Exception {
        ConnVo connFromStorage = NodeUtils.createConnFromStotageVo(storage);
        ConsoleCommand mountList = LinuxCommandFactory.mountList();
        MsgVO msg = CommonSshExecUtil.exec(connFromStorage, mountList).get(mountList);
        String[] diskMounts = msg.getSysoutMsg().split("\n");
        List<DiskVo> mountLists = new ArrayList<>();
        for (int i = 0; i < diskMounts.length; i++) {
            String[] diskInfo = diskMounts[i].split("\\|");
            if (diskInfo.length == 3) {
                DiskVo diskVo = new DiskVo();
                diskVo.setMountPath(diskInfo[0]);
                diskVo.setTotleSize(diskInfo[1]);
                diskVo.setFreeSize(diskInfo[2]);
                mountLists.add(diskVo);
            }
        }
        return mountLists;
    }

    /**
     * 获取指定磁盘目录挂载
     * @param storage
     * @return
     * @throws Exception
     */
    public static List<DiskVo> getServerDiskMount(StorageVo storage) throws Exception {
        ConnVo connFromStorage = NodeUtils.createConnFromStotageVo(storage);
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("df -h "+storage.getHostRootPath());
        MsgVO msg = CommonSshExecUtil.exec(connFromStorage, cmd).get(cmd);
        String[] diskMounts = msg.getSysoutMsg().split("\n");
        List<DiskVo> mountLists = new ArrayList<>();
        for (int i = 1; i < diskMounts.length; i++) {
            String[] diskInfo = diskMounts[i].split("\\s+");
            DiskVo diskVo = new DiskVo();
            diskVo.setMountPath(diskInfo[5]);
            diskVo.setTotleSize(diskInfo[1]);
            diskVo.setFreeSize(diskInfo[3]);
            mountLists.add(diskVo);
        }
        return mountLists;
    }

}
