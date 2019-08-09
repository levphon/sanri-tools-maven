//package com.sanri.app;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CopyOnWriteArraySet;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//
//@ServerEndpoint(value = "/chat/{userId}")
//public class WebSocketMain {
//
//    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
//    private static int onlineCount = 0;
//    //线程安全的Map
//    private static ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap<String, Session>();//建立连接的方法
//
//    @OnOpen
//    public void onOpen(Session session, @PathParam("userId") String userId) {
//        /*获取从/websocket开始的整条链接，用于获取userId？***=***的参数
//        String uri = session.getRequestURI().toString();*/
//        webSocketMap.put(userId, session);
//        addOnlineCount(); //在线数加
//        System.out.println(userId + "进入聊天室");
//        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose(Session session) {
//        Map<String, String> map = session.getPathParameters();
//        webSocketMap.remove(map.get("userId")); //从set中删除
//        for (String user : webSocketMap.keySet()) {
//            System.out.println(user);
//        }
//        subOnlineCount(); //在线数减
//        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
//    }
//
//    /**
//     * 收到客户端消息后调用的方法
//     *
//     * @param message 客户端发送过来的消息
//     * @param session 可选的参数
//     */
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        System.out.println("来自客户端的消息:" + message);
//        //获取用户ID
//        Map<String, String> map = session.getPathParameters();
//        String userId = map.get("userId");
//        for (String user : webSocketMap.keySet()) {
//            try {
//                sendMessage(user + "你好，我是" + userId + "   " + message, webSocketMap.get(user));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 发生错误时调用
//     *
//     * @param session
//     * @param error
//     */
//    @OnError
//    public void onError(Session session, Throwable error) {
//        System.out.println("发生错误");
//        error.printStackTrace();
//    }
//
//    public void sendMessage(String message, Session session) throws IOException {
//        if (session.isOpen()) {
//            session.getAsyncRemote().sendText(message);
//        }
//        //this.session.getAsyncRemote().sendText(message);
//    }
//
//
//    public static synchronized int getOnlineCount() {
//        return onlineCount;
//    }
//
//    public static synchronized void addOnlineCount() {
//        WebSocketMain.onlineCount++;
//    }
//
//    public static synchronized void subOnlineCount() {
//        WebSocketMain.onlineCount--;
//    }
//}