package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

public class AppLogDetailDTO extends AppLogBaseDTO{
//    @ApiModelProperty(name = "含有detail参数的map")
//    private Map<String,Object> values;
    @ApiModelProperty(name = "备注")
    private String remarks;



    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
