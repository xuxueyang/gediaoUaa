package uaa.web.rest;


import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uaa.domain.uaa.UaaUser;
import uaa.service.UaaUserService;
import uaa.service.dto.CreateUaaUserDTO;
import uaa.service.dto.login.UserInfo;
import util.Validators;

import javax.websocket.server.PathParam;
import java.util.List;

@Api(value = "注册",description = "注册")
@RestController
@RequestMapping("/api")
public class UaaUserResource extends BaseResource {

    @Autowired
    private UaaUserService uaaUserService;

    @PostMapping("/user/reg")
    @ApiOperation(value = "注册", httpMethod = "POST", response = ResponseEntity.class, notes = "注册")
    public ResponseEntity updateDayInfo(@RequestBody CreateUaaUserDTO createUaaUserDTO) {
        try {
            if(Validators.fieldBlank(createUaaUserDTO.getProjectType())){
                return prepareReturnResult(ReturnCode.ERROR_NO_PERMISSIONS_UPDATE,null);
            }
            if (Validators.fieldBlank(createUaaUserDTO.getPassword()) ||
                Validators.fieldBlank(createUaaUserDTO.getLoginName())) {
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY, null);
            }
            //名字不能重复
            UaaUser userByName = uaaUserService.findUserByName(createUaaUserDTO.getLoginName());
            if(userByName!=null)
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EXIST_CODE,null);
            uaaUserService.createUser(createUaaUserDTO);
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS, null);
        } catch (Exception e) {
            return prepareReturnResult(ReturnCode.ERROR_CREATE, null);
        }
    }

    //得到成员列表
    @GetMapping("/users")
    @ApiOperation(value = "得到成员列表", httpMethod = "GET", response = ResponseEntity.class, notes = "得到成员列表")
    public ResponseEntity getUsers(@PathParam("projectType") String projectType) {
        try {
            if(Validators.fieldBlank(projectType)){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            //根据project搜索用户
            List<UserInfo> infoList = uaaUserService.getUserInfosByProjectType(projectType);
            return prepareReturnResult(ReturnCode.GET_SUCCESS, infoList);
        } catch (Exception e) {
            return prepareReturnResult(ReturnCode.ERROR_QUERY, null);
        }
    }
}
