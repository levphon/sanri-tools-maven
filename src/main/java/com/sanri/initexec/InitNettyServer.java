//package com.sanri.initexec;
//
//import com.sanri.app.chat.MessageDecoder;
//import com.sanri.app.chat.MessageEncoder;
//import com.sanri.app.chat.ServerHandler;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
//import io.netty.handler.codec.string.StringDecoder;
//import io.netty.handler.codec.string.StringEncoder;
//
//import javax.annotation.PostConstruct;
//
///**
// * netty 服务初始化
// */
//public class InitNettyServer {
//
//    @PostConstruct
//    public void init() throws InterruptedException {
//        EventLoopGroup boss = new NioEventLoopGroup();
//        EventLoopGroup worker = new NioEventLoopGroup();
//
//        ServerBootstrap serverBootstrap = new ServerBootstrap();
//        serverBootstrap.group(boss,worker);
//        serverBootstrap.channel(NioServerSocketChannel.class);
//
//        serverBootstrap.childHandler(new ChildHandler());
//
//        // 这个参数只是影响还没有被accept取出的连接 ，并不影响连接数 https://www.cnblogs.com/little-fly/p/8683197.html
//        serverBootstrap.option(ChannelOption.SO_BACKLOG,128);
//        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
//
//        // 异步的绑定服务器，我们调用sync()方法来执行同步，直到绑定完成
//        ChannelFuture channelFuture = serverBootstrap.bind(10086).sync();
//        //获取该Channel的CloseFuture，并且阻塞当前线程直到它完成
//        channelFuture.channel().closeFuture().sync();
//
//        // 关闭事件循环组
//        boss.shutdownGracefully().sync();
//        worker.shutdownGracefully().sync();
//    }
//
//    class ChildHandler extends ChannelInitializer<SocketChannel> {
//
//        @Override
//        protected void initChannel(SocketChannel socketChannel) throws Exception {
//            ChannelPipeline pipeline = socketChannel.pipeline();
//            // 1<< 20
//            pipeline.addLast(new MessageDecoder(1048576, 10, 4));
//            pipeline.addLast(new MessageEncoder());
//            pipeline.addLast(new ServerHandler());
//        }
//    }
//}
