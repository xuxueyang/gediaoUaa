package uaa.web.rest.resource;

import org.springframework.beans.factory.annotation.Autowired;
import uaa.service.resource.DictService;
import uaa.web.rest.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import util.Validators;

/**
 * Created by UKi_Hi on 2018/7/13.
 */
@Api(value = "数据字典",description = "数据字典")
@RestController
@RequestMapping("/api/dist")
public class DistResource extends BaseResource {

    @Autowired
    private DictService dictService;

    @GetMapping("/codes")
    @ResponseBody
    @ApiOperation(value = "获取到所有的字典值", httpMethod = "GET", response = ResponseEntity.class, notes = "获取到所有的字典值")
    public ResponseEntity<?> getAllCode() {
       try{
           return prepareReturnResult(ReturnCode.GET_SUCCESS,null);
       }catch (Exception e){
           return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
       }
    }

    @GetMapping("/codes/{type}")
    @ResponseBody
    @ApiOperation(value = "根据type值得到字典下属", httpMethod = "GET", response = ResponseEntity.class, notes = "根据type值得到字典下属")
    public ResponseEntity<?> getAllCodeByType(@PathVariable("type") String type) {
        try{
            if(Validators.fieldBlank(type)){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }

            return prepareReturnResult(ReturnCode.GET_SUCCESS,dictService.getCodeByType(type));
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }

}
