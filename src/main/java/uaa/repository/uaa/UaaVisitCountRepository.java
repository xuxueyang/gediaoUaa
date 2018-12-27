package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uaa.domain.uaa.UaaVisitRecord;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

public interface UaaVisitCountRepository extends JpaRepository<UaaVisitRecord, String > {
//    //计数的
//    @Query(value="SELECT count(*) FROM uaa_visit_record")
//    int countAll();
    List<UaaVisitRecord> findAllByCreatedDateAfter(Instant time);
    UaaVisitRecord findByIpOrderByCreatedDate(String ip);
}
