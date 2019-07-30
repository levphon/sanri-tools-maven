package com.sanri.app.chat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.List;

public class MessageDecoder extends LengthFieldBasedFrameDecoder {
    //头部信息的大小应该是 byte+byte+int+int = 1+1+4+4 = 10
    private static final int HEADER_SIZE = 10;

    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if(in == null)return null;
        if(in.readableBytes() <= HEADER_SIZE)return null;

        in.markReaderIndex();

        byte magic = in.readByte();
        if(magic != Message.magicNum)return null;
        byte type = in.readByte();
        int from = in.readInt();
        int to = in.readInt();
        int dataLength = in.readInt();

        // FIXME 如果dataLength过大，可能导致问题
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return null;
        }

        byte[] data = new byte[dataLength];
        in.readBytes(data);
       return new Message(type,from,to,data);
    }
}
