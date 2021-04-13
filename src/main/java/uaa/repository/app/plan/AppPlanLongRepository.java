package uaa.repository.app.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uaa.domain.app.plan.AppPlanEach;
import uaa.domain.app.plan.AppPlanLong;

/**
 * @author xuxy
 * @date 2021/3/26 上午9:38
 */
public interface AppPlanLongRepository extends JpaRepository<AppPlanLong,Integer>, JpaSpecificationExecutor<AppPlanLong> {
}
