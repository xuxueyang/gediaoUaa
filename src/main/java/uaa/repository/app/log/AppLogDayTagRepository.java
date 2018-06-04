package uaa.repository.app.log;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.log.AppLogDayTag;

public interface AppLogDayTagRepository extends JpaRepository<AppLogDayTag,String> {
}
