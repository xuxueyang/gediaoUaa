package uaa.web.rest.app.chat.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

//@Configuration
//@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        // 在网页上可以通过"/applicationName/hello"来和服务器的WebSocket连接
        // ,如：http://localhost:8090/test
        // setAllowedOrigins("*")表示可以跨域
        // withSockJS()表示支持socktJS访问，在浏览器中使用
        stompEndpointRegistry.addEndpoint("/test").setAllowedOrigins("*").withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {
        // 1.订阅模块定义，可以多个,如："/topic","/uset"
        // 2.就是前端订阅了那个模块，当服务器要向那个模块发送信息时就从模块中取出对应的session,(session表明了是那个前端用户)
        // 3.就是那些前缀的URL可以
        messageBrokerRegistry.enableSimpleBroker("/topicTest", "/userTest"); // ,"/user"
        // 这句表示客户端向服务端发送时的主题上面需要加"/app"作为前缀,如：/app/hello
        messageBrokerRegistry.setApplicationDestinationPrefixes("/app");
        // 1.这句表示给指定用户发送（一对一）的主题前缀,如“'/user/'+userid + '/otherMessage'”
        // 2.如果要使用@SendToUser,就不能设置这个，里面会自动实现一套方法，是在@SendToUser的URL前加“user+sessionId"组成
        messageBrokerRegistry.setUserDestinationPrefix("/userTest");
    }
}
