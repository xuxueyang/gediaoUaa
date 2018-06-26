package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.uaa.UaaLogMessage;

import java.util.List;

public interface UaaLogMessageRepository extends JpaRepository<UaaLogMessage, String> {
    List<UaaLogMessage> findAllByProjectTypeOrderByUpdatedId(String projectType);
    UaaLogMessage findOneByProjectTypeAndIdOrderByUpdatedId(String projectType,String id);
    List<UaaLogMessage> findAllByProjectTypeAndCreatedIdOrderByUpdatedId(String projectType,String createdID);
}
