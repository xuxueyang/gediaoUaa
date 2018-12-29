package uaa.web.rest.app.chat.base;

import javax.websocket.Session;
import java.util.List;

public interface ProtocolHandleInterface {
//    void regeistrySelf();
//    DataProtocol onOpen(Session session,DataProtocol protocol);
    List<DataProtocol> onMessaeg(String sessionId, String message, DataProtocol protocol);
    String getType();
}
