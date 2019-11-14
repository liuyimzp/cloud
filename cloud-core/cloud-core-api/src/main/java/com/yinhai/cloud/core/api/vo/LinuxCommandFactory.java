package com.yinhai.cloud.core.api.vo;

import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;

/**
 * @author: zhaokai
 * @create: 2018-09-05 15:13:48
 */
public class LinuxCommandFactory {


    public static ConsoleCommand modifyHostsCmd(String ip, String hostname) {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("sed -i '/" + ip + "/d' /etc/hosts ")
                .appendCommand("bash -c \"echo '" + ip +" "+  hostname + "' >> /etc/hosts \"");

        cmd.setWithSudo(true);
        return cmd;


    }

    public static ConsoleCommand mountList() {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand("df -h|grep -v 'tmpfs\\|shm\\|/dev/dm-'|awk '{if(NR>1) print $6\"|\"$2\"|\"$4}'");

        return cmd;


    }

    public static ConsoleCommand serverPhysicalResources() {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "lscpu | grep 'CPU(s):' | awk 'NR==1{print $2}'",      //CPU个数
                "free -m | grep 'Mem:' | awk 'NR==1{print $2/1024}'",  //总内存 单位G
                "df --total|grep 'total' |awk '{print $2/1024/1024}'", //总磁盘 单位M
                "echo \"$(cat /etc/os-release|grep '^NAME='|awk -F =  '{print $2}'|sed 's/\"//g') $(cat /etc/os-release|grep '^VERSION='|awk -F =  '{print $2}'|sed 's/\"//g')\"",                         //操作系统信息
                "df --total|grep 'total' |awk '{print $4/1024/1024}'", //磁盘剩余空间
                "df --total|grep 'total' |awk '{print $3/1024/1024}'",  //磁盘已用空间
                "free -m | grep 'Mem:' | awk 'NR==1{print $NF/1024}'",// 空闲内存 单位G
                "df --total /var |grep 'total' |awk '{print $4/1024/1024}'",// /var空闲磁盘 单位G
                "df --total /var |grep 'total' |awk '{print $2/1024/1024}'"// /var总共磁盘容量
        );
        cmd.setWithSudo(true);
        return cmd;


    }

    public static ConsoleCommand serverPreCheck() {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "lscpu | grep 'CPU(s):'",      //CPU个数
                "free -m | grep 'Mem:' ",  //总内存 单位G
                "df --total|grep 'total'", //总磁盘 单位M
                "echo \"$(cat /etc/os-release|grep '^NAME='|awk -F =  '{print $2}'|sed 's/\"//g') $(cat /etc/os-release|grep '^VERSION='|awk -F =  '{print $2}'|sed 's/\"//g')\"",   //操作系统信息
                "df --total|grep 'total'", //磁盘剩余空间
                "df --total|grep 'total'",  //磁盘已用空间
                "free -m | grep 'Mem:' "// 空闲内存 单位G
        );
        cmd.setWithSudo(true);
        return cmd;


    }

    public static ConsoleCommand changeOwnerAsCurrentUser(String dir, String user, String group) {

            ConsoleCommand cmd = new ConsoleCommand();
            cmd.appendCommand(
                    "chown -R  "+user+":"+group+" "+dir
            );
            cmd.setWithSudo(true);
            return cmd;
    }

    public static ConsoleCommand ipNetworkInterface(String ip) {

        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "ip a|grep "+ip+" |awk '{print $NF}'"
        );
        return cmd;
    }

    public static ConsoleCommand subnetMaskBitWidth(String ip) {
        ConsoleCommand cmd = new ConsoleCommand();
        cmd.appendCommand(
                "ip a|grep "+ip+" |awk -F / '{print $NF}'|awk '{print $1}'"
        );
        return cmd;
    }



}
