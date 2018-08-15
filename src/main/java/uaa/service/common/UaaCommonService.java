package uaa.service.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.domain.uaa.UaaApiLog;
import uaa.domain.uaa.UaaTenantCode;
import uaa.repository.uaa.UaaApiLogRepository;
import uaa.repository.uaa.UaaTenantCodeRepository;
import uaa.service.ResultInfo;
import util.UUIDGenerator;

import java.time.ZonedDateTime;
//import uaa.service.common.redis.RedisClient;

@Service
@Transactional
public class UaaCommonService {
    //通知类型
    protected enum NOTIFY_TYPE {
        PHONE, EMAIL;
    }

    @Autowired
    private UaaApiLogRepository apiLogRepository;

    @Autowired
    private UaaTenantCodeRepository tenantCodeRepository;

    //记录API日志
    public void  log(String apiUrl,String message,String type,String  object,String projectType,String belongDate,String usrId){
        UaaApiLog log = new UaaApiLog();
        log.setApiUri(apiUrl);
        log.setMessage(message);
        log.setId(UUIDGenerator.getUUID());
        log.setType(type);
        log.setObject(object);
        log.setStatus(Constants.SAVE);
        log.setProjectType(projectType);
        //TODO 如果belongDate为空，那么不需要显示在前端
        log.setBelongDate(belongDate);
        apiLogRepository.save(log);
    }

    public UaaTenantCode findTenantById(String id){
        UaaTenantCode oneById = tenantCodeRepository.findOneById(id);
        if(oneById!=null&&Constants.TENANT_STATUS_DELETE.equals(oneById.getStatus())){
            return null;
        }
        return oneById;
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
