package uaa.web.rest.app.log;

import com.alibaba.fastjson.JSON;
import core.ReturnCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uaa.config.Constants;
import uaa.domain.UaaError;
import uaa.domain.app.log.AppLogDetail;
import uaa.domain.uaa.UaaToken;
import uaa.domain.uaa.UaaUser;
import uaa.service.app.log.AppLogDetailService;
import uaa.service.app.log.AppLogStatisService;
import uaa.service.login.UaaLoginService;
import uaa.web.rest.BaseResource;
import uaa.web.rest.util.excel.vo.app.log.LogEachInVO;
import uaa.web.rest.util.redis.Redis;
import util.Validators;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private UaaLoginService uaaLoginService;
    @Autowired
    private AppLogDetailService appLogDetailService;


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
    @GetMapping("/each-line-data")
    @ApiOperation(value = "获取每日each的完成和未完成折现统计图数据", httpMethod = "GET", response = ResponseEntity.class, notes = "获取每日each的完成和未完成折现统计图数据")
    public ResponseEntity getEachEachLineData(@RequestParam(value = "token",required = true) String token){
        try {
            //需要的数据：日期，数目，type（今日是动态，前几天的是获取到最新的静态的）
            //list<Map<Key:日期，value：num>>
            UaaToken userByToken = uaaLoginService.getUserByToken(token);
            if(userByToken==null){
                return prepareReturnResult(ReturnCode.ERROR_USER_HAS_LOGOUT,null);
            }
            Map<String,Object>map = new HashMap<>();
            map = appLogStatisService.getEachLineData(userByToken.getCreatedid());
            return prepareReturnResult(ReturnCode.GET_SUCCESS,map);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
    @GetMapping("/each-bie-data")
    @ApiOperation(value = "获取整体each的饼状图", httpMethod = "GET", response = ResponseEntity.class, notes = "获取整体each的饼状图")
    public ResponseEntity getEachEachBieData(@RequestParam(value = "token",required = true) String token){
        try {
            //需要的数据是。不同type下的数目（key：type,vale:num-name)
            UaaToken userByToken = uaaLoginService.getUserByToken(token);
            if(userByToken==null){
                return prepareReturnResult(ReturnCode.ERROR_USER_HAS_LOGOUT,null);
            }
            String data = Redis.getJedis().get(userByToken.getCreatedid() + "_each-bit-data");
            if(data!=null){
                return prepareReturnResult(ReturnCode.GET_SUCCESS,data);
            }else{
                Map<String,AppLogStatisService.ShowDto> map = new HashMap<>();
                map = appLogStatisService.getEachBieData(userByToken.getCreatedid());
                Redis.getJedis().setex(userByToken.getCreatedid()+"_each-bit-data", 3600, JSON.toJSONString(map));
                return prepareReturnResult(ReturnCode.GET_SUCCESS,map);
            }
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
    @GetMapping("/each-day-operator-data")
    @ApiOperation(value = "按照日期统计那天下的所有操作", httpMethod = "GET", response = ResponseEntity.class, notes = "按照日期统计那天下的所有操作")
    public ResponseEntity getEachDayOperatorData(@RequestParam(value = "token",required = true)String token,
                                                 @RequestParam(value = "belongDate",required = true)String belongDate
                                                 ){
        try{
            //根据所属日期获取那天的操作日志
            if(Validators.fieldBlank(belongDate)||Validators.fieldBlank(token))
            {
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            UaaToken userByToken = uaaLoginService.getUserByToken(token);
            if(userByToken==null){
                return prepareReturnResult(ReturnCode.ERROR_USER_HAS_LOGOUT,null);
            }
            //按照时间顺序赛入值
//            Redis.getJedis().setex(userByToken.getCreatedid()+belongDate,)
            List<Map<String, String>> eachDayOperatorData = appLogStatisService.getEachDayOperatorData(userByToken.getCreatedid(), belongDate);
            return prepareReturnResult(ReturnCode.GET_SUCCESS,eachDayOperatorData);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_UPDATE,null);
        }
    }
    @GetMapping("/export-each-detail")
    @ApiOperation(value = "导出", httpMethod = "GET", response = ResponseEntity.class, notes = "导出")
    public ResponseEntity exportEachDetailPDF(@RequestParam(value = "token",required = true)String token,
                                                 @RequestParam(value = "detailId",required = true)String detailId,
                                              HttpServletResponse response
    ){
        try{
            //根据所属日期获取那天的操作日志
            if(Validators.fieldBlank(detailId)||Validators.fieldBlank(token))
            {
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            UaaToken userByToken = uaaLoginService.getUserByToken(token);
            if(userByToken==null){
                return prepareReturnResult(ReturnCode.ERROR_USER_HAS_LOGOUT,null);
            }
            //导出detail文件pdf格式
            AppLogDetail detailBy = appLogDetailService.findDetailBy(detailId);
            if(detailBy==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);

            }
            UaaError uaaError = appLogStatisService.exportDetailPDF(detailBy, response);
            if(uaaError.hasError()){
                return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
            }
            return prepareReturnResult(ReturnCode.GET_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
}
