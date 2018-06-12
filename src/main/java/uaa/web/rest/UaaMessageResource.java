package uaa.web.rest;

import core.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uaa.config.Constants;
import uaa.domain.UaaError;
import uaa.domain.uaa.UaaToken;
import uaa.domain.uaa.UaaUser;
import uaa.service.UaaMessageService;
import uaa.service.UaaUserService;
import uaa.service.dto.UaaBasePremissionDTO;
import uaa.service.dto.message.CreateMessageDTO;
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

    @GetMapping("/message/develop")
    @ApiOperation(value = "获取开发日志", httpMethod = "GET", response = ResponseEntity.class, notes = "获取开发日志")
    public ResponseEntity getDevelopMessage(@PathParam("projectType") String projectType){
        try {
            //
            if(!Constants.MESSAGE_PROJECT_TYPE_QINGLONGHUI.equals(projectType)){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
            }
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,uaaMessageService.getMessagesByType(projectType));
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }
    @PostMapping("/message/develop")
    @ApiOperation(value = "添加开发日志", httpMethod = "GET", response = ResponseEntity.class, notes = "添加开发日志")
    public ResponseEntity updateDevelopMessage(@RequestBody CreateMessageDTO createMessageDTO){
        try{
            //验证有没有添加的权限
            if(Validators.fieldBlank(createMessageDTO.getProjectType())
                ||Validators.fieldBlank(createMessageDTO.getTitle())
                ||Validators.fieldBlank(createMessageDTO.getLoginName())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            if(!Validators.fieldRangeValue(createMessageDTO.getProjectType(),Constants.MESSAGE_PROJECT_TYPE_QINGLONGHUI))
            {
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
            }
            if(Validators.fieldBlank(createMessageDTO.getToken())&&(Validators.fieldBlank(createMessageDTO.getVerifyCode())
                ||Validators.fieldBlank(createMessageDTO.getLoginName()))){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            //验证消息范围
            Constants.MESSAGE_TYPE[] values = Constants.MESSAGE_TYPE.values();
            boolean has = false;
            for(Constants.MESSAGE_TYPE value:values){
                if(value.name().equals(createMessageDTO.getType()))
                {
                    has = true;
                    break;
                }
            }
            if(!has)
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
//            if (!Validators.fieldRangeValue(createMessageDTO.getType(), Constants.MESSAGE_TYPE.values())) {
//                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
//            }
            UaaError uaaError = uaaPermissionService.verifyOperation(createMessageDTO, "/api/message/develop");
            if(uaaError.hasError())
                return prepareReturnResult(uaaError.getFirstError(),null);
            //执行操作
            uaaMessageService.createMessage(((UaaUser)uaaError.getValue()).getId(),createMessageDTO.getTitle(),createMessageDTO.getProjectType(),createMessageDTO.getType(),createMessageDTO.getValue());
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }

    @DeleteMapping("/message/{id}")
    @ApiOperation(value = "修改开发日志——todo：添加一个权限表", httpMethod = "GET", response = ResponseEntity.class, notes = "获取开发日志")
    public ResponseEntity deleteDevelopMessage(@PathVariable("id") String id, @RequestBody UaaBasePremissionDTO premissionDTO){
        try {
            UaaError uaaError = uaaPermissionService.verifyOperation(premissionDTO, "/api/message/{id}");
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


}
