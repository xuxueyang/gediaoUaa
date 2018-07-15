package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * Created by UKi_Hi on 2018/6/4.
 */
public class AppLogTagDTO {

    @ApiModelProperty(name = "ID")
    private String id;
    @ApiModelProperty(name = "名字")
    private String name;

    @ApiModelProperty(name = "组织")
    private String group;

    @ApiModelProperty(name = "type")
    private String type;
    @ApiModelProperty(name = "更新时间")
    private ZonedDateTime updatedDate;


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

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
