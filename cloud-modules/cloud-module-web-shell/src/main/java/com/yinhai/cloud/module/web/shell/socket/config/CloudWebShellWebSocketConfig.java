package com.yinhai.cloud.module.web.shell.socket.config;

import com.yinhai.cloud.module.web.shell.socket.handler.CloudWebShellWebSocketHandler;
import com.yinhai.cloud.module.web.shell.socket.interceptor.CloudWebShellWebSocketHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author jianglw
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class CloudWebShellWebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // server start up
        registry.addHandler(webSocketHandler(), "/cloud/websocket/socketServer/webShellSocket.do").setAllowedOrigins("*").addInterceptors(new CloudWebShellWebSocketHandshakeInterceptor());
    }

    @Bean
    public TextWebSocketHandler webSocketHandler() {
        return new CloudWebShellWebSocketHandler();
    }
}
