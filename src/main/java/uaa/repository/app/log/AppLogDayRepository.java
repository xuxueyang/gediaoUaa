package uaa.repository.app.log;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.log.AppLogDay;

import java.util.List;

public interface AppLogDayRepository extends JpaRepository<AppLogDay,String> {
    List<AppLogDay> findAllByBelongDateAndStatus(String belongDate,String status);
}
