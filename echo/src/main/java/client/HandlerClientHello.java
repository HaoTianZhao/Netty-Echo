package client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * ChannelHandler.Sharable 注解并没有任何实际功能,只是标注此类是无状态的,可以将同一个实例多次放入一个或多个ChannelPipeline中
 * SimpleChannelInboundHandler<ByteBuf> 除了ByteBuf外,还可以是String或对象
 * ByteBuf Netty对原生的字节操作进行了封装,效率更高,使用更简便
 *
 * @author zhaotianhao
 * @version 1.0
 * @className HandlerClientHello.java
 * @description 客户端Handler, 处理IO事件.
 * @date 2020/7/13 20:27
 */
@ChannelHandler.Sharable
public class HandlerClientHello extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 处理接收到的消息
     *
     * @param ctx ctx
     * @param msg msg
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("接收到的消息: " + msg.toString(CharsetUtil.UTF_8));
    }

    /**
     * 处理异常
     *
     * @param ctx   ctx
     * @param cause cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
