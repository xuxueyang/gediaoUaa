package uaa.service.permission;

import core.ReturnCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.domain.UaaError;
import uaa.domain.uaa.UaaToken;
import uaa.domain.uaa.UaaUser;
import uaa.service.UaaUserService;
import uaa.service.dto.UaaBasePremissionDTO;
import uaa.service.login.UaaLoginService;
import util.Validators;

import java.util.Map;

@Service
@Transactional
public class UaaPermissionService {
    @Autowired
    private UaaUserService uaaUserService;
    @Autowired
    private UaaLoginService uaaLoginService;

    public UaaError verifyLogin(String userId,String token,String apiResource,String methodType){
        //TODO 之后要加上加密解密才行，不然token很容易被捕获（token+时间+随机的盐），userId+随机的盐，加密，解密（相应的时间和盐会做短暂的记录，已经用过的不能再用！）
        UaaError uaaError = new UaaError();
        //走token找出userinfo，验证userId是不是一直与存在，存在则返回UaaUser
        if(Validators.fieldBlank(token)||Validators.fieldBlank(userId)){
            uaaError.addError(ReturnCode.ERROR_HAVE_NO_PERMISSION_OPERATION);
        }else{
            UaaToken userByToken = uaaLoginService.getUserByToken(token);
            if(userByToken==null){
                uaaError.addError(ReturnCode.ERROR_USER_HAS_LOGOUT);
            }else{
                if(userByToken.getCreatedid().equals(userId)){
                    UaaUser userById = uaaUserService.findUserById(userId);
                    uaaError.setValue(userById);
                }else{
                    //token但是对不上user，说明瞎写的
                    uaaError.addError(ReturnCode.ERROR_LOGIN);
                }
            }
        }
        return uaaError;
    }

    /**
     * 因为一些情况没法登陆只能走code验证获取数据
     * @param userId
     * @param code
     * @param apiResource
     * @param methodType
     * @return
     */
    public UaaError verifyByCode(String userId,String code,String apiResource,String methodType){
        //TODO 之后要加上加密解密才行，不然token很容易被捕获（token+时间+随机的盐），userId+随机的盐，加密，解密（相应的时间和盐会做短暂的记录，已经用过的不能再用！）
        UaaError uaaError = new UaaError();
        //走token找出userinfo，验证userId是不是一直与存在，存在则返回UaaUser
        if(Validators.fieldBlank(code)||Validators.fieldBlank(userId)){
            uaaError.addError(ReturnCode.ERROR_FIELD_EMPTY);
        }else{
            UaaUser user = uaaUserService.findUserById(userId);
            if(user.getVerifyCode()!=null&&!user.getVerifyCode().equals(code)){
                //code验证失败
                uaaError.addError(ReturnCode.ERROR_HAVE_NO_PERMISSION_OPERATION);
            }else{
                uaaError.setValue(user);
            }
        }
        return uaaError;
    }
    public UaaError verifyOperation(UaaBasePremissionDTO premissionDTO,String apiResource,String methodType){
        //走token或验证码，来找出user
        UaaError uaaError = new UaaError();
        UaaUser uaa = null;
        if(Validators.fieldBlank(premissionDTO.getProjectType()))
        {
            uaaError.addError(ReturnCode.ERROR_FIELD_EMPTY);
            return uaaError;
        }
        if(Validators.fieldBlank(premissionDTO.getToken())){
            uaa = uaaUserService.findUserByName(premissionDTO.getLoginName());
            //有的话才能添加
            if(uaa==null){
                uaaError.addError(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE);
                return uaaError;
            }
            if(!premissionDTO.getVerifyCode().equals(uaa.getVerifyCode()))
            {
                uaaError.addError(ReturnCode.ERROR_NO_PERMISSIONS_UPDATE);
                return uaaError;
            }
        }else{
            //走token
            UaaToken userByToken = uaaLoginService.getUserByToken(premissionDTO.getToken());
            if(userByToken==null){
                uaaError.addError(ReturnCode.ERROR_USER_HAS_LOGOUT);
                return uaaError;
            }
            uaa = uaaUserService.findUserById(userByToken.getCreatedid());
            if(uaa==null){
                uaaError.addError(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE);
                return uaaError;
            }
        }
        //再验证这个账户对于API有没有操作的权限
        boolean op = this.verifyUserOperResource(uaa,apiResource);
        if(!op){
            uaaError.addError(ReturnCode.ERROR_NO_PERMISSIONS_UPDATE);
            return uaaError;
        }
        uaaError.setValue(uaa);
        return uaaError;
    }
    //验证用户对于某个API有没有操作的权限
    //TODO 到时候前端得获取到所有API，或者专门写一个配置API权限的页面
    public boolean verifyUserOperResource(UaaUser uaaUser,String api){

        return true;
    }

}
