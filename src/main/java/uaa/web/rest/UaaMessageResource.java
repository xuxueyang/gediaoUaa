package uaa.web.rest;

import core.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uaa.config.Constants;
import uaa.service.UaaMessageService;
import uaa.service.dto.message.CreateMessageDTO;

@Api(value = "UAA消息管理",description = "UAA消息管理")
@RestController
@RequestMapping("/api")
public class UaaMessageResource extends BaseResource {
    @Autowired
    private UaaMessageService uaaMessageService;

    @GetMapping("/message/develop")
    @ApiOperation(value = "获取开发日志", httpMethod = "GET", response = ResponseEntity.class, notes = "获取开发日志")
    public ResponseEntity getDevelopMessage(@PathVariable("project-type") String projectType){
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
    @ApiOperation(value = "修改开发日志——todo：添加一个权限表", httpMethod = "GET", response = ResponseEntity.class, notes = "获取开发日志")
    public ResponseEntity updateDevelopMessage(@RequestBody CreateMessageDTO createMessageDTO){
        try{
            //TODO 验证有没有添加的权限

            //有的话才能添加——TODO 还要增加修改删除的

            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }
}
