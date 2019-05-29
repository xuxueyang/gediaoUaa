package uaa.service.game;

import com.alibaba.fastjson.JSONObject;
import org.mortbay.util.ajax.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.domain.GameMapEditor.MapEachBase;
import uaa.domain.GameMapEditor.MapType;
import uaa.repository.game.MapEachBaseRepository;
import uaa.web.rest.game.dto.MapBaseCreateDTO;
import util.UUIDGenerator;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
public class GameMapEditorService {
    @Autowired
    private MapEachBaseRepository mapEachBaseRepository;

    public List<MapType.Type> getAllMapTypes() {
        return MapType.getAllMapType();
    }

    public List<MapEachBase> getAllMapBases() {
        return mapEachBaseRepository.findAllByStatusNot(Constants.DELETE);
    }
    public void saveMapBase(MapBaseCreateDTO dto) {
        String string = JSON.toString(dto.getObject());
        MapEachBase base = new MapEachBase();
        base.setId(UUIDGenerator.getUUID());
        BeanUtils.copyProperties(dto,base);
        base.setObject(string);
        base.setCreatedDate(ZonedDateTime.now());
        base.setUpdatedDate(ZonedDateTime.now());
        mapEachBaseRepository.save(base);
    }

}
