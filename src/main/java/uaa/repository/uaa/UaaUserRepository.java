package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.uaa.UaaUser;

import java.util.List;

public interface UaaUserRepository extends JpaRepository<UaaUser, String > {

    UaaUser findOneByName(String name);
    List<UaaUser> findAllByProjectType(String projectType);
}
