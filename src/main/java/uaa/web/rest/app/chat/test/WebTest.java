package uaa.web.rest.app.chat.test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebTest {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // 表示服务端接收客户端通过主题“/app/hello”发送过来的消息(服务端接收消息接口)
    @MessageMapping("/hello")
    // 表示这个函数执行完成后会广播结果到"/topicTest/sayHello"这个主题，前端如果订阅了这个主题就会收到这个信息，
    // 相当于simpMessagingTemplate.convertAndSend("/topicTest/sayHello",
    // "sayHello");
    @SendTo("/topicTest/sayHello")
    public String greeting(String msg) throws Exception {
        System.out.println("greeting == " + msg);
        // simpMessagingTemplate.convertAndSend("/topicTest/sayHello",
        // "sayHello");
        msg = msg + "表示服务端接收客户端通过主题";
        return msg;
    }

    // 表示服务端接收客户端通过主题“/app/hello”发送过来的消息(服务端接收消息接口)
    @MessageMapping("/hello2")
    // 表示这个函数执行完成后会广播结果到"/topicTest/sayHello"这个主题，前端如果订阅了这个主题就会收到这个信息，
    // 相当于simpMessagingTemplate.convertAndSend("/topicTest/sayHello",
    // "sayHello");
    // @SendToUser("/topicTest/sayHello2/otherMessage")
    // 相当于simpMessagingTemplate.convertAndSendToUser(user, "/otherMessage", "
    // ==SendToUser推送给单一用户2==");
    public String greeting2(String msg) throws Exception {
        System.out.println("greeting2 == " + msg);
        String user = msg;
        simpMessagingTemplate.convertAndSendToUser(user, "/otherMessage", "==SendToUser推送给单一用户2==");

        return msg;
    }

    // 表示服务端接收客户端通过主题“/app/hello”发送过来的消息(服务端接收消息接口)
    @MessageMapping("/hello3")
    // 表示这个函数执行完成后会广播结果到"/topicTest/sayHello"这个主题，前端如果订阅了这个主题就会收到这个信息，
    // 相当于simpMessagingTemplate.convertAndSend("/topicTest/sayHello",
    // "sayHello");
    @SendToUser("/userTest/sayHello3")
    // 相当于simpMessagingTemplate.convertAndSendToUser(user, "/otherMessage", "
    // ==SendToUser推送给单一用户2==");
    public String greeting3(String msg) throws Exception {
        System.out.println("greeting3 == " + msg);
        String user = msg;
        msg = msg + " ==SendToUser推送给单一用户3==";
        return msg;
    }
}
