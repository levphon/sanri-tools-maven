package com.sanri.app.chat;

import com.alibaba.fastjson.JSONObject;

import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.IOException;

public class ChatUserInfo {
    private Session session;
    private String userName;

    public ChatUserInfo(Session session, String userName) {
        this.session = session;
        this.userName = userName;
    }

    public void sendMessage(ChatMessage chatMessage) throws IOException {
        if(session.isOpen()){
            session.getAsyncRemote().sendText(JSONObject.toJSONString(chatMessage));
        }
    }

    public Session getSession() {
        return session;
    }

    public String getUserName() {
        return userName;
    }
}
