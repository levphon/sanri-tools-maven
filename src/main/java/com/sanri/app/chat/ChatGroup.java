package com.sanri.app.chat;

import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatGroup {
    private Map<String,ChatUserInfo> chatUserInfos = new ConcurrentHashMap<>();
    private Map<String,String> sessionUserNameMap = new ConcurrentHashMap<>();

    public void addChatUser(ChatUserInfo chatUserInfo){
        chatUserInfos.put(chatUserInfo.getUserName(),chatUserInfo);
        Session session = chatUserInfo.getSession();
        sessionUserNameMap.put(session.getId(),chatUserInfo.getUserName());
    }

    public boolean exist(String userName){
        return chatUserInfos.containsKey(userName);
    }

    public ChatUserInfo getUser(Session session){
        String id = session.getId();
        String userName = sessionUserNameMap.get(id);
        return chatUserInfos.get(userName);
    }

    /**
     * 广播消息
     * @param chatMessage
     * @param session 发消息的 session
     */
    public void broadcast(ChatMessage chatMessage, Session session){
        for (ChatUserInfo chatUserInfo : chatUserInfos.values()) {
            try {
                Session currentSession = chatUserInfo.getSession();
                if(session != null && currentSession.getId().equals(session.getId())){
                    // 排除发消息给自己
                    continue;
                }
                chatUserInfo.sendMessage(chatMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeChatUser(Session session) {
        chatUserInfos.remove(sessionUserNameMap.get(session.getId()));
    }

    public Set<String> userList() {
        return chatUserInfos.keySet();
    }

    public int size() {
        return chatUserInfos.size();
    }
}
