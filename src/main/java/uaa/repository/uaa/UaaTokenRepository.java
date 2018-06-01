package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.uaa.UaaToken;

public interface UaaTokenRepository extends JpaRepository<UaaToken, String> {

}


