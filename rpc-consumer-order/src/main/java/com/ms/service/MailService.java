package com.ms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@EnableAsync
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Async
    public void sendSimpleEmail(final String mail){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(env.getProperty("mail.send.from"));
            message.setTo(mail);
            message.setSubject("Simple Email");
            message.setText("hello,this mail send from simple");
            mailSender.send(message);
            log.info("发送简单文本文件-发送成功!");
        }catch (Exception e){
            log.error("发送简单文本文件-发生异常： ",e.fillInStackTrace());
        }
    }

    @Async
    public void sendHTMLEmail(final String mail){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setFrom(env.getProperty("mail.send.from"));
            helper.setTo(mail);
            helper.setSubject("Mime Email");
            helper.setText("hello,this mail send from mime");
            mailSender.send(message);
            log.info("发送花哨邮件-发送成功!");
        }catch (Exception e){
            log.error("发送花哨邮件-发生异常： ",e.fillInStackTrace());
        }
    }
}
