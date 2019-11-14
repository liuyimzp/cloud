package com.yinhai.cloud.module.web.shell.socket.interceptor;

import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.web.shell.socket.constants.NodeConstant;
import com.yinhai.cloud.module.web.shell.socket.handler.CloudWebShellWebSocketHandler;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.modules.security.api.vo.UserAccountInfo;
import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.IOptionName;
import net.neoremind.sshxcute.core.SSHExec;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author jianglw
 */
public class CloudWebShellWebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    /**
     * websocket开始握手之前
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return false:拒绝连接
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            // 拒绝未登录的用户访问
            if (session == null) {
                return false;
            }
            UserAccountInfo userAccountInfo = (UserAccountInfo) session.getAttribute(ResourceConstant.USER_INFO_KEY);
            if (userAccountInfo == null) {
                return false;
            }
            final boolean handshake = super.beforeHandshake(request, response, wsHandler, attributes);
            // 握手成功才写入缓存及session
            if (handshake) {
                // 获取SSH 连接
                final String kee = createSSHConnection((ServletServerHttpRequest) request);
                // 获取节点ID并写入attributes
                attributes.put(NodeConstant.WEBSOCKET_NODE_ID, kee);
            }
            return handshake;
        }
        return false;
    }

    /**
     * 生成SSH连接，并放入map进行缓存
     *
     * @param request
     */
    private String createSSHConnection(final ServletServerHttpRequest request) {
        final INodeBpo service = ServiceLocator.getService(INodeBpo.class);
        final NodeBasicInfoVo nodeBasicInfoVo = service.queryNodeInfoById(Long.parseLong(request.getServletRequest().getParameter(NodeConstant.PARAMETER_NODE_ID)));
        final String kee = nodeBasicInfoVo.getNodeId() + ":" + nodeBasicInfoVo.getNodeIP();
        if (!CloudWebShellWebSocketHandler.isConnectionExist(kee)) {
            SSHExec.setOption(IOptionName.SSH_PORT_NUMBER, 22);
            ConnBean conn = new ConnBean(nodeBasicInfoVo.getNodeIP(), nodeBasicInfoVo.getNodeUser(), nodeBasicInfoVo.getNodePassword());
            SSHExec ssh = new SSHExec(conn);
            ssh.connect();
            CloudWebShellWebSocketHandler.addConnection(kee, ssh);
        }
        return kee;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
