package com.sanri.initexec;

import com.sanri.app.chat.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import javax.annotation.PostConstruct;

/**
 * netty 服务初始化
 */
public class InitNettyServer {

    @PostConstruct
    public void init() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss,worker);
        serverBootstrap.channel(NioServerSocketChannel.class);

        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                // 最大帐长度 64k ,offset 为 0 ，标记长度的长度为 8 字节
                pipeline.addLast(new LengthFieldBasedFrameDecoder(64 * 1024 ,0,8));
                pipeline.addLast(new ServerHandler());
            }
        });

        // 这个参数只是影响还没有被accept取出的连接 ，并不影响连接数 https://www.cnblogs.com/little-fly/p/8683197.html
        serverBootstrap.option(ChannelOption.SO_BACKLOG,128);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);

        // 异步的绑定服务器，我们调用sync()方法来执行同步，直到绑定完成
        ChannelFuture channelFuture = serverBootstrap.bind(10086).sync();
        //获取该Channel的CloseFuture，并且阻塞当前线程直到它完成
        channelFuture.channel().closeFuture().sync();

        // 关闭事件循环组
        boss.shutdownGracefully().sync();
        worker.shutdownGracefully().sync();
    }
}
