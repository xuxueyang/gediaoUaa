package uaa.repository.app.log;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.log.AppLogPlanDay;
import uaa.domain.app.log.AppLogPlanDayItem;

/**
 * Created by UKi_Hi on 2018/6/4.
 */
public interface AppLogPlanDayItemRepository extends JpaRepository<AppLogPlanDayItem,String> {
}
