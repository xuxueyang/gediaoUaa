package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by UKi_Hi on 2018/6/4.
 */
public class AppLogTagDTO {
    @ApiModelProperty(name = "名字")
    private String name;

    @ApiModelProperty(name = "组织")
    private String group;

    @ApiModelProperty(name = "type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
