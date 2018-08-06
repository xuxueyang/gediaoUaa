package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;
import uaa.service.dto.UaaBasePremissionDTO;

/**
 * Created by UKi_Hi on 2018/6/4.
 */
public class CreateLogDetailDTO  {
    @ApiModelProperty(name = "ID")
    private String logEachId;

    @ApiModelProperty(name = "消息")
    private String remarks;

    @ApiModelProperty(name = "token")
    private String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

