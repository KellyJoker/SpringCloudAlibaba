package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * @Description 多线程BIO通信服务端
 * @Author danxiaodong
 * @Date 2023/9/13 14:49
 **/
public class MultiThreadServer {

    private static final Logger logger = LoggerFactory.getLogger(MultiThreadServer.class);

    public static void main(String[] args) throws IOException {
        //服务端启动必备
        ServerSocket serverSocket = new ServerSocket();
        //服务端监听的端口
        serverSocket.bind(new InetSocketAddress(8888));
        logger.info("start server...");

        try {
            while (true){
                new Thread(new ServerTask(serverSocket.accept())).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            serverSocket.close();
        }
    }
}
