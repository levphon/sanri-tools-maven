package com.sanri.app.servlet;

import com.sanri.app.BaseServlet;
import com.sanri.app.chat.ChartRoomWebSocket;
import com.sanri.frame.RequestMapping;

import java.util.List;
import java.util.Set;

@RequestMapping("/chat")
public class ChatServlet extends BaseServlet {

    /**
     * 登录聊天/检测用户名是否可用
     * @param userName
     * @return
     */
    public int login(String userName){
        boolean exist = ChartRoomWebSocket.chatGroup.exist(userName);
        return exist ? 1:0;
    }

    /**
     * 获取当前聊天室所有在线用户
     * @return
     */
    public Set<String> friends(){
        return ChartRoomWebSocket.chatGroup.userList();
    }
}
