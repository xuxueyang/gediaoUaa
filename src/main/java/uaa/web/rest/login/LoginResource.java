package uaa.web.rest.login;

import core.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uaa.service.dto.login.LoginDTO;
import uaa.service.login.LoginService;
import util.UUIDGenerator;
import util.Validators;


@Api(value = "登录系统",description = "登录系统")
@RestController
@RequestMapping("/api")
public class LoginResource extends BaseResource{
    @GetMapping("/login-test")
    @ResponseBody
    @ApiOperation(value = "测试登录", httpMethod = "GET", response = ResponseEntity.class, notes = "测试登录")
    public ResponseEntity<?> downloadFile() {
        //登录了
        String token = UUIDGenerator.getUUID();
        return prepareReturnResult(ReturnCode.GET_SUCCESS,"登录成功,token: "+token);
    }

    @Autowired
    private LoginService loginService;

    @PostMapping("/login/authenticate")
    @ApiOperation(value = "普通登录", notes = "必填：用户名、密码； 选填：graphCaptchaCodeId,graphCaptchaCode")
    public ResponseEntity login(final @RequestBody LoginDTO loginDTO){
        try {
            //判断字段
            if(Validators.fieldBlank(loginDTO.getName())||
                Validators.fieldBlank(loginDTO.getPassword())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            //验证验证码


            String token = loginService.login(loginDTO);
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_LOGIN,null);
        }
    }
}
