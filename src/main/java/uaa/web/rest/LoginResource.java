package uaa.web.rest;

import core.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import util.UUIDGenerator;


@Api(value = "登录系统",description = "登录系统")
@RestController
@RequestMapping("/api")
public class LoginResource extends BaseResource{
    @GetMapping("/login")
    @ResponseBody
    @ApiOperation(value = "文件下载", httpMethod = "GET", response = ResponseEntity.class, notes = "文件下载")
    public ResponseEntity<?> downloadFile() {
        //登录了
        String token = UUIDGenerator.getUUID();
        return prepareReturnResult(ReturnCode.GET_SUCCESS,"登录成功,token: "+token);
    }
}
