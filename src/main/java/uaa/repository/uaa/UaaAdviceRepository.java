package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.uaa.UaaAdvice;
import uaa.domain.uaa.UaaUser;

import java.util.List;

public interface UaaAdviceRepository extends JpaRepository<UaaAdvice, String > {

}
