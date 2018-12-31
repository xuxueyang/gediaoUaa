package uaa.web.rest.app.chat.robot;

import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uaa.domain.uaa.UaaToken;
import uaa.service.app.chat.rebot.AppChatTLRobotService;
import uaa.service.dto.app.chat.AppChatReplyDto;
import uaa.service.login.UaaLoginService;
import uaa.web.rest.BaseResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 调用图灵机械人的聊天接口
 */
@Api(value = "聊天机械人",description = "聊天机械人")
@RestController
@RequestMapping("/api/app/chat/robot/tl")
public class AppChatTLRobotResource extends BaseResource{

   @Autowired
   private AppChatTLRobotService appChatTLRobotService;
   @Autowired
   private UaaLoginService uaaLoginService;

   @GetMapping
   @ApiOperation(value = "获取机械人回复",notes = "获取机械人回复", httpMethod = "GET",response = ResponseEntity.class)
    public ResponseEntity getRebotChatReply(@PathVariable( name = "content",required = true) String chatContent,
                                            @PathVariable( name = "token",required = false) String token,
                                            HttpServletRequest request, HttpServletResponse response){
        try {
            String ipAddr = uaaLoginService.getIpAddr(request);
            UaaToken userByToken = uaaLoginService.getUserByToken(token);
//            AppChatReplyDto dto = appChatTLRobotService.getReply(chatContent,ipAddr,userByToken==null?null:userByToken.getCreatedid());
            return prepareReturnResult(ReturnCode.GET_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
   }

}
