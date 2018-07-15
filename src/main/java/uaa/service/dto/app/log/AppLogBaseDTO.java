package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;
import java.time.ZonedDateTime;


public class AppLogBaseDTO {

    @ApiModelProperty(name = "创建时间")
    private ZonedDateTime createdDate;

    @ApiModelProperty(name = "更新时间")
    private ZonedDateTime updatedDate;

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}
