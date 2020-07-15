package server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author zhaotianhao
 * @version 1.0
 * @className HandlerServerHello.java
 * @description 服务端I/O处理类
 * @date 2020/7/14 11:04
 */
@ChannelHandler.Sharable
public class HandlerServerHello extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //处理收到的消息
        ByteBuf in = (ByteBuf) msg;
        System.out.println("收到客户端发过来的消息: " + in.toString(CharsetUtil.UTF_8));

        //反馈消息到远端(客户端)
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好,我是服务端,我已经收到你发送的消息", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
