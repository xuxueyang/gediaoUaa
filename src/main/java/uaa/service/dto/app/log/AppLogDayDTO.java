package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

public class AppLogDayDTO extends  AppLogBaseDTO{
    @ApiModelProperty(name = "标题")
    private String title;

    @ApiModelProperty(name = "ID")
    private String id;

    @ApiModelProperty(name = "消息")
    private String message;

    @ApiModelProperty(name = "标签")
    private Map<String,AppLogTagDTO> tagMap;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, AppLogTagDTO> getTagMap() {
        return tagMap;
    }

    public void setTagMap(Map<String, AppLogTagDTO> tagMap) {
        this.tagMap = tagMap;
    }
}
