package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.uaa.UaaLogMessage;

import java.util.List;

public interface UaaLogMessageRepository extends JpaRepository<UaaLogMessage, String> {
    List<UaaLogMessage> findAllByProjectType(String projectType);
    UaaLogMessage findOneByProjectTypeAndId(String projectType,String id);
    List<UaaLogMessage> findAllByProjectTypeAndCreatedId(String projectType,String createdID);
}