package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

public class CreateLogTagDTO {

    @ApiModelProperty(name = "名字")
    private String name;

    @ApiModelProperty(name = "group")
    private String group;

    @ApiModelProperty(name = "type")
    private String type;

    @ApiModelProperty(name = "token")
    private String token;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
