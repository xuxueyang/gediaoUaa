package uaa.service.util;

import com.sun.mail.util.MailSSLSocketFactory;
import lombok.Data;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * @author xuxy
 * @date 2020/12/11 上午10:11
 */
public class QQEmailSendUtil extends Thread {
    //发件人信息
    private String From = "1059738716@qq.com";
    //发件人邮箱
    private String recipient = "1059738716@qq.com";
    //邮箱密码
    private String password = "oujomjozlfjmbbjf";
    //邮件发送的服务器
    private String host = "smtp.qq.com";


    private EmailUser user;

    public void SendMail(EmailUser user) {
        this.user = user;
    }

    @Override
    public void run() {
        try {
            if(user==null){
                throw new Exception("user 不能为空");
            }
            Properties properties = new Properties();

            properties.setProperty("mail.host", "smtp.qq.com");

            properties.setProperty("mail.transport.protocol", "smtp");

            properties.setProperty("mail.smtp.auth", "true");

            //QQ存在一个特性设置SSL加密
            MailSSLSocketFactory sf = null;
            try {
                sf = new MailSSLSocketFactory();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);

            //创建一个session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(recipient, password);
                }
            });

            //开启debug模式
            session.setDebug(true);

            //获取连接对象
            Transport transport = null;
            try {
                transport = session.getTransport();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }

            //连接服务器
            transport.connect(host, From, password);


            //创建一个邮件发送对象
            MimeMessage mimeMessage = new MimeMessage(session);
            //邮件发送人
            mimeMessage.setFrom(new InternetAddress(recipient));

            //邮件接收人
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getMail()));

            //邮件标题
            mimeMessage.setSubject("网站注册成功");

            //邮件内容
            mimeMessage.setContent("网站注册成功，密码为" + user.getPassword() + "，请妥善保管密码", "text/html;charset=UTF-8");

            //发送邮件
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

            transport.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //    private static QQEmailSendUtil _instance;
//
//    private QQEmailSendUtil() {
//
//    }
//
//    public static QQEmailSendUtil getInstance() {
//        if (_instance == null) {
//            _instance = new QQEmailSendUtil();
//        }
//        return _instance;
//    }
//
//    public void send() {
//        //创建一个配置文件并保存
//        Properties properties = new Properties();
//
//        properties.setProperty("mail.host", "smtp.qq.com");
//
//        properties.setProperty("mail.transport.protocol", "smtp");
//
//        properties.setProperty("mail.smtp.auth", "true");
//
//
//        //QQ存在一个特性设置SSL加密
//        MailSSLSocketFactory sf = new MailSSLSocketFactory();
//        sf.setTrustAllHosts(true);
//        properties.put("mail.smtp.ssl.enable", "true");
//        properties.put("mail.smtp.ssl.socketFactory", sf);
//
//        //创建一个session对象
//        Session session = Session.getDefaultInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("1059738716@qq.com", "oujomjozlfjmbbjf");
//            }
//        });
//
//        //开启debug模式
//        session.setDebug(true);
//
//        //获取连接对象
//        Transport transport = session.getTransport();
//
//        //连接服务器
//        transport.connect("smtp.qq.com", "1059738716@qq.com", "oujomjozlfjmbbjf");
//
//        //创建邮件对象
//        MimeMessage mimeMessage = new MimeMessage(session);
//
//        //邮件发送人
//        mimeMessage.setFrom(new InternetAddress("1059738716@qq.com"));
//
//        //邮件接收人
//        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("1059738716@qq.com"));
//
//        //邮件标题
//        mimeMessage.setSubject("Hello Mail");
//
//        //邮件内容
//        mimeMessage.setContent("我的想法是把代码放进一个循环里", "text/html;charset=UTF-8");
//
//        //发送邮件
//        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
//
//        //关闭连接
//        transport.close();
//    }
//    public static MimeMessage complexEmail(Session session) throws MessagingException {
//        //消息的固定信息
//        MimeMessage mimeMessage = new MimeMessage(session);
//
//        //发件人
//        mimeMessage.setFrom(new InternetAddress("619046217@qq.com"));
//        //收件人
//        mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress("619046217@qq.com"));
//        //邮件标题
//        mimeMessage.setSubject("带图片和附件的邮件");
//
//        //邮件内容
//        //准备图片数据
//        MimeBodyPart image = new MimeBodyPart();
//        DataHandler handler = new DataHandler(new FileDataSource("E:\\IdeaProjects\\WebEmail\\resources\\测试图片.png"));
//        image.setDataHandler(handler);
//        image.setContentID("test.png"); //设置图片id
//
//        //准备文本
//        MimeBodyPart text = new MimeBodyPart();
//        text.setContent("这是一段文本<img src='cid:test.png'>","text/html;charset=utf-8");
//
//        //附件
//        MimeBodyPart appendix = new MimeBodyPart();
//        appendix.setDataHandler(new DataHandler(new FileDataSource("E:\\IdeaProjects\\WebEmail\\resources\\测试文件.txt")));
//        appendix.setFileName("test.txt");
//
//        //拼装邮件正文
//        MimeMultipart mimeMultipart = new MimeMultipart();
//        mimeMultipart.addBodyPart(image);
//        mimeMultipart.addBodyPart(text);
//        mimeMultipart.setSubType("related");//文本和图片内嵌成功
//
//        //将拼装好的正文内容设置为主体
//        MimeBodyPart contentText = new MimeBodyPart();
//        contentText.setContent(mimeMultipart);
//
//        //拼接附件
//        MimeMultipart allFile = new MimeMultipart();
//        allFile.addBodyPart(appendix);//附件
//        allFile.addBodyPart(contentText);//正文
//        allFile.setSubType("mixed"); //正文和附件都存在邮件中，所有类型设置为mixed
//
//
//        //放到Message消息中
//        mimeMessage.setContent(allFile);
//        mimeMessage.saveChanges();//保存修改
//
//        return mimeMessage;
//    }
    @Data
    public static class EmailUser {
        private String name;
        private String password;
        private String mail;

        public EmailUser(String name, String password, String mail) {
            this.name = name;
            this.password = password;
            this.mail = mail;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }
    }
}
