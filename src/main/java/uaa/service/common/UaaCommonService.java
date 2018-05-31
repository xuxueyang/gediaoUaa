package uaa.service.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.service.ResultInfo;
import uaa.service.common.redis.RedisClient;

@Service
@Transactional
public class UaaCommonService {
    //通知类型
    protected enum NOTIFY_TYPE {
        PHONE, EMAIL;
    }
//    @Autowired
//    private RedisClient redisClient;
//
//    public ResultInfo graphCaptchaCodeVerificationWithLoginConfig(final String tenantCode,
//                                                                  final String spaceCode, final String graphCaptchaCodeId, final String graphCaptchaCode,
//                                                                  final LoginConfig loginConfig) {
//        //不支持图形验证码
//        if (!loginConfig.getOtherOptions().contains(OtherOptions.picture_captcha)) {
//            return ResultInfo.instance();
//        }
//
//        //校验：图形验证码ID不能为空
//        if (Validators.fieldBlank(graphCaptchaCodeId)) {
//            return ResultInfo.instance(ReturnCode.ERROR_FIELD_EMPTY);
//        }
//
//        //校验：图形验证码不能为空
//        if (Validators.fieldBlank(graphCaptchaCode)) {
//            return ResultInfo.instance(ReturnCode.ERROR_FIELD_EMPTY);
//        }
//
//        String returnCode = graphCaptchaCodeVerification(tenantCode, spaceCode, graphCaptchaCodeId,
//            graphCaptchaCode);
//        //校验：验证图形验证码
//        if (!StringUtils.equals(returnCode, ReturnCode.DEFAULT_SUCCESS)) {
//            return ResultInfo.instance(returnCode);
//        }
//        return ResultInfo.instance();
//    }
//
//    public void deleteGraphCaptchaCodeWithLoginConfig(final String tenantCode,
//                                                      final String spaceCode, final String graphCaptchaCodeId, final LoginConfig loginConfig) {
//        if (loginConfig.getOtherOptions().contains(OtherOptions.picture_captcha)) {
//            //移除图形验证码
//            RedisKeyDTO redisKeyDTO = new RedisKeyDTO(tenantCode, ContainerType.space.toString(),
//                spaceCode, ObjectType.graph_captcha.toString(), graphCaptchaCodeId);
//            cacheRPCClient.deleteKey(redisKeyDTO);
//        }
//    }
}
