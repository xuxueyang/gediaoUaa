package uaa.web.rest.app.log;

import core.ReturnCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uaa.domain.UaaError;
import uaa.domain.uaa.UaaUser;
import uaa.service.app.log.AppLogStatisService;
import uaa.web.rest.BaseResource;
import uaa.web.rest.util.excel.vo.app.log.LogEachInVO;
import util.Validators;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by UKi_Hi on 2018/6/4.
 */
@RestController
@RequestMapping("/api/app/log/statis")
/**
 * 用于统计任务的Resource——譬如：完成率、每日创建完成曲线等等这些的日志统计功能
 */
public class AppLogStatisResource extends BaseResource{

    @Autowired
    private AppLogStatisService appLogStatisService;

    @PostMapping("/export-each")
    @ApiOperation(value = "导出each信息", httpMethod = "POST", response = ResponseEntity.class, notes = "导出each信息")
    public ResponseEntity updateEachInfo(@RequestBody LogEachInVO logEachInVO, HttpServletResponse response){
        try {
            if(Validators.fieldBlank(logEachInVO.getUserId())||
                Validators.fieldBlank(logEachInVO.getToken())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }

            UaaError post = uaaPermissionService.verifyLogin(logEachInVO.getUserId(), logEachInVO.getToken(), "/api/app/log/statis/export-each", "POST");
            if(post.hasError()){
                return prepareReturnResult(post.getFirstError(),null);
            }
            //导出数据
            UaaError uaaError = appLogStatisService.exportEach((UaaUser)post.getValue(),response);
            return prepareReturnResult(ReturnCode.GET_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_UPDATE,null);
        }
    }
    //TODO

    /**
     * 得到统计数据：
     * 1.一个折线图，显示每天的未完成、已完成的创建数目（每天晚上12点统计之前的）、今天的则是动态变化的、、、、记录五天的
     * 2.折线图的上面，显示一行文字，全部的，已完成和未完成数。。。。根据type，显示每种type的数目
     * 3.最下面的一栏是一个消息列表。记录最近的操作日志（最毒十条，显示：编号（ID）：时间（创建时间）：消息日志（比如更新了便签，创建了TAG，创建了TAG）
     * @return
     */
    public ResponseEntity getStaticDate(){
        try {
            return prepareReturnResult(ReturnCode.GET_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
}
