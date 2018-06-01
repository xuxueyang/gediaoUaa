package uaa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.domain.uaa.UaaUser;
import uaa.repository.uaa.UaaUserRepository;

@Service
@Transactional
public class UaaUserService {

    @Autowired
    private UaaUserRepository uaaUserRepository;

    public UaaUser findUserByName(String name){
        return uaaUserRepository.findOneByName(name);
    }

}
