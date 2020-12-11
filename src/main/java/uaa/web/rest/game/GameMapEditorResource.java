package uaa.web.rest.game;


import uaa.config.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uaa.service.game.GameMapEditorService;
import uaa.web.rest.BaseResource;
import uaa.web.rest.game.dto.MapBaseCreateDTO;
import uaa.web.rest.game.dto.MapEditorBaseDTO;
import uaa.web.rest.game.dto.MapEditorCreateDTO;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/game/map/editor")
@Api(value = "地图编辑器", description = "地图编辑器")
public class GameMapEditorResource extends BaseResource {
    @Autowired
    private GameMapEditorService gameMapEditorService;

    @ResponseBody
    @GetMapping("/getAllMapTypes")
    @ApiOperation(value = "获取所有图片类型", httpMethod = "GET", response = ResponseEntity.class, notes = "获取所有图片类型")
    public ResponseEntity<?> getAllMapTypes(){
        try {
            return prepareReturnResult(ReturnCode.GET_SUCCESS,gameMapEditorService.getAllMapTypes());
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getAllMapBases")
    @ApiOperation(value = "获取所有上传的地图图片", httpMethod = "GET", response = ResponseEntity.class, notes = "获取所有上传的地图图片")
    public ResponseEntity<?> getAllMapBases(){
        try {
            return prepareReturnResult(ReturnCode.GET_SUCCESS,gameMapEditorService.getAllMapBases());
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getAllMapEditors")
    @ApiOperation(value = "获取所有上传的地图图片", httpMethod = "GET", response = ResponseEntity.class, notes = "获取所有上传的地图图片")
    public ResponseEntity<?> getAllMapEditors(){
        try {
            return prepareReturnResult(ReturnCode.GET_SUCCESS,gameMapEditorService.getAllMapEditors());
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/getMapEditor/{id}")
    @ApiOperation(value = "获取所有上传的地图图片", httpMethod = "GET", response = ResponseEntity.class, notes = "获取所有上传的地图图片")
    public ResponseEntity<?> getMapEditor(@PathVariable(value = "id") String id){
        try {
            return prepareReturnResult(ReturnCode.GET_SUCCESS,gameMapEditorService.getMapEditor(id));
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/saveMapBase")
    @ApiOperation(value = "保存图片切片", httpMethod = "GET", response = ResponseEntity.class, notes = "保存图片切片")
    public ResponseEntity<?> saveMapBase(@RequestBody MapBaseCreateDTO dto){
        try {
            gameMapEditorService.saveMapBase(dto);
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/saveMapEditor")
    @ApiOperation(value = "保存图片编辑器", httpMethod = "GET", response = ResponseEntity.class, notes = "保存图片编辑器")
    public ResponseEntity<?> saveMapBase(@RequestBody MapEditorCreateDTO dto){
        try {
            gameMapEditorService.saveMapEditor(dto);
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,e.getMessage());
        }
    }
}
