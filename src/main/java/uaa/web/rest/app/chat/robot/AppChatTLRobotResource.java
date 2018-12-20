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
import uaa.service.app.chat.rebot.AppChatTLRobotService;
import uaa.web.rest.BaseResource;

/**
 * 调用图灵机械人的聊天接口
 */
@Api(value = "聊天机械人",description = "聊天机械人")
@RestController
@RequestMapping("/api/app/chat/robot/tl")
public class AppChatTLRobotResource extends BaseResource{

   @Autowired
   private AppChatTLRobotService appChatTLRobotService;

   @GetMapping
   @ApiOperation(value = "获取机械人回复",notes = "获取机械人回复", httpMethod = "GET",response = ResponseEntity.class)
    public ResponseEntity getRebotChatReply(@PathVariable( name = "content",required = true) String chatContent){
        try {
            return prepareReturnResult(ReturnCode.GET_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
   }

}
