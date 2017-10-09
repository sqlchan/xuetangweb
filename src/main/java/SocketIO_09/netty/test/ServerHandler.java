package SocketIO_09.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by Administrator on 2017/10/9.
 */
public class ServerHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf buf=(ByteBuf)msg;
            byte[] data=new byte[buf.readableBytes()];
            buf.readBytes(data);
            String re=new String(data,"utf-8");
            System.out.println("server: "+re);
        }finally {
            ReferenceCountUtil.release(msg);
        }

    }
}