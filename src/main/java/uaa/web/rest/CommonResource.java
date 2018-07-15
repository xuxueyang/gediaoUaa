package uaa.web.rest;

import core.ReturnCode;
import core.ReturnResultDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CaptchaGenerator;


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
}
