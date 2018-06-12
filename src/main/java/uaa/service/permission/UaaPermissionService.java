package uaa.service.permission;

import core.ReturnCode;
import org.springframework.beans.factory.annotation.Autowired;
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

    //验证用户对于某个API有没有操作的权限
    //TODO 到时候前端得获取到所有API，或者专门写一个配置API权限的页面
    public boolean verifyUserOperResource(UaaUser uaaUser,String api){

        return true;
    }

    public UaaError verifyOperation(UaaBasePremissionDTO premissionDTO,String apiResource){
        //走token或验证码，来找出user
        UaaError uaaError = new UaaError();
        UaaUser uaa = null;
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
}
