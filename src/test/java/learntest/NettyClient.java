package learntest;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(worker);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new LengthFieldBasedFrameDecoder(64 * 1024 ,0,8));
                pipeline.addLast(new ClientHandler());
            }
        });
        ChannelFuture localhost = bootstrap.connect("localhost", 10086).sync();
        localhost.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("连接服务器成功");

                } else {
                    System.out.println("连接服务器失败");
                    channelFuture.cause().printStackTrace();
                    worker.shutdownGracefully(); //关闭线程组
                }

            }
        });
    }

}
