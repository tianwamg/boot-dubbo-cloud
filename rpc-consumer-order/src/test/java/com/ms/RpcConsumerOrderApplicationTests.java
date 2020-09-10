package com.ms;

import com.ms.config.RedisConfiguration;
import com.ms.config.ZookeeperConfiguration;
import com.ms.service.MailService;
import com.ms.service.RabbitSenderService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RpcConsumerOrderApplicationTests {


    @Autowired
    RabbitSenderService senderService;

    @Autowired
    MailService mailService;

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
        /*for(int i=0;i<10000;i++) {
            senderService.sendKillSuccessEmailMsg("123--"+i);
        }*/
    }

    @Test
    public void sendMail(){
        //mailService.sendSimpleEmail("");

    }

    @Test
    public void sendExpire(){
        senderService.sendKilSuccessOrderExpireMsg("1111");
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(123);
    }

    @Test
    public void redis(){
        redisTemplate.opsForValue().set("kabc","abcdefg");
        boolean flag = redisTemplate.opsForValue().setIfAbsent("ha","h",30,TimeUnit.SECONDS);
        if(flag) {
            flag = redisTemplate.opsForValue().setIfAbsent("ha","h",30,TimeUnit.SECONDS);
            System.out.println(flag);
        }
    }

    @Autowired
    RedissonClient redissonClient;

    @Test
    public void redisson(){
        RLock rLock = redissonClient.getLock("rlock");
        try{
            //args[1]:尝试获取分布式锁,并且最大等待时间为10s,args[2]获取锁之后30秒将释放锁
            boolean caches = rLock.tryLock(10,30,TimeUnit.SECONDS);
            if(caches){
                TimeUnit.SECONDS.sleep(60);
            }else {

            }
            System.out.println(123);
        }catch (Exception e){

        }finally {
            rLock.unlock();
        }
    }


    //zookeeper

    @Autowired
    CuratorFramework curatorFramework;

    String pathPrefix = "/kill/zklock";

    @Test
    public void zookeeper(){
        //定义获取分布式锁的操作组件实例
        InterProcessMutex mutex = new InterProcessMutex(curatorFramework,pathPrefix+"-ttt");
        try {
            if(mutex.acquire(10l,TimeUnit.SECONDS)){
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(mutex != null){
                try {
                    mutex.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void anno(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ZookeeperConfiguration.class);
        System.out.println(context.getBean(""));
    }

}
