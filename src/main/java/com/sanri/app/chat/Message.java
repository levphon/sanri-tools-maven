package com.sanri.app.chat;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class Message implements Serializable {
    // 只接收特定开头的包信息
    public static final byte magicNum = 1;
    //消息类型 1.文本消息
    private byte type;
    // 发送人
    private int from;
    //接收人
    private int to;

    // 数据长度及数据
    private int length;
    private byte [] body = new byte[0];

    public Message(byte type, int from, int to, byte[] body) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.body = body;
        this.length = body.length;
    }

    public Message(byte type, int from, int to, String body) {
        this(type,from,to,new byte[0]);
        try {
            this.body = body.getBytes("utf-8");
            this.length = body.length();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        try {
            return new String(this.body, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
