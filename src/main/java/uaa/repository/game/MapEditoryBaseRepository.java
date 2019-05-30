package uaa.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;
import uaa.domain.GameMapEditor.MapEditorBase;

import java.util.List;

public interface MapEditoryBaseRepository extends JpaRepository<MapEditorBase,String> {
    List<MapEditorBase> findAllByStatusNotAndMapEditorId(String status,String mapEditorId);
}
