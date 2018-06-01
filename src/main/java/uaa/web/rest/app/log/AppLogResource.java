package uaa.web.rest.app.log;


import core.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uaa.service.app.log.AppLogService;

@RestController
@RequestMapping("/api/app/log")
public class AppLogResource extends BaseResource{
    @Autowired
    private AppLogService appLogService;
    //TODO each 需要提供的接口：get、update、all-get、delete、update-overstatus
    //TODO day 需要提供的接口：get、update、all-get、delete
    //TODO detail 需要提供的接口：get、update、all-get、delete、update—classify-priority-changeMarkstar
    //TODO 可以按照天来获取全部的数据结构（day、detail、之类的），返回一个树接口
    @GetMapping("/each/{id}")
    @ApiOperation(value = "获取相应的each信息", httpMethod = "GET", response = ResponseEntity.class, notes = "获取相应的each信息")
    public ResponseEntity getEachInfo(@PathVariable("id") String id){
        try {

            return prepareReturnResult(ReturnCode.GET_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
}
//public  enum resource{
//    file_file_upload,
//    file_file_down,
//    each_changeOverState,
//    each_changeMessage,
//    each_del,
//    each_load,
//    tel_remark_visible,
//    tel_load,
//    tel_changeMessage,
//    day_remark_visible,
//    day_load,
//    day_changeMessage,
//    //        day_create, //因为从来都是在加载的时候创建LogDay，所以不需要协议
////        day_save,
////        detail_show,
////        detail_hide,
//    detail_visible,
//    detail_del,
//    detail_changeMessage,
//    detail_changeClassify,
//    detail_changePriority,
//    detail_changeMarkStar,
//    detail_move,//这个用于实时的修改窗口的位置，通知显示细节的面板能够跟随 UUID为空，不做记录
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
