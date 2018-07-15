package uaa.web.rest;

import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uaa.service.EmailService;

import javax.servlet.http.HttpServletResponse;

@Api(value = "发送邮件",description = "发送邮件")
@RestController
@RequestMapping("/api")
public class EmailResource extends BaseResource {

    @Autowired
    private EmailService emailService;
    @GetMapping("/email/test")
    @ResponseBody
    @ApiOperation(value = "测试发送邮件", httpMethod = "GET", response = ResponseEntity.class, notes = "测试发送邮件")
    public ResponseEntity<?> downloadFile(final String fileName, final String filePath, HttpServletResponse response) {
        try {
            emailService.sendEmail();
            return prepareReturnResult(ReturnCode.GET_SUCCESS, null);
        } catch (Exception e) {
//            LogUtil.error(SecurityUtils.getCurrentUserLogin(), "", "", Constants.MODULE_NAME, "uploadFile exception", e);
            return prepareReturnResult(ReturnCode.ERROR_SEND_EMAIL, null);
        }

    }
}
