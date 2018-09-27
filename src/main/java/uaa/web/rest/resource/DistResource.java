package uaa.web.rest.resource;

import org.springframework.beans.factory.annotation.Autowired;
import uaa.domain.UaaError;
import uaa.domain.uaa.UaaUser;
import uaa.service.dto.UaaBasePremissionDTO;
import uaa.service.dto.dict.UpdateDictValeDTO;
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

    @GetMapping("/code/{code}")
    @ResponseBody
    @ApiOperation(value = "根据code获取到code的值", httpMethod = "GET", response = ResponseEntity.class, notes = "根据code获取到code的值")
    public ResponseEntity<?> getCodeValue(@PathVariable("code") String code,
                                          @RequestParam(name="content",required=false) String content) {
        try{
            if(Validators.fieldBlank(code)){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }

            return prepareReturnResult(ReturnCode.GET_SUCCESS,dictService.getValueByCode(code,content));
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
    @DeleteMapping("/code/{id}")
    @ResponseBody
    @ApiOperation(value = "根据code删除到code的值", httpMethod = "GET", response = ResponseEntity.class, notes = "根据code删除到code的值")
    public ResponseEntity<?> getCodeValue(@PathVariable("id") String id,
                                          @RequestParam(name="token",required=true) String token,
                                          @RequestParam(name="userId",required=true) String userId) {
        try{
            if(Validators.fieldBlank(token)){
                return prepareReturnResult(ReturnCode.ERROR_HAVE_NO_PERMISSION_OPERATION,null);
            }
            if(Validators.fieldBlank(id)){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            UaaError post = uaaPermissionService.verifyLogin(userId, token, "/api/dist/code/{id}", "DELETE");
            if(post.hasError()){
                return prepareReturnResult(post.getFirstError(),null);
            }
            dictService.deleteValeById(id);
            return prepareReturnResult(ReturnCode.DELETE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
    @PostMapping("/code/{code}")
    @ResponseBody
    @ApiOperation(value = "改变code下的值", httpMethod = "GET", response = ResponseEntity.class, notes = "改变code下的值")
    public ResponseEntity<?> updateCodeValue(@PathVariable("code") String code,
                                          @RequestBody UpdateDictValeDTO updateDictValeDTO) {
        try{
            //需要token
            if(Validators.fieldBlank(updateDictValeDTO.getToken())){
                return prepareReturnResult(ReturnCode.ERROR_HAVE_NO_PERMISSION_OPERATION,null);
            }
            if(Validators.fieldBlank(code)){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            UaaError error = uaaPermissionService.verifyOperation(updateDictValeDTO, "/api/code/{code}", "PUT");
            if(error.hasError()){
                return prepareReturnResult(error.getFirstError(),null);
            }
            UaaUser value = (UaaUser)error.getValue();
            dictService.updateValueByCode(code,updateDictValeDTO.getValue(),updateDictValeDTO.getId(),value.getId());
            return prepareReturnResult(ReturnCode.GET_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
    @PutMapping("/code/{code}")
    @ResponseBody
    @ApiOperation(value = "添加code下的值", httpMethod = "GET", response = ResponseEntity.class, notes = "添加code下的值")
    public ResponseEntity<?> getCodeValue(@PathVariable("code") String code,
                                          @RequestBody UpdateDictValeDTO updateDictValeDTO) {
        try{
            //需要token
            if(Validators.fieldBlank(updateDictValeDTO.getToken())){
                return prepareReturnResult(ReturnCode.ERROR_HAVE_NO_PERMISSION_OPERATION,null);
            }
            if(Validators.fieldBlank(code)
                ||Validators.fieldBlank(updateDictValeDTO.getValue())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            UaaError error = uaaPermissionService.verifyOperation(updateDictValeDTO, "/api/code/{code}", "PUT");
            if(error.hasError()){
                return prepareReturnResult(error.getFirstError(),null);
            }
            UaaUser value = (UaaUser)error.getValue();
            dictService.addValueByCode(code,updateDictValeDTO.getValue(),value.getId());
            return prepareReturnResult(ReturnCode.GET_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
}
