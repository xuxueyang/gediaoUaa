package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by UKi_Hi on 2018/6/4.
 */
public class CreateLogDetailDTO {
    @ApiModelProperty(name = "ID")
    private String logEachId;

    @ApiModelProperty(name = "消息")
    private String remarks;

    public String getLogEachId() {
        return logEachId;
    }

    public void setLogEachId(String logEachId) {
        this.logEachId = logEachId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

