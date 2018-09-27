package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uaa.domain.uaa.UaaDict;
import uaa.domain.uaa.UaaDictValue;

import java.util.List;

/**
 * Created by UKi_Hi on 2018/9/2.
 */
public interface UaaDictValueRepository extends JpaRepository<UaaDictValue, String>,JpaSpecificationExecutor<UaaDictValue> {
    List<UaaDictValue> findAllByDictId(String dictId);
}
