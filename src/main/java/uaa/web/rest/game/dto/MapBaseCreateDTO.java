package uaa.web.rest.game.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

public class MapBaseCreateDTO {
    @ApiModelProperty(value = "名称，用来表示这是什么")
    private String title;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "图片")
    private String imagePath;


    @ApiModelProperty(value = "序列化特殊属性")
    private Map<String,String> object = new HashMap<>();

    @ApiModelProperty(value = "标识类型")
    private String mapTypeId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Map<String, String> getObject() {
        return object;
    }

    public void setObject(Map<String, String> object) {
        this.object = object;
    }

    public String getMapTypeId() {
        return mapTypeId;
    }

    public void setMapTypeId(String mapTypeId) {
        this.mapTypeId = mapTypeId;
    }
}
