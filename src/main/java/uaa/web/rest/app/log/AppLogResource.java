package uaa.web.rest.app.log;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import uaa.domain.UaaError;
import uaa.domain.uaa.UaaUser;
import uaa.web.rest.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uaa.config.Constants;
import uaa.domain.app.log.AppLogDay;
import uaa.domain.app.log.AppLogDetail;
import uaa.domain.app.log.AppLogEach;
import uaa.domain.app.log.AppLogTag;
import uaa.domain.uaa.UaaToken;
import uaa.service.app.log.AppLogDetailService;
import uaa.service.app.log.AppLogEachService;
import uaa.service.app.log.AppLogSingleService;
import uaa.service.dto.app.log.*;
import uaa.service.login.UaaLoginService;
import uaa.web.rest.util.CommonUtil;
import util.Validators;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

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
    private UaaLoginService uaaLoginService;

    //*******************************************Day等单例的API**********************************
    @GetMapping("/tag/{id}")
    @ApiOperation(value = "获取相应的tag信息", httpMethod = "GET", response = ResponseEntity.class, notes = "获取相应的tag信息")
    public ResponseEntity getTag(@PathVariable("id") String id){
        try {
            AppLogTag appLogTag = appLogSingleService.findTagBy(id);
            if(appLogTag==null)
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            AppLogTagDTO appLogTagDTO = appLogSingleService.prepareTagEntityToDTO(appLogTag);
            return prepareReturnResult(ReturnCode.GET_SUCCESS,appLogTagDTO);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }

    @GetMapping("/tags")
    @ApiOperation(value = "获取相应的tags信息", httpMethod = "GET", response = ResponseEntity.class, notes = "获取相应的tags信息")
    public ResponseEntity getTags(@RequestParam( name = "token",required = false) String token,
                                  @RequestParam( name = "verifyCode",required = false) String verifyCode,
                                  @RequestParam( name = "userId",required = true) String userId
                                  ){
        try {
            if(Validators.fieldBlank(token)){
                //走code验证
                UaaError get = uaaPermissionService.verifyByCode(userId, verifyCode, "/tags", "GET");
                if(get.hasError())
                    return prepareReturnResult(get.getFirstError(),null);
            }else{
                UaaError get = uaaPermissionService.verifyLogin(userId, token, "/tags", "GET");
                if(get.hasError())
                    return prepareReturnResult(get.getFirstError(),null);
            }
            //得到下属用户下的tags
            List<AppLogTagDTO> list = appLogSingleService.fingAllTag(userId);
            return prepareReturnResult(ReturnCode.GET_SUCCESS,list);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }

    @PostMapping("/tag")
    @ApiOperation(value = "更新相应的tag信息", httpMethod = "POST", response = ResponseEntity.class, notes = "更新相应的tag信息")
    public ResponseEntity updateTagInfo(@RequestBody UpdateLogTagDTO updateLogTagDTO){
        try {
            if(Validators.fieldBlank(updateLogTagDTO.getTagId())
                ||Validators.fieldBlank(updateLogTagDTO.getName())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            if(Validators.fieldBlank(Validators.fieldBlank(updateLogTagDTO.getToken()))){
                return prepareReturnResult(ReturnCode.ERROR_HAVE_NO_PERMISSION_OPERATION,null);
            }
            AppLogTag tag = appLogSingleService.findTagBy(updateLogTagDTO.getTagId());
            if(tag==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            UaaToken userByToken = uaaLoginService.getUserByToken(updateLogTagDTO.getToken());
            if(userByToken==null)
                return prepareReturnResult(ReturnCode.ERROR_USER_HAS_LOGOUT,null);

            appLogSingleService.updateTag(tag,updateLogTagDTO,userByToken.getId());
            return prepareReturnResult(ReturnCode.UPDATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_UPDATE,null);
        }
    }
    @DeleteMapping("/tag/{id}")
    @ApiOperation(value = "删除相应的tag信息", httpMethod = "DELETE", response = ResponseEntity.class, notes = "删除相应的tag信息")
    public ResponseEntity deleteTag(@PathVariable("id") String id,@RequestParam( name = "token",required = true) String token,
                                    @RequestParam( name = "userId",required = true) String userId){
        try{
            UaaError get = uaaPermissionService.verifyLogin(userId, token, "/tag/{id}", "DELETE");
            if(get.hasError())
                return prepareReturnResult(get.getFirstError(),null);
            AppLogTag tag = appLogSingleService.findTagBy(id);
            if(tag==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            appLogSingleService.deleteTag(tag);
            logApi("/api/app/log/tag/{id}","删除了标签："+tag.getName(),
                Constants.HttpType.DELETE.name(),
                id,
                Constants.ProjectType.GEDIAO.name(),
                CommonUtil.getTodayBelongDate(),((UaaUser)get.getValue()).getId());
            return prepareReturnResult(ReturnCode.DELETE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_DELETE,null);
        }
    }
    @PutMapping("/tag")
    @ApiOperation(value = "创建tag信息", httpMethod = "PUT", response = ResponseEntity.class, notes = "创建tag信息")
    public ResponseEntity createTag(@RequestBody CreateLogTagDTO createLogTagDTO){
        try{
            if(Validators.fieldBlank(createLogTagDTO.getName())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            if(Validators.fieldBlank(Validators.fieldBlank(createLogTagDTO.getToken()))){
                return prepareReturnResult(ReturnCode.ERROR_HAVE_NO_PERMISSION_OPERATION,null);
            }
            //验证user是不是存在
            UaaToken token = uaaLoginService.getUserByToken(createLogTagDTO.getToken());
            if(token==null){
                return prepareReturnResult(ReturnCode.ERROR_USER_HAS_LOGOUT,null);
            }
            AppLogTagDTO appLogTagDTO = appLogSingleService.createTag(createLogTagDTO,token.getCreatedid());
            logApi("/api/app/log/tag","创建了标签："+createLogTagDTO.getName(),
                Constants.HttpType.PUT.name(),
                appLogTagDTO.getId(),
                Constants.ProjectType.GEDIAO.name(),
                CommonUtil.getTodayBelongDate(),token.getCreatedid());
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,appLogTagDTO);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }

    //*******************************************Day等单例的API**********************************
    @GetMapping("/day/{id}")
    @ApiOperation(value = "获取相应的day信息", httpMethod = "GET", response = ResponseEntity.class, notes = "获取相应的day信息")
    public ResponseEntity getDay(@PathVariable("id") String id){
        try {
            AppLogDay appLogDay = appLogSingleService.findDayBy(id);
            if(appLogDay==null)
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            AppLogDayDTO appLogDayDTO = appLogSingleService.prepareDayEntityToDTO(appLogDay);
            logApi("/api/app/log/day/{id}","查看了某个日志",
                Constants.HttpType.POST.name(),
                id,
                Constants.ProjectType.GEDIAO.name(),
                appLogDayDTO.getBelongDate(),null);
            return prepareReturnResult(ReturnCode.GET_SUCCESS,appLogDayDTO);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }

    @GetMapping("/days")
    @ApiOperation(value = "获取相应的day信息", httpMethod = "GET", response = ResponseEntity.class, notes = "获取相应的day信息")
    public ResponseEntity getDays(@RequestParam(name="belongDate",required=true) String belongDate){
        try {
            if(!Validators.verifyBelongDate(belongDate)){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
            }
            List<AppLogDayDTO> list = appLogSingleService.fingAllDayByBelongDate(belongDate);
            logApi("/api/app/log/days","查看了某天下的所有日志",
                Constants.HttpType.POST.name(),
                belongDate,
                Constants.ProjectType.GEDIAO.name(),
                null,null);
            return prepareReturnResult(ReturnCode.GET_SUCCESS,list);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }

    @PostMapping("/day")
    @ApiOperation(value = "更新相应的day信息", httpMethod = "POST", response = ResponseEntity.class, notes = "更新相应的day信息")
    public ResponseEntity updateDayInfo(@RequestBody UpdateLogDayDTO updateLogDayDTO){
        try {
            if(Validators.fieldBlank(updateLogDayDTO.getMessage())||
                Validators.fieldBlank(updateLogDayDTO.getId())
                ||Validators.fieldBlank(updateLogDayDTO.getTitle())
                ||Validators.fieldBlank(updateLogDayDTO.getToken())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            AppLogDay appLogDay = appLogSingleService.findDayBy(updateLogDayDTO.getId());
            if(appLogDay==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            UaaToken userByToken = uaaLoginService.getUserByToken(updateLogDayDTO.getToken());
            if(userByToken==null){
                return prepareReturnResult(ReturnCode.ERROR_USER_HAS_LOGOUT,null);
            }
            appLogSingleService.updateDay(appLogDay,updateLogDayDTO,userByToken.getId());
            logApi("/api/app/log/day","更新了日志",
                Constants.HttpType.POST.name(),
                updateLogDayDTO.getId(),
                Constants.ProjectType.GEDIAO.name(),
                updateLogDayDTO.getBelongDate(),null);
            return prepareReturnResult(ReturnCode.UPDATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_UPDATE,null);
        }
    }
    @DeleteMapping("/day/{id}")
    @ApiOperation(value = "删除相应的day信息", httpMethod = "DELETE", response = ResponseEntity.class, notes = "删除相应的day信息")
    public ResponseEntity deleteDay(@PathVariable("id") String id){
        //物理删除，删除each需要把对于的detail状态也置为删除
        try{
            AppLogDay appLogDay = appLogSingleService.findDayBy(id);
            if(appLogDay==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            appLogSingleService.deleteDay(appLogDay);
            logApi("/api/app/log/day","删除了日志",
                Constants.HttpType.DELETE.name(),
                id,
                Constants.ProjectType.GEDIAO.name(),
                CommonUtil.getTodayBelongDate(),null);
            return prepareReturnResult(ReturnCode.DELETE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_DELETE,null);
        }
    }
    @PutMapping("/day")
    @ApiOperation(value = "创建day信息", httpMethod = "PUT", response = ResponseEntity.class, notes = "创建day信息")
    public ResponseEntity createDay(@RequestBody CreateLogDayDTO createLogDayDTO){
        try{
            if(Validators.fieldBlank(createLogDayDTO.getTitle())||
                Validators.fieldBlank(createLogDayDTO.getToken())
                ||Validators.fieldBlank(createLogDayDTO.getType())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            if(!Validators.verifyBelongDate(createLogDayDTO.getBelongDate())||
                !Validators.fieldRangeValue(createLogDayDTO.getType(),
                    Constants.APP_LOG_DAY_TYPE_DIARY,
                    Constants.APP_LOG_DAY_TYPE_TEL)){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
            }
            //验证user是不是存在
            UaaToken token = uaaLoginService.getUserByToken(createLogDayDTO.getToken());
            if(token==null){
                return prepareReturnResult(ReturnCode.ERROR_USER_HAS_LOGOUT,null);
            }
            AppLogDayDTO appLogDayDTO = appLogSingleService.createDay(createLogDayDTO,token.getCreatedid());
            logApi("/api/app/log/day","创建了日志",
                Constants.HttpType.PUT.name(),
                appLogDayDTO.getId(),
                Constants.ProjectType.GEDIAO.name(),
                createLogDayDTO.getBelongDate(),null);
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,appLogDayDTO);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }

    //*******************************************Detail的API**********************************
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "获取相应的detail信息", httpMethod = "GET", response = ResponseEntity.class, notes = "获取相应的detail信息")
    public ResponseEntity getDetailInfo(@PathVariable("id") String id,
                                        @RequestParam(name="token",required=true) String token){
        try {

            AppLogDetailDTO detailInfoById = appLogDetailService.getDetailInfoById(id);
            logApi("/api/app/log/detail/{id}","查看了消息详情面板",
                Constants.HttpType.GET.name(),
                id,
                Constants.ProjectType.GEDIAO.name(),
                CommonUtil.getTodayBelongDate(),null);
            return prepareReturnResult(ReturnCode.GET_SUCCESS,detailInfoById);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
    @PostMapping("/detail")
    @ApiOperation(value = "更新相应的detail信息", httpMethod = "POST", response = ResponseEntity.class, notes = "更新相应的detail信息")
    public ResponseEntity updateDetailInfo(@RequestBody UpdateLogDetailDTO updateLogDetailDTO){
        try {
            if(Validators.fieldBlank(updateLogDetailDTO.getDetailId())
                ||Validators.fieldBlank(updateLogDetailDTO.getToken())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            AppLogDetail appLogDetail = appLogDetailService.findDetailBy(updateLogDetailDTO.getDetailId());
            if(appLogDetail==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            UaaToken userByToken = uaaLoginService.getUserByToken(updateLogDetailDTO.getToken());
            if(userByToken==null)
                return prepareReturnResult(ReturnCode.ERROR_USER_HAS_LOGOUT,null);

            appLogDetailService.updateDetail(appLogDetail,updateLogDetailDTO.getRemarks(),userByToken.getId());
            logApi("/api/app/log/detail","更新了消息详情面板",
                Constants.HttpType.POST.name(),
                updateLogDetailDTO.getDetailId(),
                Constants.ProjectType.GEDIAO.name(),
                CommonUtil.getTodayBelongDate(),userByToken.getCreatedid());
            return prepareReturnResult(ReturnCode.UPDATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_UPDATE,null);
        }
    }
//    @DeleteMapping("/detail/{id}")
    @ApiOperation(value = "删除想要的detail信息", httpMethod = "DELETE", response = ResponseEntity.class, notes = "删除想要的detail信息")
    public ResponseEntity deleteDetail(@PathVariable("id") String id){
        //物理删除，删除each需要把对于的detail状态也置为删除
        try{
            AppLogDetail appLogDetail  = appLogDetailService.findDetailBy(id);
            if(appLogDetail==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            appLogDetailService.deleteDetail(appLogDetail);
            logApi("/api/app/log/detail/{id}","删除了消息详情面板",
                Constants.HttpType.DELETE.name(),
                id,
                Constants.ProjectType.GEDIAO.name(),
                CommonUtil.getTodayBelongDate(),null);
            return prepareReturnResult(ReturnCode.DELETE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_DELETE,null);
        }
    }
    @PutMapping("/detail")
    @ApiOperation(value = "创建detail信息", httpMethod = "PUT", response = ResponseEntity.class, notes = "创建detail信息")
    public ResponseEntity createDetail(@RequestBody CreateLogDetailDTO createLogDetailDTO){
        try{
            if(Validators.fieldBlank(createLogDetailDTO.getLogEachId())
                ||Validators.fieldBlank(createLogDetailDTO.getToken())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            UaaToken userByToken = uaaLoginService.getUserByToken(createLogDetailDTO.getToken());
            if(userByToken==null)
                return prepareReturnResult(ReturnCode.ERROR_USER_HAS_LOGOUT,null);

            AppLogEach appLogEach = appLogEachService.findEachById(createLogDetailDTO.getLogEachId());
            if(appLogEach==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            AppLogDetailDTO appLogDetailDTO = appLogDetailService.createDetail(createLogDetailDTO.getLogEachId(),
                createLogDetailDTO.getRemarks());
            logApi("/api/app/log/detail","创建了消息详情面板",
                Constants.HttpType.PUT.name(),
                appLogDetailDTO.getId(),
                Constants.ProjectType.GEDIAO.name(),
                CommonUtil.getTodayBelongDate(),userByToken.getCreatedid());
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,appLogDetailDTO);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }

    //*******************************************Each的API**********************************
//    @GetMapping("/each/{id}")
    @ApiOperation(value = "获取相应的each信息", httpMethod = "GET", response = ResponseEntity.class, notes = "获取相应的each信息")
    public ResponseEntity getEachInfo(@PathVariable("id") String id){
        try {
            AppLogEachDTO eachInfoById = appLogEachService.getEachInfoById(id);
            logApi("/api/app/log/each/{id}","查看了某个便签",
                Constants.HttpType.GET.name(),
                id,
                Constants.ProjectType.GEDIAO.name(),
                CommonUtil.getTodayBelongDate(),null);
            return prepareReturnResult(ReturnCode.GET_SUCCESS,eachInfoById);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
    @GetMapping("/eachs")
    @ApiOperation(value = "获取用户下的所有each信息", httpMethod = "GET", response = ResponseEntity.class, notes = "获取相应的each信息")
    public ResponseEntity getAllEachInfo(@RequestParam(name="token",required=true) String token,
                                      @RequestParam(name="userId",required=true) String userId,
                                      @RequestParam(name="startDate",required=false) String startDate,
                                      @RequestParam(name="endDate",required=false) String endDate,
                                      @RequestParam(name="type",required=false) String type,
                                      @RequestParam(name="tagId",required=false) String tagId,
                                      @RequestParam(name = "searchContext",required = false) String searchContext,
                                      @RequestParam(name="size",required=true) int size,
                                      @RequestParam(name="page",required=true) int page
//                                      @PageableDefault(value = 15, sort = { "updatedDate" }, direction = Sort.Direction.DESC)
//                                              Pageable pageable
    ){
        try {
            Pageable pageable = new PageRequest(page, size);
            //默认15个，。根据时间逆序
            UaaError post = uaaPermissionService.verifyLogin(userId, token, "/api/app/log/eachs", "GET");
            if(post.hasError()){
                return prepareReturnResult(post.getFirstError(),null);
            }
//            UaaToken userByToken = uaaLoginService.getUserByToken(token);
//            if (userByToken==null||!userByToken.getCreatedid().equals(userId)){
//                return prepareReturnResult(ReturnCode.ERROR_USER_HAS_LOGOUT,null);
//            }
            //只校验格式
            if(Validators.fieldNotBlank(startDate)&&!Validators.verifyBelongDate(startDate)){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
            }
            if(Validators.fieldNotBlank(endDate)&&!Validators.verifyBelongDate(endDate)){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
            }
            //如果日期都为空，那么带出今天的
            //但是如果搜索内容不为空日期为空，视为搜索全部的
            if(Validators.fieldBlank(startDate)&&Validators.fieldBlank(endDate)&&Validators.fieldBlank(searchContext)){
                startDate = CommonUtil.getTodayBelongDate();
                endDate = startDate;
            }
            //TODO 标签前端删选，状态，因为可能比较多，后端删选（前端也可以获取全部，自己删选）
            Map eachs = appLogEachService.getAllEach(userId,startDate,endDate,searchContext,type,tagId,pageable);
//            logApi("/api/app/log/eachs","获取了所有的标签",
//                Constants.HttpType.GET.name(),
//                null,
//                Constants.ProjectType.GEDIAO.name(),
//                null,
//                userId);
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
                Validators.fieldBlank(updateLogEachDTO.getId())||
                Validators.fieldBlank(updateLogEachDTO.getToken())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            if(!Validators.verifyBelongDate(updateLogEachDTO.getBelongDate())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
            }
            AppLogEach appLogEach = appLogEachService.findEachById(updateLogEachDTO.getId());
            if(appLogEach==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
//            UaaToken userByToken = uaaLoginService.getUserByToken(updateLogEachDTO.getToken());
//            if(userByToken==null)
//                return prepareReturnResult(ReturnCode.ERROR_USER_HAS_LOGOUT,null);
            UaaError post = uaaPermissionService.verifyLogin(updateLogEachDTO.getUserId(), updateLogEachDTO.getToken(), "/api/app/log/each", "POST");
            if(post.hasError()){
                return prepareReturnResult(post.getFirstError(),null);
            }

            appLogEachService.updateEach(appLogEach,updateLogEachDTO,(updateLogEachDTO.getUserId()));
            logApi("/api/app/log/each","更新了便签",
                Constants.HttpType.POST.name(),
                updateLogEachDTO.getId(),
                Constants.ProjectType.GEDIAO.name(),
                updateLogEachDTO.getBelongDate(),updateLogEachDTO.getUserId());
            return prepareReturnResult(ReturnCode.UPDATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_UPDATE,null);
        }
    }
    @DeleteMapping("/each/{id}")
    @ApiOperation(value = "删除想要的each信息", httpMethod = "DELETE", response = ResponseEntity.class, notes = "删除相应的EACH信息")
    public ResponseEntity deleteEachInfo(@PathVariable("id") String id,@RequestParam(name="token",required=true) String token,
                                     @RequestParam(name="userId",required=true) String userId){
        //物理删除，删除each需要把对于的detail状态也置为删除
        try{
            UaaError post = uaaPermissionService.verifyLogin(userId, token, "/api/app/log/each/{id}", "DELETE");
            if(post.hasError()){
                return prepareReturnResult(post.getFirstError(),null);
            }
            AppLogEach appLogEach  = appLogEachService.findEachById(id);
            if(appLogEach==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            appLogEachService.deleteEach(appLogEach);
            logApi("/api/app/log/each","删除了标签",
                Constants.HttpType.DELETE.name(),
                appLogEach.getId(),
                Constants.ProjectType.GEDIAO.name(),
                CommonUtil.getTodayBelongDate(),
                userId
                );
            return prepareReturnResult(ReturnCode.DELETE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_DELETE,null);
        }
    }
    @PutMapping("/each")
    @ApiOperation(value = "创建each信息", httpMethod = "PUT", response = ResponseEntity.class, notes = "创建Each")
    public ResponseEntity createEachInfo(@RequestBody CreateLogEachDTO createLogEachDTO){
        try{

            if(Validators.fieldBlank(createLogEachDTO.getTitle())||
                Validators.fieldBlank(createLogEachDTO.getToken())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            if(!Validators.verifyBelongDate(createLogEachDTO.getBelongDate())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
            }
            UaaError error = uaaPermissionService.verifyLogin(createLogEachDTO.getUserId(), createLogEachDTO.getToken(), "/api/app/log/each", "PUT");
            if(error.hasError()){
                return prepareReturnResult(error.getFirstError(),null);
            }
//            //验证user是不是存在
//            UaaToken token = uaaLoginService.getUserByToken(createLogEachDTO.getToken());
//            if(token==null){
//                return prepareReturnResult(ReturnCode.ERROR_USER_HAS_LOGOUT,null);
//            }
            AppLogEachDTO appLogEachDTO = appLogEachService.createEach(createLogEachDTO,createLogEachDTO.getUserId());
            logApi("/api/app/log/each","创建了便签",
                Constants.HttpType.PUT.name(),
                appLogEachDTO.getId(),
                Constants.ProjectType.GEDIAO.name(),
                createLogEachDTO.getBelongDate(),
                createLogEachDTO.getUserId()
            );
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,appLogEachDTO);
        }catch (Exception e){
            e.printStackTrace();
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
