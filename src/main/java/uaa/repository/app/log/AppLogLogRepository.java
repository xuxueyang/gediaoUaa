package uaa.repository.app.log;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.log.AppLogLog;

import java.util.List;


public interface AppLogLogRepository extends JpaRepository<AppLogLog,String> {
//    List<AppLogLog> findAllByBelongDateAndStatus(String belongDate, String status);
    List<AppLogLog> findOneByBelongDateAndStatusAndCreatedIdAndTypeOrderByUpdatedDateDesc(String belongDate, String status,String userId,String type);

}

