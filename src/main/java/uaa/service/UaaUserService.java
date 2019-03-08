package uaa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.domain.uaa.UaaAdvice;
import uaa.domain.uaa.UaaUser;
import uaa.repository.uaa.UaaAdviceRepository;
import uaa.repository.uaa.UaaUserRepository;
import uaa.service.dto.CreateAdviceDTO;
import uaa.service.dto.CreateUaaUserDTO;
import uaa.service.dto.login.UserInfo;
import uaa.service.login.UaaLoginService;
import util.UUIDGenerator;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UaaUserService {

    @Autowired
    private UaaAdviceRepository adviceRepository;
    @Autowired
    private UaaUserRepository uaaUserRepository;
    @Autowired
    private UaaLoginService uaaLoginService;

    public void createAdvice(CreateAdviceDTO createAdviceDTO){
        UaaAdvice advice = new UaaAdvice();
        advice.setContent(createAdviceDTO.getContent());
        advice.setContract(createAdviceDTO.getContract());
        advice.setId(UUIDGenerator.getUUID());
        adviceRepository.save(advice);
    }
    public UaaUser findUserByName(String name){
        UaaUser oneByName = uaaUserRepository.findOneByName(name);
        if(oneByName==null||Constants.USER_STATUS_DELETE.equals(oneByName.getStatus()))
            return null;
        return oneByName;

    }

    public void createUser(CreateUaaUserDTO createUaaUserDTO) {
        UaaUser uaaUser = new UaaUser();
        uaaUser.setId(UUIDGenerator.getUUID());
        uaaUser.setStatus(Constants.USER_STATUS_SAVE);
        uaaUser.setName(createUaaUserDTO.getLoginName());
        uaaUser.setNickName(createUaaUserDTO.getNickName());
        uaaUser.setPassword(createUaaUserDTO.getPassword());
        uaaUser.setVerifyCode(createUaaUserDTO.getPassword());
        uaaUser.setProjectType(createUaaUserDTO.getProjectType());
        uaaUser.setTenantCode(createUaaUserDTO.getTenantCode());
        uaaUserRepository.save(uaaUser);
    }

    public UaaUser findUserById(String id){
        UaaUser one = uaaUserRepository.findOne(id);
        if(one==null||Constants.USER_STATUS_DELETE.equals(one.getStatus())){
            return null;
        }
        return one;
    }

    public List<UserInfo> getUserInfosByProjectType(String projectType) {
        List<UaaUser> users = uaaUserRepository.findAllByProjectType(projectType);
        List<UserInfo> infoList = new ArrayList<>();
        if(users!=null&&users.size()>0){

            for(UaaUser user:users){
                UserInfo userInfo = uaaLoginService.prepareForUserInfo(user);
                if(userInfo!=null)
                    infoList.add(userInfo);
            }
        }
        return infoList;
    }
}
