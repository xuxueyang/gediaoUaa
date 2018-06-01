package uaa.repository.app.log;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.log.AppLogEach;

public interface AppLogEachRepository extends JpaRepository<AppLogEach,String> {
}
