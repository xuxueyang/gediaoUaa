package uaa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.domain.uaa.UaaUser;
import uaa.repository.uaa.UaaUserRepository;
import uaa.service.dto.CreateUaaUserDTO;
import util.UUIDGenerator;

@Service
@Transactional
public class UaaUserService {

    @Autowired
    private UaaUserRepository uaaUserRepository;

    public UaaUser findUserByName(String name){
        UaaUser oneByName = uaaUserRepository.findOneByName(name);
        if(oneByName==null||Constants.APP_LOG_STATUS_DELETE.equals(oneByName.getStatus()))
            return null;
        return oneByName;

    }

    public void createUser(CreateUaaUserDTO createUaaUserDTO) {
        UaaUser uaaUser = new UaaUser();
        uaaUser.setId(UUIDGenerator.getUUID());
        uaaUser.setStatus(Constants.APP_LOG_STATUS_SAVE);
        uaaUser.setName(createUaaUserDTO.getLoginName());
        uaaUser.setPassword(createUaaUserDTO.getPassword());
        uaaUserRepository.save(uaaUser);
    }
}
