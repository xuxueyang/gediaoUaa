package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;


public class AppLogBaseDTO {

    @ApiModelProperty(name = "创建时间")
    private Instant createdDate;

    @ApiModelProperty(name = "更新时间")
    private Instant updatedDate;

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }
}
