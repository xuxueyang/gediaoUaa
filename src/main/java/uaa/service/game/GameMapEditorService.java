package uaa.service.game;

import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.domain.GameMapEditor.MapEachBase;
import uaa.domain.GameMapEditor.MapEditor;
import uaa.domain.GameMapEditor.MapEditorBase;
import uaa.domain.GameMapEditor.MapType;
import uaa.repository.game.MapEachBaseRepository;
import uaa.repository.game.MapEditorBaseRepository;
import uaa.repository.game.MapEditorRepository;
import uaa.web.rest.game.dto.MapBaseCreateDTO;
import uaa.web.rest.game.dto.MapEditorBaseDTO;
import uaa.web.rest.game.dto.MapEditorCreateDTO;
import util.UUIDGenerator;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
public class GameMapEditorService {
    @Autowired
    private MapEachBaseRepository mapEachBaseRepository;

    @Autowired
    private MapEditorBaseRepository editoryBaseRepository;

    @Autowired
    private MapEditorRepository editoryRepository;



    public List<MapType.Type> getAllMapTypes() {
        return MapType.getAllMapType();
    }

    public List<MapEachBase> getAllMapBases() {
        return mapEachBaseRepository.findAllByStatusNot(Constants.DELETE);
    }
    public List<MapEditor> getAllMapEditors() {
        return editoryRepository.findAllByStatusNot(Constants.DELETE);
    }
    public List<MapEditorBase> getMapEditor (String id) {
        return editoryBaseRepository.findAllByStatusNotAndMapEditorId(Constants.DELETE,id);
    }

    @Transactional
    public void saveMapBase(MapBaseCreateDTO dto) {
        String string = JSON.toString(dto.getObject());
        MapEachBase base = new MapEachBase();
        base.setId(UUIDGenerator.getUUID());
//        BeanUtils.copyProperties(dto,base);
        base.setTitle(dto.getTitle());
        base.setMapTypeId(dto.getMapTypeId());
        base.setDescription(dto.getDescription());
        base.setImagePath(dto.getImagePath());
        base.setStatus(Constants.SAVE);
        base.setObject(string);
        base.setCreatedDate(ZonedDateTime.now());
        base.setUpdatedDate(ZonedDateTime.now());
        mapEachBaseRepository.save(base);
    }

    @Transactional
    public void saveMapEditor(MapEditorCreateDTO dtos) {
        String uuid = UUIDGenerator.getUUID();
        //保存编辑器

        for(List<MapEditorBaseDTO> dtoList: dtos.getData()){
            for(MapEditorBaseDTO dto:dtoList){
                String string = JSON.toString(dto.getObject());
                MapEditorBase base = new MapEditorBase();
                base.setId(UUIDGenerator.getUUID());
//        BeanUtils.copyProperties(dto,base);
                base.setTitle(dto.getTitle());
                base.setMapTypeId(dto.getMapTypeId());
                base.setDescription(dto.getDescription());
                base.setImagePath(dto.getImagePath());
                base.setStatus(Constants.SAVE);
                base.setObject(string);
                base.setRow(dto.getRow());
                base.setCol(dto.getCol());
                base.setCreatedDate(ZonedDateTime.now());
                base.setUpdatedDate(ZonedDateTime.now());

                base.setMapEditorId(uuid);

                editoryBaseRepository.save(base);
            }
        }
        MapEditor editor = new MapEditor();
        editor.setId(uuid);

        editor.setStatus(Constants.SAVE);
        editor.setCreatedDate(ZonedDateTime.now());
        editor.setUpdatedDate(ZonedDateTime.now());

        String string = JSON.toString(dtos.getObject());
        editor.setTitle(dtos.getTitle());
        editor.setDescription(dtos.getDescription());
        editor.setImagePath(dtos.getImagePath());
        editor.setObject(string);


        editoryRepository.save(editor);

    }
}
