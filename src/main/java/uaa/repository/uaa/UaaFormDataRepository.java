package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uaa.domain.uaa.UaaFormData;

public interface UaaFormDataRepository extends JpaRepository<UaaFormData, String>,JpaSpecificationExecutor<UaaFormData> {
}
