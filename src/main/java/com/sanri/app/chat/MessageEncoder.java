package com.sanri.app.chat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        out.writeByte(Message.magicNum);
        out.writeByte(msg.getType());
        out.writeInt(msg.getFrom());
        out.writeInt(msg.getTo());

        byte[] body = msg.getBody();
        out.writeInt(body.length);
        out.writeBytes(body);
    }
}
