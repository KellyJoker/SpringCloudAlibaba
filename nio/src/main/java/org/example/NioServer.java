package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/9/28 10:12
 **/
public class NioServer implements Callable<String> {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private static final Logger logger = LoggerFactory.getLogger(NioServer.class);

    public NioServer(int port){
        try {
            //创建selector选择器
            selector = Selector.open();
            //创建serverSocketChannel
            serverSocketChannel = ServerSocketChannel.open();
            //开启非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //监听端口
            serverSocketChannel.socket().bind(new InetSocketAddress(9999));
            //与客户端建立连接，监听客户端连接请求
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NioServer nioServer = new NioServer(9999);
        FutureTask<String> task = new FutureTask<>(nioServer);
        new Thread(task).start();
    }

    @Override
    public String call() throws Exception {
        while (true){
            /**
             * 获取当前有哪些事件
             * 阻塞到至少有一个通道在你注册的事件上就绪了，但最长阻塞事件为 1000 毫秒
             */
            int num = selector.select(1000);
            //logger.info("有{}个channel已准备就绪", num);

            /**
             * select()方法返回的 int 值表示有多少通道已经就绪,是自上次调用 select()方法后有多少通道变成就绪状态。
             * 一旦调用 select()方法，并且返回值不为 0 时，则可以通过调用 Selector 的selectedKeys()方法来访问已选择键集合。
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            /**
             * 这个时候，循环遍历 selectedKeys 集中的每个键，并检测各个键所对应的通道的就绪
             * 事件，再通过 SelectionKey 关联的 Selector 和 Channel 进行实际的业务处理。
             */
            for (SelectionKey selectionKey : selectionKeys) {
                /**
                 * 我们必须首先将处理过的 SelectionKey 从选定的键集合中删除。
                 * 如果我们没有删除处理过的键，那么它仍然会在主集合中以一个激活的键出现，这会导致我们尝试再次处理它。
                 */
                selectionKeys.remove(selectionKey);

                //处理事件的发生
                handleInput(selectionKey);
            }
        }
    }

    /**
     * 处理事件的发生
     * @param key key
     * @throws IOException IOException
     */
    private void handleInput(SelectionKey key) throws IOException {
        //在调用某个 key 时,需要使用 isValid 进行校验
        if (key.isValid()){
            //处理新接入的客户端的请求
            //检查这些操作是否就绪，比如 selectionKey.isAcceptable();
            if (key.isAcceptable()){
                //处理连接请求，使用 ServerSocketChannel 处理
                //获取关心当前事件的Channel
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                //接受连接
                SocketChannel sc = ssc.accept();
                logger.info("-----建立连接-----");

                //开启非阻塞模式
                sc.configureBlocking(false);
                //关注读事件
                sc.register(selector, SelectionKey.OP_READ);
            }

            //处理对端的发送的数据
            if (key.isReadable()){
                //处理读、写事件，使用 SocketChannel 处理
                SocketChannel sc = (SocketChannel) key.channel();

                /**
                 * 要想获得一个 Buffer 对象首先要进行分配。 每一个 Buffer 类都有 allocate 方法(可以在堆上分配，也可以在直接内存上分配)。
                 */
                //创建ByteBuffer，开辟一个缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                //从通道里读取数据，然后写入buffer
                int reads = sc.read(buffer);
                if (reads > 0){
                    //flip 方法将 Buffer 从写模式切换到读模式
                    buffer.flip();

                    //根据缓冲区可读字节数创建字节数组
                    byte[] bytes = new byte[buffer.remaining()];
                    //将缓冲区可读字节数组复制到新建的数组中
                    buffer.get(bytes);

                    String msg = new String(bytes, "UTF-8");
                    logger.info("服务器收到消息：{}", msg);

                    //处理数据
                    String result = "已收到---" + msg;

                    //发送应答消息
                    byte[] resultBytes = result.getBytes();
                    //分配内存buffer
                    ByteBuffer resultByteBuffer = ByteBuffer.allocate(resultBytes.length);
                    //写入到buffer中
                    resultByteBuffer.put(resultBytes);
                    resultByteBuffer.flip();
                    //将结果写入到管道中
                    sc.write(resultByteBuffer);
                }else if(reads < 0){
                    //取消特定的注册关系
                    key.cancel();
                    //关闭通道
                    sc.close();
                }
            }
        }
    }
}
