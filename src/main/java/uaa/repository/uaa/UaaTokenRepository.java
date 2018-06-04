package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.uaa.UaaToken;

import java.util.List;

public interface UaaTokenRepository extends JpaRepository<UaaToken, String> {
    UaaToken findOneByAccesstoken(String accesstoken);

    List<UaaToken> findAllByCreatedid(String userId);
}


