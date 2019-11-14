package com.yinhai.cloud.core.api.util;

import com.yinhai.modules.codetable.api.util.CodeTableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: zhaokai
 * @create: 2018-08-02 16:17:22
 */
public class CommonUtil {
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static Logger logger = LoggerFactory.getLogger(CommonUtil.class);
    private static String localhostIP = "127.0.0.1";

    public static List<Map<String, String>> getCodeDescListForSelect(String codeType) {
        List<Map<String, String>> list = CodeTableUtil.getCodeList(codeType, null).stream().map(code -> {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", code.getCodeDESC());
            map.put("name", code.getCodeDESC());
            return map;
        }).collect(Collectors.toList());
        return list;

    }

    public static String getNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    public static String getTime(String format) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }


    public static String getExceptionMsgContent(Exception e) {
        StringWriter out = null;
        String log;
        List<String> logList = new ArrayList<>();
        try {
            out = new StringWriter();
            e.printStackTrace(new PrintWriter(out));
            log = out.toString();

            String[] lines = log.split("\n");
            // 解析异常中的信息
            for (String line : lines) {
                if (line == null || "".equals(line) || line.trim().startsWith("at") || line.trim().startsWith("...")) {
                    continue;
                }
                int startIndex = line.lastIndexOf("Exception:");
                if (startIndex < 0) {
                    if (!logList.contains(line.trim())) {
                        logList.add(line.trim());
                    }
                    continue;
                } else {
                    String lineMsg = line.substring(startIndex + "Exception:".length()).trim();
                    if (!logList.contains(lineMsg)) {
                        logList.add(lineMsg);
                    }
                }
            }
            // 无信息，则返回原始堆栈信息
            if (logList.isEmpty()) {
                return log;
            }
            // 有信息，则返回解析后的信息
            StringBuilder sb = new StringBuilder();
            for (String s : logList) {
                sb.append(s).append("\n");
            }

            return sb.toString().trim().replaceAll("java.lang.reflect.InvocationTargetException", "") + "\n";
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e1) {
                logger.error(e.getMessage());
            }
        }

    }

    /**
     * 根据错误提示信息，获取用户权限解决方式
     *
     * @param msg
     * @return
     */
    public static String solveUserPermission(String msg) {

        String errorMsgEnglish = "you must have a tty to run sudo";
        String errorMsgChinese = "您必须拥有一个终端来执行 sudo";

        if (msg != null && (msg.contains(errorMsgEnglish) || msg.contains(errorMsgChinese))) {
            return "( 请注释掉/etc/sudoers 文件中 Defaults    requiretty 配置项 )";
        }
        String notSudoerChinese = "不在 sudoers 文件中";
        String notSudoerEnginlish = "is not in the sudoers file";
        if (msg != null && (msg.contains(notSudoerChinese) || msg.contains(notSudoerEnginlish))) {
            return "( 请在文件/etc/sudoers 中添加sudo 用户配置，型如：username     ALL=(ALL)       ALL )";
        }
        return msg;

    }

    public static String wrapWithBoldTag(String content) {
        return "<b>" + content + "</b>";
    }


    public static List<String> getCurrentMachineIp() {
        List<String> localIpList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresss = ni.getInetAddresses();
                while (addresss.hasMoreElements()) {
                    InetAddress e = addresss.nextElement();
                    if (!e.getHostAddress().contains(":") && !localhostIP.equals(e.getHostAddress())) {
                        localIpList.add(e.getHostAddress());
                    }

                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        return localIpList;
    }


}
