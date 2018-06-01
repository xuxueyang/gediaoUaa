package uaa.service.dto.app.log;


import io.swagger.annotations.ApiModelProperty;
import uaa.domain.app.log.AppLogDay;

import java.util.List;

public class GetAllLogDTO {
    //得到全部的树结构（根据日期）
    @ApiModelProperty(name = "所有天的日志")
    private List<AppLogDayDTO> appLogDayDTOList;

    public List<AppLogDayDTO> getAppLogDayDTOList() {
        return appLogDayDTOList;
    }

    public void setAppLogDayDTOList(List<AppLogDayDTO> appLogDayDTOList) {
        this.appLogDayDTOList = appLogDayDTOList;
    }
}
