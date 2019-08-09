package com.sanri.app.chat;

import java.util.Date;

public class ChatMessage {
    private String from;
    private String to;
    private String content;
    private String messageType;
    private Date time;

    public ChatMessage() {
    }

    public ChatMessage(String from, String to, String messageType) {
        this.from = from;
        this.to = to;
        this.messageType = messageType;
        this.time = new Date();
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getContent() {
        return content;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
