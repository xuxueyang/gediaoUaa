package uaa.repository.app.log;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.log.AppLogEachTag;
import uaa.domain.app.log.AppLogPlanDay;

/**
 * Created by UKi_Hi on 2018/6/4.
 */
public interface AppLogPlanDayRepository extends JpaRepository<AppLogPlanDay,String> {
}
