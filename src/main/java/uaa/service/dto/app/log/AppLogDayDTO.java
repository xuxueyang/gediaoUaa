package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

public class AppLogDayDTO extends  AppLogBaseDTO{
    @ApiModelProperty(name = "该天下的任务日志")
    private List<AppLogEachDTO> appLogEachDTOList;




//    @ApiModelProperty(name = "含有day参数的map")
//    private Map<String,Object> values;

    public List<AppLogEachDTO> getAppLogEachDTOList() {
        return appLogEachDTOList;
    }

    public void setAppLogEachDTOList(List<AppLogEachDTO> appLogEachDTOList) {
        this.appLogEachDTOList = appLogEachDTOList;
    }

//    public Map<String, Object> getValue() {
//        return values;
//    }
//
//    public void setValue(Map<String, Object> values) {
//        this.values = values;
//    }
}
