package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Description 客户端
 * @Author danxiaodong
 * @Date 2023/9/13 12:47
 **/
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws IOException {
        //客户端启动必备
        Socket socket = null;

        //实例化与服务端通信的输入与输出流
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;

        //服务器的通信地址
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 8888);

        try {
            socket = new Socket();

            //连接服务器
            socket.connect(socketAddress);
            logger.info("Connect Server success!!");

            //实例化输入输出流(顺序不能颠倒，需先输出到服务端，再接收服务端请求)
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            logger.info("Ready send message.....");

            //输出请求到服务器
            outputStream.writeUTF("tom");
            //清空输出流
            outputStream.flush();


            //接收服务器请求
            String str = inputStream.readUTF();
            logger.info(str);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (socket != null){
                socket.close();
            }
            if (inputStream != null){
                inputStream.close();
            }
            if (outputStream != null){
                outputStream.close();
            }
        }
    }
}
