package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by UKi_Hi on 2018/6/2.
 */
public class UpdateLogEachDTO {
    //只更新LogEach的信息
    @ApiModelProperty(name = "ID")
    private String id;

    @ApiModelProperty(name = "标题")
    private String title;

    @ApiModelProperty(name = "消息")
    private String message;

    @ApiModelProperty(name = "所属的日期")
    private String belongDate;

    @ApiModelProperty(name = "tags的ID")
    private List<String> tags;

    @ApiModelProperty(name = "状态设置（未完成N还是已完成Y）")
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
