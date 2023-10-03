package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/9/13 14:53
 **/
public class ServerTask implements Runnable{
    private Socket socket;

    private static final Logger logger = LoggerFactory.getLogger(ServerTask.class);

    public ServerTask(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
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
}
