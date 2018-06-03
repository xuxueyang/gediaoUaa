package uaa.service.dto.app.log;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

public class AppLogEachDTO extends AppLogBaseDTO{
    @ApiModelProperty(name = "每个each的细节面板（分类也算是detail的）,架构上支持多个映射，现在只有一个基本")
    private List<AppLogDetailDTO> appLogDetailDTOList;


    @ApiModelProperty(name = "ID")
    private String id;

    @ApiModelProperty(name = "所属于的日期")
    private String belongDate;

    @ApiModelProperty(name = "消息")
    private String message;

    @ApiModelProperty(name = "标题")
    private String title;

    @ApiModelProperty(name = "类型")
    private String type;

    @ApiModelProperty(name = "标签")
    private Map<String,AppLogTagDTO> tagMap;


//    @ApiModelProperty(name = "含有each参数的map")
//    private Map<String,Object> values;


//    public Map<String, Object> getValue() {
//        return values;
//    }
//
//    public void setValue(Map<String, Object> values) {
//        this.values = values;
//    }

    public List<AppLogDetailDTO> getAppLogDetailDTOList() {
        return appLogDetailDTOList;
    }

    public void setAppLogDetailDTOList(List<AppLogDetailDTO> appLogDetailDTOList) {
        this.appLogDetailDTOList = appLogDetailDTOList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, AppLogTagDTO> getTagMap() {
        return tagMap;
    }

    public void setTagMap(Map<String, AppLogTagDTO> tagMap) {
        this.tagMap = tagMap;
    }
}
