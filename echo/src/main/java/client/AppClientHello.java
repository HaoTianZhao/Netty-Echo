package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * ChannelInitializer 通道channel的初始化工作,如加入多个handler,都在这里进行
 * pipeline() 建立连接后,会自动创建一个管道pipeline,这个管道又被称为责任链,保证顺序执行,同时可以灵活配置各种handler.这是一个很精妙的设计,既减少了线程切换的上下文开销和麻烦,也提高了性能
 *
 *
 * @author zhaotianhao
 * @version 1.0
 * @className AppClientHello.java
 * @description 客户端启动类
 * @date 2020/7/13 20:44
 */
public class AppClientHello {
    private final String host;
    private final int port;

    public AppClientHello(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 配置相应的参数,提供连接到远端的方法
     */
    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //客户端辅助启动类
            Bootstrap bs = new Bootstrap();

            bs.group(group)
                    .channel(NioSocketChannel.class) //实例化一个Channel
                    .remoteAddress(host, port)
                    .handler(new ChannelInitializer<SocketChannel>() { //进行通道初始化配置
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HandlerClientHello()); //添加自定义的Handler
                        }
                    });

            //连接到远程节点,同步等待
            ChannelFuture future = bs.connect().sync();

            //发送消息到服务器
            future.channel().writeAndFlush(Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8));

            //阻塞操作,closeFuture()开启了一个channel的监听器(这期间channel在进行各项工作),直到链路断开
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new AppClientHello("127.0.0.1", 18080).run();
    }
}
