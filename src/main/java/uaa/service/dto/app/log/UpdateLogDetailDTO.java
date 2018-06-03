package uaa.service.dto.app.log;


import io.swagger.annotations.ApiModelProperty;

/**
 * Created by UKi_Hi on 2018/6/4.
 */
public class UpdateLogDetailDTO {

    @ApiModelProperty(name = "ID")
    private String detailId;


    @ApiModelProperty(name = "备注")
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
}
