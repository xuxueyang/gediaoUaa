package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uaa.domain.uaa.UaaFile;
import uaa.domain.uaa.UaaTenantCode;

import java.util.List;


public interface UaaTenantCodeRepository extends JpaRepository<UaaTenantCode, String>,JpaSpecificationExecutor<UaaTenantCode> {
    UaaTenantCode findOneById(String id);
}
