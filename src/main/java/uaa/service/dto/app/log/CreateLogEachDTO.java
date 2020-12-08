package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by UKi_Hi on 2018/6/3.
 */
@Data
public class CreateLogEachDTO {
    @ApiModelProperty(name = "标题")
    private String title;

    @ApiModelProperty(name = "消息")
    private String message;

    @ApiModelProperty(name = "状态")
    private String status;


    @ApiModelProperty(name = "所属的日期")
    private String belongDate;


    @ApiModelProperty(name = "token")
    private String token;

    @ApiModelProperty(name = "userId")
    private String userId;

    @ApiModelProperty(name = "tags的ID")
    private List<String> tags;

    @ApiModelProperty(name = "番茄工作法的状态")
    private String tomatoType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTomatoType() {
        return tomatoType;
    }

    public void setTomatoType(String tomatoType) {
        this.tomatoType = tomatoType;
    }
}
