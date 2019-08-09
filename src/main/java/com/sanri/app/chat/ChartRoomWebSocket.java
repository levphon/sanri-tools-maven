package com.sanri.app.chat;

import com.alibaba.fastjson.JSONObject;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat/{userName}")
public class ChartRoomWebSocket {
    public static ChatGroup chatGroup = new ChatGroup();
    private RobotChat robotChat = new RobotChat();

    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) {
        ChatMessage chatMessage = new ChatMessage("system", userName, "up");
        chatMessage.setContent(userName+" 上线了");
        chatGroup.broadcast(chatMessage,session);
       chatGroup.addChatUser(new ChatUserInfo(session,userName));
    }

    @OnClose
    public void onClose(Session session){
        ChatUserInfo user = chatGroup.getUser(session);
        ChatMessage chatMessage = new ChatMessage("system", user.getUserName(), "down");
        chatMessage.setContent(user.getUserName()+" 下线了");
        chatGroup.broadcast(chatMessage,session);

        chatGroup.removeChatUser(session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        ChatMessage chatMessage = JSONObject.parseObject(message, ChatMessage.class);
        chatMessage.setMessageType("message");
        chatMessage.setTime(new Date());
        chatGroup.broadcast(chatMessage,session);

        //如果没有人广播消息，则由机器人广播一条对应内容
        if(chatGroup.size() == 1){
            String respone = robotChat.chat(chatMessage.getContent());
            ChatMessage responeMessage = new ChatMessage("system", "", "message");
            responeMessage.setContent(respone);
            chatGroup.broadcast(responeMessage,null);
        }
    }

}
