package org.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/9/19 16:51
 **/
public class EchoServer {
    private static final Logger logger = LoggerFactory.getLogger(EchoServer.class);

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        EchoServer echoServer = new EchoServer(9999);
        logger.info("服务器启动。。。");
        echoServer.start();
        logger.info("服务器关闭。。。");
    }

    public void start() {
        //事件
        final EchoServerHandler handler = new EchoServerHandler();
        final EncryptServerHandler encryptHandler = new EncryptServerHandler();
        final CompressServerHandler compressHandler = new CompressServerHandler();


        //设置线程组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            //服务端启动必备(Netty框架的启动类和主入口类)
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioServerSocketChannel.class) //指定使用NIO的通信模式
                    .localAddress(port) //指定监听端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(handler,encryptHandler,compressHandler);
                        }
                    }); //添加事件

            //异步绑定到服务器，sync()会阻塞到完成
            ChannelFuture sync = bootstrap.bind().sync();
            //阻塞当前线程，直到服务器的ServerChannel被关闭
            sync.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("报错了={}",e.getMessage());
        }finally {
            try {
                eventLoopGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
