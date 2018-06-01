package uaa.repository.app.log;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.log.AppLogDetail;

public interface AppLogDetailRepository extends JpaRepository<AppLogDetail,String> {

}
