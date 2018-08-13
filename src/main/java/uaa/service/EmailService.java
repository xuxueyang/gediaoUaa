package uaa.service;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import uaa.config.ApplicationProperties;
import uaa.config.Constants;
import uaa.domain.SenderInfo;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Transactional
@Service
public class EmailService {
    private final Logger log = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private Sender sender;
    @Autowired
    private ApplicationProperties applicationProperties;


    public void sendEmail()throws Exception{
        Context context = new Context(Locale.CHINA);
        context.setVariable(Constants.EMAIL_VAR_URL,"http://193.112.161.157:8080");
        String subject = applicationProperties.getConfig().getEmail().getSubject().getCreate();
        String emailContent = templateEngine.process(Constants.EMAIL_TEMPLATE_TEST_SEND, context);

        SenderInfo senderInfo = new SenderInfo();
        senderInfo.setSubject(subject);
        senderInfo.setToAddress("1059738716@qq.com");
        senderInfo.setHtmlMessage(emailContent);
        senderInfo.setTextMessage("测试Uaa短信");
        sender.sendHTMLFormattedEmail(senderInfo);
    }
//    http://www.runoob.com/jsp/jsp-sending-email.html
//    public static  void main(String[] args){
//        sendEmail2();
//    }
    public static void sendEmail2(){
        String result;
        // 收件人的电子邮件
        String to = "1059738716@qq.com";

        // 发件人的电子邮件
        String from = "mcmohd@gmail.com";

        // 假设你是从本地主机发送电子邮件
        String host = "localhost";

        // 获取系统属性对象
        System.setProperty("java.net.preferIPv4Stack", "true");
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        // 获取默认的Session对象。
        Session mailSession = Session.getDefaultInstance(properties);
        try{
            // 创建一个默认的MimeMessage对象。
            MimeMessage message = new MimeMessage(mailSession);
            // 设置 From: 头部的header字段
            message.setFrom(new InternetAddress(from));
//            type：这个值将会被设置成TO，CC,或BCC。CC代表副本，BCC代表黑色副本，例子程序中使用的是TO。
//            addresses：这是一个邮箱地址的数组，当指定邮箱地址时需要使用InternetAddress()方法。
            // 设置 To: 头部的header字段
            message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(to));
            // 设置 Subject: header字段
            message.setSubject("This is the Subject Line!");
            // 现在设置的实际消息
            message.setText("This is actual message");
            // 发送消息
            Transport.send(message);
            result = "Sent message successfully....";
        }catch (MessagingException mex) {
            mex.printStackTrace();
            result = "Error: unable to send message....";
        }
    }
}
