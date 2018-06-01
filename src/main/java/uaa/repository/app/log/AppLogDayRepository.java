package uaa.repository.app.log;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.log.AppLogDay;

public interface AppLogDayRepository extends JpaRepository<AppLogDay,String> {
}
