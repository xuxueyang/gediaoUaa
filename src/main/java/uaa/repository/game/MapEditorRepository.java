package uaa.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.GameMapEditor.MapEditor;

import java.util.List;

public interface MapEditorRepository extends JpaRepository<MapEditor,String> {
    List<MapEditor> findAllByStatusNot(String status);
}
