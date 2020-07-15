package server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 两个eventLoopGroup分别负责接收客户端的连接和处理I/O消息
 *
 * @author zhaotianhao
 * @version 1.0
 * @className AppServerHello.java
 * @description 服务器启动类
 * @date 2020/7/14 11:09
 */
public class AppServerHello {

    private int port;

    public AppServerHello(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bs = new ServerBootstrap(); //用于启动NIO服务
            bs.group(group, childGroup)
                    .channel(NioServerSocketChannel.class) //通过工厂方法设计模式实例化一个channel
                    .localAddress(new InetSocketAddress(port)) //设置监听端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HandlerServerHello()); //配置childHandler来通知一个关于消息处理的InfoServerHandler实例
                        }
                    });

            //绑定服务器,同步阻塞,该实例将提供有关IO操作的结果或状态的信息
            ChannelFuture channelFuture = bs.bind().sync();
            System.out.println("在" + channelFuture.channel().localAddress() + "上开启监听");

            //阻塞操作,closeFuture()开启了一个channel监听器(这期间channel在进行各种工作),直到链路断开
            channelFuture.channel().closeFuture().sync();
        } finally {
            //关闭eventLoop并释放所有资源,包括创建的所有线程
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new AppServerHello(18080).run();
    }
}
