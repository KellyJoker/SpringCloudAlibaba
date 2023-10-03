package org.example;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/10/1 19:41
 **/
public class ZkClientDemo {
    private ZooKeeper zooKeeper;

    private static final Logger logger = LoggerFactory.getLogger(ZkClientDemo.class);

    public ZkClientDemo(){
        try {
            CountDownLatch count = new CountDownLatch(1);
            zooKeeper = new ZooKeeper("localhost:2181", 4000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (Event.KeeperState.SyncConnected==event.getState()
                            && Event.EventType.None==event.getType()){
                        //如果收到了服务端的响应事件，连接成功
                        count.countDown();
                        logger.info("连接建立。。。");
                    }
                }
            });
            count.await();
            logger.info("连接状态={}", zooKeeper.getState());
        }catch (IOException e){
            //TODO
        }catch (InterruptedException e){
            //TODO
        }
    }

    public static void main(String[] args) throws InterruptedException, KeeperException {
        ZkClientDemo zkClient = new ZkClientDemo();
        zkClient.createSyncNode();
    }

    public void create() throws InterruptedException, KeeperException {
        //创建持久节点
        // create(path, data, acl,createMode): 创建一个给定路径的 znode，
        // 并在znode 保存 data[]的 数据，createMode指定 znode 的类型。
        zooKeeper.create("/user", "danxiaodong".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public void createSyncNode() throws InterruptedException, KeeperException {
        zooKeeper.create("/admin1", "admin1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public void createAsyncNode() throws InterruptedException, KeeperException {
        zooKeeper.getData("/user", false, (rc, path, ctx, data, stat) -> {
            Thread thread = Thread.currentThread();
            logger.info("Thread Name: {}, rc: {}, path: {}, ctx: {}, data: {}, stat: {}", thread.getName(), rc,
                    path, ctx, data, stat);
        }, "user");
        logger.info("over");
    }
}
