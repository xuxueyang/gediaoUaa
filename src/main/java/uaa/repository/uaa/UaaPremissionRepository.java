package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uaa.domain.uaa.UaaPremission;

import java.util.List;


public interface UaaPremissionRepository extends JpaRepository<UaaPremission, String>,JpaSpecificationExecutor<UaaPremission> {
//    List<UaaPremission> findAllByUserId
}
