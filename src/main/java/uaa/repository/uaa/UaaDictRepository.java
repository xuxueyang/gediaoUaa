package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uaa.domain.uaa.UaaDict;

import java.util.List;

/**
 * Created by UKi_Hi on 2018/7/15.
 */
public interface UaaDictRepository extends JpaRepository<UaaDict, String>,JpaSpecificationExecutor<UaaDict> {
    List<UaaDict> findAllByType(String type);
    UaaDict findOneByCode(String code);

}
