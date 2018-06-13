package uaa.service.dto.message;

import io.swagger.annotations.ApiModelProperty;
import uaa.service.dto.UaaBasePremissionDTO;

public class TranferMessageDTO extends UaaBasePremissionDTO{
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "修改的备注信息")
    private String ps;

    @ApiModelProperty(value = "要转换的类别")
    private String type;

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
