package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uaa.domain.uaa.UaaApiLog;

import java.util.List;

/**
 * Created by UKi_Hi on 2018/7/15.
 */
public interface UaaApiLogRepository extends JpaRepository<UaaApiLog, String>,JpaSpecificationExecutor<UaaApiLog> {
//    List<UaaDict> findAllByType(String type);
}
