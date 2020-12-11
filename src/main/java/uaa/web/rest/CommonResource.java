package uaa.web.rest;

import com.codahale.metrics.annotation.Timed;
import uaa.config.ReturnCode;
import core.ReturnResultDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import util.CaptchaGenerator;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * 一些基础API
 */
@RestController
@RequestMapping("/api")
public class CommonResource extends BaseResource {

    @GetMapping("/captcha/graph")
//    @LogAnnotation(api = "/api/captcha/graph/{id}", httpMethod = "GET")
    @ApiOperation(value = "图形验证码生成", notes = "图形验证码生成, 需要uuid")
    public ResponseEntity<ReturnResultDTO<String>> generateCaptcha(@ApiParam(value = "验证码编号", required = true) @PathVariable("id") String id) {
        try {
            //生成图形验证码
            String captchaCode = CaptchaGenerator.generateCaptchaCode(4);
//            LogUtil.debug(Constants.CUBE_DEFAULT_USER_ID, Constants.CUBE_DEFAULT_TENANT_CODE, Constants.MODULE_NAME, "captchaCode", captchaCode);
            //Base64编码
            String captchaCode_base64 = CaptchaGenerator.outputImageBase64(300, 80, captchaCode);
            //保存图形码
//            RedisObjectDTO redisObjectDTO = new RedisObjectDTO(tenantCode, ContainerType.space.toString(), spaceCode, ObjectType.graph_captcha.toString(), id);
//            redisObjectDTO.setValue(captchaCode);
//            redisObjectDTO.setTimeout(86400L);
//            cubeRedisClient.setObject(redisObjectDTO);
            return prepareReturnResult(ReturnCode.DEFAULT_SUCCESS, captchaCode_base64);
        } catch (Exception e) {
//            LogUtil.error(Constants.CUBE_DEFAULT_USER_ID, Constants.CUBE_DEFAULT_TENANT_CODE, Constants.MODULE_NAME, "", "errMsg", e);
            return prepareReturnResult(ReturnCode.ERROR_GRAPH_CODE, null);
        }
    }
//    public static void  main(String[]args){
//        //生成图形验证码
//        String captchaCode = CaptchaGenerator.generateCaptchaCode(4);
////            LogUtil.debug(Constants.CUBE_DEFAULT_USER_ID, Constants.CUBE_DEFAULT_TENANT_CODE, Constants.MODULE_NAME, "captchaCode", captchaCode);
//        //Base64编码
//        try {
//            String captchaCode_base64 = CaptchaGenerator.outputImageBase64(300, 80, captchaCode);
//            CaptchaGenerator.GenerateImage(captchaCode_base64, "D:\\wangyc.jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    @Timed
    @GetMapping("/sys/wx/message")
    @ApiOperation(value = "微信开发者接入",httpMethod = "GET",response = ReturnResultDTO.class)
    @ApiResponses({@io.swagger.annotations.ApiResponse(code = 200,message = "成功")})
    public void sysWxMessage(@RequestParam("signature")String signature,
                             @RequestParam("timestamp")String timestamp,
                             @RequestParam("nonce")String nonce,
                             @RequestParam("echostr") String echostr,HttpServletResponse response){
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
    //        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
    //            out.print(echostr);
    //        }
        out.close();
        out = null;
    //        return prepareReturnResult(ReturnCode.GET_SUCCESS,echostr);
    }
}
