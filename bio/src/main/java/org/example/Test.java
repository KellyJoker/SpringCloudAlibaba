package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/9/27 11:32
 **/
public class Test {
    public static void main(String[] args) throws IOException {
        //创建选择器
        Selector selector = Selector.open();

        //打开监听通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //开启非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //绑定端口，进行监听
        serverSocketChannel.socket().bind(new InetSocketAddress(8888), 1024);
        //监听客户端连接请求
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }
}
