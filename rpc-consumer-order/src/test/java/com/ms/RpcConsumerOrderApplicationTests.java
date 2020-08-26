package com.ms;

import com.ms.service.MailService;
import com.ms.service.RabbitSenderService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RpcConsumerOrderApplicationTests {


    @Autowired
    RabbitSenderService senderService;

    @Autowired
    MailService mailService;

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

}
