package com.bestinyu.wowo.websocket;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.net.URI;

public class WebSocketTest {

    public static void main(String[] args) {
        try {
            WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();

            WsClient client = new WsClient();
            webSocketContainer.connectToServer(client, new URI("ws://172.16.210.179:8080/plugin/ga"));

        } catch (Exception e) {

        }
    }
}
