package com.bestinyu.wowo.websocket;

import javax.websocket.*;
import java.io.IOException;

@ClientEndpoint
public class WsClient {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("session open.");
    }

    @OnMessage
    public void onMessage(String msg) {
        System.out.println("get message: " + msg);
    }

    @OnError
    public void OnError(Throwable t) {
        t.printStackTrace();
    }

    @OnClose
    public void onClose() {
        this.session = null;
    }

    public void send(String msg) throws IOException {
        if (session == null) {
            System.out.println("session is null.");
        } else if (!session.isOpen()) {
            System.out.println("session is close.");
        } else {
            this.session.getBasicRemote().sendText(msg);
        }
    }
}
