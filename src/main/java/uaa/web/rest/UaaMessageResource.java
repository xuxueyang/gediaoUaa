package uaa.web.rest;

import com.sun.org.apache.regexp.internal.REUtil;
import core.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uaa.config.Constants;
import uaa.domain.UaaError;
import uaa.domain.uaa.UaaLogMessage;
import uaa.domain.uaa.UaaToken;
import uaa.domain.uaa.UaaUser;
import uaa.service.UaaMessageService;
import uaa.service.UaaUserService;
import uaa.service.dto.UaaBasePremissionDTO;
import uaa.service.dto.message.CreateMessageDTO;
import uaa.service.dto.message.TranferMessageDTO;
import uaa.service.dto.message.UpdateMessageDTO;
import uaa.service.login.UaaLoginService;
import uaa.service.permission.UaaPermissionService;
import util.Validators;

import javax.websocket.server.PathParam;

@Api(value = "UAA消息管理",description = "UAA消息管理")
@RestController
@RequestMapping("/api")
public class UaaMessageResource extends BaseResource {
    @Autowired
    private UaaMessageService uaaMessageService;
    @Autowired
    private UaaUserService uaaUserService;
    @Autowired
    private UaaLoginService uaaLoginService;
    @Autowired
    private UaaPermissionService uaaPermissionService;

    @GetMapping("/message")
    @ApiOperation(value = "获取开发日志", httpMethod = "GET", response = ResponseEntity.class, notes = "获取开发日志")
    public ResponseEntity getDevelopMessage(@PathParam("projectType") String projectType,@PathParam("token") String token){
        try {
//            //TODO 对于projectType也需要做权限认真，看是不是要登录名等
//            //QLH不做验证，返回其他的需要验证
//            if(Validators.fieldBlank(token)&&(
//                !Constants.PROJECT_TYPE_QINGLONGHUI.equals(projectType)
//                &&!Constants.PROJECT_TYPE_UE4_XY.equals(projectType)
//            )){
//                return prepareReturnResult(ReturnCode.ERROR_NO_PERMISSIONS_UPDATE,null);
//            }
            //如果token不为空需要找出User,然后搜索出下面的消息
            if(Validators.fieldBlank(token))
                return prepareReturnResult(ReturnCode.CREATE_SUCCESS,uaaMessageService.getMessagesByProjectType(projectType));
            else{
                UaaToken userByToken = uaaLoginService.getUserByToken(token);
                if(userByToken==null)
                    return prepareReturnResult(ReturnCode.ERROR_NO_PERMISSIONS_UPDATE,null);
                return prepareReturnResult(ReturnCode.CREATE_SUCCESS,uaaMessageService.getMessagesByProjectTypeAndCreatedId(projectType,userByToken.getCreatedid()));
            }
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }
//    @GetMapping("/message/personal")
//    @ApiOperation(value = "获取个人备忘录", httpMethod = "GET", response = ResponseEntity.class, notes = "获取个人备忘录")
//    public ResponseEntity getPersonMessage(@PathParam("projectType") String projectType){
//        try {
//            //
//            if(!Constants.MESSAGE_PROJECT_TYPE_QINGLONGHUI.equals(projectType)){
//                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
//            }
//            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,uaaMessageService.getMessagesByType(projectType));
//        }catch (Exception e){
//            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
//        }
//    }

    @PostMapping("/message")
    @ApiOperation(value = "添加开发日志", httpMethod = "GET", response = ResponseEntity.class, notes = "添加开发日志")
    public ResponseEntity updateDevelopMessage(@RequestBody CreateMessageDTO createMessageDTO){
        try{
            //验证有没有添加的权限
            if(Validators.fieldBlank(createMessageDTO.getProjectType())
                ||Validators.fieldBlank(createMessageDTO.getTitle())
                ||Validators.fieldBlank(createMessageDTO.getLoginName())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
//            if(!Validators.fieldRangeValue(createMessageDTO.getProjectType(),
//                Constants.PROJECT_TYPE_QINGLONGHUI,
//                Constants.PROJECT_TYPE_UE4_XY))
//            {
//                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
//            }
            if(Validators.fieldBlank(createMessageDTO.getToken())&&(Validators.fieldBlank(createMessageDTO.getVerifyCode())
                ||Validators.fieldBlank(createMessageDTO.getLoginName()))){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            //验证消息范围
            if(!Validators.fieldRangeValue(
                createMessageDTO.getType(),
                Constants.MESSAGE_TYPE_TODO,
                Constants.MESSAGE_TYPE_DONE,
                Constants.MESSAGE_TYPE_BUG,
                Constants.MESSAGE_TYPE_PAD,
                Constants.MESSAGE_TYPE_MEMBER_SAY
            )){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
            }

            UaaError<UaaUser> uaaError = uaaPermissionService.verifyOperation(createMessageDTO, "/api/message","POST");
            if(uaaError.hasError())
                return prepareReturnResult(uaaError.getFirstError(),null);
            //执行操作
            uaaMessageService.createMessage(createMessageDTO.getPs(),((UaaUser)uaaError.getValue()).getId(),createMessageDTO.getTitle(),createMessageDTO.getProjectType(),
                createMessageDTO.getType(),createMessageDTO.getValue());
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }

    @DeleteMapping("/message/{id}")
    @ApiOperation(value = "删除开发日志", httpMethod = "DELETE", response = ResponseEntity.class, notes = "删除开发日志")
    public ResponseEntity deleteDevelopMessage(@PathVariable("id") String id, @RequestBody UaaBasePremissionDTO premissionDTO){
        try {
            UaaError<UaaUser> uaaError = uaaPermissionService.verifyOperation(premissionDTO, "/api/message/{id}","DELETE");
            if(uaaError.hasError())
                return prepareReturnResult(uaaError.getFirstError(),null);
            UaaError uaaError2 = uaaMessageService.deleteMessage(id);
            if(uaaError2.hasError())
                return prepareReturnResult(uaaError2.getFirstError(),null);
            return prepareReturnResult(ReturnCode.DELETE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_DELETE,null);
        }
    }

    @PutMapping("/message/tranfer")
    @ApiOperation(value = "转换开发日志", httpMethod = "PUT", response = ResponseEntity.class, notes = "转换开发日志")
    public ResponseEntity tranferDevelopMessage(@RequestBody TranferMessageDTO tranferMessageDTO){
        try{
            //验证状态，不符合不行（如果类别不变算错误，删除现有的，新建复制一个
            //权限验证
            UaaError<UaaUser> uaaError = uaaPermissionService.verifyOperation(tranferMessageDTO, "/message/tranfer","PUT");
            if(uaaError.hasError())
                return prepareReturnResult(uaaError.getFirstError(),null);
            //TODO 走事物逻辑了
            //如果类型不一样了那么视为字段错误
            UaaLogMessage logMessage = uaaMessageService.findProjectCanUpdateMessageById(tranferMessageDTO.getProjectType(),
                tranferMessageDTO.getId());
            if(logMessage==null)
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            //只有三种能狗更新字段
            if(!Validators.fieldRangeValue(tranferMessageDTO.getType(),
                Constants.MESSAGE_TYPE_TODO,
                Constants.MESSAGE_TYPE_DONE,
                Constants.MESSAGE_TYPE_BUG
                )){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
            }
            if(logMessage.getType().equals(tranferMessageDTO.getType()))
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);

            //更新ps、id、date、字段(这种转移是需要复制的
            uaaMessageService.transferMessageTypeAndPs(logMessage,tranferMessageDTO.getType(),tranferMessageDTO.getPs(),uaaError.getValue().getId());
            return prepareReturnResult(ReturnCode.UPDATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_UPDATE,null);
        }
    }
    @PutMapping("/message/update")
    @ApiOperation(value = "更新开发日志", httpMethod = "PUT", response = ResponseEntity.class, notes = "更新开发日志")
    public ResponseEntity updateDevelopMessage(@RequestBody UpdateMessageDTO updateMessageDTO){
        try{
            //权限验证
            UaaError<UaaUser> uaaError = uaaPermissionService.verifyOperation(updateMessageDTO, "/message/update","PUT");
            if(uaaError.hasError())
                return prepareReturnResult(uaaError.getFirstError(),null);
            //走事物逻辑了
            if(Validators.fieldBlank(updateMessageDTO.getId())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            UaaLogMessage logMessage = uaaMessageService.findProjectCanUpdateMessageById(updateMessageDTO.getProjectType(),
                updateMessageDTO.getId());
            if(logMessage==null)
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);

            uaaMessageService.updateMessageValueAndPs(logMessage,updateMessageDTO.getTitle(),updateMessageDTO.getValue(),updateMessageDTO.getPs(),uaaError.getValue().getId());
            return prepareReturnResult(ReturnCode.UPDATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_UPDATE,null);
        }
    }
}
