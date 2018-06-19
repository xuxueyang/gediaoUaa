package uaa.web.rest.login;

import core.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uaa.domain.uaa.UaaUser;
import uaa.service.UaaUserService;
import uaa.service.dto.login.LoginDTO;
import uaa.service.login.UaaLoginService;
import util.UUIDGenerator;
import util.Validators;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Api(value = "登录系统",description = "登录系统")
@RestController
@RequestMapping("/api")
public class UaaLoginResource extends BaseResource{
    @GetMapping("/login/test-graph")
    @ResponseBody
    @ApiOperation(value = "测试登录", httpMethod = "GET", response = ResponseEntity.class, notes = "测试登录")
    public ResponseEntity<?> downloadFile() {
        //登录了
        String token = UUIDGenerator.getUUID();
        return prepareReturnResult(ReturnCode.GET_SUCCESS,"登录成功,token: "+token);
    }

    @Autowired
    private UaaLoginService uaaLoginService;

    @Autowired
    private UaaUserService uaaUserService;

    @GetMapping("/login/graph")
    @ApiOperation(value = "获取验证码", httpMethod = "GET", response = ResponseEntity.class, notes = "获取验证码")
    public ResponseEntity<?> getGraph() {
        try {
            Map<String,Object> map = uaaLoginService.createGraph();
            return prepareReturnResult(ReturnCode.GET_SUCCESS,map);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_GRAPH_CODE,null);
        }
    }
    @PostMapping("/login/visit-count")
    @ApiOperation(value = "访问计数", httpMethod = "POST", response = ResponseEntity.class, notes = "访问计数")
    public ResponseEntity<?> visitCount(HttpServletRequest request, HttpServletResponse response) {
        try {
            int num = uaaLoginService.visitcount(request);
            return prepareReturnResult(ReturnCode.GET_SUCCESS,num);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_GRAPH_CODE,null);
        }
    }

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
            //前期先判断如果ID不为空再验证
            if(!Validators.fieldBlank(loginDTO.getGraphCaptchaCodeId())){
                if(!uaaLoginService.verifyGraph(loginDTO.getGraphCaptchaCodeId(),loginDTO.getGraphCaptchaCode())){
                    return prepareReturnResult(ReturnCode.ERROR_GRAPH_CODE,null);

                }
            }
            UaaUser uaaUser = uaaUserService.findUserByName(loginDTO.getName());
            if(uaaUser==null){
                return prepareReturnResult(ReturnCode.ERROR_PASSWORD_NOT_CORRECT_CODE,null);
            }
            if(!uaaUser.getPassword().equals(loginDTO.getPassword())){
                return prepareReturnResult(ReturnCode.ERROR_PASSWORD_NOT_CORRECT_CODE,null);
            }
            Map<String,Object> map = uaaLoginService.login(uaaUser);
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,map);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_LOGIN,null);
        }
    }


}
