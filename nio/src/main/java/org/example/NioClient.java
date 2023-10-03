package org.example;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/9/28 17:13
 **/
public class NioClient implements Callable<String> {

    private Selector selector;

    private SocketChannel socketChannel;

    public NioClient(){
        try {
            //创建选择器
            selector = Selector.open();
            //创建SocketChannel
            socketChannel = SocketChannel.open();
            //设置通道为非阻塞模式
            socketChannel.configureBlocking(false);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String call(){

        return null;
    }
}
