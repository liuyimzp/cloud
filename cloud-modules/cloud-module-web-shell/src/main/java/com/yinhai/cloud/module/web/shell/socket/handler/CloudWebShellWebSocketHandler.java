package com.yinhai.cloud.module.web.shell.socket.handler;

import com.yinhai.cloud.module.web.shell.socket.constants.NodeConstant;
import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author jianglw
 */
public class CloudWebShellWebSocketHandler extends TextWebSocketHandler {

    private final static Logger logger = LoggerFactory.getLogger(CloudWebShellWebSocketHandler.class);

    private static final HashMap<String, SSHExec> SSH_CONNECTION_COLLECTION = new HashMap<>();

    public static void addConnection(String kee, SSHExec connection) {
        SSH_CONNECTION_COLLECTION.put(kee, connection);
    }

    public static boolean isConnectionExist(String kee) {
        return SSH_CONNECTION_COLLECTION.containsKey(kee);
    }

    /**
     * 连接成功时候，会触发页面上onopen方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        return;
    }

    /**
     * js调用websocket.send时候，会调用该方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        final String kee = (String) session.getAttributes().get(NodeConstant.WEBSOCKET_NODE_ID);
        final String cmdFromFrontEnd = message.getPayload().replaceAll("\n", "").replaceAll("\\s+", " ");

        final SSHExec sshExec = SSH_CONNECTION_COLLECTION.get(kee);
        CustomTask task = new ExecCommand(cmdFromFrontEnd);
        Result rs;
        try {
            rs = sshExec.exec(task);
        } catch (TaskExecFailException e) {
            sendMessageToClient(session, kee, new TextMessage(NodeConstant.FAILURE_WITH_NO_CONNECTION.getBytes(StandardCharsets.UTF_8)));
            return;
        }
        if (rs.isSuccess && rs.rc == 0) {
            sendMessageToClient(session, kee, new TextMessage(rs.sysout.getBytes(Charset.forName("gbk"))));
        } else {
            final String v = NodeConstant.FAILURE_WITH_COMMAND_NOT_USEFUL + cmdFromFrontEnd + "\r\n" + rs.error_msg;
            sendMessageToClient(session, kee, new TextMessage(v.getBytes(StandardCharsets.UTF_8)));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        final String kee = (String) session.getAttributes().get(NodeConstant.WEBSOCKET_NODE_ID);
        disposeSSHConnection(kee);
    }

    /**
     * 关闭已经打开的SSH连接并从缓存中移除
     *
     * @param kee
     */
    private void disposeSSHConnection(final String kee) {
        if (SSH_CONNECTION_COLLECTION.containsKey(kee)) {
            final SSHExec sshExec = SSH_CONNECTION_COLLECTION.get(kee);
            if (sshExec != null) {
                sshExec.disconnect();
            }
            SSH_CONNECTION_COLLECTION.remove(kee);
        }
    }

    /**
     * 关闭连接时触发
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        final String kee = (String) session.getAttributes().get(NodeConstant.WEBSOCKET_NODE_ID);
        disposeSSHConnection(kee);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给某个用户发送消息
     *
     * @param nodeId
     * @param message
     */
    private void sendMessageToClient(WebSocketSession session, String nodeId, TextMessage message) {
        if (session.getAttributes().get(NodeConstant.WEBSOCKET_NODE_ID).equals(nodeId)) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                logger.error(logger.getName() + "context",e);
            }
        }
    }
}
