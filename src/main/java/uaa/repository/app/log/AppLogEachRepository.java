package uaa.repository.app.log;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.log.AppLogEach;

import java.util.List;

public interface AppLogEachRepository extends JpaRepository<AppLogEach,String> {
    List<AppLogEach> findAllByCreatedId(String userId);
}
