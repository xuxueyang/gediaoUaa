package uaa.repository.uaa;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.uaa.UaaGraph;


public interface UaaGraphRepository extends JpaRepository<UaaGraph, String> {
    UaaGraph findById(String id);
    int deleteById(String id);

}
