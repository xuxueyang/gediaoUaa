package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

public class UpdateLogTagDTO {

    @ApiModelProperty(name = "ID")
    private String tagId;


    @ApiModelProperty(name = "名字")
    private String name;

    @ApiModelProperty(name = "group")
    private String group;

    @ApiModelProperty(name = "token")
    private String token;

    @ApiModelProperty(name = "type")
    private String type;

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

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
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
