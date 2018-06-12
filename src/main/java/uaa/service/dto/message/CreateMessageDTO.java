package uaa.service.dto.message;

import io.swagger.annotations.ApiModelProperty;
import uaa.service.dto.UaaBasePremissionDTO;

public class CreateMessageDTO  extends UaaBasePremissionDTO{

    @ApiModelProperty(value = "为那个项目类别添加日志")
    private String projectType;

    @ApiModelProperty(value = "添加的消息类型")
    private String type;

    @ApiModelProperty(value = "添加的值")
    private String value;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

}
