package uaa.repository.app.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uaa.domain.app.log.AppLogEach;

import java.util.List;

public interface AppLogEachRepository extends JpaRepository<AppLogEach,String>,JpaSpecificationExecutor<AppLogEach> {
    List<AppLogEach> findAllByCreatedIdAndStatusNotOrderByUpdatedDateDesc(String userId,String status);

    List<AppLogEach> findAllByCreatedIdAndStatusNotOrderByBelongDate(String userId,String status);
}
