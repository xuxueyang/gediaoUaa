package uaa.web.rest;


import core.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uaa.domain.uaa.UaaToken;
import uaa.service.UaaUserService;
import uaa.service.dto.CreateUaaUserDTO;
import util.Validators;

@Api(value = "注册",description = "注册")
@RestController
@RequestMapping("/api")
public class UaaUserResource extends BaseResource{

    @Autowired
    private UaaUserService uaaUserService;
    @PostMapping("/user/reg")
    @ApiOperation(value = "注册", httpMethod = "POST", response = ResponseEntity.class, notes = "注册")
    public ResponseEntity updateDayInfo(@RequestBody CreateUaaUserDTO createUaaUserDTO){
        try {
            if(Validators.fieldBlank(createUaaUserDTO.getPassword())||
                Validators.fieldBlank(createUaaUserDTO.getLoginName())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            uaaUserService.createUser(createUaaUserDTO);
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }
}
