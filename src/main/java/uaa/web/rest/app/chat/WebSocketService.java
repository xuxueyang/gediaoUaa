package uaa.web.rest.app.chat;


import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.socket.server.standard.SpringConfigurator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import uaa.domain.app.chat.AppChatContent;
import uaa.domain.uaa.UaaToken;
import uaa.repository.app.blog.AppChatContentRepository;
import uaa.service.UaaUserService;
import uaa.service.login.UaaLoginService;
import uaa.web.rest.app.chat.base.DataProtocol;
import uaa.web.rest.app.chat.base.GetHttpSessionConfigurator;
import uaa.web.rest.app.chat.base.Protocol;
import uaa.web.rest.app.chat.base.ProtocolHandleInterface;
import util.UUIDGenerator;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static uaa.service.app.chat.rebot.AppChatTLRobotService.getMessage;
import static uaa.web.rest.app.chat.base.DataProtocol.getDataByLast;
import static uaa.web.rest.app.chat.base.DataProtocol.getErrorDataProtocol;

@ServerEndpoint(value = "/websocket",configurator = GetHttpSessionConfigurator.class)
//@ServerEndpoint(value = "/websocket/{token}")
@Component
public class WebSocketService {
    @Autowired
    private UaaLoginService uaaLoginService;
    @Autowired
    private UaaUserService uaaUserService;
    @Autowired
    private AppChatContentRepository appChatContentRepository;


    private HttpSession httpSession;
    public static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    //用本地线程保存session
    private static ThreadLocal<Session> sessions = new ThreadLocal<Session>();

    //保存所有连接上的session__sessionId和session本身
    private static Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();
    private static Map<String,String> userIdSessionIdMap = new ConcurrentHashMap<>();
    private static Map<String,String> sessionIdUserIdMap = new ConcurrentHashMap<>();


    // 一级协议和下属的处理消息
    public static Map<String,ProtocolHandleInterface> firstProtocolMap = new ConcurrentHashMap<>();
    static {
        // 注册系统消息处理器
//        SysProtocolHandle sysProtocolHandle = new SysProtocolHandle();
//        initAddHandle(sysProtocolHandle);
        // 聊天消息处理器
        ChatProtocolHandle chatProtocolHandle = new ChatProtocolHandle();
        initAddHandle(chatProtocolHandle);
    }

    private boolean checkProtocol(String protocol){
        if(Protocol.protocols.contains(protocol))
            return true;
        return false;
    }
    //连接
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
//    public void onOpen(Session session) {
        //这里作为websocket的接口，不做修改
        if(this.uaaLoginService==null||this.uaaUserService==null||this.appChatContentRepository==null){
            this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
            if(httpSession != null){
                ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(httpSession.getServletContext());
                if(this.uaaUserService==null){
                    this.uaaUserService = (UaaUserService) ctx.getBean(UaaUserService.class);
                }
                if(this.uaaLoginService==null){
                    this.uaaLoginService = (UaaLoginService) ctx.getBean(UaaLoginService.class);
                }
                if(this.appChatContentRepository==null){
                    this.appChatContentRepository = (AppChatContentRepository)ctx.getBean(AppChatContentRepository.class);
                }
            }
        }

//        原文：https://blog.csdn.net/qq_38162143/article/details/78275018
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        if(!requestParameterMap.containsKey("token")||!requestParameterMap.containsKey("userId")){
            // 参数不够
            _sendMsg(session,DataProtocol.getCheckData("参数错误",Protocol.CloseSession));
            return;
        }
        sessions.set(session);
        addOnlineCount();
        sessionMap.put(session.getId(), session);
        //两者必须匹配
        UaaToken token = null;
        try {
            token = uaaLoginService.getUserByToken(requestParameterMap.get("token").toArray()[0].toString());
            if(token==null||!token.getCreatedid().equals(requestParameterMap.get("userId").toArray()[0].toString())){
                // 参数不够
                _sendMsg(session,DataProtocol.getCheckData("参数错误",Protocol.CloseSession));
                return;
            }
        }catch (Exception e){
            _sendMsg(session,DataProtocol.getCheckData("参数错误",Protocol.CloseSession));
            return;
        }
        sessionIdUserIdMap.put(session.getId(),token.getCreatedid());
        userIdSessionIdMap.put(token.getCreatedid(),session.getId());
//        logger.debug("[" + session.getId() + "】连接上服务器======当前在线人数[" + getOnlineCount() + "]");
        System.out.println("[" + session.getId() + "】连接上服务器======当前在线人数[" + getOnlineCount() + "]");
        //连接上后给客户端一个消息
//        sendMsg(session, "连接服务器成功！");
        _sendMsg(session,DataProtocol.getCheckData("",Protocol.OnCreatedSys));
    }

    //关闭
    @OnClose
    public void onClose(Session session) {
        if(sessionMap.containsKey(session.getId())){
            subOnlineCount();
            if(sessionIdUserIdMap.containsKey(session.getId())){
                String userId = sessionIdUserIdMap.get(session.getId());
                sessionIdUserIdMap.remove(session.getId());
                if(userIdSessionIdMap.containsKey(userId))
                    userIdSessionIdMap.remove(userId);
            }
            sessionMap.remove(session.getId());
            System.out.println("[" + session.getId() + "]退出了连接======当前在线人数[" + getOnlineCount() + "]");
        }
    }


    //接收消息   客户端发送过来的消息
    @OnMessage
    public void onMessage(String message, Session session) {
        DataProtocol dataProtocol = JSON.parseObject(message, DataProtocol.class);
        if(org.apache.commons.lang.StringUtils.isBlank(dataProtocol.getId())
            && org.apache.commons.lang3.StringUtils.isNotBlank(dataProtocol.getProtocol())){
            // 存储消息
            AppChatContent chatContent = new AppChatContent();
            BeanUtils.copyProperties(dataProtocol,chatContent);
            chatContent.setId(UUIDGenerator.getUUID());
            dataProtocol.setId(chatContent.getId());
            appChatContentRepository.save(chatContent);
        }
        try {
            System.out.println("[" + session.getId() + "]客户端的发送消息======内容[" + message + "]");
            // 基础验证

            if(StringUtils.isBlank(dataProtocol.getToken())){
                DataProtocol errorDataProtocol = getErrorDataProtocol("身份验证失效，请重新登录", dataProtocol);
                sendMsg(getErrorDataProtocol("未知的协议",errorDataProtocol));
                return;
            }
            UaaToken userByToken = uaaLoginService.getUserByToken(dataProtocol.getToken());
            if(userByToken==null){
                DataProtocol errorDataProtocol = getErrorDataProtocol("身份验证失效，请重新登录", dataProtocol);
                sendMsg(getErrorDataProtocol("未知的协议",errorDataProtocol));
            }
            String userId = userByToken.getCreatedid();
            //这边塞入值
            dataProtocol.setSessionId(session.getId());
            dataProtocol.setUserId(userId);

            // 分发任务
            if(dataProtocol.getProtocol()!=null&&!"".equals(dataProtocol.getProtocol())){
                String firstProtocol = dataProtocol.getProtocol().split(Protocol.SPLIT)[0];
                // TODO判断是不是有的协议
                if(checkProtocol(dataProtocol.getProtocol())){
                    ProtocolHandleInterface handleInterface = firstProtocolMap.get(firstProtocol);
                    if(handleInterface!=null){
                        List<DataProtocol> dataProtocols = handleInterface.onMessaeg(session.getId(),message, dataProtocol);
                        if(dataProtocols!=null){
                            for(DataProtocol dataProtocol1:dataProtocols){
                                sendMsg(dataProtocol1);
                            }
                        }
                    }
                }else{
                    //不存在的协议
                    sendMsg(getErrorDataProtocol("未知的协议",dataProtocol));
                }
            }
        }catch (Exception e){
            _sendMsg(session,getErrorDataProtocol(e.getMessage(),dataProtocol));
        }

    }
    //统一的发送消息方法
    public synchronized void sendMsg(DataProtocol dataProtocol) {
        String messageType = dataProtocol.getMessageType();
        if(Protocol.messageType.All.name().equals(messageType)){
           // 群发
            for (Session s : sessionMap.values()) {
                if (!s.getId().equals(sessionIdUserIdMap.get(dataProtocol.getFromUserId()))
                    &&userIdSessionIdMap.containsKey(dataProtocol.getUserId())) {
                    DataProtocol dataByLast = getDataByLast(dataProtocol);
                    dataByLast.setMessage("[" + uaaUserService.findUserById(dataProtocol.getUserId()) + "]发送给[您]的广播消息:\n[" + dataProtocol.getMessage() + "]");
                    _sendMsg(s, dataByLast);
                }
//                else {
//                    DataProtocol dataByLast = getDataByLast(dataProtocol);
//                    dataByLast.setMessage("格调回复道:"+ getMessage(dataProtocol.getMessage()));
//                    _sendMsg(s,dataByLast);
//                }
            }
        }else if(Protocol.messageType.One.name().equals(messageType)){
            //单发
            DataProtocol dataByLast = getDataByLast(dataProtocol);
            dataByLast.setProtocol(Protocol.sendChatMessage);
            dataByLast.setMessageType(Protocol.messageType.One.name());
            dataByLast.setMessage("格调回复道:"+ getMessage(dataProtocol.getMessage()));
            if(StringUtils.isNotBlank(dataProtocol.getToUserId())
                &&userIdSessionIdMap.containsKey(dataProtocol.getToUserId())){
                dataByLast.setUserId("0");//系统发送
                dataByLast.setToUserId(dataProtocol.getToUserId());
                _sendMsg(sessionMap.get(userIdSessionIdMap.get(dataProtocol.getToUserId())),dataByLast);
            }
        }
    }
    private synchronized void _sendMsg(Session session,DataProtocol dataProtocol){
        try {
            // TODO 存入所有的消息(在开始时候不创建）
            AppChatContent chatContent = new AppChatContent();
            BeanUtils.copyProperties(dataProtocol,chatContent);
            if(StringUtils.isBlank(chatContent.getId())){
                chatContent.setId(UUIDGenerator.getUUID());
            }
            appChatContentRepository.save(chatContent);

            session.getBasicRemote().sendText(JSON.toJSONString(dataProtocol));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //异常
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("发生异常!");
        throwable.printStackTrace();
    }
    //统计在线人数
    private static int onlineCount = 0;
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        onlineCount--;
    }

    private static void  initAddHandle(ProtocolHandleInterface handleInterface) {
        if(handleInterface!=null){
            firstProtocolMap.put(handleInterface.getType(),handleInterface);
        }
    }
    public static Session getSessionById(String id){
        return sessionMap.get(id);
    }


}
