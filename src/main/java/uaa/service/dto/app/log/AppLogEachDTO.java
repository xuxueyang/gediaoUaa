package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

public class AppLogEachDTO extends AppLogBaseDTO{
    @ApiModelProperty(name = "每个each的细节面板（分类也算是detail的）,架构上支持多个映射，现在只有一个基本")
    private List<AppLogDetailDTO> appLogDetailDTOList;


//    @ApiModelProperty(name = "含有each参数的map")
//    private Map<String,Object> values;


//    public Map<String, Object> getValue() {
//        return values;
//    }
//
//    public void setValue(Map<String, Object> values) {
//        this.values = values;
//    }

    public List<AppLogDetailDTO> getAppLogDetailDTOList() {
        return appLogDetailDTOList;
    }

    public void setAppLogDetailDTOList(List<AppLogDetailDTO> appLogDetailDTOList) {
        this.appLogDetailDTOList = appLogDetailDTOList;
    }
}
