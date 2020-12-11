package uaa.web.rest.app.log;

import uaa.config.ReturnCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uaa.domain.app.log.AppLogPlanDay;
import uaa.domain.app.log.AppLogTag;
import uaa.service.app.log.AppLogPlanDayService;
import uaa.service.dto.app.log.AppLogTagDTO;
import uaa.web.rest.BaseResource;

@RestController
@RequestMapping("/api/app/plan")
public class AppLogPlanDayResource extends BaseResource {
    @Autowired
    private AppLogPlanDayService planDayService;

    @GetMapping("/day")
    @ApiOperation(value = "获取日期，如果没有就默认今年的，如果今天没有则创建。", httpMethod = "GET", response = ResponseEntity.class)
    public ResponseEntity getPlanDay(){
        try {
            return prepareReturnResult(ReturnCode.GET_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
    @PostMapping("/day-item")
    @ApiOperation(value = "创建今天的", httpMethod = "GET", response = ResponseEntity.class)
    public ResponseEntity createPlanDay(
        @RequestParam(name="desc",required=false) String desc
    ){
        try {//todo 创建，设置状态为false
            return prepareReturnResult(ReturnCode.GET_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
    @GetMapping("/days")
    @ApiOperation(value = "分页获取全部的da", httpMethod = "GET", response = ResponseEntity.class)
    public ResponseEntity getPlanDays(PageRequest page,
                                        @RequestParam(name="desc",required=false) String desc
                                      ){
        try {
            return prepareReturnResult(ReturnCode.GET_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
}
