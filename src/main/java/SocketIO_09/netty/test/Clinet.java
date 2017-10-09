package SocketIO_09.netty.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Administrator on 2017/10/9.
 */
public class Clinet {
    public static void main(final String[] args){

        //第二个用于实际的业务处理操作的
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try {
            //创建一个bootstrap辅助类，对server进行一系列的配置
            Bootstrap b=new Bootstrap();
            //把两个工作线程组加入进来
            b.group(workerGroup);
            //指定使用NioServerSocketChannel通道
            b.channel(NioSocketChannel.class);
            //一定要使用childhandler去绑定具体的事件处理器
            b.handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel sc) throws Exception {
                    sc.pipeline().addLast(new ClientHandler());
                }
            });
            //绑定指定端口进行监听
            ChannelFuture of=b.bind("127.0.0.1",8555).sync();

            of.channel().write(Unpooled.copiedBuffer("777".getBytes()));
            of.channel().flush();
            //异步的释放连接
            of.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
        }


    }
}
