package uaa.web.rest.app.log;

import uaa.config.ReturnCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uaa.config.Constants;
import uaa.domain.UaaError;
import uaa.service.app.log.AppLogDetailService;
import uaa.service.app.log.AppLogEachService;
import uaa.service.app.log.AppLogSingleService;
import uaa.service.login.UaaLoginService;
import uaa.web.rest.BaseResource;
import uaa.web.rest.util.CommonUtil;
import util.Validators;

import java.util.Map;

/**
 * Created by UKi_Hi on 2018/10/21.
 */
@RestController
@RequestMapping("/api/app/log/quick")
public class AppQuickLogResource extends BaseResource {
    private AppLogEachService appLogEachService;
    @Autowired
    private AppLogDetailService appLogDetailService;
    @Autowired
    private AppLogSingleService appLogSingleService;
    @Autowired
    private UaaLoginService uaaLoginService;

    @GetMapping("/eachs")
    @ApiOperation(value = "获取用户下的所有each信息", httpMethod = "GET", response = ResponseEntity.class, notes = "获取相应的each信息")
    public ResponseEntity getAllEachInfo(@RequestParam(name="token",required=true) String token,
                                         @RequestParam(name="userId",required=true) String userId,
                                         @RequestParam(name="startDate",required=false) String startDate,
                                         @RequestParam(name="endDate",required=false) String endDate,
                                         @RequestParam(name="tomatoType",required = false) String tomatoType
    ){
        try {
            //默认15个，。根据时间逆序
            UaaError post = uaaPermissionService.verifyLogin(userId, token, "/api/app/log/quick/eachs", "GET");
            if(post.hasError()){
                return prepareReturnResult(post.getFirstError(),null);
            }
            //只校验格式
            if(Validators.fieldNotBlank(startDate)&&!Validators.verifyBelongDate(startDate)){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
            }
            if(Validators.fieldNotBlank(endDate)&&!Validators.verifyBelongDate(endDate)){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
            }
            //如果日期都为空，那么带出今天的
            //但是如果搜索内容不为空日期为空，视为搜索全部的
            if(Validators.fieldBlank(startDate)&&Validators.fieldBlank(endDate)){
                startDate = CommonUtil.getTodayBelongDate();
                endDate = startDate;
            }
            Map eachs = appLogEachService.getAllEach(userId,startDate,endDate,"", Constants.LogEach_Type.JOURNAL,"",new PageRequest(0,100),tomatoType);
            return prepareReturnResult(ReturnCode.GET_SUCCESS,eachs);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
}
