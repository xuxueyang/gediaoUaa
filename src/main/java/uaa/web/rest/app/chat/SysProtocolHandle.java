package uaa.web.rest.app.chat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import uaa.domain.uaa.UaaToken;
import uaa.service.login.UaaLoginService;
import uaa.web.rest.app.chat.base.DataProtocol;
import uaa.web.rest.app.chat.base.Protocol;
import uaa.web.rest.app.chat.base.ProtocolHandleInterface;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static uaa.web.rest.app.chat.base.DataProtocol.getSuccessDataProtocol;


// 全部单例
public final class SysProtocolHandle implements ProtocolHandleInterface {

// 保存userId和sessionId之间的互相映射关系


    public SysProtocolHandle(){

    }

    @Override
    public List<DataProtocol> onMessaeg(String sessionId, String message, DataProtocol protocol) {
        switch (protocol.getProtocol()){
            case Protocol.OnCreated:
                return OnCreateSuccess(sessionId,message,protocol);
        }
        return null;
    }


    //TODO 需要传入token，从token获取到用户，然后塞入userId和会话ID的匹配键
    private List<DataProtocol>  OnCreateSuccess(String sessionId,String message, DataProtocol protocol){
//        WebSocketService.saveUserIdToSessionId(protocol.getUserId(),sessionId);
//        WebSocketService.saveUserIdToSessionId(sessionId,protocol.getUserId());
        List<DataProtocol> returnList = new ArrayList<>();
        returnList.add(getSuccessDataProtocol("",protocol));
        return returnList ;
    }
    @Override
    public String getType() {
        return Protocol.FirstProtocol_CHAT;
    }
}
