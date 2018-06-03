package uaa.web.rest.app.log;


import core.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uaa.config.Constants;
import uaa.domain.app.log.AppLogDetail;
import uaa.domain.app.log.AppLogEach;
import uaa.domain.uaa.UaaToken;
import uaa.service.app.log.AppLogDetailService;
import uaa.service.app.log.AppLogEachService;
import uaa.service.app.log.AppLogSingleService;
import uaa.service.dto.app.log.*;
import uaa.service.login.LoginService;
import util.Validators;

import java.util.List;

@RestController
@RequestMapping("/api/app/log")
public class AppLogResource extends BaseResource{
    @Autowired
    private AppLogEachService appLogEachService;
    @Autowired
    private AppLogDetailService appLogDetailService;
    @Autowired
    private AppLogSingleService appLogSingleService;
    @Autowired
    private LoginService loginService;
    //TODO day 需要提供的接口：get、update、all-get、delete
    //TODO 可以按照天来获取全部的数据结构（day、detail、之类的），返回一个树接口


    //*******************************************Day等单例的API**********************************


    //*******************************************Detail的API**********************************
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "获取相应的detail信息", httpMethod = "GET", response = ResponseEntity.class, notes = "获取相应的detail信息")
    public ResponseEntity getDetailInfo(@PathVariable("id") String id){
        try {
            AppLogDetailDTO detailInfoById = appLogDetailService.getDetailInfoById(id);
            return prepareReturnResult(ReturnCode.GET_SUCCESS,detailInfoById);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
    @PostMapping("/detail")
    @ApiOperation(value = "更新相应的detail信息", httpMethod = "POST", response = ResponseEntity.class, notes = "更新相应的detail信息")
    public ResponseEntity updateDetailInfo(@RequestBody UpdateLogDetailDTO updateLogDetailDTO){
        try {
            if(Validators.fieldBlank(updateLogDetailDTO.getRemarks())||
                Validators.fieldBlank(updateLogDetailDTO.getDetailId())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            AppLogDetail appLogDetail = appLogDetailService.findDetailBy(updateLogDetailDTO.getDetailId());
            if(appLogDetail==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            appLogDetailService.updateDetail(appLogDetail,updateLogDetailDTO.getRemarks());
            return prepareReturnResult(ReturnCode.UPDATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_UPDATE,null);
        }
    }
    @DeleteMapping("/detail/{id}")
    @ApiOperation(value = "删除想要的detail信息", httpMethod = "DELETE", response = ResponseEntity.class, notes = "删除想要的detail信息")
    public ResponseEntity deleteDetail(@PathVariable("id") String id){
        //物理删除，删除each需要把对于的detail状态也置为删除
        try{
            AppLogDetail appLogDetail  = appLogDetailService.findDetailBy(id);
            if(appLogDetail==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            appLogDetailService.deleteDetail(appLogDetail);
            return prepareReturnResult(ReturnCode.DELETE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_DELETE,null);
        }
    }
    @PutMapping("/detail")
    @ApiOperation(value = "创建detail信息", httpMethod = "PUT", response = ResponseEntity.class, notes = "创建detail信息")
    public ResponseEntity createEach(@RequestBody CreateLogDetailDTO createLogDetailDTO){
        try{
            if(Validators.fieldBlank(createLogDetailDTO.getLogEachId())||
                Validators.fieldBlank(createLogDetailDTO.getRemarks())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            AppLogEach appLogEach = appLogEachService.findEachById(createLogDetailDTO.getLogEachId());
            if(appLogEach==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            AppLogDetailDTO appLogDetailDTO = appLogDetailService.createDetail(createLogDetailDTO.getLogEachId(),
                createLogDetailDTO.getRemarks());
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,appLogDetailDTO);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }

    //*******************************************Each的API**********************************
    @GetMapping("/each/{id}")
    @ApiOperation(value = "获取相应的each信息", httpMethod = "GET", response = ResponseEntity.class, notes = "获取相应的each信息")
    public ResponseEntity getEachInfo(@PathVariable("id") String id){
        try {
            AppLogEachDTO eachInfoById = appLogEachService.getEachInfoById(id);
            return prepareReturnResult(ReturnCode.GET_SUCCESS,eachInfoById);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
    @GetMapping("/eachs")
    @ApiOperation(value = "获取用户下的所有each信息", httpMethod = "GET", response = ResponseEntity.class, notes = "获取相应的each信息")
    public ResponseEntity getEachInfo(@PathVariable("userId") String userId,@PathVariable("token") String token){
        try {
            UaaToken userByToken = loginService.getUserByToken(token);
            if (userByToken==null||!userByToken.getCreatedid().equals(userId)){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            List<AppLogEachDTO> eachs = appLogEachService.getAllEach(userId);
            return prepareReturnResult(ReturnCode.GET_SUCCESS,eachs);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
    @PostMapping("/each")
    @ApiOperation(value = "更新相应的each信息", httpMethod = "POST", response = ResponseEntity.class, notes = "更新相应的each信息")
    public ResponseEntity updateEachInfo(@RequestBody UpdateLogEachDTO updateLogEachDTO){
        try {
            if(Validators.fieldBlank(updateLogEachDTO.getBelongDate())||
                Validators.fieldBlank(updateLogEachDTO.getTitle())||
                Validators.fieldBlank(updateLogEachDTO.getId())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            if(Validators.verifyBelongDate(updateLogEachDTO.getBelongDate())||
                Validators.fieldRangeValue(updateLogEachDTO.getStatus(),
                    Constants.APP_LOG_STATUS_N,
                    Constants.APP_LOG_STATUS_Y)){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
            }
            AppLogEach appLogEach = appLogEachService.findEachById(updateLogEachDTO.getId());
            if(appLogEach==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            appLogEachService.updateEach(appLogEach,updateLogEachDTO);
            return prepareReturnResult(ReturnCode.UPDATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_UPDATE,null);
        }
    }
    @DeleteMapping("/each/{id}")
    @ApiOperation(value = "删除想要的each信息", httpMethod = "DELETE", response = ResponseEntity.class, notes = "删除相应的EACH信息")
    public ResponseEntity deleteEach(@PathVariable("id") String id){
        //物理删除，删除each需要把对于的detail状态也置为删除
        try{
            AppLogEach appLogEach  = appLogEachService.findEachById(id);
            if(appLogEach==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            appLogEachService.deleteEach(appLogEach);
            return prepareReturnResult(ReturnCode.DELETE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_DELETE,null);
        }
    }
    @PutMapping("/each")
    @ApiOperation(value = "创建each信息", httpMethod = "PUT", response = ResponseEntity.class, notes = "创建Each")
    public ResponseEntity createEach(@RequestBody CreateLogEachDTO createLogEachDTO){
        try{
            if(Validators.fieldBlank(createLogEachDTO.getTitle())||
                Validators.fieldBlank(createLogEachDTO.getToken())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            if(Validators.verifyBelongDate(createLogEachDTO.getBelongDate())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
            }
            //验证user是不是存在
            UaaToken token = loginService.getUserByToken(createLogEachDTO.getToken());
            if(token==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            AppLogEachDTO appLogEachDTO = appLogEachService.createEach(createLogEachDTO,token.getCreatedid());
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,appLogEachDTO);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }
}
//public  enum resource{
//    file_file_upload,
//    file_file_down,
//    tel_remark_visible,
//    tel_load,
//    tel_changeMessage,
//    day_remark_visible,
//    day_load,
//    day_changeMessage,
//    //        day_create, //因为从来都是在加载的时候创建LogDay，所以不需要协议
////        day_save,
//    //主界面的功能
//    mainFun_showAllOver,
//    mainFun_showAllNoOver,
//    mainFun_noteTree,
//    menu_add,
//    //        menu_can_add,
//    menu_chooseCalender,
//    global_data_save,//UUID为空，不做记录
//    global_data_init,
//    global_exit,
//    Return,
//}
