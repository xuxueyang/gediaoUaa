package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by UKi_Hi on 2018/6/2.
 */
public class UpdateLogDayDTO {
    @ApiModelProperty(name = "ID")
    private String id;

    @ApiModelProperty(name = "标题")
    private String title;

    @ApiModelProperty(name = "消息")
    private String message;

    @ApiModelProperty(name = "token")
    private String token;

    @ApiModelProperty(name = "所属的日期")
    private String belongDate;

    @ApiModelProperty(name = "tags的ID")
    private List<String> tags;


    public String getBelongDate() {
        return belongDate;
    }

    public void setBelongDate(String belongDate) {
        this.belongDate = belongDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
