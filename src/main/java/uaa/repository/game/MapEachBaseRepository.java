package uaa.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.GameMapEditor.MapEachBase;

import java.util.List;

public interface MapEachBaseRepository extends JpaRepository<MapEachBase,String> {
    List<MapEachBase> findAllByStatusNot(String status);
}
