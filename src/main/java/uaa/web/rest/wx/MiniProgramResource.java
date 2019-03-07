package uaa.web.rest.wx;


import core.ReturnCode;
import core.ReturnResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uaa.security.SecurityUtils;
import uaa.service.MiniprogramService;
import uaa.service.dto.WechateDecryptInDTO;
import uaa.web.rest.BaseResource;

import java.util.Map;

@Api(value = "小程序注册登录", description = "小程序注册登录")
@RestController
@RequestMapping("/api")
public class MiniProgramResource extends BaseResource {

    @Autowired
    private MiniprogramService miniprogramService;


    @ApiOperation(value = "获取unionId,openId等信息", response = ReturnResultDTO.class, notes = "returnCode:2004,5001,6100(微信数据获取失败)，4000")
    @PostMapping("/mini/{code}")
//    @LogAnnotation(api = "/api/mini/{code}", httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    public ResponseEntity getWechatInfo(@PathVariable String code, @RequestBody WechateDecryptInDTO decryptInDTO) {

        try {
            if (StringUtils.isBlank(code) || StringUtils.isBlank(decryptInDTO.getFromSource())) {
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY, null);
            }
            Map<String, Object> result = miniprogramService.getWechatInfo(code, decryptInDTO);
            String resultCode = (String) result.get("returnCode");
            Object data = result.get("data");
            if ("6100".equals(resultCode)) {
//                LogUtil.error(SecurityUtils.getCurrentUserIdStr(), SecurityUtils.getCurrentTenantCode(),
//                    "/api/mini/{code}", "获取微信数据异常", String.valueOf(data), "");
                return prepareReturnResult("6100", data);
            }

            return prepareReturnResult(ReturnCode.GET_SUCCESS, data);

        } catch (Exception e) {
//            LogUtil.error("", "", "/api/mini/{code}", "失败", "error", e);
            return prepareReturnResult(ReturnCode.ERROR_QUERY, null);
        }
    }
}
