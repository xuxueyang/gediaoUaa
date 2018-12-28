package uaa.repository.app.log;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.app.log.AppLogDetail;

import java.util.List;

public interface AppLogDetailRepository extends JpaRepository<AppLogDetail,String> {
    List<AppLogDetail>  findAllByLogEachIdAndStatusNot(String logEachId,String status);
    AppLogDetail findOneByIdAndStatusNot(String id,String status);
}
