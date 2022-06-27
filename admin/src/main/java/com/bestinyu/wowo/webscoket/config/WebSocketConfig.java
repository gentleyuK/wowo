package com.bestinyu.wowo.webscoket.config;

import com.bestinyu.wowo.webscoket.handler.PluginHandler;
import com.bestinyu.wowo.webscoket.interceptor.WebSocketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private PluginHandler pluginHandler;
    @Autowired
    private WebSocketInterceptor webSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(pluginHandler, "/plugin/{method}")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
    }

}
