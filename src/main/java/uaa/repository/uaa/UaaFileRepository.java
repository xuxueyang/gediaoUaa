package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uaa.domain.uaa.UaaFile;

import java.util.List;


public interface UaaFileRepository extends JpaRepository<UaaFile, String>,JpaSpecificationExecutor<UaaFile> {
    List<UaaFile> findAllByMd5AndStatusNot(String md5,String status);
//    List<UaaFile> findAllByName(String name);
    List<UaaFile> findAllByStatusNotAndNameLike(String status,String name);
}
