package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uaa.domain.uaa.UaaFile;

import java.util.List;


public interface UaaFileRepository extends JpaRepository<UaaFile, String>,JpaSpecificationExecutor<UaaFile> {
    UaaFile findOneByMd5(String md5);
//    List<UaaFile> findAllByName(String name);
}
