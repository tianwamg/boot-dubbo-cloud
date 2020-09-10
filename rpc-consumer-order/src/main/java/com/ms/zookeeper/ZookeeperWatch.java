package com.ms.zookeeper;

import com.ms.service.RabbitReceiveService;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.util.Profiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper watchEvent
 */
public class ZookeeperWatch implements Watcher,Runnable {

    private static final Logger log = LoggerFactory.getLogger(RabbitReceiveService.class);

    /* 等待连接建立成功的信号*/
    private CountDownLatch connectedSemaphore = new CountDownLatch(1);

    /*退出系统的信号*/
    private static Integer quitSemaphore = Integer.valueOf(-1);
    String zNode;

    ZooKeeper zooKeeper;

    public ZookeeperWatch(String host,String zNode){
        this.zNode = zNode;
        System.out.println("开始连接到zookeeper");
        try {
            zooKeeper = new ZooKeeper(host,3000,this);
            System.out.println("等待连接成功的event");
            connectedSemaphore.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.toString());
        if(Event.EventType.None.equals(watchedEvent.getType())){
            //连接状态发生改变
            if(Event.KeeperState.SyncConnected.equals(watchedEvent.getState())){
                //建立连接成功
                connectedSemaphore.countDown();
            }
        }else if(Event.EventType.NodeDataChanged.equals(watchedEvent.getType())
            ||Event.EventType.NodeCreated.equals(watchedEvent.getType())){
            String path = watchedEvent.getPath();
            if(path != null && path.equals(zNode)){
                //节点数据被修改
                //TODO
            }
        }else if(Event.EventType.NodeDeleted.equals(watchedEvent.getType())){
            String path = watchedEvent.getPath();
            if(path != null && path.equals(zNode)){
                //节点数据被删除,通知退出线程
                synchronized (quitSemaphore){
                    quitSemaphore.notify();
                }
            }
        }
    }

    private void readNodeData(){
        try{
            Stat stat = new Stat();
            byte[] data = zooKeeper.getData(zNode,true,stat);
            if(data != null){
                log.info("{}, value={}, version={}", zNode, new String(data), stat.getVersion());
            }
        }catch (Exception e){
            log.info("{} 不存在", zNode);
            try {
                //目的是添加watch
                zooKeeper.exists(zNode,true);
            } catch (KeeperException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void run() {
        synchronized (quitSemaphore){
            try {
                quitSemaphore.wait();
                log.info("{} 被删除，退出", zNode);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
