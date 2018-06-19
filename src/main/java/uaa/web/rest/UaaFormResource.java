package uaa.web.rest;

import core.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uaa.service.UaaFormDataService;
import uaa.service.dto.form.CreateDataDTO;
import util.Validators;

@Api(value = "UAA表单管理",description = "UAA表单管理")
@RestController
@RequestMapping("/api")
public class UaaFormResource extends BaseResource {
    @Autowired
    private UaaFormDataService uaaFormDataService;
    //创建任务表单，动态表单，用MongoDB存储
    //TODO 暂时的单例申请
    @PostMapping("/form/data")
    @ApiOperation(value = "单例申请", httpMethod = "POST", response = ResponseEntity.class, notes = "单例申请")
    public ResponseEntity postData(@RequestBody CreateDataDTO createDataDTO){
        try {
            if(Validators.fieldBlank(createDataDTO.getData())||
                Validators.fieldBlank(createDataDTO.getFormType())||
                Validators.fieldBlank(createDataDTO.getProjectType())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            uaaFormDataService.saveFormData(createDataDTO);
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }
}
