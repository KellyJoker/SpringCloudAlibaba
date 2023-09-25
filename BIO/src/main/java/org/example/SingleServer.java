package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description 单线程BIO通信服务端
 * @Author danxiaodong
 * @Date 2023/9/13 12:47
 **/
public class SingleServer {
    private static final Logger logger = LoggerFactory.getLogger(SingleServer.class);

    public static void main(String[] args) throws IOException {
        //服务端启动必备
        ServerSocket serverSocket = new ServerSocket();
        //服务端监听的端口
        serverSocket.bind(new InetSocketAddress(8888));
        logger.info("start server...");

        int count = 0;
        try {
            while (true){
                Socket socket = serverSocket.accept();

                try {
                    //实例化与客户端的输入输出流
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

                    //服务端的输入，也就是客户端的输出
                    String name = inputStream.readUTF();
                    logger.info("Accept client message:{}", name);

                    //服务端的输出，也就是客户端的输入
                    outputStream.writeUTF("hello " + name);

                    //清空输出流
                    outputStream.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            serverSocket.close();
        }
    }
}
