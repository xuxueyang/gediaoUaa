package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;
import uaa.config.Constants;

import java.util.List;

public class CreateLogDayDTO {

    @ApiModelProperty(name = "所属日期")
    private String belongDate;

    @ApiModelProperty(name = "标题")
    private String title;

    @ApiModelProperty(name = "内容")
    private String message;

    @ApiModelProperty(name = "token")
    private String token;

    @ApiModelProperty(name = "type:"+ Constants.APP_LOG_DAY_TYPE_DIARY+" 和 "+Constants.APP_LOG_DAY_TYPE_TEL)
    private String type;

    @ApiModelProperty(name = "标签的ID")
    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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

    public String getBelongDate() {
        return belongDate;
    }

    public void setBelongDate(String belongDate) {
        this.belongDate = belongDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
