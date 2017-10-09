package SocketIO_09.netty.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Administrator on 2017/10/9.
 */
public class Server {
    public static void main(final String[] args){
        //第一个用于接收clinent端连接的。
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        //第二个用于实际的业务处理操作的
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try {
            //创建一个bootstrap辅助类，对server进行一系列的配置
            ServerBootstrap b=new ServerBootstrap();
            //把两个工作线程组加入进来
            b.group(bossGroup, workerGroup);
            //指定使用NioServerSocketChannel通道
            b.channel(NioServerSocketChannel.class);
            //一定要使用childhandler去绑定具体的事件处理器
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel sc) throws Exception {
                    sc.pipeline().addLast(new ServerHandler());
                }
            }).option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            //上两步，设置tcp连接缓冲区，和保存连接
            //绑定指定端口进行监听
            ChannelFuture f=b.bind(8555).sync();
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }


    }
}
