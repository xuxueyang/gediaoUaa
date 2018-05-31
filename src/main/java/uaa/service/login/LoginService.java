package uaa.service.login;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.service.dto.login.LoginDTO;

@Service
@Transactional
public class LoginService {

    public String login(LoginDTO loginDTO) {
        return "";
    }
}
