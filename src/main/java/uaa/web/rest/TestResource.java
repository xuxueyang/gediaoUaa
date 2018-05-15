package uaa.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uaa.service.dto.UserDTO;
import uaa.web.rest.util.PaginationUtil;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TestResource {

    @ApiOperation(value = "普通登录", notes = "必填：用户名、密码； 选填：graphCaptchaCodeId,graphCaptchaCode", response = String.class)
    @GetMapping("/xxy-authenticate")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "创建成功")})
    @Timed
    public String authorize() {
        return "测试";
    }


}
