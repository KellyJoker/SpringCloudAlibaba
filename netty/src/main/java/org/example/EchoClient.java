package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/9/20 10:26
 **/
public class EchoClient {
    private final int port;
    private final String host;

    private static final Logger logger = LoggerFactory.getLogger(EchoClient.class);

    public EchoClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public static void main(String[] args) {
        EchoClient echoClient = new EchoClient(9999, "127.0.0.1");
        logger.info("客户端启动。。。");
        echoClient.start();
        logger.info("客户端关闭。。。");
    }

    public void start(){
        //线程组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            //客户端启动器
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class) //指定使用NIO的通信模式
                    .remoteAddress(new InetSocketAddress(host, port)) //指定服务器的IP地址和端口
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            //异步连接到服务器，sync()会阻塞到完成
            ChannelFuture cf = bootstrap.connect().sync();
            //阻塞当前线程，直到客户端的Channel被关闭
            cf.channel().closeFuture().sync();
        }catch (Exception e){
            logger.error("客户端报错={}", e.getMessage());
        }finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
